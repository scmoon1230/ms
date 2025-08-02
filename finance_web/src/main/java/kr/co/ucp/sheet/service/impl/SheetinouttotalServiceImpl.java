package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetinouttotalMapper;
import kr.co.ucp.sheet.service.SheetinouttotalService;

@Service("sheetinouttotalService")
public class SheetinouttotalServiceImpl implements SheetinouttotalService {

	@Resource(name="sheetinouttotalMapper")
	private SheetinouttotalMapper sheetinouttotalMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetInouttotal(Map<String, String> args) throws Exception {
		return sheetinouttotalMapper.selectSheetInouttotal(args);
	}

}
