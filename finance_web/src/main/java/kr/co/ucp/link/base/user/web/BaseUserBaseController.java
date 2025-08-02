package kr.co.ucp.link.base.user.web;

import java.util.HashMap;
import java.util.List;
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

import kr.co.ucp.link.base.user.service.BaseUserBaseService;
import kr.co.ucp.link.cmmn.service.LinkCmmnService;

@RestController
public class BaseUserBaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "baseUserBaseService")
	private BaseUserBaseService baseUserBaseService;

	public BaseUserBaseController() {
	}

	@PostMapping("/link/base/userbase.xx")
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> linkBaseUserBaseReq(@RequestHeader MultiValueMap<String, String> headers, @RequestBody Map<String, Object> bodyMap) throws Exception {
		logger.info("==== /link/base/userbase.xx >>>> {}", bodyMap.toString());
		
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

		rtnMap = linkBaseUserBaseData(bodyMap);

        if (!rtnMap.isEmpty() && rtnMap.toString().length() > 100) {
        	logger.info("==== rtnMap1 >>>> {}", rtnMap.toString().substring(0, 100));
        } else {
            logger.info("==== rtnMap2 >>>> {}", rtnMap.toString());
        }
        
		return rtnMap;
	}

	private Map<String, Object> linkBaseUserBaseData(Map<String, Object> rqMap) throws Exception {
		String result_msg = "success";
		String result_code = "000";
		
		HashMap<String, Object> dstrtCdUserMap = new HashMap<String, Object>();

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("responseCd", result_code);
		rtnMap.put("responseMsg", result_msg);

		try {
			dstrtCdUserMap.put("dstrtCd", rqMap.get("rcptDstrtCd"));	// 기초지구코드
			List<Map<String, Object>> userMap = baseUserBaseService.selectUserList(dstrtCdUserMap);
			
			int dataCnt = userMap.size();
			rtnMap.put("dataCnt", dataCnt);
			rtnMap.put("data", userMap);
			
		} catch (Exception e) {
			logger.error("==== ERROR Exception >>>> {}", e.getMessage());
			result_code = "901";
			result_msg = "Exception " + e.getMessage();
		}

		rtnMap.put("responseCd", result_code);
		rtnMap.put("responseMsg", result_msg);

		logger.debug("==== rtnMap chk >>>> {},{},{}", result_code, result_msg);

		return rtnMap;
	}
}
