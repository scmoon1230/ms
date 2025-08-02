package kr.co.ucp.link.job.cctvlog.web;

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

import kr.co.ucp.link.job.cctvlog.service.JobCctvLogCnnctService;

// 광영카메라 접속정보를 기초로 보내기
@RestController
public class JobCctvLogCnnctController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 서비스 생성
	@Resource(name = "jobCctvLogCnnctService")
	private JobCctvLogCnnctService jobCctvLogCnnctService;

	public JobCctvLogCnnctController() {
	}

	
	@SuppressWarnings("unused")
//	@RequestMapping(value = "/link/cctvlogcnnct/test.xx", method = RequestMethod.GET)
	@RequestMapping("/link/cctvlogcnnct/test.xx")
//	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
//	public HashMap<String, Object> jobCctvLogCnnctChk(@RequestHeader MultiValueMap<String, String> headers, @RequestParam(required = false) String dstrctsCd) throws Exception {
	public HashMap<String, Object> jobCctvLogCnnctChk(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception {
		logger.info("==== /link/cctvlogcnnct/test.xx >>>>");
		
		String result_msg = "success";
		String result_code = "000";
		
		String dstrctsCd = "ALL";		// 모든 기초지구
		String rtnMsg = jobCctvLogCnnctService.jobCctvLogCnnctChk(dstrctsCd);

		if (!"".equals(rtnMsg)) {
			result_msg = rtnMsg;
			result_code = "901";
		}
		logger.debug("==== /link/cctvlogcnnct/test.xx >>>> {}", dstrctsCd);

		HashMap<String, Object> map = new HashMap<>();
		map.put("responseMsg", result_msg);
		map.put("responseCd", result_code);
		map.put("dstrctsCd", dstrctsCd);
		map.put("session", 1);
		map.put("msg", "처리하였습니다.");
		
		return map;
	}

	// 정기 작업 실행
	public void jobCctvLogCnnct() throws Exception {
		jobCctvLogCnnctService.jobCctvLogCnnctChk("ALL");
	}

}
