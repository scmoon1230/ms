package kr.co.ucp.link.base.user.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.link.base.user.service.BaseUserBaseMapper;
import kr.co.ucp.link.base.user.service.BaseUserBaseService;

@Service("baseUserBaseService")
public class BaseUserBaseServiceImpl implements BaseUserBaseService {

	@Resource(name = "BaseUserBaseMapper")
	private BaseUserBaseMapper baseUserBaseMapper;

	@Override
	public List<Map<String, Object>> selectUserList(Map<String, Object> map) throws Exception {
		return baseUserBaseMapper.selectUserList(map);
	}

}
