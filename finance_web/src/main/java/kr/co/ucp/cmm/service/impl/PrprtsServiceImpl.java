package kr.co.ucp.cmm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.service.ConfigureVO;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.service.PrprtsMapper;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.cmm.util.ConfigUtil;

@Service("prprtsService")
public class PrprtsServiceImpl implements PrprtsService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsMapper")
	private PrprtsMapper prprtsMapper;

	@Resource(name = "config")
	private Properties config;

	@Resource(name = "configureService")
	private ConfigureService configureService;

	@Resource(name = "configUtil")
	private ConfigUtil configUtil;

	// private static List<HashMap<Object, Object>> prprtsList = new ArrayList<HashMap<Object, Object>>();
//	private static List<EgovMap> prprtsList = new ArrayList<EgovMap>();
	private static Map<String, Object> prprtsMap = new HashMap<String, Object>();
	private static String prprtsId = "";

	@PostConstruct
	public void init() {
		prprtsMap.clear();
		
		//prprtsId = CommUtil.objNullToVal(config.get("Globals.PrprtsId"), "089");
		logger.info("--> PrprtsService.init(), prprtsId >>>> {}", prprtsId);
		
		try {
			//selectPrprtsList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("--> 프로퍼티 read error >>>> {},{}", prprtsId,e.getMessage());
		}
	}

	@Override
	public void selectPrprtsList() throws Exception {
		if (prprtsMap.isEmpty()) {
			synchronized (prprtsMap) {
				if (prprtsMap.isEmpty()) {
					List<EgovMap> egovMapList = (ArrayList<EgovMap>) prprtsMapper.selectPrprtsList(prprtsId);
					setPrprtsMap(egovMapList);
				}
			}
		}
	}
	/*
	 * 자주사용하는 코드 리스트
	 */
	@Override
	public void selectCodeList() throws Exception {
		List<EgovMap> useGrpList = (ArrayList<EgovMap>) prprtsMapper.selectCodeList("USE_TY");
		List<EgovMap> rowPerPageList = (ArrayList<EgovMap>) prprtsMapper.selectCodeList("PAGE_ROWS");
		prprtsMap.put("useGrpList", useGrpList);
		prprtsMap.put("rowPerPageList", rowPerPageList);
	}

	@Override
	public void reloadPrprts(HttpServletRequest request) throws Exception {
		List<EgovMap> egovMapList = (ArrayList<EgovMap>) prprtsMapper.selectPrprtsList(prprtsId);
		setPrprtsMap(egovMapList);
		logger.debug("--> prprts reload >>>> {}", prprtsId);

//		request.getSession().setAttribute("LOAD_TIME", CommUtil.getCurrentTime14());
//		request.getSession().setAttribute("RELOAD", "Y");
//		reloadSession(request);
	}
	@Override
	public void reloadSession(HttpServletRequest request) throws Exception {
//		Map<String, String> tMap = (Map<String, String>) request.getSession().getAttribute("urlMapping");
//		logger.debug("--> linkoutUrl >>>> {}",tMap.get("linkoutUrl"));

		// Configure
//		ConfigureVO configure = configureService.getConfigure(resultVO.getUserId(), dstrtCd, sysId);
//		configure.setNetworkIp(configUtil.getClientIp(request));
//
		EgovMap cfgVO = (EgovMap) request.getSession().getAttribute("CfgVO");
		logger.debug("--> cfgVO >>>> {}", cfgVO.toString());

		// IP Mapping
		Map<String, String> ipMapping = configureService.getIpMapping(cfgVO);
		request.getSession().setAttribute("ipMapping", ipMapping);
		
		EgovMap cmConfig = configureService.getCmConfig();
//		request.getSession().setAttribute("cmConfig", cmConfig);

//		EgovMap tvoConfig = configureService.getTvoConfig();
//		request.getSession().setAttribute("tvoConfig", tvoConfig);

		String gisProxyYn = "Y";
		if (cmConfig.containsKey("gisProxyYn")) {
			gisProxyYn = cmConfig.get("gisProxyYn").toString();
		}
		ipMapping.put("gisProxyYn", gisProxyYn);
		
		Map<String, String> urlMapping = configUtil.urlMapping(ipMapping);
		request.getSession().setAttribute("urlMapping", urlMapping);
		
		request.getSession().setAttribute("LOAD_TIME", CommUtil.getCurrentTime14());
		logger.debug("--> reloadSession >>>> LOAD_TIME:{}", request.getSession().getAttribute("LOAD_TIME"));
	}

	/*
	 * prprtsMap 생성
	 */
	private void setPrprtsMap(List<EgovMap> egovMapList) throws Exception {
		prprtsMap.clear();
		if (prprtsMap.isEmpty()) {
			synchronized (prprtsMap) {
				prprtsMap.clear();
				Iterator<EgovMap> iterator = egovMapList.iterator();
				while (iterator.hasNext()) {
					EgovMap egovMap = (EgovMap) iterator.next();
					prprtsMap.put((String)egovMap.get("prprtsKey"), (String)egovMap.get("prprtsVal"));
				}
				prprtsMap.put("DSTRT_CD", prprtsId);
				prprtsMap.put("LOAD_TIME", CommUtil.getCurrentTime14());
				logger.debug("--> prprtsMap >>>> {},{}", prprtsId, prprtsMap.toString());
			}
		}
		selectCodeList();
		
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {			os = "WINDOWS";
		} else if (os.contains("linux")) {	os = "LINUX";
		}
		prprtsMap.put("SERVER_OS", os);

		prprtsMap = new TreeMap<>(prprtsMap);		// key정렬
		prprtsMap.forEach((key, value) -> {
			System.out.println("--> prprtsMap >>>> "+key + " = " + value);
		});
		
	}

