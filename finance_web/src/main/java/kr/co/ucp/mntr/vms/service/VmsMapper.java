/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : VmsMapper.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 11. 25. SaintJuny@ubolt.co.kr 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.vms.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper("vmsMapper")
public interface VmsMapper {

	void insertPtzLog(VmsLogVO vo) throws Exception;

	void insertViewLog(VmsLogVO vo) throws Exception;

	List<EgovMap> selectMngMemberList(Map<String, String> params) throws Exception;

	List<EgovMap> selectSameLocationList(Map<String, String> params) throws Exception;
}
