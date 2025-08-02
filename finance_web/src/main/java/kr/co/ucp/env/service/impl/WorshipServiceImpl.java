package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.WorshipMapper;
import kr.co.ucp.env.service.WorshipService;

@Service("worshipService")
public class WorshipServiceImpl implements WorshipService {

	@Resource(name="worshipMapper")
	private WorshipMapper worshipMapper;

	// 예배코드 조건검색
	@Override
	public List<Map<String, String>> selectWorship(Map<String, String> args) throws Exception {
		return worshipMapper.selectWorship(args);
	}

	// 예배코드 입력
	@Override
	public int insertWorship(Map<String, Object> args)  throws Exception {
		return worshipMapper.insertWorship(args);
	}

	// 예배코드 수정
	@Override
	public int updateWorship(Map<String, Object> args) throws Exception {
		return worshipMapper.updateWorship(args);
	}

	// 예배코드 삭제
	@Override
	public int deleteWorship(Map<String, String> args) throws Exception {
		return worshipMapper.deleteWorship(args);
	}

	// 예배코드 다중삭제
	@Override
	public int deleteWorshipMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			worshipMapper.deleteWorship(arg);
		}

		return 1;
	}

}
