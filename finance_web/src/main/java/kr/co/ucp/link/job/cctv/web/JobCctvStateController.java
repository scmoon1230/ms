package kr.co.ucp.link.job.cctv.web;

import kr.co.ucp.link.job.cctv.service.JobCctvStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;

// 카메라상태정보를 광역으로 가져오기
@RestController
public class JobCctvStateController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 서비스 생성
	@Resource(name = "jobCctvStateService")
	private JobCctvStateService jobCctvStateService;

	public JobCctvStateController() {
	}

	@SuppressWarnings("unused")
//	@RequestMapping(value = "/link/cctvstate/test", method = RequestMethod.GET)
	@RequestMapping("/link/cctvstate/test.xx")
//	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
//	public HashMap<String, Object> linkCctvStateTest(@RequestHeader MultiValueMap<String, String> headers, @RequestParam(required = false) String dstrctsCd) throws Exception {
	public HashMap<String, Object> linkCctvStateTest(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception {
		logger.info("==== /link/cctvstate/test.xx >>>>");
		
		String result_msg = "success";
		String result_code = "000";

		String dstrctsCd = "ALL";		// 모든 기초지구
		String rtnMsg = jobCctvStateService.jobCctvStateChk(dstrctsCd);
		
		if (!"".equals(rtnMsg)) {
			result_msg = rtnMsg;
			result_code = "901";
		}
		logger.debug("==== /link/cctvstate/test.xx >>>> {}", dstrctsCd);

		HashMap<String, Object> map = new HashMap<>();
		map.put("responseMsg", result_msg);
		map.put("responseCd", result_code);
		map.put("dstrctsCd", dstrctsCd);
		map.put("session", 1);
		map.put("msg", "처리하였습니다.");
		
		return map;
	}

	// 정기 작업 실행
	public void jobCctvState() throws Exception {
		jobCctvStateService.jobCctvStateChk("ALL");
	}

}
