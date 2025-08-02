/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : CmmCodeMapper.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2014. 11. 7. is 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.cmm.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper("cmmCodeMapper")
public interface CmmCodeMapper {
	public List<EgovMap> selectCodeList(CmmCodeVO vo) throws Exception;

	public List<EgovMap> selectFcltKindCodeList() throws Exception;

	public List<EgovMap> selectDistrictList() throws Exception;

	public List<EgovMap> selectFcltUsedTyList(CmmCodeVO vo) throws Exception;

	public List<EgovMap> selectSigunguList() throws Exception;

	public List<EgovMap> selectSysList() throws Exception;

	public List<EgovMap> selectFcltUsedTyAll(CommonVO vo) throws Exception;

	public List<EgovMap> selectFcltUsedTy(FcltVO vo) throws Exception;

	public List<EgovMap> selectPlcList(CmmCodeVO vo) throws Exception;

	public int insertCmMuneUsedLog(Map<String, Object> args) throws Exception;

	List<EgovMap> selectListDstrtInfo(Map<String, String> params) throws Exception;

	public List<EgovMap> selectUserList(Map<String, Object> params);
}
