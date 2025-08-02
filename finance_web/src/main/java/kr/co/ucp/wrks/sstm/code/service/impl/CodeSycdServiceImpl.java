package kr.co.ucp.wrks.sstm.code.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.code.service.CodeSycdMapper;
import kr.co.ucp.wrks.sstm.code.service.CodeSycdService;

import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------
 * @Class Name : CodeSycdServiceImpl
 * @Description : 시스템코드
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
@Service("codeSycdService")
public class CodeSycdServiceImpl implements CodeSycdService {

	@Resource(name="codeSycdMapper")
	private CodeSycdMapper codeSycdMapper;

	// 시스템코드 조건검색
	@Override
	public List<Map<String, String>> sycdList(Map<String, String> args) throws Exception {
		return codeSycdMapper.sycdList(args);
	}

	// 시스템코드 입력
	@Override
	public int insert(Map<String, Object> args) throws Exception {
		return codeSycdMapper.insert(args);
	}

	// 시스템코드 수정
	@Override
	public int update(Map<String, Object> args) throws Exception {
		return codeSycdMapper.update(args);
	}

	// 시스템코드 삭제
	@Override
	public int delete(Map<String, String> args) throws Exception {
		return codeSycdMapper.delete(args);
	}

	// 시스템코드 다중삭제
	@Override
	public int deleteMulti(List<Map<String, String>> list) throws Exception {
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			codeSycdMapper.delete(arg);
		}

		return 1;
	}
}


