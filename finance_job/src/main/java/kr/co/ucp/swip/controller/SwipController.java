package kr.co.ucp.swip.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.ucp.cmns.BeanUtil;
import kr.co.ucp.cmns.CommUtil;
import kr.co.ucp.cmns.ConfigManager;
import kr.co.ucp.swip.service.SwipService;
import kr.co.ucp.pve.service.PveService;

public class SwipController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private SwipService swipService = (SwipService) BeanUtil.getBean("swipService");
	private PveService pveService = (PveService) BeanUtil.getBean("pveService");

	public SwipController() throws Exception {
		
	}
	
	public String swipJobStart() {
		String rtn = "success";
		logger.debug("==== swipJobStart >>>> ");

		int swipCctvCnt = 0;

		try {
			HashMap<String, Object> paraMap = new HashMap<String, Object>();
			List<HashMap<String, Object>> fcltUsedTyList = swipService.selectCmTcFcltUsed(paraMap);	// swip 용도정보 리스트
			if (fcltUsedTyList == null || fcltUsedTyList.isEmpty()) {
				logger.error("### swip에 등록된 용도 정보가 없습니다!!!! ###");
				return "swip data empty";
			} else {
				pveService.dumpCmTcFcltUsed(fcltUsedTyList);		// swip 용도 정보를 영상반출 db에 받은 그대로 담아둔다.
			}
			
			List<HashMap<String, Object>> swipCctvList = swipService.selectCmFacility();	// swip 카메라 리스트
			if (swipCctvList == null || swipCctvList.isEmpty()) {
				logger.error("### swip에 등록된 카메라 정보가 없습니다!!!! ###");
				return "swip data empty";
			} else {
				pveService.dumpCmFacility(swipCctvList);		// swip 카메라 정보를 영상반출 db에 받은 그대로 담아둔다.
				//pveService.dumpCmFacilityGis(swipCctvList);	// swip 카메라 정보를 영상반출 db에 받은 그대로 담아둔다.
			}
			swipCctvCnt = swipCctvList.size();

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
