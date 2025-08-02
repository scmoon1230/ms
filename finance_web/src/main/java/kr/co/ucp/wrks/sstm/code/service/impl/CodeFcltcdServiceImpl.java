package kr.co.ucp.wrks.sstm.code.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.code.service.CodeFcltcdMapper;
import kr.co.ucp.wrks.sstm.code.service.CodeFcltcdService;

import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------
 * @Class Name : CodeFcltcdServiceImpl
 * @Description : 시설물용도별유형코드
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-03-19   설준환       최초작성
 *
 * ----------------------------------------------------------
 * */
@Service("codeFcltcdService")
public class CodeFcltcdServiceImpl implements CodeFcltcdService {

	@Resource(name="codeFcltcdMapper")
	private CodeFcltcdMapper codeFcltcdMapper;

	/*
     * 시설물용도별유형코드 조건검색
     */

	@Override
	public List<Map<String, String>> fcltcdList(Map<String, String> args) throws Exception {
		return codeFcltcdMapper.fcltcdList(args);
	}

	/*
     * 시설물용도별유형코드 입력
     */
	@Override
	public int insert(Map<String, Object> args)  throws Exception {
		return codeFcltcdMapper.insert(args);
	}

	/*
     * 시설물용도별유형코드 수정
     */
	@Override
	public int update(Map<String, Object> args) throws Exception {
		return codeFcltcdMapper.update(args);
	}

	/*
     * 시설물용도별유형코드 삭제
     */
	@Override
	public int delete(Map<String, String> args) throws Exception {
		return codeFcltcdMapper.delete(args);
	}

	/*
     * 시설물용도별유형코드 다중삭제
     */
	@Override
	public int deleteMulti(List<Map<String, String>> list) throws Exception{
		int result = 0;

		for(int i=0; i<list.size(); i++){
		Map<String, String> arg = (Map<String, String>)list.get(i);
		codeFcltcdMapper.delete(arg);
		}

		return result;
		}


}