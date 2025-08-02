package kr.co.ucp.link.job.cctv.service.impl;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmmn.CamelMap;
import kr.co.ucp.cmmn.CommUtil;
//import kr.co.ucp.cmmn.GblVal;
import kr.co.ucp.cmmn.RestUtil;
import kr.co.ucp.gis.service.GisService;
import kr.co.ucp.link.cmmn.service.LinkCmmnService;
import kr.co.ucp.link.job.cctv.service.JobCctvListMapper;
//import kr.co.ucp.link.cmmn.service.PrprtsService;
import kr.co.ucp.link.job.cctv.service.JobCctvListService;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service("jobCctvListService")
public class JobCctvListServiceImpl implements JobCctvListService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name = "linkCmmnService")
	private LinkCmmnService linkCmmnService;

	@Resource(name = "gisService")
	private GisService gisService;

	@Resource(name = "jobCctvListMapper")
	private JobCctvListMapper jobCctvListMapper;

	private String linkUri = "/link/base/cctvlist.xx";
	

	// CCTV정보 동기화
	public String jobCctvListChk(String pDstrtCd) throws Exception {
		String rtnMsg = "";
		// 현재 시스템 지구코드, 발송지구코드
		String wDstrtCd = prprtsService.getString("DSTRT_CD");
		String linkAddr = "";
		try {
			// 공통
			Map<String, String> headMap = new HashMap<>();
			Map<String, Object> responseMap = new HashMap<>();
			Map<String, Object> reqMap = new HashMap<>();
			// 대상 지구리스트
			List<Map<String, Object>> dstrtList = null;
			Map<String, Object> argMap = new HashMap<>();
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
			//String authKey, aliveChkRspnsCd = "";
			// 지구별
			for (int i = 0; i < dstrtCnt; i++) {
				Map<String, Object> urlMap = (Map<String, Object>) dstrtList.get(i);
				linkAddr = CommUtil.objNullToVal(urlMap.get("linkAddr"), "");
				dstrtCd = CommUtil.objNullToVal(urlMap.get("dstrtCd"), "");
				dstrtNm = CommUtil.objNullToVal(urlMap.get("dstrtNm"), "");
//				authKey = CommUtil.objNullToVal(urlMap.get("authKey"), "");
//				aliveChkRspnsCd = CommUtil.objNullToVal(urlMap.get("aliveChkRspnsCd"), "");

//				if (!"000".equals(aliveChkRspnsCd)) {	// 네트워크 연결 안됨
//					logger.error("==== ERROR 기초연결상태 오류(alive) >>>> {}", dstrtCd);	continue;
//				}
//				if ("".equals(authKey)) {			// key가 없는 경우 신규요청
//					logger.error("==== ERROR 인증키(authKey) 없음 >>>> {}", dstrtCd);		continue;
//				}
				if ("".equals(linkAddr)) {			// key가 없는 경우 신규요청
					logger.error("==== ERROR 접속정보(linkAddr) 없음 >>>> {}", dstrtCd);	continue;
				}

				try {
					// request 전문 작성 시작
					reqMap.put("trnsDstrtCd", wDstrtCd);
					reqMap.put("rcptDstrtCd", dstrtCd);
					// request 전문 작성 종료

					//headMap.put("x-auth-key", authKey);
					responseMap = RestUtil.postHttp(linkAddr, reqMap, 15, headMap);
					
					String reDstrtCd = CommUtil.objNullToVal(responseMap.get("dstrtCd"), "");
					if ( "".equalsIgnoreCase(reDstrtCd)) {	responseMap.put("dstrtCd", dstrtCd);	}
					dstrtCd = CommUtil.objNullToVal(responseMap.get("dstrtCd"), "");
					logger.info("==== responseMap >>>> {},{},length:{}", dstrtCd,dstrtNm,responseMap.toString().length());

					//responseMap.put("dstrtCd", dstrtCd);
					//responseMap.put("dstrtNm", dstrtNm);

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

	private String rsData(Map<String, Object> rsMap) throws Exception {
		String rtnMsg = "";
		String responseMsg = CommUtil.objNullToVal(rsMap.get("responseMsg"), "");
		String responseCd = CommUtil.objNullToVal(rsMap.get("responseCd"), "");
		String dstrtCd = CommUtil.objNullToVal(rsMap.get("dstrtCd"), "");
		logger.info("==== rsData >>>> {},{},{}", dstrtCd,responseCd,responseMsg);

		if (responseMsg.length() > 100) {
			responseMsg = responseMsg.substring(0, 100);
		}

		// response 데이터 처리 시작
		int regCnt = 0;
		int updCnt = 0;
		int delCnt = 0;
		if ("000".equals(responseCd)) {
			Object rqMapDataCnt = rsMap.get("dataCnt");
			if (rqMapDataCnt != null && NumberUtils.isCreatable(rqMapDataCnt.toString())) {
				// 01. WIDE 시설물 테이블로 부터 카메라 목록 획득.
				Map<String, Object> params = new HashMap<>();
				params.put("dstrtCd", dstrtCd);
				List<CamelMap> wideCctvList = selectCctvList(params);

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
							boolean hasUpdate = false;
							// 데이터 비교 시작
							Iterator<Map.Entry<String, Object>> it = baseCctvMap.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry<String, Object> entrySet = (Map.Entry<String, Object>) it.next();
								String key = entrySet.getKey();
								String value = CommUtil.objNullToStr(entrySet.getValue());
								// logger.debug("{} : {}, {}", key, value, CommUtil.objNullToStr(wideCctvMap.get(key)));
								if (!value.equals(CommUtil.objNullToStr(wideCctvMap.get(key)))) hasUpdate = true;
							}
							// 데이터 비교 종료
							if (hasUpdate) {
								baseCctvMap.put("dstrtCd", dstrtCd);
								int r = updateCctv(baseCctvMap);
								if (r == 1) updCnt++;
							}
							baseCctvIter.remove();
							wideCctvIter.remove();
							break;
						}
					}
				}

				logger.info("updateCctv : {}", updCnt);

				logger.info("insertCctv : {}", baseCctvList.size());

				// 04. Insert
				for (Map<String, Object> baseCctvMap : baseCctvList) {
					baseCctvMap.put("dstrtCd", dstrtCd);
					int r = insertCctv(baseCctvMap);
					if (r == 1) regCnt++;
				}

				logger.info("deleteCctv : {}", wideCctvList.size());

				// 05. Delete
				for (Map<String, Object> wideCctvMap : wideCctvList) {
					wideCctvMap.put("dstrtCd", dstrtCd);
					wideCctvMap.put("useYn", "D");
					int r = updateCctv(wideCctvMap);
					if (r == 1) delCnt++;
				}

				// 06. map projection 좌표 업데이트
				gisService.updatePointProjection(dstrtCd);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("updCnt", updCnt);
		map.put("regCnt", regCnt);
		map.put("delCnt", delCnt);
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

	@Override
	public List<CamelMap> selectCctvList(Map<String, Object> params) throws Exception {
		return jobCctvListMapper.selectCctvList(params);
	}

	@Override
	public int updateCctv(Map<String, Object> params) throws Exception {
		return jobCctvListMapper.updateCctv(params);
	}

	@Override
	public int insertCctv(Map<String, Object> params) throws Exception {
		return jobCctvListMapper.insertCctv(params);
	}
}
