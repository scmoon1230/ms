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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.mntr.cmm.service.ConfigureVO;

public interface PrprtsService {

	void selectPrprtsList() throws Exception;

//	List<EgovMap> selectPrprtsList(String PrprtsId) throws Exception;

	String getString(String key);

	String getString(String key, String defaultValue);

	void setString(String key, String value);

	int getInt(String key);

	int getInt(String key, int defaultValue);

	String getGlobals(String key);

	String getGlobals(String key, String defaultValue);

	void reloadPrprts(HttpServletRequest request) throws Exception;

	void reloadSession(HttpServletRequest request) throws Exception;

	//String getCdDscrt(String cdId, String cdGrpId) throws Exception;

	//String getUserNm(String userId) throws Exception;

	int updatePrprts(Map<String, Object> map) throws Exception;

	void selectCodeList() throws Exception;

	List<EgovMap> getList(String key);

	String getDigest(String userId);

	String getSalt(int b);
}
