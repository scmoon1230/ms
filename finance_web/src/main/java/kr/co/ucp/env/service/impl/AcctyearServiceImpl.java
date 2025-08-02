package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.AcctyearMapper;
import kr.co.ucp.env.service.AcctyearService;

@Service("acctyearService")
public class AcctyearServiceImpl implements AcctyearService {

	@Resource(name="acctyearMapper")
	private AcctyearMapper acctyearMapper;

	// 직분코드 조건검색
	@Override
	public List<Map<String, String>> selectAcctyear(Map<String, String> args) throws Exception {
		return acctyearMapper.selectAcctyear(args);
	}

	// 직분코드 입력
	@Override
	public int insertAcctyear(Map<String, Object> args)  throws Exception {
		return acctyearMapper.insertAcctyear(args);
	}

	// 직분코드 수정
	@Override
	public int updateAcctyear(Map<String, Object> args) throws Exception {
		return acctyearMapper.updateAcctyear(args);
	}

	// 직분코드 삭제
	@Override
	public int deleteAcctyear(Map<String, String> args) throws Exception {
		return acctyearMapper.deleteAcctyear(args);
	}

	// 직분코드 다중삭제
	@Override
	public int deleteAcctyearMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			acctyearMapper.deleteAcctyear(arg);
		}

		return 1;
	}

}
