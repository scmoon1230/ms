package kr.co.ucp.wrks.sstm.usr.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("usrInfoMapper")
public interface UsrInfoMapper {

	List<Map<String, String>> selectCmGroupList(Map<String, String> args);

	List<Map<String, String>> selectCmGrpUserList(Map<String, String> args);

	List<Map<String, String>> selectUserList(Map<String, String> map);

	List<Map<String, String>> selectAllUserList(Map<String, String> map);

	Map<String, String> selectUserDtl(Map<String, String> map);

//	int insert(Map<String, Object> map, List<Map<String, String>> grplist,
//	int update(Map<String, Object> map, List<Map<String, String>> grplist);
//	int deleteUser2(Map<String, String> map);	

	int deleteMulti(List<Map<String, String>> list);
	int deleteMultiGrpUser(List<Map<String, String>> args);
	int deleteMultiUserArea(List<Map<String, String>> args);

	//int insertUserDstrt(Map<String, String> map);
	//int deleteUserDstrt(Map<String, String> map);

	List<Map<String, String>> getDstrtCd(Map<String, String> args);

	List<Map<String, String>> getGrpId(Map<String, String> args);

	List<Map<String, String>> getDstrtCdList(Map<String, String> map);

	int selectUserIdCnt(Map<String, Object> map);

	int insertUserInfo(Map<String, Object> map);
	int updateUserInfo(Map<String, Object> map);
	int updateUser(Map<String, Object> map);
	int deleteUser(Map<String, Object> map);

	int insertCmGrpUser(Map<String, String> arg);
	int updateCmGrpUser(Map<String, String> arg);
	int deleteCmGrpUser(Map<String, String> arg);

}
