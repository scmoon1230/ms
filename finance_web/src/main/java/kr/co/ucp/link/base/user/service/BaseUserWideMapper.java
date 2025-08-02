package kr.co.ucp.link.base.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("BaseUserWideMapper")
public interface BaseUserWideMapper {
	
	List<Map<String, Object>> selectUserList(Map<String, Object> map);

	//HashMap<String, Object> updateUserList();

	int updateUserUseTyCd(HashMap<String, Object> localMap);

	int updateUser(HashMap<String, Object> r_map);

	int insertUser(HashMap<String, Object> r_map);

	void insertGrpUser(HashMap<String, Object> r_map);

	//void insertUserDstrt(HashMap<String, Object> r_map);

	//Map<String, Object> selectDstrtCd();

}
