package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetyeardifferMapper;
import kr.co.ucp.sheet.service.SheetyeardifferService;

@Service("sheetyeardifferService")
public class SheetyeardifferServiceImpl implements SheetyeardifferService {

	@Resource(name="sheetyeardifferMapper")
	private SheetyeardifferMapper sheetyeardifferMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetyeardiffer(Map<String, String> args) throws Exception {
		return sheetyeardifferMapper.selectSheetyeardiffer(args);
	}

	// 입력
	@Override
	public int insertSheet(Map<String, Object> args)  throws Exception {
		return sheetyeardifferMapper.insertSheet(args);
	}

	// 수정
	@Override
	public int updateSheet(Map<String, Object> args) throws Exception {
		return sheetyeardifferMapper.updateSheet(args);
	}

	// 삭제
	@Override
	public int deleteSheet(Map<String, String> args) throws Exception {
		return sheetyeardifferMapper.deleteSheet(args);
	}

	// 다중삭제
	@Override
	public int deleteSheetMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			sheetyeardifferMapper.deleteSheet(arg);
		}

		return 1;
	}
	
}
