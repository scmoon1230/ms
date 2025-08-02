/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : ApiServiceImpl.java
 * @Description : Api와 관련된 요청을 처리하는 서비스 구현
 * @Version : 1.0
 * Copyright (c) 2017 by KR.CO.WIDECUBE All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2017. 10. 25. saintjuny 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.api.service.ApiService;
import kr.co.ucp.mntr.api.service.ApiSrchVO;
import kr.co.ucp.mntr.cmm.util.SessionUtil;

@Service("apiService")
public class ApiServiceImpl implements ApiService {

	//private final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);

	@Resource(name="apiMapper")
	private ApiMapper apiMapper;

	@Override
	public EgovMap selectCoordToAddrList(ApiSrchVO vo) throws Exception {
		EgovMap resultMap = new EgovMap();

		String pointX = EgovStringUtil.nullConvert(vo.getPointX());
		String pointY = EgovStringUtil.nullConvert(vo.getPointY());
		if ("".equals(pointX) || "".equals(pointY) || !NumberUtils.isNumber(pointX) || !NumberUtils.isNumber(pointY)) {
			resultMap.put("msg", "파라메터가 누락되었거나 잘못된 형식입니다.");
		}
		else {
			List<EgovMap> addrList = apiMapper.selectCoordToAddrList(vo);
			if (addrList == null || addrList.isEmpty()) {
				resultMap.put("msg", "총 0건의 주소가 검색되었습니다");
			}
			else {
				resultMap.put("msg", "총 " + addrList.size() + "건의 주소가 검색되었습니다");
				resultMap.put("addr", addrList.get(0));
			}
		}

		return resultMap;
	}

