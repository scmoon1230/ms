package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.wrks.sstm.grp.service.GrpUserAccMapper;
import kr.co.ucp.wrks.sstm.grp.service.GrpUserAccService;

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
@Service("grpUserAccService")
public class GrpUserAccServiceImpl implements GrpUserAccService {

	@Resource(name="grpUserAccMapper")
	private GrpUserAccMapper grpUserAccMapper;


	/*
     * 그룹별이벤트 그룹별 권한레벨 조회
     */
	@Override
	public List<Map<String, String>> getCM_GRP_AUTH_LVL(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_GRP_AUTH_LVL(map);
	}

	/*
     * 그룹별이벤트 지구코드 조회
     */
	@Override
	public List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_DSTRT_CD_MNG(map);
	}

	/*
     * 그룹별이벤트 조건검색
     */
	@Override
	public List<Map<String, String>> getCM_GROUP(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_GROUP(map);
	}

	/*
     * 그룹별이벤트 이벤트 조건검색
     */
	@Override
	public List<Map<String, String>> getCM_GRP_EVT(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_GRP_EVT(map);
	}

	/*
     * 그룹별이벤트 이벤트조회
     */
	@Override
	public List<Map<String, String>> getCM_EVENT(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_EVENT(map);
	}

	/*
     * 그룹별이벤트 사용자권한레벨 조건검색
     */
	@Override
	public List<Map<String, String>> getCM_GRP_EVT_USER_LVL(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_GRP_EVT_USER_LVL(map);
	}

	/*
     * 그룹별이벤트 사용자권한레벨 조회
     */
	@Override
	public List<Map<String, String>> getCM_USER_POPUP(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_USER_POPUP(map);
	}

	/*
     * 그룹별이벤트 모바일기기권한레벨 조건검색
     */
	@Override
	public List<Map<String, String>> getCM_GRP_EVT_MOBL_LVL(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_GRP_EVT_MOBL_LVL(map);
	}

	/*
     * 그룹별이벤트 모바일기기권한레벨 조회
     */
	@Override
	public List<Map<String, String>> getCM_GRP_MOBL(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_GRP_MOBL(map);
	}

	/*
     * 그룹별이벤트 이벤트 입력
     */
	@Override
	public int insert_event(List<Map<String, String>> args) throws Exception {

		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpUserAccMapper.insert_event(arg);
		}
		return 1;
	}

	/*
     * 그룹별이벤트 이벤트 삭제
     */
	@Override
	public int delete_event(List<Map<String, String>> args) throws Exception {

		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpUserAccMapper.delete_event(arg);
		}
		return 1;
	}

	/*
     * 그룹별이벤트 사용자권한레벨 입력
     */
	@Override
	public int insert_user_acc(List<Map<String, String>> args) throws Exception {

		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpUserAccMapper.insert_user_acc(arg);
		}
		return 1;
	}

	/*
     * 그룹별이벤트 사용자권한레벨 수정
     */
	@Override
	public int update_user_acc(List<Map<String, String>> args) throws Exception {

		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpUserAccMapper.update_user_acc(arg);
		}
		return 1;
	}

	/*
     * 그룹별이벤트 사용자권한레벨 삭제
     */
	@Override
	public int delete_user_acc(List<Map<String, String>> args) throws Exception {

		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpUserAccMapper.delete_user_acc(arg);
		}
		return 1;
	}

	/*
     * 그룹별이벤트 모바일기기권한레벨 입력
     */
	@Override
	public int insert_mobl_acc(List<Map<String, String>> args) throws Exception {

		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpUserAccMapper.insert_mobl_acc(arg);
		}
		return 1;
	}

	/*
     * 그룹별이벤트 모바일기기권한레벨 수정
     */
	@Override
	public int update_mobl_acc(List<Map<String, String>> args) throws Exception {

		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpUserAccMapper.update_mobl_acc(arg);
		}
		return 1;
	}

	/*
     * 그룹별이벤트 모바일기기권한레벨 삭제
     */
	@Override
	public int delete_mobl_acc(List<Map<String, String>> args) throws Exception {

		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpUserAccMapper.delete_mobl_acc(arg);
		}
		return 1;
	}

	/*
     * 그룹별이벤트 사용자권한레벨 입력 선택추가
     */
	@Override
	public int insert_user_acc_sel(List<Map<String, String>> args) throws Exception {

		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpUserAccMapper.insert_user_acc_sel(arg);
		}
		return 1;
	}

	/*
     * 그룹별이벤트 사용자권한레벨 조회 선택추가 팝업
     */
	@Override
	public List<Map<String, String>> getCM_USER_SEL_POPUP(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_USER_SEL_POPUP(map);
	}

	/*
     * 그룹별이벤트 모바일기기권한레벨 조회 선택추가 팝업
     */
	@Override
	public List<Map<String, String>> getCM_MOBL_SEL_POPUP(Map<String, String> map) throws Exception {
		return grpUserAccMapper.getCM_MOBL_SEL_POPUP(map);
	}

	/*
     * 그룹별이벤트 모바일기기권한레벨 입력 선택추가
     */
	@Override
	public int insert_mobl_acc_sel(List<Map<String, String>> args) throws Exception {

		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			
			grpUserAccMapper.insert_mobl_acc_sel(arg);
		}
		return 1;
	}

}
