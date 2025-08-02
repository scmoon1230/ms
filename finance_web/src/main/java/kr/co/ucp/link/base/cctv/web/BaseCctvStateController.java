package kr.co.ucp.link.base.cctv.web;

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

import kr.co.ucp.link.base.cctv.service.BaseCctvService;
import kr.co.ucp.link.cmmn.service.LinkCmmnService;

@RestController
public class BaseCctvStateController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "linkCmmnService")
    private LinkCmmnService linkCmmnService;

    @Resource(name = "baseCctvService")
    private BaseCctvService baseCctvService;

    public BaseCctvStateController() {
    }

    @PostMapping("/link/base/cctvstate.xx")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> linkBaseCctvStateReq(@RequestHeader MultiValueMap<String, String> headers, @RequestBody Map<String, Object> bodyMap) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        // 공통 auth key check-->
//        if ("Y".equals(gVal.getAuthKeyChk())) {
//            String rtnMsg = linkCmmnService.checkAuthKey(headers, bodyMap);
//            if (!"ok".equals(rtnMsg)) {
//                rtnMap.put("responseCd", GlobalVariable.G_FAILED_CODE);
//                rtnMap.put("responseMsg", rtnMsg);
//                return rtnMap;
//            }
//        }
        // 공통 --;
        try {
            rtnMap = baseCctvService.linkBaseCctvStateData(bodyMap);
            if (!rtnMap.isEmpty() && rtnMap.toString().length() > 100) {
            	logger.info("==== rtnMap1 >>>> {}", rtnMap.toString().substring(0, 100));
            } else {
                logger.info("==== rtnMap2 >>>> {}", rtnMap.toString());
            }
        } catch (Exception e) {
            logger.error("==== ERROR Exception >>>> {}", e.getMessage());
            rtnMap.put("responseCd", "901");
            rtnMap.put("responseMsg", e.getMessage());
            return rtnMap;
        }
        // 공통 --;
        return rtnMap;
    }
}
