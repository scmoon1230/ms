package kr.co.ucp.fin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.fin.service.FinmngMapper;
import kr.co.ucp.fin.service.FinmngService;

@Service("finmngService")
public class FinmngServiceImpl implements FinmngService {

	@Resource(name="finmngMapper")
	private FinmngMapper finmngMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectFinance(Map<String, String> args) throws Exception {
		return finmngMapper.selectFinance(args);
	}

	// 입력
	@Override
	public int insertFinance(Map<String, Object> args)  throws Exception {
		return finmngMapper.insertFinance(args);
	}

	// 수정
	@Override
	public int updateFinance(Map<String, Object> args) throws Exception {
		return finmngMapper.updateFinance(args);
	}

	// 삭제
	@Override
	public int deleteFinance(Map<String, String> args) throws Exception {
		return finmngMapper.deleteFinance(args);
	}

	// 다중삭제
	@Override
	public int deleteFinanceMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			finmngMapper.deleteFinance(arg);
		}

		return 1;
	}

}
