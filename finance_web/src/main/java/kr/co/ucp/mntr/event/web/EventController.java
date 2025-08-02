/**
 * ----------------------------------------------------------------------------------------------
 *
 * @Class Name : EventController.java
 * @Description : 이벤트 관련 컨트롤러
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP All rights reserved.
 * @Modification Information ---------------------------------------------------------------------------------------------- DATE AUTHOR DESCRIPTION ---------------------------------------------------------------------------------------------- 2014. 11. 13. SaintJuny@ubolt.co.kr
 * 최초작성 ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.event.web;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import kr.co.ucp.cmm.service.PrprtsService;
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
import kr.co.ucp.egov.com.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.CommonVO;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
//import kr.co.ucp.mntr.event.service.EventService;
//import kr.co.ucp.mntr.event.service.EventVO;
//import kr.co.ucp.mntr.event.service.IvxService;
import kr.co.ucp.mntr.gis.util.GisUtil;

@Controller
@RequestMapping(value = "mntr")
public class EventController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Resource(name = "eventService")
//    private EventService eventService;
//
//    @Resource(name = "ivxService")
//    private IvxService ivxService;

    @Resource(name = "commonUtil")
    private CommonUtil commonUtil;

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

    @Resource(name = "gisUtil")
    private GisUtil gisUtil;
//
//    /*
//     * 미완된 이벤트 목록을 가져온다.
//     */
//    @RequestMapping(value = "/unfinishedEventList.json", method = RequestMethod.POST)
//    public @ResponseBody
//    Map<String, Object> getUnfinishedEventList(@ModelAttribute EventVO vo, ModelMap model) throws Exception {
//        LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
//        vo.setUserId(lgnVO.getUserId());
//
//        Object obj = SessionUtil.getAttribute("cmConfig");
//        if (obj != null && obj.getClass() == EgovMap.class) {
//            EgovMap cmConfig = (EgovMap) obj;
//            vo.setBoundsLeft(EgovStringUtil.nullConvert(cmConfig.get(("gisExtentLeft"))));
//            vo.setBoundsBottom(EgovStringUtil.nullConvert(cmConfig.get(("gisExtentBottom"))));
//            vo.setBoundsRight(EgovStringUtil.nullConvert(cmConfig.get(("gisExtentRight"))));
//            vo.setBoundsTop(EgovStringUtil.nullConvert(cmConfig.get(("gisExtentTop"))));
//        }
//
//        PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
//        List<EgovMap> list = eventService.selectUnfinishedEventList(vo);
//
//        int totCnt = commonUtil.setTotalRecordCount(list);
//        paginationInfo.setTotalRecordCount(totCnt);
//
//        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//        resultMap.put("totalPages", paginationInfo.getTotalPageCount());
//        resultMap.put("totalRows", totCnt);
//        resultMap.put("rows", list);
//        resultMap.put("page", vo.getPage());
//        return resultMap;
//    }
//
//    @RequestMapping(value = "/eventById.json", method = RequestMethod.POST)
//    public @ResponseBody
//    EgovMap getEventById(@ModelAttribute EventVO vo) throws Exception {
//        EgovMap map = eventService.selectEvent(vo);
//        return map;
//    }
//
//    /*
//     * 이벤트 종류 리스트를 가져온다.
//     */
//    @RequestMapping(value = "/eventKindList.json", method = RequestMethod.POST)
//    public @ResponseBody
//    Map<String, Object> getEventKindList(@ModelAttribute EventVO vo) throws Exception {
//        LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
//        vo.setUserId(lgnVO.getUserId());
//
//        List<?> list = eventService.selectEventKindList(vo);
//        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//        resultMap.put("list", list);
//        return resultMap;
//    }
//
//    /*
//     * 서비스 종류 리스트를 가져온다.
//     */
//    @RequestMapping(value = "/usvcGrpList.json", method = RequestMethod.POST)
//    public @ResponseBody
//    Map<String, Object> getUsvcGrpList() throws Exception {
//        List<?> list = eventService.selectUsvcGrpList();
//
//        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//        resultMap.put("list", list);
//        return resultMap;
//    }
//
//    /*
//     * 서비스 종류 리스트를 가져온다.
//     */
//    @RequestMapping(value = "/eventGrpList.json", method = RequestMethod.POST)
//    public @ResponseBody
//    Map<String, Object> getEventGrpList() throws Exception {
//        List<?> list = eventService.selectEventGrpList();
//
//        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//        resultMap.put("list", list);
//        return resultMap;
//    }
//
//    /*
//     * 이벤트 권한 체크
//     */
//    @RequestMapping(value = "/hasAuthorityEvent.json", method = RequestMethod.POST)
//    public @ResponseBody
//    Map<String, Object> hasAuthorityEvent(@ModelAttribute EventVO vo, ModelMap model) throws Exception {
//        LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
//        vo.setUserId(lgnVO.getUserId());
//        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//        int result = eventService.hasAuthorityEvent(vo);
//        resultMap.put("result", result);
//        return resultMap;
//    }
//
//    /*
//     * 차량 위치 정보를 가져온다.
//     */
//    @RequestMapping(value = "/carLcInfoList.json")
//    public @ResponseBody
//    Map<String, Object> getCarLcInfoList(@ModelAttribute EventVO vo) throws Exception {
//        PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
//
//        List<EgovMap> list = eventService.selectCarLcInfoList(vo);
//
//        int totCnt = commonUtil.setTotalRecordCount(list);
//        paginationInfo.setTotalRecordCount(totCnt);
//
//        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//        resultMap.put("totalPages", paginationInfo.getTotalPageCount());
//        resultMap.put("totalRows", totCnt);
//        resultMap.put("rows", list);
//        resultMap.put("page", vo.getPage());
//        return resultMap;
//    }
//
//    @RequestMapping(value = "/carLcDtlList.json")
//    public @ResponseBody
//    Map<String, Object> getCarLcDtlList(@ModelAttribute EventVO vo) throws Exception {
//        List<EgovMap> list = eventService.selectCarLcDtlList(vo);
//
//        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//        resultMap.put("rows", list);
//        return resultMap;
//    }
//
//    /*
//     * 이벤트 정보를 가져온다.
//     */
//    @RequestMapping(value = "/evtMntrList.json")
//    public ModelAndView getEvtMntrList(@ModelAttribute EventVO vo) throws Exception {
//        vo.setUserId(SessionUtil.getUserId());
//        ModelAndView mav = new ModelAndView("jsonView");
//        PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
//
//        /* 사용자그룹이 MOJ인경우 사용자가 요청한 리스트만 보기
//         * 법무부 이벤트리스트
//         * 2023-01-03
//         * */
//        LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
//        vo.setUserGrpId(lgnVO.getGrpId());
//
//        List<EgovMap> list = eventService.selectEvtMntrList(vo);
//
//        int totCnt = commonUtil.setTotalRecordCount(list);
//        paginationInfo.setTotalRecordCount(totCnt);
//
//        mav.addObject("totalRows", totCnt);
//        mav.addObject("totalPages", paginationInfo.getTotalPageCount());
//        mav.addObject("rows", list);
//        mav.addObject("page", vo.getPage());
//        return mav;
//    }
//
//    /*
//     * 이벤트 리스트 데이터를 가져온다.
//     */
//    @RequestMapping(value = "/eventList.json", method = RequestMethod.POST)
//    public ModelAndView getEventList(@RequestParam Map<String, Object> params, ModelMap model) throws Exception {
//        model.remove("common");
//        ModelAndView mav = new ModelAndView("jsonView");
//        List<EgovMap> eventList = eventService.selectEventList(params);
//        List<EgovMap> systemList = eventService.selectSystemList(params);
//
//        mav.addObject("event", eventList);
//        mav.addObject("system", systemList);
//        return mav;
//    }
//
//    /*
//     * 이벤트 리스트 데이터를 가져온다.
//     */
//    @RequestMapping(value = "/insertVideoSeaReq.json", method = RequestMethod.POST)
//    public ModelAndView insertVideoSeaReq(@ModelAttribute EventVO vo) throws Exception {
//        ModelAndView mav = new ModelAndView("jsonView");
//        Map<String, String> result = ivxService.insertVideoSeaReq(vo);
//        mav.addObject("result", result);
//        return mav;
//    }
//
//    @RequestMapping(value = "/selectAutodisUserEvtList.json")
//    public ModelAndView selectAutodisUserEvtList() throws Exception {
//        ModelAndView mav = new ModelAndView("jsonView");
//        mav.addObject("result", SessionUtil.getAttribute("autodisUserEvtList"));
//        return mav;
//    }

//    @RequestMapping(value = "/selectUserEventList.json")
//    public ModelAndView selectUserEventList() throws Exception {
//        ModelAndView mav = new ModelAndView("jsonView");
//        mav.addObject("userEventList", SessionUtil.getAttribute("userEventList"));
//        return mav;
//    }

//    @RequestMapping(value = "/event/image.json")
//    public ModelAndView selectEventImage(@ModelAttribute EventVO vo) throws Exception {
//        ModelAndView mav = new ModelAndView("jsonView");
//        mav.addObject("dirEventImg", prprtsService.getString("DIR_EVENT_IMG"));
//        mav.addObject("dirEss", prprtsService.getString("DIR_ESS"));
//        mav.addObject("eventImage", eventService.selectEventImage(vo));
//        return mav;
//    }
}
