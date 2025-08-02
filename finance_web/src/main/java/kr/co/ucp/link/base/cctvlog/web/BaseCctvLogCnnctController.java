package kr.co.ucp.link.base.cctvlog.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmmn.CommUtil;
import kr.co.ucp.link.base.cctvlog.service.BaseCctvLogCnnctService;
import kr.co.ucp.link.cmmn.service.LinkCmmnService;

@RestController
public class BaseCctvLogCnnctController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;
	
	@Resource(name = "baseCctvLogCnnctService")
	private BaseCctvLogCnnctService baseCctvLogCnnctService;

	public BaseCctvLogCnnctController() {
	}

	@PostMapping("/link/base/cctvlogcnnct.xx")
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> linkBaseCctvLogCnnct(@RequestHeader MultiValueMap<String, String> headers, @RequestBody Map<String, Object> bodyMap) throws Exception {
		logger.info("==== /link/base/cctvlogcnnct.xx {}", bodyMap.toString());
		
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		// 공통 auth key check-->
//		if ("Y".equals(gVal.getAuthKeyChk())) {
//			String rtnMsg = linkCmmnService.checkAuthKey(headers, bodyMap);
//			if (!"ok".equals(rtnMsg)) {
//				rtnMap.put("responseCd", GlobalVariable.G_FAILED_CODE);
//				rtnMap.put("responseMsg", rtnMsg);
//				return rtnMap;
//			}
//		}
		// 공통 --;

		rtnMap = cctvLogCnnctData(bodyMap);

        if (!rtnMap.isEmpty() && rtnMap.toString().length() > 100) {
        	logger.info("==== rtnMap1 >>>> {}", rtnMap.toString().substring(0, 100));
        } else {
            logger.info("==== rtnMap2 >>>> {}", rtnMap.toString());
        }
        
		return rtnMap;
	}

	private Map<String, Object> cctvLogCnnctData(Map<String, Object> rqMap) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
        String result_msg = "success";
        String result_code = "000";
		
		String dstrtCd = prprtsService.getString("DSTRT_CD");
		String rcptDstrtCd = CommUtil.objNullToVal(rqMap.get("rcptDstrtCd"), ""); // 수신처지구코드;
		try {
			if (!dstrtCd.equals(rcptDstrtCd)) {
				rtnMap.put("responseCd", "901");
				rtnMap.put("responseMsg", "지구코드 오류");
				return rtnMap;
			}
			int resultCode = baseCctvLogCnnctService.updateCctvLogCnnct(rqMap);
			
		} catch (Exception e) {
			logger.error("==== ERROR Exception >>>> {}", e.getMessage());
			rtnMap.put("responseCd", "901");
			rtnMap.put("responseMsg", e.getMessage());
			return rtnMap;
		}
//		rtnMap.put("responseCd", GlobalVariable.G_SUCCESS_CODE);
//		rtnMap.put("responseMsg", GlobalVariable.G_SUCCESS_MSG);
        rtnMap.put("responseCd", result_code);
        rtnMap.put("responseMsg", result_msg);

		logger.debug("==== rtnMap chk >>>> {},{},{}", result_code, result_msg);

		return rtnMap;
	}

}