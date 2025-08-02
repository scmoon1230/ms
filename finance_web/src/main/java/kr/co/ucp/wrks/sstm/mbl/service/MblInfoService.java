package kr.co.ucp.wrks.sstm.mbl.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : MblInfoService
 * @Description : 모바일 기기정보
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
public interface MblInfoService {

	Map<String, String> getUser(String userid) throws Exception;

	List<Map<String, String>> getList(Map<String, String> args) throws Exception;

	List<Map<String, String>> getListDetail(String mppId) throws Exception;

	List<Map<String, String>> getListMapType() throws Exception;

	List<Map<String, String>> getListOsType() throws Exception;

	int insert(Map<String, Object> args, List<Map<String, String>> list) throws Exception;

	int update(Map<String, Object> args) throws Exception;

	int delete(Map<String, Object> args) throws Exception;

	int deleteMulti(List<Map<String, Object>> args) throws Exception;

	Map<String, Object> excelUpload(List<Map<String, String>> args) throws Exception;

	List<Map<String, String>> rcvTimeList(Map<String, Object> args) throws Exception;

	int deleteRcvTime(Map<String, Object> args) throws Exception;

	int updateRcvTime(List<Map<String, String>> args) throws Exception;

	int insertRcvTime(List<Map<String, String>> args) throws Exception;

	List<Map<String, String>> getUserList(Map<String, Object> args) throws Exception;

}



