package kr.co.ucp.wrks.sstm.mbl.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.mbl.service.MblEvtService;
import net.sf.json.JSONArray;

@Controller
public class MblEvtController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "mblEvtService")
	private MblEvtService MblEvtService;

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

	@Resource(name = "codeCmcdService")
	private CodeCmcdService codeCmcdService;

	/*
	 * 그룹별 사용자조회 리스트
	 */
	@RequestMapping(value = "/wrks/sstm/mbl/mblEvt.do")
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("cdGrpId", "SEND_TY");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_ID ASC");
		request.setAttribute("sendTyCd", codeCmcdService.grpList(args));

		return "wrks/sstm/mbl/mbl_evt";
	}

	@RequestMapping(value = "/wrks/sstm/mbl/list_event.json")
	@ResponseBody
	public Map<String, Object> eventList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		String evtId = EgovStringUtil.nullConvert(request.getParameter("evtId"));
		String evtNm = EgovStringUtil.nullConvert(request.getParameter("evtNm"));
		String pageNo = EgovStringUtil.nullConvert(request.getParameter("page"));
		String sidx = EgovStringUtil.nullConvert(request.getParameter("sidx"));
		String sord = EgovStringUtil.nullConvert(request.getParameter("sord"));

		args.put("evtId", evtId);
		args.put("evtNm", evtNm);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,String>> resultRows = MblEvtService.getEventList(args);

		map.put("page",pageNo);
		map.put("rows",resultRows);

		return map;
	}

	@RequestMapping(value = "/wrks/sstm/mbl/list_eventNm.json")
	@ResponseBody
	public Map<String, Object> eventListNm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		String evtId = EgovStringUtil.nullConvert(request.getParameter("evtId"));
		String pageNo = EgovStringUtil.nullConvert(request.getParameter("page"));
		String sidx = EgovStringUtil.nullConvert(request.getParameter("sidx"));
		String sord = EgovStringUtil.nullConvert(request.getParameter("sord"));

		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		args.put("evtId", evtId);

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,String>> resultRows = MblEvtService.getEventNmList(args);

		map.put("page",pageNo);
		map.put("rows",resultRows);
		return map;
	}

	@RequestMapping(value = "/wrks/sstm/mbl/list_eventNm_popup.json")
	@ResponseBody
	public Map<String, Object> eventListNmPopup(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		String evtId = EgovStringUtil.nullConvert(request.getParameter("evtId"));
		String pageNo = EgovStringUtil.nullConvert(request.getParameter("page"));
		String sidx = EgovStringUtil.nullConvert(request.getParameter("sidx"));
		String sord = EgovStringUtil.nullConvert(request.getParameter("sord"));

		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		args.put("evtId", evtId);

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,String>> resultRows = MblEvtService.getEventNmListPopup(args);

		map.put("page",pageNo);
		map.put("rows",resultRows);
		return map;
	}

	@RequestMapping(value = "/wrks/sstm/mbl/updatembl.json")
	@ResponseBody
	public int updateEvent(@RequestParam String data, String oRowEvtId) throws Exception{
		int chkSuccess = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			List<Map<String,Object>> oList = new ArrayList<Map<String,Object>>();
			oRowEvtId = StringEscapeUtils.unescapeHtml(oRowEvtId);
			oList = JSONArray.fromObject(oRowEvtId);

			List<Map<String,Object>> oData = new ArrayList<Map<String,Object>>();
			data = StringEscapeUtils.unescapeHtml(data);
			oData = JSONArray.fromObject(data);

			for (int i = 0 ; i < oList.size(); i++) {
				for (Map<String, Object> list : oData) {

					logger.debug(">>>>>>oData>>>>>{}",oData);
					map.put("evtId", oList.get(i));
					map.put("pushSendTyCd", list.get("pushSendTyCd"));
					map.put("smsSendTyCd", list.get("smsSendTyCd"));
					map.put("useYn", list.get("useYn"));
					map.put("grpId", list.get("grpId"));
					map.put("moblId", list.get("moblId"));
					logger.debug(">>>>>>list>>>>>{}",list);

					Map<String, Object> checkMap = new HashMap<String, Object>();

					checkMap.put("evtId", oList.get(i));
					checkMap.put("moblId", list.get("moblId"));

					int checkEvent = MblEvtService.checkMblUser(checkMap);

					if(checkEvent != 0) {
						int updateEvent = MblEvtService.updateMbl(map);
						if (updateEvent == 1) {
							chkSuccess++;
						}
					}
				}
			}
		} catch (Exception e) {
			  logger.debug("error >>> {}",e);
		}
		return chkSuccess;
	}

	@RequestMapping(value = "/wrks/sstm/mbl/insetMblUser.json")
	@ResponseBody
	public int insetMblUser(@RequestParam String data, String oRowEvtId) throws Exception{
		int chkSuccess = 0;
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String userId = lgnVO.getUserId();
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			List<Map<String,Object>> oList = new ArrayList<Map<String,Object>>();
			oRowEvtId = StringEscapeUtils.unescapeHtml(oRowEvtId);
			oList = JSONArray.fromObject(oRowEvtId);

			List<Map<String,Object>> oData = new ArrayList<Map<String,Object>>();
			data = StringEscapeUtils.unescapeHtml(data);
			oData = JSONArray.fromObject(data);

			for (int i = 0 ; i < oList.size(); i++) {
				for (Map<String, Object> list : oData) {

					logger.debug(">>>>>>oData>>>>>{}",oData);
					map.put("evtId", oList.get(i));
					map.put("moblId", list.get("moblId"));
					map.put("userId", userId);
					map.put("pushSendTyCd", list.get("pushSendTyCd"));
					map.put("smsSendTyCd", list.get("smsSendTyCd"));
					map.put("useYn", list.get("useYn"));
					logger.debug(">>>>>>list>>>>>{}",list);
					Map<String, Object> checkMap = new HashMap<String, Object>();

					checkMap.put("evtId", oList.get(i));
					checkMap.put("moblId", list.get("moblId"));

					int checkEvent = MblEvtService.checkMblUser(checkMap);

					if(checkEvent == 0) {
						int insertEvent = MblEvtService.insertMblUser(map);
						if (insertEvent == 1) {
							chkSuccess++;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.debug("error >>> {}",e);
		}
		return chkSuccess;
	}

	@RequestMapping(value = "/wrks/sstm/mbl/deletembl.json")
	@ResponseBody
	public int deleteEvent(@RequestParam String data, String oRowEvtId) throws Exception{
		int chkSuccess = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			List<Map<String,Object>> oList = new ArrayList<Map<String,Object>>();
			oRowEvtId = StringEscapeUtils.unescapeHtml(oRowEvtId);
			oList = JSONArray.fromObject(oRowEvtId);

			List<Map<String,Object>> oData = new ArrayList<Map<String,Object>>();
			data = StringEscapeUtils.unescapeHtml(data);
			oData = JSONArray.fromObject(data);
		for (int i = 0 ; i < oList.size(); i++) {
				map.put("evtId", oList.get(i));
				for (Map<String, Object> list : oData) {
					map.put("grpId", list.get("grpId"));
					map.put("moblId", list.get("moblId"));
					Map<String, Object> checkMap = new HashMap<String, Object>();

					checkMap.put("evtId", oList.get(i));
					checkMap.put("moblId", list.get("moblId"));

					int checkEvent = MblEvtService.checkMblUser(checkMap);

					if(checkEvent != 0) {
						int deleteEvent = MblEvtService.deleteMbl(map);
						if (deleteEvent == 1) {
							chkSuccess++;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.debug("error >>> {}",e);
		}
		return chkSuccess;
	}

}
