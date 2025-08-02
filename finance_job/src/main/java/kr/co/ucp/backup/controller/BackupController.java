package kr.co.ucp.backup.controller;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.ucp.backup.service.BackupService;
import kr.co.ucp.cmns.BeanUtil;

public class BackupController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private BackupService backupService = (BackupService) BeanUtil.getBean("backupService");
	//private PveService pveService = (PveService) BeanUtil.getBean("pveService");

	public BackupController() throws Exception {
		
	}
	
	public String backupJobStart() {
		String rtn = "success";
		logger.debug("==== backupJobStart >>>> ");

		int swipCctvCnt = 0;

		try {
			HashMap<String, Object> paraMap = new HashMap<String, Object>();

			List<HashMap<String, Object>> tableNameList = backupService.selectTableNameList(paraMap);
			for ( int i=0 ; i<tableNameList.size() ; i++ ) {
				String tableName = tableNameList.get(i).get("tableName").toString();
				if ( !"TB_MAST_MONEY_ARCH".equalsIgnoreCase(tableName)) {	// 헌금 ARCHIVE는 매년 1월에 수작업한다.
					paraMap.put("tableName", tableName);
					logger.info("== [[{}]], [{}] ========================================",i,tableName);							
					String res = backupService.backupTableData(paraMap);
				}
			}
		} catch (Exception e) {
			logger.error("### Exception >>>> {} ###",e.getMessage());
			e.printStackTrace();
			rtn = "fail";
		}
		logger.info("============================================");
		logger.info("==== gis cctv count >>>> {}", swipCctvCnt);
		logger.info("============================================");
		
		return rtn;
	}
}
