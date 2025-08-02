package kr.co.ucp.wrks.sstm.code.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.wrks.sstm.code.service.DetailService;

@Service("detailService")
public class DeatilServiceImpl implements DetailService {
	
	@Resource(name="detailDAO")
	private DetailDAO detailDAO;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List view(Map<String, Object> map) throws Exception {
		return detailDAO.view(map);
	}

}
