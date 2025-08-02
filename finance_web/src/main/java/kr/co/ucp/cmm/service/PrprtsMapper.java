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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper("prprtsMapper")
@Repository("prprtsMapper")
public interface PrprtsMapper {

	List<EgovMap> selectPrprtsList(String prprtsId);

	int insertPrprts(Map<String, Object> map);

	int updatePrprts(Map<String, Object> map);

	//String getCdDscrt(Map<String, Object> map);

	//String getUserNm(String userId);

	ArrayList<EgovMap> selectCodeList(String string);

	String getDigest(String userId);
}
