package kr.co.ucp.report.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.report.service.RprtsumyearMapper;
import kr.co.ucp.report.service.RprtsumyearService;

@Service("rprtsumyearService")
public class RprtsumyearServiceImpl implements RprtsumyearService {

	@Resource(name="rprtsumyearMapper")
	private RprtsumyearMapper rprtsumyearMapper;

	// 조건검색
	@Override
	public Map<String, String> selectDurationYear(Map<String, String> args) throws Exception {
		return rprtsumyearMapper.selectDurationYear(args);
	}

	// 조건검색
	@Override
	public Map<String, String> selectNextMonth(Map<String, String> args) throws Exception {
		return rprtsumyearMapper.selectNextMonth(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyMonth(Map<String, String> args) throws Exception {
		return rprtsumyearMapper.selectMoneyMonth(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyYear(Map<String, String> args) throws Exception {
		return rprtsumyearMapper.selectMoneyYear(args);
	}

	// 합계
	@Override
	public String selectMonthAmnt(Map<String, String> args) throws Exception {
		return rprtsumyearMapper.selectMonthAmnt(args);
	}

	// 합계
	@Override
	public String selectTotalAmnt(Map<String, String> args) throws Exception {
		return rprtsumyearMapper.selectTotalAmnt(args);
	}

	// 입력
	@Override
	public int insertMoney(Map<String, Object> args)  throws Exception {
		return rprtsumyearMapper.insertMoney(args);
	}

	// 수정
	@Override
	public int updateMoney(Map<String, Object> args) throws Exception {
		return rprtsumyearMapper.updateMoney(args);
	}

	// 삭제
	@Override
	public int deleteMoney(Map<String, String> args) throws Exception {
		return rprtsumyearMapper.deleteMoney(args);
	}

	// 다중삭제
	@Override
	public int deleteMoneyMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			rprtsumyearMapper.deleteMoney(arg);
		}

		return 1;
	}
	
}
