package kr.co.ucp.wrks.sstm.code.service;

import java.util.List;
import java.util.Map;

public interface DetailService {
	/*
	 * 코드상세 목록조회 메서드
	 * */
	@SuppressWarnings("rawtypes")
	List view(Map<String, Object> map) throws Exception;
}
