package kr.co.ucp.link.job.cctv.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmmn.CamelMap;
import kr.co.ucp.cmmn.CommUtil;
import kr.co.ucp.cmmn.RestUtil;
import kr.co.ucp.link.cmmn.service.LinkCmmnService;
import kr.co.ucp.link.job.cctv.service.JobCctvListService;
import kr.co.ucp.link.job.cctv.service.JobCctvStateService;

@Service("jobCctvStateService")
public class JobCctvStateServiceImpl implements JobCctvStateService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;
	
	@Resource(name = "linkCmmnService")
	private LinkCmmnService linkCmmnService;

	// 서비스 생성
	@Resource(name = "jobCctvListService")
	private JobCctvListService jobCctvListService;

	private String linkUri = "/link/base/cctvstate.xx";

	
	
	// CCTV정보 동기화
	@Override
	public String jobCctvStateChk(String pDstrtCd) throws Exception {
		String rtnMsg = "";
		// 현재 시스템 지구코드, 발송지구코드
		String wDstrtCd = prprtsService.getString("DSTRT_CD");
		String linkAddr = "";
		try {
			// 공통
			Map<String, String> headMap = new HashMap<String, String>();
			Map<String, Object> responseMap = new HashMap<String, Object>();
			Map<String, Object> reqMap = new HashMap<String, Object>();
			// 대상 지구리스트
			List<Map<String, Object>> dstrtList = null;
			Map<String, Object> argMap = new HashMap<String, Object>();
			argMap.put("dstrtCd", pDstrtCd);
			argMap.put("wDstrtCd", wDstrtCd);
			argMap.put("linkUri", linkUri);
			
			// 수신대상지구리스트
			dstrtList = linkCmmnService.selectDstrtLinkAddrList(argMap);

			int dstrtCnt = dstrtList.size();
			if (dstrtCnt < 1) {
				rtnMsg += "지구정보 없음 ";
				return rtnMsg;
			}

			String dstrtCd, dstrtNm = "";
//			String authKey, aliveChkRspnsCd = "";
			
			// 지구별
			for (int i = 0; i < dstrtCnt; i++) {
				Map<String, Object> urlMap = (Map<String, Object>) dstrtList.get(i);
				linkAddr = CommUtil.objNullToVal(urlMap.get("linkAddr"), "");
				dstrtCd = CommUtil.objNullToVal(urlMap.get("dstrtCd"), "");
				dstrtNm = CommUtil.objNullToVal(urlMap.get("dstrtNm"), "");
//				authKey = CommUtil.objNullToVal(urlMap.get("authKey"), "");
//				aliveChkRspnsCd = CommUtil.objNullToVal(urlMap.get("aliveChkRspnsCd"), "");

//				if (!"000".equals(aliveChkRspnsCd)) {	// 네트워크 연결 안됨
//					logger.error("==== ERROR 기초연결상태 오류(alive) >>>> {}", dstrtCd);		continue;
//				}
//				if ("".equals(authKey)) {				// key가 없는 경우 신규요청
//					logger.error("==== ERROR 인증키(authKey) 없음 >>>> {}", dstrtCd);			continue;
//				}
				if ("".equals(linkAddr)) {				// key가 없는 경우 신규요청
					logger.error("==== ERROR 접속정보(linkAddr) 없음 >>>> {}", dstrtCd);		continue;
				}

				try {
					// request 전문 작성 시작
					reqMap.put("trnsDstrtCd", wDstrtCd);
					reqMap.put("rcptDstrtCd", dstrtCd);
					// request 전문 작성 종료

//					headMap.put("x-auth-key", authKey);
					responseMap = RestUtil.postHttp(linkAddr, reqMap, 15, headMap);
					logger.info("==== responseMap >>>> {},{},length:{}", dstrtCd,dstrtNm,responseMap.toString().length());
					rtnMsg += rsData(responseMap);

				} catch (Exception e) {
					logger.error("==== ERROR Exception >>>> {},{},{}", dstrtCd, dstrtNm, e.getMessage());
					rtnMsg += "Exception ";
				}
			}
		} catch (Exception e) {
			logger.error("==== ERROR Exception >>>> {}", e.getMessage());
			rtnMsg += "Exception ";
		}
		return rtnMsg;
	}

	@Override
	public String rsData(Map<String, Object> rsMap) throws Exception {
		String rtnMsg = "";
		Map<String, Object> map = new HashMap<String, Object>();

		String responseMsg = CommUtil.objNullToVal(rsMap.get("responseMsg"), "");
		String responseCd = CommUtil.objNullToVal(rsMap.get("responseCd"), "");
		String dstrtCd = CommUtil.objNullToVal(rsMap.get("dstrtCd"), "");

		if (responseMsg.length() > 100) {
			responseMsg = responseMsg.substring(0, 100);
		}

		// response 데이터 처리 시작
		int updCnt = 0;

		if ("000".equals(responseCd)) {
			Object rqMapDataCnt = rsMap.get("dataCnt");
			if (rqMapDataCnt != null && NumberUtils.isCreatable(rqMapDataCnt.toString())) {
				// 01. WIDE 시설물 테이블로 부터 카메라 목록 획득.
				Map<String, Object> params = new HashMap<>();
				params.put("dstrtCd", dstrtCd);
				params.put("stateYn", "Y");
				List<CamelMap> wideCctvList = jobCctvListService.selectCctvList(params);

				// 02. BASE 시설물 테이블로 부터 카메라 목록 획득.
				List<Map<String, Object>> baseCctvList = (List<Map<String, Object>>) rsMap.get("data");

				// 03. Update
				for (Iterator<Map<String, Object>> baseCctvIter = baseCctvList.iterator(); baseCctvIter.hasNext(); ) {
					Map<String, Object> baseCctvMap = baseCctvIter.next();
					String baseCctvId = CommUtil.objNullToStr(baseCctvMap.get("cctvId"));

					for (Iterator<CamelMap> wideCctvIter = wideCctvList.iterator(); wideCctvIter.hasNext(); ) {
						CamelMap wideCctvMap = wideCctvIter.next();
						String wideCctvId = CommUtil.objNullToStr(wideCctvMap.get("cctvId"));

						if (baseCctvId.equals(wideCctvId)) {
							// 데이터 비교 시작
							String baseCctvMapStatus = CommUtil.objNullToStr(baseCctvMap.get("cctvStatus"));
							String wideCctvStatus = CommUtil.objNullToStr(wideCctvMap.get("cctvStatus"));
							if (!baseCctvMapStatus.equals(wideCctvStatus)) {
								baseCctvMap.put("dstrtCd", dstrtCd);
								int r = jobCctvListService.updateCctv(baseCctvMap);
								if (r == 1) updCnt++;
							}
							baseCctvIter.remove();
							wideCctvIter.remove();
							break;
						}
					}
				}
			}
		}

		logger.info("updateCctv : {}", updCnt);

		map.put("updCnt", updCnt);
		// response 데이터 처리 종료

		// 결과 로그 저장
		map.put("dstrtCd", dstrtCd);
		map.put("linkUri", linkUri);
		map.put("responseCd", responseCd);
		map.put("responseMsg", responseMsg);
		map.put("responseYmdhms", CommUtil.getCurrentTime14());
		linkCmmnService.insertWdLinkLog(map);
		return rtnMsg;
	}
	
}
