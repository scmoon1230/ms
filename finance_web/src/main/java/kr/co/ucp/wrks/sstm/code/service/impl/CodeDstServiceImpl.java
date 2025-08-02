package kr.co.ucp.wrks.sstm.code.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.code.service.CodeDstMapper;
import kr.co.ucp.wrks.sstm.code.service.CodeDstService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * ----------------------------------------------------------
 * @Class Name : CodeDstServiceImpl
 * @Description : 지구코드
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
@Service("codeDstService")
public class CodeDstServiceImpl extends EgovAbstractServiceImpl implements CodeDstService {
	
	static Logger logger = LoggerFactory.getLogger(CodeDstServiceImpl.class);
	
	@Resource(name="codeDstMapper")
	private CodeDstMapper codeDstMapper;

	// 시군구코드 목록조회
//	@Override
//	public List<Map<String, String>> sggList( Map<String, Object> args) throws Exception {
//		List<Map<String, String>> list = codeDstMapper.sggList(args);
//		return list;
//	}

	// 지구코드그룹 조건검색
	@Override
	public List<Map<String, String>> codeDstList( Map<String, String> args) throws Exception {
		List<Map<String, String>> list = codeDstMapper.codeDstList(args);
		return list;
	}

	// 지구코드그룹 입력
	@Override
	public int insertCodeDst(Map<String, Object> args) throws Exception {
		int ret = codeDstMapper.insertCodeDst(args);
		return ret;
	}

	// 지구코드그룹 수정
	@Override
	public int updateCodeDst(Map<String, Object> args) throws Exception {
		int ret = codeDstMapper.updateCodeDst(args);
		return ret;
	}

	// 지구코드그룹 삭제
	@Override
	public int deleteCodeDst(Map<String, String> args) throws Exception {
		int ret = codeDstMapper.deleteCodeDst(args);
		return ret;
	}

	// 지구코드그룹 다중삭제
	@Override
	public int deleteCodeDstMulti(List<Map<String, String>> list) throws Exception {
		int result = 0;

		for(int i=0; i<list.size(); i++){
			Map<String, String> arg = (Map<String, String>)list.get(i);
			codeDstMapper.deleteCodeDst(arg);
		}

		return result;
	}
	
}
