/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : ConfigureMapper.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 3. 18. SaintJuny@ubolt.co.kr 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.cmm.service;

import java.util.List;
import java.util.Map;

import kr.co.ucp.cmm.LoginVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper("configureMapper")
public interface ConfigureMapper {

//	ConfigureVO selectUmConfigure(ConfigureVO configure) throws Exception;

//	EgovMap selectUmConfigInfo(ConfigureVO configure) throws Exception;

	List<EgovMap> selectUmConfigList(Map<String, String> params) throws Exception;

	void insertUmConfigure(ConfigureVO configure) throws Exception;

//	void updateUmConfigure(ConfigureVO configure) throws Exception;

	List<ConfigureVO> selectMappingIp(EgovMap ipConfigure) throws Exception;

	List<EgovMap> selectCctvCtlUsedTy(LoginVO vo) throws Exception;

	EgovMap selectCctvCtlPtzTy(LoginVO vo) throws Exception;

	List<EgovMap> selectLayerMngList(ConfigureVO configure) throws Exception;

	int updateLayerMng(ConfigureVO configure) throws Exception;

	int updateAllLayerMng(ConfigureVO configure) throws Exception;

	//EgovMap selectCmConfig(Map<String, String> params) throws Exception;
	
	EgovMap selectDstrtInfo(Map<String, String> params) throws Exception;
	
	List<EgovMap> selectCmConfigList(Map<String, String> params) throws Exception;

//	public EgovMap selectTvoConfig(Map<String, String> params) throws Exception;
	
	List<EgovMap> selectTvoConfigList(Map<String, String> params) throws Exception;

//	public int updateConfig(Map<String, String> params) throws Exception;

}
