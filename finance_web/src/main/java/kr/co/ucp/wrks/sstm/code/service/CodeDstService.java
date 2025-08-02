package kr.co.ucp.wrks.sstm.code.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : CodeDstService
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
public interface CodeDstService {

	//List<Map<String, String>> sggList(Map<String, Object> args) throws Exception;

	List<Map<String, String>> codeDstList(Map<String, String> args) throws Exception;

	int insertCodeDst(Map<String, Object> args) throws Exception;

	int updateCodeDst(Map<String, Object> args) throws Exception;

	int deleteCodeDst(Map<String, String> args) throws Exception;

	int deleteCodeDstMulti(List<Map<String, String>> args) throws Exception;
}