//	@Override
//	public List<EgovMap> selectPrprtsList(String PrprtsId) throws Exception {
//		return (ArrayList<EgovMap>) prprtsMapper.selectPrprtsList(prprtsId);
//	}

	public static void clear() throws Exception {
		prprtsMap.clear();
	}

//	public static void reload(List<EgovMap> egovMapList) throws Exception {
//		prprtsList.clear();
//		if (prprtsList.isEmpty()) {
//			synchronized (prprtsList) {
//				if (prprtsList.isEmpty()) {
//					prprtsList.clear();
//					prprtsList.addAll(egovMapList);
////					System.out.print(egovMapList.toString());
//				}
//			}
//		}
//	}

	public static String getPrprtsVal(String prprtsKey) throws Exception {
		String prprtsVal = EgovStringUtil.nullConvert(prprtsMap.get(prprtsKey));
//		Iterator<EgovMap> iterator = prprtsList.iterator();
//		while (iterator.hasNext()) {
//			EgovMap egovMap = (EgovMap) iterator.next();
//			if(prprtsKey.equals((String)egovMap.get("prprtsKey"))) {
//				prprtsVal = (String)egovMap.get("prprtsVal");
//				break;
//			}
//		}
		return prprtsVal;
	}
//	public static List<EgovMap> getPrprtsList() throws Exception {
//		return prprtsList;
//	}
	public static Map<String, Object> getPrprtsMap() throws Exception {
		return prprtsMap;
	}
	public static String getPrprtsId() throws Exception {
		return prprtsId;
	}

	/**
	 *  DB에 저장된 프로퍼티 값을 가져온다.
	 *
	 * @param key
	 * @return prprts
	 * @throws Exception
	 */
	private String getPrprts(String key) {
		String property = EgovStringUtil.nullConvert(prprtsMap.get(key));
//		if ("".equals(property)) {
//			for (EgovMap prprts : prprtsList) {
//				String prprtsKey = EgovStringUtil.nullConvert(prprts.get("prprtsKey"));
//				String prprtsVal = EgovStringUtil.nullConvert(prprts.get("prprtsVal"));
//				String defaultVal = EgovStringUtil.nullConvert(prprts.get("defaultVal"));
//				if (prprtsKey.equals(key)) {
//					if(!"".equals(prprtsVal)) {
//						property = prprtsVal;
//						break;
//					} else {
//						if (!"".equals(defaultVal)) {
//							property = defaultVal;
//							break;
//						}
//					}
//				}
//			}
//		}
		return property.trim();
	}
	private List<EgovMap>  getPrprtsList(String key) {
		return (List<EgovMap>) prprtsMap.get(key);
	}
	private String getGlobalPrprts(String key) {
		String property = EgovStringUtil.nullConvert(config.get(key));
		return property.trim();
	}
	/*
	 * com.prprts파일의 프로퍼티
	 */
	@Override
	public String getGlobals(String key) {
		return getGlobalPrprts(key);
	}
	@Override
	public String getGlobals(String key, String defaultValue) {
		String property = getGlobals(key);
		if ("".equals(property)) {
			property = defaultValue;
		}
		return property;
	}

	@Override
	public List<EgovMap> getList(String key) {
		return getPrprtsList(key);
	}

	@Override
	public String getString(String key) {
		return getPrprts(key);
	}
	
	@Override
	public String getString(String key, String defaultValue) {
		String property = getString(key);
		if ("".equals(property)) {
			property = defaultValue;
		}
		return property;
	}

	@Override
	public void setString(String key, String value) {
		prprtsMap.put(key, value);
	}

	@Override
	public int getInt(String key) {
		String property = getPrprts(key);
		int i = 0;
		if (NumberUtils.isCreatable(property)) {
			try {
				i = Integer.parseInt(property);
			} catch (NumberFormatException nfe) {
				i = 0;
			}
		}
		return i;
	}

	@Override
	public int getInt(String key, int defaultValue) {
		int property = getInt(key);
		if (0 == defaultValue) {
			property = defaultValue;
		}
		return property;
	}

	// 공통코드에서 cd_dstrt get map  = map.put("cdId"), map.put("cdGrpId")
//	@Override
//	public String getCdDscrt(String cdId, String cdGrpId) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("cdId",cdId);
//		map.put("cdGrpId",cdGrpId);
//		return prprtsMapper.getCdDscrt(map);
//	}

//	@Override
//	public String getUserNm(String userId) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		return prprtsMapper.getUserNm(userId);
//	}

	@Override
	public int updatePrprts(Map<String, Object> map) throws Exception {
		//return prprtsMapper.updatePrprts(map);
		return prprtsMapper.insertPrprts(map);
	}

	@Override
	public String getDigest(String userId) {
		return prprtsMapper.getDigest(userId);
	}

	@Override
	public String getSalt(int b)
    {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < b){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, b);
    }

}