	@Override
	public EgovMap selectAddrToCoordList(ApiSrchVO vo) throws Exception {
		EgovMap resultMap = new EgovMap();
		String keyword = EgovStringUtil.nullConvert(vo.getKeyword());

		String keywordArr[] = keyword.split("\\s+");
		int keywordLength = keywordArr.length;

		if (!"".equals(keyword)) {
			// 단일 키워드 일때
			if (keywordLength == 1) {
				String lastWord = keyword.substring(keyword.length() - 1);
				if ("도".equals(lastWord) || "시".equals(lastWord) || "군".equals(lastWord) || "구".equals(lastWord)) {
					// 도/시/군/구 - 검색 x
					resultMap.put("msg", "주소를 상세히 입력해 주시기 바랍니다.(예:세종대로 209, 국립중앙박물관, 상암동 1596)");
					return resultMap;
				}
				else {
					if ("읍".equals(lastWord) || "면".equals(lastWord) || "동".equals(lastWord) || "가".equals(lastWord)) {
						// 읍/면/동/가 - ENTRC.LG_EMD_NM, ENTRC.BULD_NM 검색
						vo.setLgEmdNm(keyword);
					}
					else if ("로".equals(lastWord) || "길".equals(lastWord)) {
						// 로/길 - ENTRC.ROAD_NM, ENTRC.BULD_NM 검색
						vo.setRoadNm(keyword);
					}
					else if ("리".equals(lastWord)) {
						// 리 - JIBUN.LG_LI_NM, ENTRC.BULD_NM 검색
						vo.setLgLiNm(keyword);
					}
					else {
						ArrayList<String> buldNm = new ArrayList<String>();
						buldNm.add(keyword);
						// 그외 ENTRC.BULD_NM 검색
						vo.setBuldNm(buldNm);
					}
				}
			}
			else if (keywordLength == 2) {
				// 두개의 키워드 일때
				String firstWord = keywordArr[0];
				String secondWord = keywordArr[1];
				ArrayList<String> buldNm = new ArrayList<String>();

				String lastWord = firstWord.substring(firstWord.length() - 1);
				if ("도".equals(lastWord) || "시".equals(lastWord) || "군".equals(lastWord) || "구".equals(lastWord)) {
					// 도/시/군/구 - 검색 x
					resultMap.put("msg", "주소를 상세히 입력해 주시기 바랍니다.(예:세종대로 209, 국립중앙박물관, 상암동 1596)");
					return resultMap;
				}
				else if ("읍".equals(lastWord) || "면".equals(lastWord) || "동".equals(lastWord) || "가".equals(lastWord)) {
					// 읍/면/동/가 - ENTRC.LG_EMD_NM, ENTRC.BULD_NM 검색
					vo.setLgEmdNm(firstWord);
				}
				else if ("로".equals(lastWord) || "길".equals(lastWord)) {
					// 로/길 - ENTRC.ROAD_NM, ENTRC.BULD_NM 검색
					vo.setRoadNm(firstWord);
				}
				else if ("리".equals(lastWord)) {
					// 리 - JIBUN.LG_LI_NM, ENTRC.BULD_NM 검색
					vo.setLgLiNm(firstWord);
				}
				else {
					buldNm.add(firstWord);
				}

				boolean isNumberSecondWord = NumberUtils.isNumber(secondWord);
				if (isNumberSecondWord) {
					// isNumber - 읍/면/동/가 ENTRC.BULD_MAIN_NO,
					// JIBUN.JIBUN_MAIN_NO
					// isNumber - 로/길 ENTRC.BULD_MAIN_NO
					vo.setMainNo(secondWord);
				}
				else {
					String secondWordArr[] = secondWord.split(Pattern.quote("-"));
					int secondWordLength = secondWordArr.length;
					if (secondWordLength == 2 && NumberUtils.isNumber(secondWordArr[0]) && NumberUtils.isNumber(secondWordArr[1])) {
						vo.setMainNo(secondWordArr[0]);
						vo.setSubNo(secondWordArr[1]);
					}
					else {
						vo.setLgEmdNm(null);
						vo.setRoadNm(null);
						vo.setLgLiNm(null);
						buldNm.clear();
						buldNm.add(firstWord);
						buldNm.add(secondWord);
					}
				}
				vo.setBuldNm(buldNm);
			}
			else {
				vo.setBuldNm(new ArrayList<String>(Arrays.asList(keywordArr)));
			}

			List<EgovMap> addrList = apiMapper.selectAddrToCoordList(vo);
			if (addrList == null || addrList.isEmpty()) {
				resultMap.put("msg", keyword + "을(를) 검색한 결과 총 0건의 주소가 검색되었습니다");
			}
			else {
				resultMap.put("msg", keyword + "을(를) 검색한 결과 총 " + addrList.size() + "건 입니다.");
				resultMap.put("addr", addrList);
			}
		}
		else {
			resultMap.put("msg", "파라메터가 누락되었거나 잘못된 형식입니다.");
		}

		return resultMap;
	}

