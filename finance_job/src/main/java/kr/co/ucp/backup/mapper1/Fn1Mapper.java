package kr.co.ucp.backup.mapper1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Fn1Mapper {

	List<HashMap<String, Object>> selectTableNameList(Map<String, Object> map);

	List<HashMap<String, Object>> selectTableDataList(Map<String, Object> map);

	List<HashMap<String, Object>> selectColumnInfoList(Map<String, Object> map);

}
