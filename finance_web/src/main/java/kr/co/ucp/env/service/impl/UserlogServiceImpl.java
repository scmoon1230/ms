package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.UserlogMapper;
import kr.co.ucp.env.service.UserlogService;

@Service("userlogService")
public class UserlogServiceImpl implements UserlogService {

	@Resource(name="userlogMapper")
	private UserlogMapper userlogMapper;

	// 사용기록 조건검색
	@Override
	public List<Map<String, String>> selectUserlog(Map<String, String> args) throws Exception {
		return userlogMapper.selectUserlog(args);
	}

}
