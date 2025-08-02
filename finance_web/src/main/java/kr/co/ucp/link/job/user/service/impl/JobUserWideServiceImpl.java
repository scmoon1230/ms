package kr.co.ucp.link.job.user.service.impl;

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
import kr.co.ucp.link.job.user.service.JobUserWideMapper;
import kr.co.ucp.link.job.user.service.JobUserWideService;

@Service("jobUserWideService")
public class JobUserWideServiceImpl implements JobUserWideService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name = "linkCmmnService")
	private LinkCmmnService linkCmmnService;

	@Resource(name = "JobUserWideMapper")
	private JobUserWideMapper jobUserWideMapper;

//	private String linkId = "uriBaseUserWide";
	private String linkUri = "/link/base/userwide.xx";


	// 사용자정보 동기화
	public String jobUserWideChk(String pDstrtCd) throws Exception {
		String rtnMsg = "";
		// 현재 시스템 지구코드, 발송지구코드
		String wDstrtCd = prprtsService.getString("DSTRT_CD");
		String linkAddr = "";
		try {
			// 공통
			Map<String, String> headMap = new HashMap<String, String>();
			Map<String, Object> responseMap = new HashMap<String, Object>();
			Map<String, Object> reqMap = new HashMap<String, Object>();

			// 0. 연계 지자체 지구리스트
			List<Map<String, Object>> dstrtList = null;
			Map<String, Object> argMap = new HashMap<String, Object>();

			argMap.put("dstrtCd", pDstrtCd);	// 기초지구코드(수신,ALL)
			argMap.put("wDstrtCd", wDstrtCd);	// 광역지구코드
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

			// 2-1. 광역사용자 리스트
//			Map<String, Object> aMap = new HashMap<String, Object>();
			argMap.put("dstrtCd", wDstrtCd);
			List<Map<String, Object>> userMap = selectUserList(argMap);

			// 2-2. 광역사용자 지구별 전송처리
			for (int i = 0; i < dstrtCnt; i++) {
				Map<String, Object> urlMap = (Map<String, Object>) dstrtList.get(i);

				dstrtCd = CommUtil.objNullToVal(urlMap.get("dstrtCd"), "");
				dstrtNm = CommUtil.objNullToVal(urlMap.get("dstrtNm"), "");
				linkAddr = CommUtil.objNullToVal(urlMap.get("linkAddr"), "");
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
					Map<String, Object> sendMap = new HashMap<String, Object>();
					sendMap.put("trnsDstrtCd", wDstrtCd);
					sendMap.put("rcptDstrtCd", dstrtCd);
					sendMap.put("dataCnt", userMap.size());
					sendMap.put("data", userMap);

//					headMap.put("x-auth-key", authKey);
					responseMap = RestUtil.postHttp(linkAddr, sendMap, 5, headMap);

					String reDstrtCd = CommUtil.objNullToVal(responseMap.get("dstrtCd"), "");
					if ( "".equalsIgnoreCase(reDstrtCd)) {	responseMap.put("dstrtCd", dstrtCd);	}
					dstrtCd = CommUtil.objNullToVal(responseMap.get("dstrtCd"), "");
					logger.info("==== responseMap >>>> {},{},length:{}", dstrtCd,dstrtNm,responseMap.toString().length());
					
					//responseMap.put("dstrtCd", dstrtCd);
					//responseMap.put("dstrtNm", dstrtNm);
					
					responseMap.put("linkCnt", userMap.size());

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
		String dstrtCd = CommUtil.objNullToVal(rsMap.get("dstrtCd"), "99999");

		if (responseMsg.length() > 100) {
			responseMsg = responseMsg.substring(0, 100);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dstrtCd", dstrtCd);
		map.put("linkUri", linkUri);
		map.put("responseCd", responseCd);
		map.put("responseMsg", responseMsg);
		map.put("responseYmdhms", CommUtil.getCurrentTime14());
		linkCmmnService.insertWdLinkLog(map);

		return rtnMsg;
	}
	
	@Override
	public List<Map<String, Object>> selectUserList(Map<String, Object> map) throws Exception {
		return jobUserWideMapper.selectUserList(map);
	}
}