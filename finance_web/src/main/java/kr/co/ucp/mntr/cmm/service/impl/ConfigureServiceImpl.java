/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : ConfigureServiceImpl.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 3. 18. SaintJuny@ubolt.co.kr 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.cmm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.CmmCodeService;
import kr.co.ucp.mntr.cmm.service.ConfigureMapper;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.service.ConfigureVO;
import kr.co.ucp.mntr.cmm.util.SessionUtil;

@Service("configureService")
public class ConfigureServiceImpl implements ConfigureService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="cmmCodeService")
	private CmmCodeService cmmCodeService;

	@Resource(name="configureMapper")
	private ConfigureMapper configureMapper;

	@Resource(name = "config")
	private Properties config;

//	@Override
//	public EgovMap getUmConfigure() throws Exception {
//		
//		String dstrtCd = EgovStringUtil.nullConvert(configure.getDstrtCd());
//		String sysId = EgovStringUtil.nullConvert(configure.getSysId());
//		String userId = EgovStringUtil.nullConvert(configure.getUserId());
//
//		if ("".equals(dstrtCd)) {
//			dstrtCd = prprtsService.getString("DSTRT_CD");
//			configure.setDstrtCd(dstrtCd);
//		}
//		/*
//		if("DUC".equals(sysId) || "112".equals(sysId) || "119".equals(sysId) || "WPS".equals(sysId) || "ESS".equals(sysId) || "SCN".equals(sysId)) {
//			configure.setSysId(sysId);
//		}
//		else if("".equals(sysId)) {
//			configure.setSysId(SessionUtil.getUserInfo().getSysId());
//		}
//		if(!"".equals(sysId)) {
//			if(SessionUtil.isLoggedIn()) {
//				sysId = SessionUtil.getUserInfo().getSysId();
//			}
//			configure.setSysId(sysId);
//		}
//		else {
//			configure.setSysId("TVO");
//		}
//		*/
//		//configure.setUserId("TVO");
//		
//		return getUmConfigInfo();
//	}

	@Override
	public EgovMap getUmConfigInfo() throws Exception {

//		String dstrtCd = EgovStringUtil.nullConvert(configure.getDstrtCd());
//		String sysId = EgovStringUtil.nullConvert(configure.getSysId());
//		if ("".equals(dstrtCd)) {
//			dstrtCd = prprtsService.getString("DSTRT_CD");
//			configure.setDstrtCd(dstrtCd);
//		}
//		if ("".equals(sysId)) {
//			sysId = SessionUtil.getUserInfo().getSysId();
//			configure.setSysId(sysId);
//		}
//		EgovMap map2 = configureMapper.selectUmConfigInfo(configure);
//		logger.debug("--> umConfig2 => "+map2.toString());
		

		Map<String, String> params = new HashMap<String, String>();
		params.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
		
		EgovMap map = new EgovMap();
		map.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
		
		List<EgovMap> mapList = configureMapper.selectUmConfigList(params);
		for (EgovMap tmp : mapList) {
			map.put(tmp.get("prprtsKey"), tmp.get("prprtsVal"));
		}
		
		logger.debug("--> umConfig => "+map.toString());
		
		return map;
	}

	@Override
	public void registerUmConfigure(ConfigureVO configure) throws Exception {
		String dstrtCd = EgovStringUtil.nullConvert(configure.getDstrtCd());
		if ("".equals(dstrtCd)) {
			dstrtCd = prprtsService.getString("DSTRT_CD");
			configure.setDstrtCd(dstrtCd);
		}
		configureMapper.insertUmConfigure(configure);
	}

	@Override
	public List<ConfigureVO> selectMappingIp(EgovMap ipConfigure) throws Exception {
		return configureMapper.selectMappingIp(ipConfigure);
	}

	@Override
	public List<EgovMap> selectCctvCtlUsedTy(LoginVO vo) throws Exception {
		return configureMapper.selectCctvCtlUsedTy(vo);
	}

	@Override
	public EgovMap selectCctvCtlPtzTy(LoginVO vo) throws Exception {
		return configureMapper.selectCctvCtlPtzTy(vo);
	}

	/**
	 * 환경설정레이어를 가져온다.
	 */
	@Override
	public List<EgovMap> selectLayerMngList(ConfigureVO configure) throws Exception {
		LoginVO loginVO = SessionUtil.getUserInfo();
		configure.setUserId(loginVO.getUserId());
		configure.setGrpId(loginVO.getGrpId());
		configure.setAuthLvl(loginVO.getAuthLvl());

		String dstrtCd = EgovStringUtil.nullConvert(configure.getDstrtCd());
		if ("".equals(dstrtCd)) {
			dstrtCd = prprtsService.getString("DSTRT_CD");
			configure.setDstrtCd(dstrtCd);
		}

		List<EgovMap> list = configureMapper.selectLayerMngList(configure);

		boolean isChecked = true;
		for (EgovMap map : list) {
			String checked = EgovStringUtil.nullConvert(map.get("checkYn"));
			String layerGrpId = EgovStringUtil.nullConvert(map.get("layerGrpId"));

			if ("".equals(checked) && !"ETC".equals(layerGrpId)) {
				isChecked = false;
			}
		}
		if (!isChecked) {
			configure.setCheckYn("Y");
			int i = updateAllLayerMng(configure);
			logger.info("--> 사용자 레이어 정보 최초 생성: {}", i);
			list = configureMapper.selectLayerMngList(configure);
		}
		return list;
	}

	/**
	 * 환경설정레이어를 수정한다.
	 */
	@Override
	public int updateLayerMng(ConfigureVO configure) throws Exception {
		String userId = EgovStringUtil.nullConvert(configure.getUserId());
		String dstrtCd = EgovStringUtil.nullConvert(configure.getDstrtCd());
		if ("".equals(userId)) {
			configure.setUserId(SessionUtil.getUserId());
		}
		if ("".equals(dstrtCd)) {
			dstrtCd = prprtsService.getString("DSTRT_CD");
			configure.setDstrtCd(dstrtCd);
		}
		return configureMapper.updateLayerMng(configure);
	}

	@Override
	public int updateAllLayerMng(ConfigureVO configure) throws Exception {
		LoginVO loginVO = SessionUtil.getUserInfo();
		configure.setUserId(loginVO.getUserId());
		configure.setGrpId(loginVO.getGrpId());
		configure.setAuthLvl(loginVO.getAuthLvl());

		String dstrtCd = EgovStringUtil.nullConvert(configure.getDstrtCd());
		if ("".equals(dstrtCd)) {
			dstrtCd = prprtsService.getString("DSTRT_CD");
			configure.setDstrtCd(dstrtCd);
		}
		return configureMapper.updateAllLayerMng(configure);
	}

	@Override
	public EgovMap refresh(EgovMap configure, LoginVO lgnVO, HttpServletRequest request) throws Exception {
		//ConfigureVO tmpConfigure = getUmConfigure(configure);
		EgovMap tmpConfigure = configure;
		if (SessionUtil.isLoggedIn()) {
			tmpConfigure = getUmConfigInfo();
			logger.info("--> SessionUtil.isLoggedIn ========");
		} else {
			logger.info("--> SessionUtil.isNotLoggedIn ========");
		}
		tmpConfigure.put("ucpId",(prprtsService.getString("UCP_ID", "UCP")));
		//tmpConfigure.setExeEnv(prprtsService.getGlobals("Globals.exeEnv", "DEV"));
		tmpConfigure.put("networkIp",getClientIp(request));

		EgovMap cctvCtlPtzTy = selectCctvCtlPtzTy(lgnVO);
		if (cctvCtlPtzTy != null) {
			String cctvAccessYn = EgovStringUtil.nullConvert(cctvCtlPtzTy.get("cctvAccessYn"));
			String cctvSearchYn = EgovStringUtil.nullConvert(cctvCtlPtzTy.get("cctvSearchYn"));
			String ptzCntrTy = EgovStringUtil.nullConvert(cctvCtlPtzTy.get("ptzCntrTy"));
			tmpConfigure.put("ptzCntrTy",ptzCntrTy);
			tmpConfigure.put("cctvAccessYn",cctvAccessYn);
			tmpConfigure.put("cctvSearchYn",cctvSearchYn);
		}

		String ucpNm = "";
//		List<EgovMap> codeList = cmmCodeService.selectCodeList(new CmmCodeVO("UCP_ID", tmpConfigure.get("ucpId").toString()));
//		if(codeList != null && codeList.size() == 1) {
//			EgovMap codeMap = codeList.get(0);
//			ucpNm = EgovStringUtil.nullConvert(codeMap.get("cdNmKo"));
//		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
		EgovMap map = configureMapper.selectDstrtInfo(params);
		ucpNm = map.get("dstrtNm").toString();
		
		if("".equals(ucpNm)) {
			ucpNm = "시연용센터";
		}
		tmpConfigure.put("ucpNm",ucpNm);
		
		logger.info("--> tmpConfigure => {}", tmpConfigure.toString());
		return tmpConfigure;
	}

	@Override
	public EgovMap getConfigure(String userId, String dstrtCd, String sysId) throws Exception {
		
		EgovMap configure = getUmConfigInfo();

		return configure;
	}

	@Override
	public Map<String, String> getIpMapping(EgovMap configure) throws Exception {
		Map<String, String> ipMapping = new HashMap<String, String>();
		
		if ("Y".equals(prprtsService.getString("IP_MAPPING_YN"))) {
			logger.info("--> getIpMapping(), IP MAPPING:{}", configure.get("networkIp").toString());
			
			//List<ConfigureVO> ipMappingList = selectMappingIp(configure);
			List<ConfigureVO> ipMappingList = configureMapper.selectMappingIp(configure);
			if (ipMappingList != null) {
				// 2017.02.09 space
				List<String> aryRtspIp = new ArrayList<String>();
				List<String> aryRtepIpMp = new ArrayList<String>();

				for (ConfigureVO vo : ipMappingList) {
					logger.info("--> getIpMapping(), GET MAPPING, NETWORK ID:{}, IP:{}, MP_IP:{}", vo.getNetworkId(), vo.getNetworkIp(), vo.getNetworkMpIp());
					if (vo.getNetworkId().startsWith("GIS_")) {
						ipMapping.put("gis", vo.getNetworkIp());
						ipMapping.put("gisMp", vo.getNetworkMpIp());
					} else if (vo.getNetworkId().startsWith("SCMP_")) {
						ipMapping.put("scmp", vo.getNetworkIp());
						ipMapping.put("scmpMp", vo.getNetworkMpIp());
					} else if (vo.getNetworkId().startsWith("SCMPIMG_")) {
						ipMapping.put("scmpImg", vo.getNetworkIp());
						ipMapping.put("scmpImgMp", vo.getNetworkMpIp());
					} else if (vo.getNetworkId().startsWith("SCMPMSG_")) {
						ipMapping.put("scmpMsg", vo.getNetworkIp());
						ipMapping.put("scmpMsgMp", vo.getNetworkMpIp());
					} else if (vo.getNetworkId().startsWith("WEBSOC")) {
						ipMapping.put("websocket", vo.getNetworkIp());
						ipMapping.put("websocketMp", vo.getNetworkMpIp());
					} else if (vo.getNetworkId().startsWith("VMS_")) {
						ipMapping.put("vms", vo.getNetworkIp());
						ipMapping.put("vmsMp", vo.getNetworkMpIp());
					} else if (vo.getNetworkId().startsWith("GEO_")) {
						ipMapping.put("geo", vo.getNetworkIp());
						ipMapping.put("geoMp", vo.getNetworkMpIp());
					} else if (vo.getNetworkId().startsWith("LINKOUT_")) {
						ipMapping.put("linkout", vo.getNetworkIp());
						ipMapping.put("linkoutMp", vo.getNetworkMpIp());
					} else if (vo.getNetworkId().startsWith("NVR_")) {
						ipMapping.put("nvr", vo.getNetworkIp());
						ipMapping.put("nvrMp", vo.getNetworkMpIp());
					} else if (vo.getNetworkId().startsWith("RTSP_")) {
						aryRtspIp.add(vo.getNetworkIp());
						aryRtepIpMp.add(vo.getNetworkMpIp());
					}
					if (!aryRtspIp.isEmpty()) {
						ipMapping.put("rtspIp", Joiner.on(",").join(aryRtspIp));
						ipMapping.put("rtspIpMp", Joiner.on(",").join(aryRtepIpMp));
						logger.debug("--> getIpMapping(), IP MAPPING => {} : {}", ipMapping.get("rtspIp"), ipMapping.get("rtspIpMp"));
					}
				}
			}
		}
		else {
			logger.info("--> NO IP MAPPING ==========");
		}

		return ipMapping;
	}

	public String getClientIp(HttpServletRequest request) {
		String clientIp = request.getHeader("X-Forwarded-For");

		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("Proxy-Client-IP");
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("WL-Proxy-Client-IP");
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("HTTP_CLIENT_IP");
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getRemoteAddr();
		}
		logger.info("--> getClientIp(), clientIp => {}", clientIp);
		return clientIp;
	}

	@Override
	public EgovMap getCmConfig() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
