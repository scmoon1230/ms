package kr.co.ucp.mntr.main.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.egov.com.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.mntr.cmm.service.CmmCodeService;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.mntr.gis.service.GeoDataService;
import kr.co.ucp.mntr.gis.util.GisUtil;
import kr.co.ucp.mntr.main.service.DivService;
import kr.co.ucp.mntr.main.service.DivVO;

@Controller
public class DivController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Resource(name="gisUtil")
	private GisUtil gisUtil;

	@Resource(name="commonUtil")
	private CommonUtil commonUtil;

    @Resource(name = "divService")
    private DivService divService;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "cmmCodeService")
    private CmmCodeService cmmCodeService;

	@Resource(name="geoDataService")
	private GeoDataService geoDataService;
	
	@RequestMapping(value="/mntr/viewDivision.do")
	public String viewDivision(@RequestParam String divId
			, @RequestParam(value = "fcltId", required = false) String fcltId, ModelMap model) throws Exception {
		LOGGER.info("============= Request DIV [{}], fcltId[{}]", divId, fcltId);
		
		String path = null;
		if (fcltId != null && "DIV-FCLT".equals(divId)) {
			kr.co.ucp.mntr.cmm.service.FcltVO fcltVO = new kr.co.ucp.mntr.cmm.service.FcltVO();
			fcltVO.setFcltId(fcltId);
			EgovMap map = geoDataService.selectFcltById(fcltVO);
			model.addAttribute("divFclt", map);
			path = "blank/layout/div/fclt";
			
		} else if (fcltId != null && "DIV-DETAIL".equals(divId)) {
			kr.co.ucp.mntr.cmm.service.FcltVO fcltVO = new kr.co.ucp.mntr.cmm.service.FcltVO();
			fcltVO.setFcltId(fcltId);
			EgovMap map = geoDataService.selectFcltById(fcltVO);
			model.addAttribute("divFclt", map);
			path = "blank/layout/div/fcltDetail";
			
		} else if ("DIV-VMS".equals(divId)) {

	        Map<String, String> args = new HashMap<>();
			args.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
	        List<EgovMap> list = cmmCodeService.selectListDstrtInfo(args);
	        for (EgovMap dstrtInfo : list) {
	            String playbackSpeed = EgovStringUtil.nullConvert(dstrtInfo.get("playbackSpeed"));
	            if (!"".equals(playbackSpeed)) {
	                //model.addAttribute("playbackSpeedList", Arrays.asList(playbackSpeed.split(Pattern.quote(","))));
	            	String[] info = playbackSpeed.split(":");
	            	if ( info.length != 2 ) {
	            		model.addAttribute("playbackSpeedList", Arrays.asList(playbackSpeed.split(Pattern.quote(","))));
	            		model.addAttribute("playbackSpeedDefault", "2");
	            	} else if ( info.length == 2 ) {
	            		model.addAttribute("playbackSpeedList", Arrays.asList(info[0].split(Pattern.quote(","))));
	            		model.addAttribute("playbackSpeedDefault", info[1]);
	            	}
	            }
	        }
			
			path = "blank/layout/div/evtVms";
		}

		LOGGER.info("============= Response DIV FileName [{}] ==============", path);
		if (path == null) {
			path = "blank/layout/div/gnrNotFound";
		}
		return path;
	}

    @RequestMapping(value = "/mntr/viewTargetDivision.do", method = RequestMethod.POST)
    public String viewDivision(@RequestParam Map<String, String> params) throws Exception {
        String sTarget = EgovStringUtil.nullConvert(params.get("target"));
        LOGGER.info("--> viewTargetDivision: {}", sTarget);
        /**
         * <pre>
         * StringBuffer sbPath = new StringBuffer(servletContext.getRealPath("WEB-INF/jsp/ucp/mntr/layout/div/"));
         * sbPath.append(sTarget);
         * sbPath.append(".jsp");
         *
         * String sOsName = System.getProperty("os.name");
         * if (sOsName.toLowerCase().indexOf("win") < 0) {
         * 	sbPath = new StringBuffer(sbPath.toString().replace("\\", "/"));
         * }
         * logger.info("--> viewTargetDivision: ", sbPath.toString());
         * File f = new File(sbPath.toString());
         * if (f.exists()) {
         * 	return "empty/layout/div/" + sTarget;
         * } else {
         * 	return "empty/layout/div/gnrNotFound";
         * }
         * </pre>
         */

        String targetPath = "blank/layout/div/gnrNotFound";

        if (!"".equals(sTarget)) {
            String[] temp = sTarget.split("/");

            if (temp.length > 1) {
                targetPath = sTarget;
            } else {
                targetPath = "blank/layout/div/" + sTarget;
            }
        }
        LOGGER.info("--> DIV Target Path: {}", targetPath);
        return targetPath;
    }

    /*
     * DIV 목록을 가져온다.
     */
    @RequestMapping(value = "/mntr/divList.json", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getDivListData(@ModelAttribute DivVO vo, ModelMap model) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String divTyCd = EgovStringUtil.nullConvert(vo.getDivTyCd());
        String evtId = EgovStringUtil.nullConvert(vo.getEvtId());
        if ("EVENT".equals(divTyCd)) {
            if ("EVENT".equals(evtId)) {
                resultMap.put("rows", divService.selectEventBaseDivList(vo));
            } else {
                resultMap.put("rows", divService.selectEventDivList(vo));
            }
        } else if ("GENERAL".equals(divTyCd)) {
            resultMap.put("rows", divService.selectGeneralDivList(vo));
        }
        return resultMap;
    }

    /*
     * 이벤트 목록을 가져온다.
     */
    @RequestMapping(value = "/mntr/divEventList.json", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getEventList(@ModelAttribute DivVO vo, ModelMap model) throws Exception {
        LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        vo.setUserId(lgnVO.getUserId());
        PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);

        List<EgovMap> list = divService.selectEventList(vo);

        int totCnt = commonUtil.setTotalRecordCount(list);
        paginationInfo.setTotalRecordCount(totCnt);

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("totalPages", paginationInfo.getTotalPageCount());
        resultMap.put("totalRows", totCnt);
        resultMap.put("rows", list);
        resultMap.put("page", vo.getPage());
        return resultMap;
    }

    @RequestMapping(value = "/mntr/divNormalPosition.json", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getDivNormalPosition(@ModelAttribute DivVO vo, ModelMap model) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String divTyCd = EgovStringUtil.nullConvert(vo.getDivTyCd());
        if ("EVENT".equals(divTyCd)) {
            resultMap.put("rows", divService.selectEventBaseDivLcList(vo));
        } else if ("GENERAL".equals(divTyCd)) {
            resultMap.put("rows", divService.selectGeneralDivLcList(vo));
        }
        return resultMap;
    }

    @RequestMapping(value = "/mntr/divSituationPosition.json", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getDivSituationPosition(@ModelAttribute DivVO vo, ModelMap model) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        if (!"EVENT".equals(vo.getDivTyCd())) {
            LoginVO loginVO = SessionUtil.getUserInfo();
            vo.setGrpId(loginVO.getGrpId());
            vo.setAuthLvl(loginVO.getAuthLvl());
        }

        List<EgovMap> list = divService.selectEventDivLcList(vo);
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping(value = "/mntr/saveDivNormalPosition.json", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> mergeDivNormalPosition(@ModelAttribute DivVO vo, ModelMap model) throws Exception {
        LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        vo.setUserId(lgnVO.getUserId());

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        int result = divService.mergeDivNormalPosition(vo);
        resultMap.put("result", result);
        return resultMap;
    }

    @RequestMapping(value = "/mntr/saveDivSituationPosition.json", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> mergeDivSituationPosition(@ModelAttribute DivVO vo, ModelMap model) throws Exception {
        LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        vo.setUserId(lgnVO.getUserId());

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        String grpId = EgovStringUtil.nullConvert(vo.getGrpId());
        String authLvl = EgovStringUtil.nullConvert(vo.getAuthLvl());

        int result = 0;
        if (!"".equals(grpId) && !"".equals(authLvl)) {
            result = divService.mergeDivSituationPositionGrpAuthLvl(vo);
        } else {
            result = divService.mergeDivSituationPosition(vo);
        }
        resultMap.put("result", result);
        return resultMap;
    }

    @RequestMapping(value = "/mntr/mngDiv/selectGrpAuthLvlList.json", method = RequestMethod.POST)
    public ModelAndView selectGrpAuthLvlList(@RequestParam Map<String, Object> params) throws Exception {
        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("rows", divService.selectGrpAuthLvlList(params));
        return mav;
    }

}
