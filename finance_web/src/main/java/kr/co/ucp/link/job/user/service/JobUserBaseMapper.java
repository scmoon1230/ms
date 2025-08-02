package kr.co.ucp.link.job.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("JobUserBaseMapper")
public interface JobUserBaseMapper {
	
	List<Map<String, Object>> selectDstrtUserList(Map<String, Object> map);

	HashMap<String, Object> updateUserList();

	int updateUserUseTyCd(HashMap<String, Object> l_map);

	int updateUser(HashMap<String, Object> r_map);

	int insertUser(HashMap<String, Object> r_map);

	void insertGrpUser(HashMap<String, Object> r_map);

//	void insertUserDstrt(HashMap<String, Object> r_map);
	
}
