package kr.co.ucp.wrks.sstm.usr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeDtcdMapper;
import kr.co.ucp.wrks.sstm.usr.service.UsrApproveMapper;
import kr.co.ucp.wrks.sstm.usr.service.UsrApproveService;

@Service("usrApproveService")
public class UsrApproveServiceImpl implements UsrApproveService {

	@Resource(name="codeDtcdMapper")
	private CodeDtcdMapper codeDtcdMapper;

	@Resource(name="usrApproveMapper")
	private UsrApproveMapper usrApproveMapper;

	// 사용자 승인
	@Override
	public int approve(Map<String, Object> map) throws Exception {
		 List<String> userIdList = (List<String>) map.get("userId");
		 List<String> filteredUserIdList = userIdList.stream()
				 	.map(userId -> ("sys".equals(userId) || "scmng".equals(userId)) ? "" : userId)
				    .collect(Collectors.toList());
		 map.replace("userId", filteredUserIdList);
		 
		return usrApproveMapper.approve(map);
	}

	// 사용자 미승인
	@Override
	public int notApprove(Map<String, Object> map) throws Exception {
		 List<String> userIdList = (List<String>) map.get("userId");
		 List<String> filteredUserIdList = userIdList.stream()
				 	.map(userId -> ("sys".equals(userId) || "scmng".equals(userId)) ? "" : userId)
				    .collect(Collectors.toList());
		 map.replace("userId", filteredUserIdList);

		return usrApproveMapper.notApprove(map);
	}
	
//	@Override
//	public List<Map<String, String>> getCmGroupList(Map<String, String> args) throws Exception {
//		return usrApproveMapper.selectCmGroupList(args);
//	}
	
	@Override
	public List<Map<String, String>> getCmGrpUserList(Map<String, String> args) throws Exception {
		return usrApproveMapper.selectCmGrpUserList(args);
	}
	
}