	@Override
	public EgovMap selectKeywordToCoordList(ApiSrchVO vo) throws Exception {

		LoginVO loginVO = SessionUtil.getUserInfo();
		vo.setUserId(loginVO.getUserId());
		vo.setGrpId(loginVO.getGrpId());
		vo.setAuthLvl(loginVO.getAuthLvl());

		EgovMap resultMap = new EgovMap();
		String keyword = EgovStringUtil.nullConvert(vo.getKeyword());
		String keywordArr[] = keyword.split("\\s+");
		int keywordLength = keywordArr.length;

		if (!"".equals(keyword)) {
			// 단일 키워드 일때
			if (keywordLength == 1) {
				String lastWord = keyword.substring(keyword.length() - 1);
				if ("도".equals(lastWord) || "시".equals(lastWord) || "군".equals(lastWord) || "구".equals(lastWord)) {
					// 도/시/군/구 - 검색 x
					resultMap.put("msg", "주소를 상세히 입력해 주시기 바랍니다.(예:세종대로 209, 국립중앙박물관, 상암동 1596)");
					return resultMap;
				}
				else {
					if ("읍".equals(lastWord) || "면".equals(lastWord) || "동".equals(lastWord) || "가".equals(lastWord)) {
						// 읍/면/동/가 - ENTRC.LG_EMD_NM, ENTRC.BULD_NM 검색
						vo.setLgEmdNm(keyword);
					}
					else if ("로".equals(lastWord) || "길".equals(lastWord)) {
						// 로/길 - ENTRC.ROAD_NM, ENTRC.BULD_NM 검색
						vo.setRoadNm(keyword);
					}
					else if ("리".equals(lastWord)) {
						// 리 - JIBUN.LG_LI_NM, ENTRC.BULD_NM 검색
						vo.setLgLiNm(keyword);
					}
					else {
						ArrayList<String> buldNm = new ArrayList<String>();
						buldNm.add(keyword);
						// 그외 ENTRC.BULD_NM 검색
						vo.setBuldNm(buldNm);
					}
				}
			}
			else if (keywordLength == 2) {
				// 두개의 키워드 일때
				String firstWord = keywordArr[0];
				String secondWord = keywordArr[1];
				ArrayList<String> buldNm = new ArrayList<String>();

				String lastWord = firstWord.substring(firstWord.length() - 1);
				if ("도".equals(lastWord) || "시".equals(lastWord) || "군".equals(lastWord) || "구".equals(lastWord)) {
					// 도/시/군/구 - 검색 x
					resultMap.put("msg", "주소를 상세히 입력해 주시기 바랍니다.(예:세종대로 209, 국립중앙박물관, 상암동 1596)");
					return resultMap;
				}
				else if ("읍".equals(lastWord) || "면".equals(lastWord) || "동".equals(lastWord) || "가".equals(lastWord)) {
					// 읍/면/동/가 - ENTRC.LG_EMD_NM, ENTRC.BULD_NM 검색
					vo.setLgEmdNm(firstWord);
				}
				else if ("로".equals(lastWord) || "길".equals(lastWord)) {
					// 로/길 - ENTRC.ROAD_NM, ENTRC.BULD_NM 검색
					vo.setRoadNm(firstWord);
				}
				else if ("리".equals(lastWord)) {
					// 리 - JIBUN.LG_LI_NM, ENTRC.BULD_NM 검색
					vo.setLgLiNm(firstWord);
				}
				else {
					buldNm.add(firstWord);
				}

				boolean isNumberSecondWord = NumberUtils.isNumber(secondWord);
				if (isNumberSecondWord) {
					// isNumber - 읍/면/동/가 ENTRC.BULD_MAIN_NO,
					// JIBUN.JIBUN_MAIN_NO
					// isNumber - 로/길 ENTRC.BULD_MAIN_NO
					vo.setMainNo(secondWord);
				}
				else {
					String secondWordArr[] = secondWord.split(Pattern.quote("-"));
					int secondWordLength = secondWordArr.length;
					if (secondWordLength == 2 && NumberUtils.isNumber(secondWordArr[0]) && NumberUtils.isNumber(secondWordArr[1])) {
						vo.setMainNo(secondWordArr[0]);
						vo.setSubNo(secondWordArr[1]);
					}
					else {
						vo.setLgEmdNm(null);
						vo.setRoadNm(null);
						vo.setLgLiNm(null);
						buldNm.clear();
						buldNm.add(firstWord);
						buldNm.add(secondWord);
					}
				}
				vo.setBuldNm(buldNm);
			}
			else {
				vo.setBuldNm(new ArrayList<String>(Arrays.asList(keywordArr)));
			}

			List<EgovMap> addrList = apiMapper.selectKeywordToCoordList(vo);
			if (addrList == null || addrList.isEmpty()) {
				resultMap.put("msg", keyword + "을(를) 검색한 결과 총 0건의 주소가 검색되었습니다");
			}
			else {
				resultMap.put("msg", keyword + "을(를) 검색한 결과 총 " + addrList.size() + "건 입니다.");
				resultMap.put("addr", addrList);
			}
		}
		else {
			resultMap.put("msg", "파라메터가 누락되었거나 잘못된 형식입니다.");
		}

		return resultMap;
	}

	@Override
	public Object selectAddrListByPnu(ApiSrchVO vo) throws Exception {
		EgovMap resultMap = new EgovMap();
		List<EgovMap> addrList = apiMapper.selectAddrListByPnu(vo);
		resultMap.put("addr", addrList);
		return resultMap;
	}

}
