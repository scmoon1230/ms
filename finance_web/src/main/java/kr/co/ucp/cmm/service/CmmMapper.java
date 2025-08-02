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

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Repository;

@Mapper("cmmMapper")
@Repository("cmmMapper")
public interface CmmMapper {

	Map<String, String> getYmdHms();

	Map<String, String> getYmdHmsCal(Map<String, Object> args);

	List<EgovMap> selectTableInfoList(Map<String, Object> args);

	void updateUserPwd(Map<String, Object> args);

	Map<String, Object> getCdInfo(Map<String, Object> args);

	String getSeqDt();

	String getSeqNo(String prefix);
}