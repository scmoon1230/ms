package kr.co.ucp.wrks.sstm.code.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("detailDAO")
public class DetailDAO extends EgovAbstractMapper{

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List view(Map<String, Object> map) {
		return list("detailDAO_list", map);
	}

}




