package kr.co.ucp.link.job.user.web;

import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ucp.link.job.user.service.JobUserBaseService;

// 기초사용자를 광역으로 가져오기
@RestController
public class JobUserBaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 서비스 생성
	@Resource(name = "jobUserBaseService")
	private JobUserBaseService jobUserBaseService;

	public JobUserBaseController() {
	}

	// 사용하지 않음(기초사용자를 광역으로 가져올 필요가 없음) 
	@RequestMapping(value = "/link/userbase/test.xx", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public HashMap<String, Object> linkUserBaseTest(@RequestHeader MultiValueMap<String, String> headers, @RequestParam(required = false) String dstrctsCd) throws Exception {
		logger.info("==== /link/userbase/test.xx >>>>");

		HashMap<String, Object> map = new HashMap<>();
		String result_msg = "success";
		String result_code = "000";

		dstrctsCd = "ALL";		// 모든 기초지구
		String rtnMsg = jobUserBaseService.jobUserBaseChk(dstrctsCd);
		
		if (!"".equals(rtnMsg)) {
			result_msg = rtnMsg;
			result_code = "901";
		}
		logger.debug("==== /link/userbase/test.xx >>>> {}", dstrctsCd);

		map.put("responseMsg", result_msg);
		map.put("responseCd", result_code);
		map.put("dstrctsCd", dstrctsCd);

		return map;
	}

	// 정기 작업 실행
	public void jobUserBase() throws Exception {
		jobUserBaseService.jobUserBaseChk("ALL");
	}

}
