package kr.co.ucp.link.job.cctv.web;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ucp.link.job.cctv.service.JobCctvListService;

// 기초카메라를 광역으로 가져오기
@RestController
public class JobCctvListController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 서비스 생성
	@Resource(name = "jobCctvListService")
	private JobCctvListService jobCctvListService;

	public JobCctvListController() {
	}

	@SuppressWarnings("unused")
//	@RequestMapping(value = "/link/cctvlist/test.xx", method = RequestMethod.GET)
	@RequestMapping("/link/cctvlist/test.xx")
//	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
//	public HashMap<String, Object> jobCctvListChk(@RequestHeader MultiValueMap<String, String> headers, @RequestParam(required = false) String dstrctsCd) throws Exception {
	public HashMap<String, Object> jobCctvListChk(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception {
		logger.info("==== /link/cctvlist/test.xx >>>>");

		String result_msg = "success";
		String result_code = "000";
		
		String dstrctsCd = "ALL";		// 모든 기초지구
		String rtnMsg = jobCctvListService.jobCctvListChk(dstrctsCd);
		
		if (!"".equals(rtnMsg)) {
			result_msg = rtnMsg;
			result_code = "901";
		}
		logger.debug("==== /link/cctvlist/test.xx >>>> {}", dstrctsCd);

		HashMap<String, Object> map = new HashMap<>();
		map.put("responseMsg", result_msg);
		map.put("responseCd", result_code);
		map.put("dstrctsCd", dstrctsCd);
		map.put("session", 1);
		map.put("msg", "처리하였습니다.");
		
		return map;
	}

	// 정기 작업 실행
	public void jobCctvList() throws Exception {
		jobCctvListService.jobCctvListChk("ALL");
	}
	
}
