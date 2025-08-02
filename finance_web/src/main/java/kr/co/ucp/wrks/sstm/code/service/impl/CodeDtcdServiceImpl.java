package kr.co.ucp.wrks.sstm.code.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.code.service.CodeDtcdMapper;
import kr.co.ucp.wrks.sstm.code.service.CodeDtcdService;

import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------
 * @Class Name : CodeDtcdServiceImpl
 * @Description : 코드상세
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
@Service("codeDtcdService")
public class CodeDtcdServiceImpl implements CodeDtcdService {

	@Resource(name="codeDtcdMapper")
	private CodeDtcdMapper codeDtcdMapper;

	// 코드상세 조건검색
	@Override
	public List<Map<String, String>> dtcdList(Map<String, String> map) throws Exception {
		return codeDtcdMapper.dtcdList(map);
	}

	// 코드상세 입력
	@Override
	public int insert(Map<String, Object> map) throws Exception {
		return codeDtcdMapper.insert(map);
	}

	// 코드상세 수정
	@Override
	public int update(Map<String, Object> map) throws Exception {
		return codeDtcdMapper.update(map);
	}

	// 코드상세 삭제
	@Override
	public int delete(Map<String, String> map) throws Exception{
		return codeDtcdMapper.delete(map);
	}

	// 코드상세 다중삭제
	@Override
	public int deleteMulti(List<Map<String, String>> list) throws Exception {
		int result = 0;

		for(int i=0; i<list.size(); i++){
			Map<String, String> arg = (Map<String, String>)list.get(i);
			codeDtcdMapper.delete(arg);
		}

		return result;
	}

}
