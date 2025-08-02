package kr.co.ucp.link.base.user.web;

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

import kr.co.ucp.link.base.user.service.BaseUserWideService;
import kr.co.ucp.link.cmmn.service.LinkCmmnService;

@RestController
public class BaseUserWideController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "baseUserWideService")
	private BaseUserWideService baseUserWideService;

	public BaseUserWideController() {
	}

	@PostMapping("/link/base/userwide.xx")
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> linkBaseUserWideReq(@RequestHeader MultiValueMap<String, String> headers, @RequestBody Map<String, Object> bodyMap) throws Exception {
		logger.info("==== /link/base/userwide.xx >>>> {}", bodyMap.toString());
		
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

		rtnMap = linkBaseUserWideData(bodyMap);

        if (!rtnMap.isEmpty() && rtnMap.toString().length() > 100) {
        	logger.info("==== rtnMap1 >>>> {}", rtnMap.toString().substring(0, 100));
        } else {
            logger.info("==== rtnMap2 >>>> {}", rtnMap.toString());
        }
        
		return rtnMap;
	}

	private Map<String, Object> linkBaseUserWideData(Map<String, Object> rqMap) throws Exception {

		int updCnt = 0;
		int insCnt = 0;
		int delCnt = 0;
		String dstrtCd = "";
		String result_msg = "success";
		String result_code = "000";

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("responseCd", result_code);
		rtnMap.put("responseMsg", result_msg);

		try {
			HashMap<String, Object> updateMap = baseUserWideService.updateUserList(rqMap);

			updCnt = Integer.parseInt(updateMap.get("updCnt").toString());
			insCnt = Integer.parseInt(updateMap.get("insCnt").toString());
			delCnt = Integer.parseInt(updateMap.get("delCnt").toString());
			dstrtCd = updateMap.get("dstrtCd").toString();

		} catch (Exception e) {
			logger.error("==== ERROR Exception >>>> {}", e.getMessage());
			result_code = "901";
			result_msg = "Exception " + e.getMessage();
		}

		rtnMap.put("responseCd", result_code);
		rtnMap.put("responseMsg", result_msg);
		rtnMap.put("updCnt", updCnt);
		rtnMap.put("insCnt", insCnt);
		rtnMap.put("delCnt", delCnt);
		rtnMap.put("dstrtCd", dstrtCd);

		logger.debug("==== rtnMap chk >>>> insCnt:{},updCnt:{},delCnt:{}", insCnt, updCnt, delCnt);
		logger.debug("==== rtnMap chk >>>> {},{},{}", result_code, result_msg);

		return rtnMap;
	}
	
}