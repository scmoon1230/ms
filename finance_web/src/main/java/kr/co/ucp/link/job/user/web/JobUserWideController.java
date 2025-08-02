package kr.co.ucp.link.job.user.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmmn.CommUtil;
import kr.co.ucp.cmmn.RestUtil;
import kr.co.ucp.link.cmmn.service.LinkCmmnService;
import kr.co.ucp.link.job.user.service.JobUserWideService;

// 광역사용자를 기초로 보내기
@RestController
public class JobUserWideController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 서비스 생성
	@Resource(name = "jobUserWideService")
	private JobUserWideService jobUserWideService;

	public JobUserWideController() {
	}

	
	@SuppressWarnings("unused")
//	@RequestMapping(value = "/link/userwide/test.xx", method = RequestMethod.GET)
	@RequestMapping("/link/userwide/test.xx")
//	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
//	public HashMap<String, Object> linkUserWideTest(@RequestHeader MultiValueMap<String, String> headers, @RequestParam(required = false) String dstrctsCd) throws Exception {
	public HashMap<String, Object> linkUserWideTest(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception {
		logger.info("==== /link/base/userwide.xx >>>>");
		
		String result_msg = "success";
		String result_code = "000";
		
		String dstrctsCd = "ALL";		// 모든 기초지구
		String rtnMsg = jobUserWideService.jobUserWideChk(dstrctsCd);

		if (!"".equals(rtnMsg)) {
			result_msg = rtnMsg;
			result_code = "901";
		}
		logger.debug("==== /link/base/userwide.xx >>>> {}", dstrctsCd);

		HashMap<String, Object> map = new HashMap<>();
		map.put("responseMsg", result_msg);
		map.put("responseCd", result_code);
		map.put("dstrctsCd", dstrctsCd);
		map.put("session", 1);
		map.put("msg", "처리하였습니다.");

		return map;
	}

	// 정기 작업 실행
	public void jobUserWide() throws Exception {
		jobUserWideService.jobUserWideChk("ALL");
	}

}
