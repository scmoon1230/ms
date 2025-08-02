package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("userMapper")
public interface UserMapper {

	List<Map<String, String>> selectUser(Map<String, String> args) throws Exception;

	int insertUser(Map<String, Object> args) throws Exception;

	int updateUser(Map<String, Object> args) throws Exception;

	int deleteUser(Map<String, String> args) throws Exception;

	int selectUserIdCnt(Map<String, Object> map);

}
