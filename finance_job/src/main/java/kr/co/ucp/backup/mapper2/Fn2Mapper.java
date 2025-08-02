package kr.co.ucp.backup.mapper2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Fn2Mapper {

	int deleteBackupTableData(Map<String, Object> map);

	int insertBackupTableData(Map<String, Object> map);
	
}
