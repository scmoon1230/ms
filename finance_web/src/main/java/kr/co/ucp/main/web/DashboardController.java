package kr.co.ucp.main.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.main.service.DashboardService;
import kr.co.ucp.mntr.cmm.util.SessionUtil;

@Controller
public class DashboardController {

	@Resource(name = "dashboardService")
	private DashboardService dashboard;

	@RequestMapping("/main/main.do")
	public String main(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		LoginVO vo = SessionUtil.getUserInfo();
		
		String mainPage = vo.getMainPage();

		return "redirect:"+mainPage;
	}

	@RequestMapping("/main/dashboard.do")
	public String dashboard(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		//return "tvoBlank/main/dashboard";
		return "main/dashboard";
	}

	@RequestMapping(value = "/main/dashboardData.json", method = RequestMethod.POST)
	public ModelAndView dashboardData(ModelMap model, @RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

//		EgovMap tvoViewState = dashboard.selectTvoViewState();					// 금일 열람 현황
//		EgovMap tvoViewExtnState = dashboard.selectTvoViewExtnState();			// 금일 열람연장 현황
//		EgovMap tvoOutState = dashboard.selectTvoOutState();					// 금일 반출 현황
//		EgovMap tvoOutExtnState = dashboard.selectTvoOutExtnState();			// 금일 반출연장 현황
//		EgovMap tvoCctvState = dashboard.selectTvoCctvState();					// CCTV 현황
//
//		List<EgovMap> tvoViewAprvStateDaily = dashboard.selectTvoViewAprvStateDaily();	// 금월 일자별 영상 열람승인 현황
//		List<EgovMap> tvoOutAprvStateDaily = dashboard.selectTvoOutAprvStateDaily();	// 금월 일자별 영상 반출승인 현황
//
//		EgovMap tvoViewAprvStateMonthly = dashboard.selectTvoViewAprvStateMonthly();	// 금월 영상 열람승인 현황
//		EgovMap tvoOutAprvStateMonthly = dashboard.selectTvoOutAprvStateMonthly();		// 금월 영상 반출승인 현황
				
/*		mav.addObject("tvoViewState", dashboard.selectTvoViewState());					// 금일 열람 현황
		mav.addObject("tvoViewExtnState", dashboard.selectTvoViewExtnState());			// 금일 열람연장 현황
		mav.addObject("tvoOutState", dashboard.selectTvoOutState());					// 금일 반출 현황
		mav.addObject("tvoOutExtnState", dashboard.selectTvoOutExtnState());			// 금일 반출연장 현황
		mav.addObject("tvoCctvState", dashboard.selectTvoCctvState());					// CCTV 현황

		mav.addObject("tvoViewAprvStateDaily", dashboard.selectTvoViewAprvStateDaily());	// 금월 일자별 영상 열람승인 현황
		mav.addObject("tvoOutAprvStateDaily", dashboard.selectTvoOutAprvStateDaily());		// 금월 일자별 영상 반출승인 현황
		
		mav.addObject("tvoViewAprvStateMonthly", dashboard.selectTvoViewAprvStateMonthly());	// 금월 영상 열람승인 현황
		mav.addObject("tvoOutAprvStateMonthly", dashboard.selectTvoOutAprvStateMonthly());		// 금월 영상 반출승인 현황

		mav.addObject("storageState", dashboard.getStorageState());	// storage 현황
*/
		return mav;
	}
	
}
