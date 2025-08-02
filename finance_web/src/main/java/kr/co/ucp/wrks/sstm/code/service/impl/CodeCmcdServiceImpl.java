package kr.co.ucp.wrks.sstm.code.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.code.service.CodeCmcdMapper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * ----------------------------------------------------------
 * @Class Name : CodeCmcdServiceImpl
 * @Description : 공통코드그룹
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

@Service("codeCmcdService")
public class CodeCmcdServiceImpl extends EgovAbstractServiceImpl implements CodeCmcdService {

	@Resource(name="codeCmcdMapper")
	private CodeCmcdMapper codeCmcdMapper;

	@Override
	public List<Map<String, String>> sysList(Map<String, Object> map) throws Exception {
		return codeCmcdMapper.sysList(map);
	}

	// 코드별 그룹조회
	@Override
	public List<Map<String, String>> grpList(Map<String, Object> map) throws Exception {
		List<Map<String, String>> list;
		if(map.get("sysCd") != null){
			list = codeCmcdMapper.sysList(map);
		}else{
			list = codeCmcdMapper.grpList(map);
		}
		return list;
	}

	// 시스템 그룹조회
	@Override
	public List<Map<String, String>> sysCodeList(Map<String, Object> map) throws Exception {
		return codeCmcdMapper.sysCodeList(map);
	}

	// 공통코드그룹 조건검색
	@Override
	public List<Map<String, String>> cmcdList(Map<String, String> map) throws Exception {
		return codeCmcdMapper.cmcdList(map);
	}

	// 공통코드그룹 등록
	@Override
	public int insert(Map<String, Object> map) throws Exception {
		return codeCmcdMapper.insert(map);
	}

	// 공통코드그룹 삭제
	@Override
	public int delete(Map<String, String> map) throws Exception {
		return codeCmcdMapper.delete(map);
	}

	// 공통코드그룹 수정
	@Override
	public int update(Map<String, Object> map) throws Exception {
		return codeCmcdMapper.update(map);
	}

	// 공통코드그룹 다중삭제
	@Override
	public int deleteMulti(List<Map<String, String>> list) throws Exception {
		for(int i=0; i<list.size(); i++){
			Map<String, String> arg = (Map<String, String>)list.get(i);
			codeCmcdMapper.delete(arg);
		}
		return 1;
	}

	// 현재시간 검색
	@Override
	public String getCurrentDay() throws Exception {
		return codeCmcdMapper.getCurrentDay();
	}

}
