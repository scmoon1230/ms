package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.PositionMapper;
import kr.co.ucp.env.service.PositionService;

@Service("positionService")
public class PositionServiceImpl implements PositionService {

	@Resource(name="positionMapper")
	private PositionMapper positionMapper;

	// 직분코드 조건검색
	@Override
	public List<Map<String, String>> selectPosition(Map<String, String> args) throws Exception {
		return positionMapper.selectPosition(args);
	}

	// 직분코드 입력
	@Override
	public int insertPosition(Map<String, Object> args)  throws Exception {
		return positionMapper.insertPosition(args);
	}

	// 직분코드 수정
	@Override
	public int updatePosition(Map<String, Object> args) throws Exception {
		return positionMapper.updatePosition(args);
	}

	// 직분코드 삭제
	@Override
	public int deletePosition(Map<String, String> args) throws Exception {
		return positionMapper.deletePosition(args);
	}

	// 직분코드 다중삭제
	@Override
	public int deletePositionMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			positionMapper.deletePosition(arg);
		}

		return 1;
	}

}
