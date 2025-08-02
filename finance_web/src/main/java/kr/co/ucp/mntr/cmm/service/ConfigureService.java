/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : ConfigureService.java
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

import javax.servlet.http.HttpServletRequest;

import kr.co.ucp.cmm.LoginVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface ConfigureService {
	
//	ConfigureVO getUmConfigure(ConfigureVO configure) throws Exception;

	EgovMap getUmConfigInfo() throws Exception;

	void registerUmConfigure(ConfigureVO configure) throws Exception;

//	void updateUmConfigure(ConfigureVO configure) throws Exception;

	List<ConfigureVO> selectMappingIp(EgovMap ipConfigure) throws Exception;

	List<EgovMap> selectCctvCtlUsedTy(LoginVO vo) throws Exception;

	EgovMap selectCctvCtlPtzTy(LoginVO resultVO) throws Exception;

	List<EgovMap> selectLayerMngList(ConfigureVO configure) throws Exception;

	int updateLayerMng(ConfigureVO configure) throws Exception;

	int updateAllLayerMng(ConfigureVO configureVO) throws Exception;

	EgovMap getConfigure(String userId, String dstrtCd, String sysId) throws Exception;

	EgovMap refresh(EgovMap configure, LoginVO lgnVO, HttpServletRequest request) throws Exception;

	Map<String, String> getIpMapping(EgovMap configure) throws Exception;

	EgovMap getCmConfig() throws Exception;

	EgovMap getTvoConfig() throws Exception;

//	Map<String, String> updateConfig(Map<String, String> params) throws Exception;

}
