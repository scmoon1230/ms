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
import kr.co.ucp.link.job.user.service.JobUserBaseMapper;
import kr.co.ucp.link.job.user.service.JobUserBaseService;

@Service("jobUserBaseService")
public class JobUserBaseServiceImpl implements JobUserBaseService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;
	
	@Resource(name = "linkCmmnService")
	private LinkCmmnService linkCmmnService;

	@Resource(name = "JobUserBaseMapper")
	private JobUserBaseMapper jobUserBaseMapper;

//	private String linkId = "uriBaseUserBase";
	private String linkUri = "/link/base/userbase.xx";


	// 사용자정보 동기화
	public String jobUserBaseChk(String pDstrtCd) throws Exception {
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
			// 지구별
			for (int i = 0; i < dstrtCnt; i++) {
				Map<String, Object> urlMap = (Map<String, Object>) dstrtList.get(i);
				
				dstrtCd = CommUtil.objNullToVal(urlMap.get("dstrtCd"), "");
				dstrtNm = CommUtil.objNullToVal(urlMap.get("dstrtNm"), "");
				linkAddr = CommUtil.objNullToVal(urlMap.get("linkAddr"), "");
//				authKey = CommUtil.objNullToVal(urlMap.get("authKey"), "");
//				aliveChkRspnsCd = CommUtil.objNullToVal(urlMap.get("aliveChkRspnsCd"), "");

//				if (!"000".equals(aliveChkRspnsCd)) {		// 네트워크 연결 안됨
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
					reqMap.put("trnsDstrtCd", wDstrtCd);	// 광역지구코드
					reqMap.put("rcptDstrtCd", dstrtCd);		// 기초지구코드
					// request 전문 작성 종료
					
					//headMap.put("x-auth-key", authKey);
					responseMap = RestUtil.postHttp(linkAddr, reqMap, 5, headMap);

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
		Map<String, Object> map = new HashMap<String, Object>();
		
		String responseMsg = CommUtil.objNullToVal(rsMap.get("responseMsg"), "");
		String responseCd = CommUtil.objNullToVal(rsMap.get("responseCd"), "");
		String dstrtCd = CommUtil.objNullToVal(rsMap.get("dstrtCd"), "");
		logger.info("==== rsData >>>> {},{},{}", dstrtCd,responseCd,responseMsg);

		if (responseMsg.length() > 100) {
			responseMsg = responseMsg.substring(0, 100);
		}

		// response 데이터 처리 시작
		if ("000".equals(responseCd)) {
			try {
				HashMap<String, Object> updateMap = updateUserList(rsMap);
				map.put("linkCnt", updateMap.get("lnkCnt"));
				map.put("regCnt", updateMap.get("regCnt"));
				map.put("updCnt", updateMap.get("updCnt"));
				map.put("delCnt", updateMap.get("delCnt"));
				
			} catch (Exception e) {
				logger.error("==== ERROR Exception >>>> {}", e.getMessage());
			}
		}

		// response 데이터 처리 종료
		map.put("dstrtCd", dstrtCd);
		map.put("responseCd", responseCd);
		map.put("responseMsg", responseMsg);
		map.put("responseYmdhms", CommUtil.getCurrentTime14());

		// 결과 로그 저장
		map.put("linkUri", linkUri);
		linkCmmnService.insertWdLinkLog(map);

		return rtnMsg;
	}
	
	@Override
	public HashMap<String, Object> updateUserList(Map<String, Object> rsMap) throws Exception {
		// Base에서 받은 데이터 리스트
		List<Map<String, Object>> rqMapData = (List<Map<String, Object>>) rsMap.get("data");
		HashMap<String, Object> r_map = new HashMap<String, Object>();
		HashMap<String, Object> l_map = new HashMap<String, Object>();
		HashMap<String, Object> dstrtCdUserMap = new HashMap<String, Object>();

		String rcptDstrtCd = rsMap.get("dstrtCd").toString();
		String r_userId = "";
		String r_userNm = "";
		String r_partNm = "";
		String r_pstnNm = "";
		String r_telNo = "";
		String r_eMail = "";
		String r_useYn = "";
		String l_userId = "";
		String l_userNm = "";
		String l_partNm = "";
		String l_pstnNm = "";
		String l_telNo = "";
		String l_eMail = "";
		String l_useYn = "";
		String userFind = "N";

		int regCnt = 0;
		int updCnt = 0;
		int delCnt = 0;
		int lnkCnt = 0;

		// 수신한 지구코드 사용자 리스트
		dstrtCdUserMap.put("dstrtCd", rcptDstrtCd);
		List<Map<String, Object>> mapData = jobUserBaseMapper.selectDstrtUserList(dstrtCdUserMap);
		lnkCnt = mapData.size();
		int rqMapDataCnt = rqMapData.size();
		int mapDataCnt = mapData.size();

		for (int i = 0; i < rqMapDataCnt; i++) {
			r_map = (HashMap<String, Object>) rqMapData.get(i);
			r_userId = CommUtil.objNullToStr(r_map.get("userId"));
			r_userNm = CommUtil.objNullToStr(r_map.get("userNm"));
			r_partNm = CommUtil.objNullToStr(r_map.get("partNm"));
			r_pstnNm = CommUtil.objNullToStr(r_map.get("pstnNm"));
			r_telNo = CommUtil.objNullToStr(r_map.get("telNo"));
			r_eMail = CommUtil.objNullToStr(r_map.get("eMail"));
			r_useYn = CommUtil.objNullToStr(r_map.get("useYn"));

			// Insert를 위해 mode를 I로 변경한다.
			rqMapData.get(i).put("mode", "I");
			rqMapData.get(i).put("rcptDstrtCd", rcptDstrtCd);
			// Wide 유저와 동일한 데이터인지 비교 regCnt 카운트용
			userFind = "N";
			// Local 리스트
			for (int k = 0; k < mapDataCnt; k++) {
				l_map = (HashMap<String, Object>) mapData.get(k);
				l_userId = CommUtil.objNullToStr(l_map.get("userId"));
				l_userNm = CommUtil.objNullToStr(l_map.get("userNm"));
				l_partNm = CommUtil.objNullToStr(l_map.get("partNm"));
				l_pstnNm = CommUtil.objNullToStr(l_map.get("pstnNm"));
				l_telNo = CommUtil.objNullToStr(l_map.get("telNo"));
				l_eMail = CommUtil.objNullToStr(l_map.get("eMail"));
				l_useYn = CommUtil.objNullToStr(l_map.get("useYn"));

				// Base리스트와 Wide리스트 둘 다 있으면 업데이트처리
				if (r_userId.equals(l_userId)) {
					// Wide리스트에서 사용유형을 D로 가져오는데, Base에서 가져온 데이터에 존재하면 사용으로 처리
					mapData.get(k).put("useTyCd", "Y"); // 사용으로 처리

					// Update를 위해 mode를 U로 변경한다.
					rqMapData.get(i).put("mode", "U");

					// Wide 유저와 동일한 데이터임을 확인하여 Y로 변경한다.
					userFind = "Y";
					if (r_userNm.equals(l_userNm) && r_partNm.equals(l_partNm) && r_pstnNm.equals(l_pstnNm) && r_telNo.equals(l_telNo) && r_eMail.equals(l_eMail) && r_useYn.equals(l_useYn)) {
						rqMapData.get(i).put("mode", "");
					}
					break;
				}
			}
			if (!"Y".equals(userFind)) {
				regCnt++;
			}
		}

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		int localCnt = mapData.size();
		int rqCnt = rqMapData.size();
		// 비밀번호 1111
		String password = "9Gxtjs1KkNek4KVEPIrGhWoz09droGTWdbEjjr8d3XA=";

		for (int i = 0; i < localCnt; i++) {
			l_map = (HashMap<String, Object>) mapData.get(i);
			if ("D".equals(l_map.get("useTyCd").toString())) {
				int r = jobUserBaseMapper.updateUserUseTyCd(l_map);
				delCnt += r;
			}
		}

		for (int i = 0; i < rqCnt; i++) {
			int r = 0;
			r_map = (HashMap<String, Object>) rqMapData.get(i);
			r_map.put("password", password);
			if ("U".equals(r_map.get("mode").toString())) {
				if (r_map.get("rcptDstrtCd").equals(r_map.get("dstrtCd"))) {
					r = jobUserBaseMapper.updateUser(r_map);
					updCnt += r;
				}
			} else if ("I".equals(r_map.get("mode").toString())) {
				if (r_map.get("rcptDstrtCd").equals(r_map.get("dstrtCd"))) {
					r = jobUserBaseMapper.insertUser(r_map);
					// 유저 추가시에 필요한 테이블, 쿼리문 추가 필요
					jobUserBaseMapper.insertGrpUser(r_map);
					//jobUserBaseMapper.insertUserDstrt(r_map);
					regCnt += r;
				}
			}
		}
		resultMap.put("regCnt", regCnt);
		resultMap.put("updCnt", updCnt);
		resultMap.put("delCnt", delCnt);
		resultMap.put("lnkCnt", lnkCnt);

		return resultMap;
	}
}