package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

public interface SheetinouttotalService {

	List<Map<String, String>> selectSheetInouttotal(Map<String, String> args) throws Exception;

}
