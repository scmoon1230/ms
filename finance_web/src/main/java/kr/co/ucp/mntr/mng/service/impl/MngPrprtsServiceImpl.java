package kr.co.ucp.mntr.mng.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.mntr.mng.service.MngPrprtsService;

@Service("mngPrprtsService")
public class MngPrprtsServiceImpl implements MngPrprtsService {

	@Resource(name="mngPrprtsMapper")
	private MngPrprtsMapper mngPrprtsMapper;

	@Override
	public List<Map<String, Object>> prprtsIdList(Map<String, Object> args) throws Exception {
		return mngPrprtsMapper.prprtsIdList(args);
	}

	@Override
	public List<Map<String, Object>> prprtsTyList(Map<String, Object> args) throws Exception {
		return mngPrprtsMapper.prprtsTyList(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> prprtsList(Map<String, String> args) throws Exception {
		return mngPrprtsMapper.prprtsList(args);
	}

	// 입력
	@Override
	public int insert(Map<String, Object> args) throws Exception {
		return mngPrprtsMapper.insert(args);
	}

	// 수정
	@Override
	public int update(Map<String, Object> args) throws Exception {
		return mngPrprtsMapper.update(args);
	}

	// 삭제
	@Override
	public int delete(Map<String, String> args) throws Exception {
		return mngPrprtsMapper.delete(args);
	}

	// 다중삭제
	@Override
	public int deleteMulti(List<Map<String, String>> list) throws Exception {
		for(int i=0; i<list.size(); i++){
			Map<String, String> arg = (Map<String, String>)list.get(i);
			mngPrprtsMapper.delete(arg);
		}
		return 1;
	}
}


