package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetinouttotalallMapper;
import kr.co.ucp.sheet.service.SheetinouttotalallService;

@Service("sheetinouttotalallService")
public class SheetinouttotalallServiceImpl implements SheetinouttotalallService {

	@Resource(name="sheetinouttotalallMapper")
	private SheetinouttotalallMapper sheetinouttotalallMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetInouttotalall(Map<String, String> args) throws Exception {
		return sheetinouttotalallMapper.selectSheetInouttotalall(args);
	}

}
