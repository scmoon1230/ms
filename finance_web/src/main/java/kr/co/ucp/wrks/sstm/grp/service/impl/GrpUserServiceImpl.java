package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.wrks.sstm.grp.service.GrpUserMapper;
import kr.co.ucp.wrks.sstm.grp.service.GrpUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpUserServiceImpl
 * @Description : 그룹별 사용자 조회
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 * 
 * ----------------------------------------------------------
 * */
@Service("grpUserService")
public class GrpUserServiceImpl implements GrpUserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="grpUserMapper")
	private GrpUserMapper grpUserMapper;

	
	// 그룹별 사용자조회 지구코드 조회
	@Override
	public List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception {
		return grpUserMapper.list_cm_dstrt_cd_mng(map);
	}
	
	// 그룹별 사용자조회 그룹 조건검색
	@Override
	public List<Map<String, String>> getCM_GROUP(Map<String, String> map) throws Exception {
		return grpUserMapper.list_cm_group(map);
	}
	
	// 그룹별 사용자조회 그룹에 따른 사용자 조건검색
	@Override
	public List<Map<String, String>> getCM_GRP_USER(Map<String, String> map) throws Exception {
		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			map.put("saltText", saltText);
		}		
		return grpUserMapper.list_cm_grp_user(map);
	}
	
}