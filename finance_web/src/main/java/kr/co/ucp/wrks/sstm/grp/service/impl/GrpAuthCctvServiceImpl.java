package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.wrks.sstm.grp.service.GrpAuthCctvMapper;
import kr.co.ucp.wrks.sstm.grp.service.GrpAuthCctvService;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpUserAccServiceImpl
 * @Description : 그룹별이벤트
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
@Service("grpAuthCctvService")
public class GrpAuthCctvServiceImpl implements GrpAuthCctvService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="grpAuthCctvMapper")
	private GrpAuthCctvMapper grpAuthCctvMapper;
	
	/*
     * 그룹별이벤트 지구코드 조회
     */
	@Override
	public List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception {
		return grpAuthCctvMapper.list_cm_dstrt_cd_mng(map);
	}
	
	/*
     * 그룹리스트
     */
	@Override
	public List<Map<String, String>> getGroupList(Map<String, String> map) throws Exception{
		return grpAuthCctvMapper.list_cm_group(map);
	}

	/*
     * 그룹별사용자 리스트
     */
	@Override
	public List<Map<String, String>> getUserList(Map<String, String> map) throws Exception{

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			map.put("saltText", saltText);
		}		
		
		return grpAuthCctvMapper.list_cm_grp_user(map);
	}

	/*
     * 그룹별 사용유형
     */
	@Override
	public List<Map<String, String>> getCctvUsedList(Map<String, String> map) throws Exception{
		return grpAuthCctvMapper.list_cm_cctv_used(map);
	}
	
	/*
     * 그룹별 사용권한
     */
	@Override
	public List<Map<String, String>> getCctvAuthList(Map<String, String> map) throws Exception{
		return grpAuthCctvMapper.list_cm_cctv_auth(map);
	}
	
	/*
     * 시설물 용도 리스트
     */
	@Override
	public List<Map<String, String>> getFcltUseList(Map<String, String> map) throws Exception{
		return grpAuthCctvMapper.list_fclt_use(map);
	}
	
	/*
     * 사용유형 입력
     */
	@Override
	public int insert_cctv_used(List<Map<String, String>> args) throws Exception {
		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			grpAuthCctvMapper.insert_cctv_used(arg);
		}
		return 1;
	}
	
	
	/*
     * 그룹별이벤트 사용자권한레벨 삭제
     */
	@Override
	public int delete_cctv_used(List<Map<String, String>> args) throws Exception {
		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			grpAuthCctvMapper.delete_cctv_used(arg);
		}
		return 1;
	}
		
	/*
     * 사용유형 입력
     */
	@Override
	public int insert_cctv_auth(List<Map<String, String>> args) throws Exception {
		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpAuthCctvMapper.insert_cctv_auth(arg);
		}
		return 1;
	}
	
	
	/*
     * 그룹별이벤트 사용자권한레벨 삭제
     */
	@Override
	public int delete_cctv_auth(List<Map<String, String>> args) throws Exception {
		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			grpAuthCctvMapper.delete_cctv_auth(arg);
		}
		return 1;
	}

	
	
}
