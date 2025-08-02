package kr.co.ucp.backup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.backup.mapper1.Fn1Mapper;
import kr.co.ucp.backup.mapper2.Fn2Mapper;

@Service("backupService")
public class BackupService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "fn1Mapper")
    private Fn1Mapper fn1Mapper;

    @Resource(name = "fn2Mapper")
    private Fn2Mapper fn2Mapper;

	public List<HashMap<String, Object>> selectTableNameList(Map<String, Object> map) throws Exception {
		List<HashMap<String, Object>> tableNameList = fn1Mapper.selectTableNameList(map);
		//logger.info("- tableNameList.size : {}", tableNameList.size());
		return tableNameList;
	}

	public String backupTableData(Map<String, Object> paraMap) throws Exception {
		String res = "";
		String tbn = paraMap.get("tableName").toString();

		// 정보 조회
		List<HashMap<String, Object>> columnInfoList = fn1Mapper.selectColumnInfoList(paraMap);
		for ( int k=0 ; k<columnInfoList.size() ; k++ ) {
			String dataType        = columnInfoList.get(k).get("dataType").toString();
			String columnName      = columnInfoList.get(k).get("columnName").toString();
			String columnNameCamel = columnInfoList.get(k).get("columnNameCamel").toString();
			logger.info("-- [{}], {}, {}, {}",tbn,columnName,columnNameCamel,dataType);
		}
		
		List<HashMap<String, Object>> tableDataList = fn1Mapper.selectTableDataList(paraMap);
		for ( int j=0 ; j<tableDataList.size() ; j++ ) {	if (j>=5) break;
			logger.info("-- [{}], {}",tbn,tableDataList.get(j).toString());
		}

		// 백업 시작
		int r = fn2Mapper.deleteBackupTableData(paraMap);

		for ( int j=0 ; j<tableDataList.size() ; j++ ) {		if (j>=500) break;
			String qry = "insert into "+tbn;
			qry += " ( ";
			for ( int k=0 ; k<columnInfoList.size() ; k++ ) {
				String columnName = columnInfoList.get(k).get("columnName").toString();
				if ( !"REG_TIME".equalsIgnoreCase(columnName) && !"USE_TIME".equalsIgnoreCase(columnName) ) {
					if ( k != 0 )	qry += ", ";
					qry += columnName;
				}
			}
			qry += " ) ";
			qry += " values ( ";
			for ( int k=0 ; k<columnInfoList.size() ; k++ ) {
				String columnNameCamel = columnInfoList.get(k).get("columnNameCamel").toString();
				if ( !"regTime".equalsIgnoreCase(columnNameCamel) && !"useTime".equalsIgnoreCase(columnNameCamel) ) {
					HashMap<String, Object> columnInfo = columnInfoList.get(k);
					if ( k != 0 )	qry += ", ";
					if ( tableDataList.get(j).get(columnInfo.get("columnNameCamel").toString()) == null ) {
						qry += "null";
					} else {
						if(columnInfo.get("dataType").toString().indexOf("CHAR") != -1) {	// 문자일 때
							qry += "'"+tableDataList.get(j).get(columnInfo.get("columnNameCamel").toString()).toString().trim().replaceAll("'", "")+"'";
						} else {	// 문자 아닐 때
							qry += tableDataList.get(j).get(columnInfo.get("columnNameCamel").toString()).toString();
						}
					}					
				}

			}			
			qry += " ) ";
			logger.info("-- [{}], {}",tbn,qry);
			
			paraMap.put("qry", qry);
			int rr = fn2Mapper.insertBackupTableData(paraMap);
		}
		
		return res;
	}
	
	
	
//	public List<HashMap<String, Object>> selectTableDataList(Map<String, Object> map) throws Exception {
//		List<HashMap<String, Object>> tableDataList = fn1Mapper.selectTableDataList(map);
//		//logger.info("- tableDataList.size : {}", tableDataList.size());
//		return tableDataList;
//	}
//
//	public List<HashMap<String, Object>> selectColumnNameList(Map<String, Object> map) throws Exception {
//		List<HashMap<String, Object>> columnNameList = fn1Mapper.selectColumnNameList(map);
//		//logger.info("- columnNameList.size : {}", columnNameList.size());
//		return columnNameList;
//	}
//
//	public int deleteTableData(Map<String, Object> map) throws Exception {
//		int r = 0;
//		r = fn2Mapper.deleteBackupTableData(map);	// 백업한 데이타 삭제
//		return r;
//	}
	
	
//	public List<HashMap<String, Object>> selectUser(Map<String, Object> map) throws Exception {
//		List<HashMap<String, Object>> list2 = fn2Mapper.selectUser(map);
//		logger.info("- size2 : {}", list2.size());
//		return list2;
//	}
        
}
