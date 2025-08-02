package kr.co.ucp.wrks.sstm.usr.service;

import java.util.List;
import java.util.Map;

public interface UsrInfoService {
	
	// 사용자관리 사용자별 그룹 조회(등록된 그룹 이외)
	List<Map<String, String>> getCmGroupList(Map<String, String> args) throws Exception;
	
	// 사용자관리 사용자별 그룹 조회
	List<Map<String, String>> getCmGrpUserList(Map<String, String> args) throws Exception;
	
	// 사용자관리 검색
	List<Map<String, String>> selectUserList(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectAllUserList(Map<String, String> args) throws Exception;
	
	// 상세
	Map<String, String> selectUserDtl(Map<String, String> map) throws Exception;

	// 사용자아이디 중복 확인
	int selectUserIdCnt(Map<String, Object> map) throws Exception;
	
	// 사용자관리 입력 
	int insertUser(Map<String, Object> map, List<Map<String, String>> grplist, Map<String, String> dstrtMap) throws Exception;
	
	// 사용자관리 사용자그룹 추가 
	int insertMultiGrpUser(List<Map<String, String>> args) throws Exception;
	
	// 사용자관리 수정 
	int updateUser(Map<String, Object> map, List<Map<String, String>> grplist ) throws Exception;

	// 사용자관리 수정 
	int update(Map<String, Object> map ) throws Exception;

	// 사용자관리 삭제 
	int deleteUser(Map<String, Object> map) throws Exception;
	
	// 사용자관리 다중삭제 
	int deleteMulti(List<Map<String, Object>> list) throws Exception;
	
	// 사용자관리 사용자그룹 다중삭제 
	int deleteMultiGrpUser(List<Map<String, String>> args) throws Exception;
	
	// 사용자관리 사용자지역 다중삭제 
	//int deleteMultiUserArea(List<Map<String, String>> args) throws Exception;
	
	// 사용자관리 사용자지구 입력
	//int insertUserDstrt(Map<String, String> map) throws Exception;
	
	// 사용자관리 사용자지구 삭제
	//int deleteUserDstrt(Map<String, String> map) throws Exception;

	// 사용자관리 사용자 그룹조회
	List<Map<String, String>> getDstrtCd(Map<String, String> args) throws Exception;
	
	// 사용자관리 등록된 그룹아이디 조회
	List<Map<String, String>> getGrpId(Map<String, String> args) throws Exception;
	
	// 사용자관리 등록된 지구코드 조회
	List<Map<String, String>> getDstrtCdList(Map<String, String> map) throws Exception;
	
	// 기관명 등록
	Map<String, String> registerInsttInfo(Map<String, String> map) throws Exception;
	
}
