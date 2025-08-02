package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("memberMapper")
public interface MemberMapper {

	List<Map<String, String>> selectMember(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectMemberExt(Map<String, String> args) throws Exception;

	Map<String, String> selectNewMemberId(Map<String, String> args) throws Exception;

	int insertMember(Map<String, Object> args) throws Exception;

	int updateMember(Map<String, Object> args) throws Exception;

	int deleteMember(Map<String, String> args) throws Exception;

}
