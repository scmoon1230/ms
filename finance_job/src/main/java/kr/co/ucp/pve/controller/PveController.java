package kr.co.ucp.pve.controller;

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

public class PveController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private PveService pveService = (PveService) BeanUtil.getBean("pveService");

	public static String WORK_STATUS = "STANDBY";	// 실행에 소요되는 시간이 호출주기보다 길어질 수 있으므로, 진행상태를 비교하여 수행여부를 결정한다.

	//private String ONETIME_ENC_RQST_CNT = "4";		// 암호화모듈로 한번에 요청할 개수
	
	public PveController() throws Exception {
		//logger.info("### PveController >>>> ONETIME_ENC_RQST_CNT : {}", ONETIME_ENC_RQST_CNT);
	}
	
	public String pveJobStart() {
		String rtn = "success";
		logger.debug("==== pveJobStart >>>> ");

		try {

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("prprtsKey","DSTRT_CD");			// 지구코드
			HashMap<String, String> cmPrprts = pveService.selectCmPrprts(params);
			
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("dstrtCd", 		cmPrprts.get("prprtsVal"));		// 지구코드
			
			String k = String.valueOf(System.currentTimeMillis());			
			logger.info("--> doWork(), [{}], WORK_STATUS:{} <----------------------------------------------------------------------------------------------------", k, WORK_STATUS);
			
			if ( !"STANDBY".equalsIgnoreCase(WORK_STATUS)) {	// 대기가 아닐 때
				logger.info("--> doWork(), [{}], 이전 작업이 끝나지 않아 새로운 작업을 시작하지 않는다. <++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++", k, WORK_STATUS);
				
			} else {
				WORK_STATUS = "RUNNING";						// 진행중

				pveService.requestMaEncVdo(param);		// 암호화대기 상태의 파일을 DB에서 조회하여 암호화를 요청한다.

				pveService.workMaOrgVdoResult(param);	// 마크애니 영상입수 결과파일 result.xml 을 읽어서 DB에 등록하고 결과파일은 이동한다.


				WORK_STATUS = "STANDBY";						// 대기
				logger.info("--> doWork(), [{}], WORK_STATUS:{} <====================================================================================================", k, WORK_STATUS);
			}
			

		} catch (Exception e) {
			logger.error("### Exception >>>> {} ###",e.getMessage());
			e.printStackTrace();
			rtn = "fail";
		}

		return rtn;
	}
}
