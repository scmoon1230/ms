package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.AcctgbMapper;
import kr.co.ucp.env.service.AcctgbService;

@Service("acctgbService")
public class AcctgbServiceImpl implements AcctgbService {

	@Resource(name="acctgbMapper")
	private AcctgbMapper acctgbMapper;

	// 재정구분 조건검색
	@Override
	public List<Map<String, String>> selectAcctgb(Map<String, String> args) throws Exception {
		return acctgbMapper.selectAcctgb(args);
	}

	// 재정구분 입력
	@Override
	public int insertAcctgb(Map<String, Object> args)  throws Exception {
		return acctgbMapper.insertAcctgb(args);
	}

	// 재정구분 수정
	@Override
	public int updateAcctgb(Map<String, Object> args) throws Exception {
		return acctgbMapper.updateAcctgb(args);
	}

	// 재정구분 삭제
	@Override
	public int deleteAcctgb(Map<String, String> args) throws Exception {
		return acctgbMapper.deleteAcctgb(args);
	}

	// 재정구분 다중삭제
	@Override
	public int deleteAcctgbMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			acctgbMapper.deleteAcctgb(arg);
		}

		return 1;
	}

}
