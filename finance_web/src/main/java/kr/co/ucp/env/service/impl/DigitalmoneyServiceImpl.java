package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.DigitalmoneyMapper;
import kr.co.ucp.env.service.DigitalmoneyService;

@Service("digitalmoneyService")
public class DigitalmoneyServiceImpl implements DigitalmoneyService {

	@Resource(name="digitalmoneyMapper")
	private DigitalmoneyMapper digitalmoneyMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectDigitalmoney(Map<String, String> args) throws Exception {
		return digitalmoneyMapper.selectDigitalmoney(args);
	}

	// 입력
	@Override
	public int insertDigitalmoney(Map<String, Object> args)  throws Exception {
		return digitalmoneyMapper.insertDigitalmoney(args);
	}

	// 수정
	@Override
	public int updateDigitalmoney(Map<String, Object> args) throws Exception {
		return digitalmoneyMapper.updateDigitalmoney(args);
	}

	// 삭제
	@Override
	public int deleteDigitalmoney(Map<String, String> args) throws Exception {
		return digitalmoneyMapper.deleteDigitalmoney(args);
	}

	// 다중삭제
	@Override
	public int deleteDigitalmoneyMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			digitalmoneyMapper.deleteDigitalmoney(arg);
		}

		return 1;
	}

}