//		EgovMap map = configureMapper.selectCmConfig(params);
		EgovMap map = configureMapper.selectDstrtInfo(params);
		
		List<EgovMap> mapList = configureMapper.selectCmConfigList(params);
		for (EgovMap tmp : mapList) {
			map.put(tmp.get("prprtsKey"), tmp.get("prprtsVal"));
		}
		//map.put("dstrtCd", dstrtCd);
		logger.debug("--> cmConfig => "+map.toString());
		
		return map;
	}

	@Override
	public EgovMap getTvoConfig() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
		
//		EgovMap map2 = configureMapper.selectTvoConfig(params);

		EgovMap map = new EgovMap();
		map.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
		
		List<EgovMap> mapList = configureMapper.selectTvoConfigList(params);
		for (EgovMap tmp : mapList) {
			map.put(tmp.get("prprtsKey"), tmp.get("prprtsVal"));
		}
		logger.debug("--> tvoConfig => "+map.toString());
		
		return map;
	}

//	@Override
//	public Map<String, String> updateConfig(Map<String, String> params) throws Exception {
//		String userId = SessionUtil.getUserId();
//		params.put("userId", userId);
//		
//		Map<String, String> result = new HashMap<String, String>();		
//		int r = configureMapper.updateConfig(params);
//
//		result.put("result", String.valueOf(r));
//		return result;
//	}
	
}
