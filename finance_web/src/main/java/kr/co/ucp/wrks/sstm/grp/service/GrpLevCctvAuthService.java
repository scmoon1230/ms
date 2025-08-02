/**
 * -----------------------------------------------------------------
 * @Class Name : GrpLevCctvAuthService.java
 * @Description : 
 * @Version : 1.0
 * Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * -----------------------------------------------------------------
 * DATE           AUTHOR      DESCRIPTION
 * -----------------------------------------------------------------
 * 2018. 4. 9.   seungJun      New Write
 * -----------------------------------------------------------------
 */
package kr.co.ucp.wrks.sstm.grp.service;

import java.util.HashMap;
import java.util.List;

public interface GrpLevCctvAuthService
{
	List<HashMap<String, String>> grpList() throws Exception;
	List<HashMap<String, String>> grpAuthlist(HashMap<String, String> args) throws Exception;
	List<HashMap<String, String>> list(HashMap<String, String> args) throws Exception;
	int insert(HashMap<String, Object> args) throws Exception;
	int update(HashMap<String, Object> args) throws Exception;
	int delete(HashMap<String, Object> args) throws Exception;
}
