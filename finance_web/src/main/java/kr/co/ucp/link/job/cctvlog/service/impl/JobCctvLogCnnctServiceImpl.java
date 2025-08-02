package kr.co.ucp.link.job.cctvlog.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmmn.CommUtil;
import kr.co.ucp.cmmn.RestUtil;
import kr.co.ucp.link.cmmn.service.LinkCmmnService;
import kr.co.ucp.link.job.cctvlog.service.JobCctvLogCnnctMapper;
import kr.co.ucp.link.job.cctvlog.service.JobCctvLogCnnctService;

@Service("jobCctvLogCnnctService")
public class JobCctvLogCnnctServiceImpl  implements JobCctvLogCnnctService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name = "linkCmmnService")
	private LinkCmmnService linkCmmnService;
	
	@Resource(name="jobCctvLogCnnctMapper")
	private JobCctvLogCnnctMapper jobCctvLogCnnctMapper;

	private String linkUri = "/link/base/cctvlogcnnct.xx";

	
	public String jobCctvLogCnnctChk(String pDstrtCd) throws Exception {
		String rtnMsg = "";
		// 현재 시스템 지구코드, 발송지구코드
		String wDstrtCd = prprtsService.getString("DSTRT_CD");
		String linkAddr = "";
		try {
			// 공통
			Map<String, String> headMap = new HashMap<String, String>();
			Map<String, Object> responseMap = new HashMap<String, Object>();
			Map<String, Object> reqMap = new HashMap<String, Object>();
			// 연계 지자체 지구리스트
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
			
			// DB에서 가져온 값 초기화
			String  dstrtNm, frYmdHms, toYmdHms, dstrtCd = "";
//			String  authKey, aliveChkRspnsCd = "";

			// 지구별
			for (int i = 0; i < dstrtCnt; i++) {
				Map<String, Object> urlMap = (Map<String, Object>) dstrtList.get(i);
				linkAddr = CommUtil.objNullToVal(urlMap.get("linkAddr"), "");
				dstrtCd = CommUtil.objNullToVal(urlMap.get("dstrtCd"), "");
				dstrtNm = CommUtil.objNullToVal(urlMap.get("dstrtNm"), "");
				frYmdHms = CommUtil.objNullToVal(urlMap.get("frYmdHms"), "");
				toYmdHms = CommUtil.getCalDateStr("SECOND", -1, "yyyyMMddHHmmss");
//				authKey = CommUtil.objNullToVal(urlMap.get("authKey"), "");
//				aliveChkRspnsCd = CommUtil.objNullToVal(urlMap.get("aliveChkRspnsCd"), "");

//				if (!"000".equals(aliveChkRspnsCd)) {	// 네트워크 연결 안됨
//					logger.error("==== ERROR 기초연결상태 오류(alive) >>>> {}", dstrtCd);	continue;
//				}
//				if ("".equals(authKey)) {				// key가 없는 경우 신규요청
//					logger.error("==== ERROR 인증키(authKey) 없음 >>>> {}", dstrtCd);		continue;
//				}
				if ("".equals(linkAddr)) {				// key가 없는 경우 신규요청
					logger.error("==== ERROR 접속정보(linkAddr) 없음 >>>> {}", dstrtCd);	continue;
				}

				try {
					// request 전문 작성 시작
					urlMap.put("toYmdHms", toYmdHms);
					urlMap.put("wDstrtCd", wDstrtCd);
					List<Map<String, Object>> dataList = selectCctvLogCnnctSend(urlMap);

					Map<String, Object> sendMap = new HashMap<String, Object>();
					sendMap.put("trnsDstrtCd", wDstrtCd);
					sendMap.put("rcptDstrtCd", dstrtCd);
					sendMap.put("data", dataList);

//					headMap.put("x-auth-key", authKey);
					responseMap = RestUtil.postHttp(linkAddr, sendMap, 5, headMap);

					String reDstrtCd = CommUtil.objNullToVal(responseMap.get("dstrtCd"), "");
					if ( "".equalsIgnoreCase(reDstrtCd)) {	responseMap.put("dstrtCd", dstrtCd);	}
					dstrtCd = CommUtil.objNullToVal(responseMap.get("dstrtCd"), "");
					logger.info("==== responseMap >>>> {},{},length:{}", dstrtCd,dstrtNm,responseMap.toString().length());

					responseMap.put("dstrtCd", dstrtCd);
					responseMap.put("frYmdHms", frYmdHms);
					responseMap.put("toYmdHms", toYmdHms);
					
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
		String responseCd = CommUtil.objNullToVal(rsMap.get("responseCd"), "");
		String responseMsg = CommUtil.objNullToVal(rsMap.get("responseMsg"), "");
		String dstrtCd =CommUtil.objNullToVal(rsMap.get("dstrtCd"), "99999");

		if (responseMsg.length() > 100) {
			responseMsg = responseMsg.substring(0, 100);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		// 결과 로그 저장
		map.put("dstrtCd", dstrtCd);
		map.put("linkUri", linkUri);
		
		// 결과가 정상이면 최종업데이트 일자로, 비정상이면 기존일자 그대로 유지
		if ("000".equals(responseCd)) {
			map.put("linkYmdHmsLast", rsMap.get("toYmdHms"));
		} else {
			map.put("linkYmdHmsLast", rsMap.get("frYmdHms"));
		}

		map.put("responseCd", responseCd);
		map.put("responseMsg", responseMsg);
		map.put("responseYmdhms", CommUtil.getCurrentTime14());
		linkCmmnService.insertWdLinkLog(map);
		return rtnMsg;
	}
	
	@Override
	public List<Map<String, Object>> selectCctvLogCnnctSend(Map<String, Object> map) throws Exception {
		return jobCctvLogCnnctMapper.selectCctvLogCnnctSend(map);
	}
	
}
