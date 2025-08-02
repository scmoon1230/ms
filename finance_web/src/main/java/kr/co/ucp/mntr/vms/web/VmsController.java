/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : VmsController.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 11. 18. SaintJuny@ubolt.co.kr 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.vms.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

import com.google.gson.GsonBuilder;

import egovframework.rte.fdl.string.EgovNumericUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.mntr.cmm.service.CmmCodeService;
import kr.co.ucp.mntr.cmm.service.FcltVO;
import kr.co.ucp.mntr.gis.service.GeoDataService;
import kr.co.ucp.mntr.vms.service.VmsLogVO;
import kr.co.ucp.mntr.vms.service.VmsService;

@Controller
public class VmsController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="vmsService")
	private VmsService vmsService;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "cmmCodeService")
    private CmmCodeService cmmCodeService;

	@Resource(name="geoDataService")
	private GeoDataService geoDataService;

	@RequestMapping(value="/mntr/vms/playVms.do")
	public String showVmsPlayer(ModelMap model, @ModelAttribute FcltVO vo) throws Exception {
		EgovMap map = geoDataService.selectFcltById(vo);
		model.addAttribute("data", map);
		
		String page = "";
		String sViewerTyCd = EgovStringUtil.nullConvert(map.get("viewerTyCd"));
		if ("VMS".equals(sViewerTyCd)) {
			page = "blank/vms/vmsPlayer";
		} else if ("RTSP".equals(sViewerTyCd)) {
			page = "blank/vms/rtspPlayer";
		} else {
			model.addAttribute("msg", "지원하지 않는 뷰어타입 입니다");
			page = "blank/cmm/error";
		}
		return page;
	}

	@RequestMapping(value="/mntr/vms/playWebRTC.do")
	public String playWebRTC(ModelMap model, @ModelAttribute FcltVO vo, @RequestParam Map<String, Object> params) throws Exception {
		logger.info("--> playWebRTC(), FcltVO: {}", vo);
		logger.info("--> playWebRTC(), params: {}", params);
		
		EgovMap map = geoDataService.selectFcltById(vo);
		
//        String viewerTyCd = EgovStringUtil.nullConvert(map.get("viewerTyCd"));
//        String evtOcrNo = EgovStringUtil.nullConvert(params.get("evtOcrNo"));
		model.addAttribute("data", map);
        model.addAttribute("params", params);
//        if (!"".equals(evtOcrNo)) {
//            EventVO eventVO = new EventVO();
//            eventVO.setEvtOcrNo(evtOcrNo);
//            EgovMap eventMap = eventService.selectEvent(eventVO);
//            model.addAttribute("event", eventMap);
//        }
		
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

		String page = "";
		String sViewerTyCd = EgovStringUtil.nullConvert(map.get("viewerTyCd"));
		if ("VMS".equals(sViewerTyCd)) {
			page = "blank/vms/webRtcPlayer";
		} else {
			model.addAttribute("msg", "지원하지 않는 뷰어타입 입니다");
			page = "blank/cmm/error";
		}
		return page;
	}

	@RequestMapping(value="/mntr/vms/playRtsp.do")
	public String showRtspPlayer(ModelMap model, @ModelAttribute FcltVO vo) throws Exception {
		EgovMap map = geoDataService.selectFcltById(vo);
		model.addAttribute("data", map);
		return "blank/vms/rtspPlayer";
	}

	@RequestMapping(value="/mntr/vms/selectMngMemberList.json", method = RequestMethod.POST)
	public ModelAndView selectMngMemberList(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("memberList", vmsService.selectMngMemberList(params));
		return mav;
	}

	@RequestMapping(value="/mntr/vms/insertPtzLog.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertPtzLog(ModelMap model, @ModelAttribute VmsLogVO vo) throws Exception {
		if (EgovUserDetailsHelper.isAuthenticated()) {
			LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			vo.setUserId(lgnVO.getUserId());
		}
		long seqNo = vmsService.insertPtzLog(vo);
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("seqNo", seqNo);
		return resultMap;
	}

	@RequestMapping(value="/mntr/vms/insertViewLog.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> insertViewLog(ModelMap model, @ModelAttribute VmsLogVO vo) throws Exception {
		if (EgovUserDetailsHelper.isAuthenticated()) {
			LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			vo.setUserId(lgnVO.getUserId());
		}
		
		vo.setDstrtCd(prprtsService.getString("DSTRT_CD"));
		
		long seqNo = vmsService.insertViewLog(vo);
		
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("seqNo", seqNo);
		return resultMap;
	}

	@RequestMapping(value="/mntr/vms/ptz.json")
	public ModelAndView ptz(@RequestParam Map<String, Object> params, ModelMap model) throws Exception {
		String apiUrl = "";
		String type = EgovStringUtil.nullConvert(params.get("type"));
		String cmd = EgovStringUtil.nullConvert(params.get("cmd"));
		String uid = EgovStringUtil.nullConvert(params.get("uid"));
		String spd = EgovStringUtil.nullConvert(params.get("spd"));
		String duration = EgovStringUtil.nullConvert(params.get("duration"));

		ModelAndView mav = new ModelAndView("jsonView");

		//if ("".equals(sysCd) || "".equals(apiUrl) || "".equals(apiKey) || "".equals(centerCd)) {
		//if ("".equals(sysCd) || "".equals(apiUrl) || "".equals(apiKey)) {
		//	mav.addObject("msg", "VMS API 설정에 필요한 항목 중 누락된 항목이 있습니다.");
		//	return mav;
		//}

		if ("".equals(type) || "".equals(cmd) || "".equals(uid) || "".equals(spd) || "".equals(duration)) {
			mav.addObject("msg", "VMS PTZ 제어에 필요한 항목 중 누락된 항목이 있습니다.");
			return mav;
		}

		if (!EgovNumericUtil.isNumber(cmd) || !EgovNumericUtil.isNumber(spd) || !EgovNumericUtil.isNumber(duration)) {
			mav.addObject("msg", "VMS PTZ 제어에 필요한 항목 중 숫자 형식이어야 하는 항목이 있습니다.");
			return mav;
		}

		HttpPost httpPost = new HttpPost(apiUrl + "/api/vms/ptz");
		httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
		//httpPost.setHeader("x-auth-key", apiKey);
		//httpPost.setHeader("vms-sys-cd", sysCd);
		//httpPost.setHeader("center-cd", centerCd);

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("type", type);
		map.put("cmd", Integer.parseInt(cmd));
		map.put("uid", uid);
		map.put("spd", Integer.parseInt(spd));
		map.put("duration", Integer.parseInt(duration));

		try {
			String jsonStr = new GsonBuilder().create().toJson(map);
			httpPost.setEntity(new StringEntity(jsonStr));
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpPost);
			// Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body = handler.handleResponse(response);
				mav.addObject("result", body);

			} else {
				mav.addObject("msg", "error: " + response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			mav.addObject("msg", e.getMessage());
		}

		mav.addObject("params", params);
		return mav;
	}

	@RequestMapping(value="/mntr/vms/preset.json")
	public ModelAndView preset(@RequestParam Map<String, Object> params, ModelMap model) throws Exception {
		String apiUrl = "";
		String type = EgovStringUtil.nullConvert(params.get("type"));
		String uid = EgovStringUtil.nullConvert(params.get("uid"));
		String presetNo = EgovStringUtil.nullConvert(params.get("presetNo"));

		ModelAndView mav = new ModelAndView("jsonView");

		//if ("".equals(sysCd) || "".equals(apiUrl) || "".equals(apiKey) || "".equals(centerCd)) {
		//if ("".equals(sysCd) || "".equals(apiUrl) || "".equals(apiKey)) {
		//	mav.addObject("msg", "VMS API 설정에 필요한 항목 중 누락된 항목이 있습니다.");
		//	return mav;
		//}

		if ("".equals(type) || "".equals(uid) || "".equals(presetNo)) {
			mav.addObject("msg", "VMS PRESET 제어에 필요한 항목 중 누락된 항목이 있습니다.");
			return mav;
		}

		if (!EgovNumericUtil.isNumber(presetNo)) {
			mav.addObject("msg", "VMS PRESET 제어에 필요한 항목 중 숫자 형식이어야 하는 항목이 있습니다.");
			return mav;
		}

		HttpPost httpPost = new HttpPost(apiUrl + "/api/vms/preset");
		httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
		//httpPost.setHeader("x-auth-key", apiKey);
		//httpPost.setHeader("vms-sys-cd", sysCd);
		//httpPost.setHeader("center-cd", centerCd);

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("type", type);
		map.put("uid", uid);
		map.put("preset_no", Integer.parseInt(presetNo));

		try {
			String jsonStr = new GsonBuilder().create().toJson(map);
			httpPost.setEntity(new StringEntity(jsonStr));
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpPost);
			// Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body = handler.handleResponse(response);
				mav.addObject("result", body);

			} else {
				mav.addObject("msg", "error: " + response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			mav.addObject("msg", e.getMessage());
		}

		mav.addObject("params", params);
		return mav;
	}
}
