package kr.co.ucp.yearend.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.yearend.service.YearendmemberMapper;
import kr.co.ucp.yearend.service.YearendmemberService;

@Service("yearendmemberService")
public class YearendmemberServiceImpl implements YearendmemberService {

	@Resource(name="yearendmemberMapper")
	private YearendmemberMapper yearendmemberMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectMember(Map<String, String> args) throws Exception {
		return yearendmemberMapper.selectMember(args);
	}

}
