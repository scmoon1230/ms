/**
 * -----------------------------------------------------------------
 *
 * @Class Name : WrkrptLogService.java
 * @Description :
 * @Version : 1.0 Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * -----------------------------------------------------------------
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------------------------
 * 2017. 10. 31.   seungJun      New Write
 * -----------------------------------------------------------------
 *               </pre>
 */
package kr.co.ucp.cmm.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CmmService {

	Map<String, String> getYmdHms() throws Exception;

	Map<String, String> getYmdHmsCal(Map<String, Object> args) throws Exception;

	List<EgovMap> selectTableInfoList(String tableName) throws Exception;

	Map<String, Object> cmmChgPw(String userId) throws Exception;

	Map<String, Object> getCdInfo(String cdId, String cdGrpId) throws Exception;

	String getPwd(String pwd, String ty, String userId) throws Exception;

	void buildExcelDocument(Map<String, Object> modelMap, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
}