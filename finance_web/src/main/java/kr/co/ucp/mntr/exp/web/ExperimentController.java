package kr.co.ucp.mntr.exp.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.ucp.mntr.cmm.service.CommonVO;
import kr.co.ucp.mntr.cmm.util.CommonUtil;

@Controller
public class ExperimentController {
	//private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="commonUtil")
	private CommonUtil commonUtil;

	@RequestMapping(value="/mntr/experiment.do")
	public String viewAnticrime(ModelMap model, @ModelAttribute CommonVO common) {
		
//		private final String[] commonData = { "Experiment", "etc", "experiment" };
//		commonUtil.setCommonVOData(common, commonData);
//		model.addAttribute("common", common);
		
		return "nomapL/exp/experiment";
	}
}
