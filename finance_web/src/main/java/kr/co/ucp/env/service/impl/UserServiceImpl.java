package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.UserMapper;
import kr.co.ucp.env.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource(name="userMapper")
	private UserMapper userMapper;

	// 사용자 조건검색
	@Override
	public List<Map<String, String>> selectUser(Map<String, String> args) throws Exception {
		return userMapper.selectUser(args);
	}

	// 사용자 입력
	@Override
	public int insertUser(Map<String, Object> args)  throws Exception {
		return userMapper.insertUser(args);
	}

	// 사용자 수정
	@Override
	public int updateUser(Map<String, Object> args) throws Exception {
		return userMapper.updateUser(args);
	}

	// 사용자 삭제
	@Override
	public int deleteUser(Map<String, String> args) throws Exception {
		return userMapper.deleteUser(args);
	}

	// 사용자 다중삭제
	@Override
	public int deleteUserMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			userMapper.deleteUser(arg);
		}

		return 1;
	}

	@Override
	public int selectUserIdCnt(Map<String, Object> map) throws Exception {
		return userMapper.selectUserIdCnt(map);
	}
	
}
