package kr.co.ucp.link.base.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmmn.CommUtil;
import kr.co.ucp.link.base.user.service.BaseUserWideMapper;
import kr.co.ucp.link.base.user.service.BaseUserWideService;

@Service("baseUserWideService")
public class BaseUserWideServiceImpl implements BaseUserWideService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;
	
	@Resource(name = "BaseUserWideMapper")
	private BaseUserWideMapper baseUserWideMapper;

	@Override
	public HashMap<String, Object> updateUserList(Map<String, Object> rqMap) throws Exception {
		// Wide에서 받은 데이터 리스트
		List<Map<String, Object>> rqMapData = (List<Map<String, Object>>) rqMap.get("data");
		HashMap<String, Object> r_map = new HashMap<String, Object>();
		HashMap<String, Object> l_map = new HashMap<String, Object>();
		HashMap<String, Object> dstrtCdUserMap = new HashMap<String, Object>();

		int updCnt = 0;
		int insCnt = 0;
		int delCnt = 0;
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

		dstrtCdUserMap.put("dstrtCd", rqMap.get("trnsDstrtCd"));	// 광역지구코드
		List<Map<String, Object>> mapData = baseUserWideMapper.selectUserList(dstrtCdUserMap);
		
		int rqMapDataCnt = rqMapData.size();
		int mapDataCnt = mapData.size();
		
		// Wide에서 받은 리스트
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
			// Local 유저와 동일한 데이터인지 비교 insCnt 카운트용
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

				// Wide리스트와 Local리스트 둘 다 있으면 업데이트처리
				if (r_userId.equals(l_userId)) {
					// Local리스트에서 사용유형을 D로 가져오는데, Wide에서 가져온 데이터에 존재하면 사용으로 처리
					mapData.get(k).put("useTyCd", "Y"); // 사용으로 처리

					// Update를 위해 mode를 U로 변경한다.
					rqMapData.get(i).put("mode", "U");

					// Local 유저와 동일한 데이터임을 확인하여 Y로 변경한다.
					userFind = "Y";
					if (r_userNm.equals(l_userNm) && r_partNm.equals(l_partNm) && r_pstnNm.equals(l_pstnNm) && r_telNo.equals(l_telNo) && r_eMail.equals(l_eMail) && r_useYn.equals(l_useYn)) {
						rqMapData.get(i).put("mode", "");
					}
					break;
				}
			}
			if (!"Y".equals(userFind)) {
				insCnt++;
			}
		}

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		int localCnt = mapData.size();
		int rqCnt = rqMapData.size();
		// 사용자 구분
		for (int i = 0; i < localCnt; i++) {
			l_map = (HashMap<String, Object>) mapData.get(i);
			if ("D".equals(l_map.get("useTyCd").toString())) {
				int r = baseUserWideMapper.updateUserUseTyCd(l_map);
				delCnt += r;
			}
		}


		for (int i = 0; i < rqCnt; i++) {
			int r = 0;
			r_map = (HashMap<String, Object>) rqMapData.get(i);
			if ("U".equals(r_map.get("mode").toString())) {
				r = baseUserWideMapper.updateUser(r_map);
				updCnt += r;
			} else if ("I".equals(r_map.get("mode").toString())) {
				r = baseUserWideMapper.insertUser(r_map);
				baseUserWideMapper.insertGrpUser(r_map);
				//baseUserWideMapper.insertUserDstrt(r_map);
				insCnt += r;
			}
		}
		
		//Map<String, Object> dstrtCdMap = baseUserWideMapper.selectDstrtCd();
		String dstrtCd = prprtsService.getString("DSTRT_CD");

		resultMap.put("updCnt", updCnt);
		resultMap.put("insCnt", insCnt);
		resultMap.put("delCnt", delCnt);
		//resultMap.put("dstrtCd", dstrtCdMap.get("prprtsVal"));
		resultMap.put("dstrtCd", dstrtCd);

		return resultMap;
	}
}
