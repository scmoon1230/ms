package kr.co.ucp.wrks.sstm.usr.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("usrApproveMapper")
public interface UsrApproveMapper {

	int approve(Map<String, Object> map) throws Exception;

	int notApprove(Map<String, Object> map) throws Exception;

//	List<Map<String, String>> selectCmGroupList(Map<String, String> args);

	List<Map<String, String>> selectCmGrpUserList(Map<String, String> args);

}
