package kr.co.ucp.report.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.report.service.RprtmoneyMapper;
import kr.co.ucp.report.service.RprtmoneyService;

@Service("rprtmoneyService")
public class RprtmoneyServiceImpl implements RprtmoneyService {

	@Resource(name="rprtmoneyMapper")
	private RprtmoneyMapper rprtmoneyMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectRprtMoney(Map<String, String> args) throws Exception {
		return rprtmoneyMapper.selectRprtMoney(args);
	}

	// 합계
	@Override
	public String selectRprtMoneyTotalAmnt(Map<String, String> args) throws Exception {
		return rprtmoneyMapper.selectRprtMoneyTotalAmnt(args);
	}

	// 입력
	@Override
	public int insertMoney(Map<String, Object> args)  throws Exception {
		return rprtmoneyMapper.insertMoney(args);
	}

	// 수정
	@Override
	public int updateMoney(Map<String, Object> args) throws Exception {
		return rprtmoneyMapper.updateMoney(args);
	}

	// 삭제
	@Override
	public int deleteMoney(Map<String, String> args) throws Exception {
		return rprtmoneyMapper.deleteMoney(args);
	}

	// 다중삭제
	@Override
	public int deleteMoneyMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			rprtmoneyMapper.deleteMoney(arg);
		}

		return 1;
	}
	
}
