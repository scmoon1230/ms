package kr.co.ucp.wrks.sstm.menu.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.wrks.sstm.menu.service.MenuInfoMapper;
import kr.co.ucp.wrks.sstm.menu.service.MenuInfoService;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuInfoServiceImpl
 * @Description : 프로그램정보
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
@Service("menuInfoService")
public class MenuInfoServiceImpl implements MenuInfoService {
	
	@Resource(name="menuInfoMapper")
	private MenuInfoMapper menuInfoMapper;
	
	/*
	 * 프로그램정보 기능유형 설정
	 */
	@Override
	public List<Map<String, String>> fnctList(Map<String, Object> args) throws Exception {
		return menuInfoMapper.fnctList(args);
	}
	
	
	/*
     * 프로그램정보 조건검색
     */
	@Override
	public List<Map<String, String>> list(Map<String, String> args) throws Exception {
		return menuInfoMapper.list(args);
	}

	
	/*
     * 프로그램정보 등록
     */
	@Override
	public int insert(Map<String, Object> args) throws Exception {
		return menuInfoMapper.insert(args);
	}

	
	/*
     * 프로그램정보 수정
     */
	@Override
	public int update(Map<String, Object> args) throws Exception {
		return menuInfoMapper.update(args);
	}

	
	/*
     * 프로그램정보 삭제
     */
	@Override
	public int delete(Map<String, Object> args) throws Exception {
		return menuInfoMapper.delete(args);
	}

	
	/*
     * 프로그램정보 다중삭제
     */
	@Override
	public int deleteMulti(List<Map<String, Object>> args) throws Exception {
//		return menuInfoDAO.deleteMulti(args);
		int result = 0;
		
		for(int i=0; i<args.size(); i++){
			Map<String, Object> arg = args.get(i);
			result += menuInfoMapper.delete(arg);
		}
		
		return result;		
	}
}
