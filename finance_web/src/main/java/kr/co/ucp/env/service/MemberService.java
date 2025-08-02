package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

public interface MemberService {

	List<Map<String, String>> selectMember(Map<String, String> args) throws Exception;

	int insertMember(Map<String, Object> args) throws Exception;

	int updateMember(Map<String, Object> args) throws Exception;

	int deleteMember(Map<String, String> args) throws Exception;

//	int deleteMemberMulti(List<Map<String, String>> args) throws Exception;
	
}
