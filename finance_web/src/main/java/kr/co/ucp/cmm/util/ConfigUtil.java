package kr.co.ucp.cmm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import kr.co.ucp.cmm.service.PrprtsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;

@Component("configUtil")
public class ConfigUtil {

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public Map<String, String> urlMapping(Map<String, String> ipMapping) throws Exception {
		// URL 매핑처리
		Map<String, String> urlMapping = new HashMap<String, String>();
		String gisEngine = prprtsService.getString("GIS_ENGINE");
		String gisProjection = prprtsService.getString("GIS_PROJECTION");
		String gisUrl = prprtsService.getString("GIS_URL");
		String gisUrlAerial = prprtsService.getString("GIS_URL_AERIAL");
		//String serverIp = prprtsService.getGlobals("Globals.ServerIp") + ":" + prprtsService.getGlobals("Globals.ServerPort");
		//String scmpImg = prprtsService.getGlobals("Globals.ServerIp") + ":" + prprtsService.getGlobals("Globals.ServerPort");
		//String scmpMsg = prprtsService.getGlobals("Globals.ServerIp") + ":" + prprtsService.getGlobals("Globals.MsgServerPort");
		//String websocket = prprtsService.getString("WEB_SOCKET_CONN_IP") + ":" + prprtsService.getString("WEB_SOCKET_CONN_PORT");
		//String webRtcUrl = prprtsService.getString("VMS_WEBRTC_URL");
		String gisUrlWms = prprtsService.getString("GIS_URL_WMS");
		String gisUrlWfs = prprtsService.getString("GIS_URL_WFS");
		String gisUrlUti = prprtsService.getString("GIS_URL_UTI");
		//String linkoutUrl = prprtsService.getString("SWIP_SERVICE_LINKOUT_URL");

		urlMapping.put("gisEngine", gisEngine);
		urlMapping.put("gisProjection", gisProjection);
		urlMapping.put("gisUrl", gisUrl);
		urlMapping.put("gisUrlAerial", gisUrlAerial);
		//urlMapping.put("serverIp", serverIp);
		//urlMapping.put("scmpImg", scmpImg);
		//urlMapping.put("scmpMsg", scmpMsg);
		//urlMapping.put("websocket", websocket);
		//urlMapping.put("webRtcUrl", webRtcUrl);
		urlMapping.put("gisUrlWms", gisUrlWms);
		urlMapping.put("gisUrlWfs", gisUrlWfs);
		urlMapping.put("gisUrlUti", gisUrlUti);
		//urlMapping.put("linkoutUrl", linkoutUrl);
		logger.debug("--> bf mp >>>> {}", urlMapping.toString());
		
		if (ipMapping.isEmpty()) {
			return urlMapping;
		}

		String gisProxyYn = "Y";
		if (ipMapping.containsKey("gisProxyYn")) {
			gisProxyYn = ipMapping.get("gisProxyYn");
		}
		if (ipMapping.containsKey("gisMp") && ipMapping.get("gisMp").length() > 0 && !"Y".equals(gisProxyYn)) {
			gisUrl = gisUrl.replace(ipMapping.get("gis"), ipMapping.get("gisMp"));
			gisUrlAerial = gisUrlAerial.replace(ipMapping.get("gis"), ipMapping.get("gisMp"));
		}
		//if (ipMapping.containsKey("scmpMp") && ipMapping.get("scmpMp").length() > 0) {
		//	serverIp = serverIp.replace(ipMapping.get("scmp"), ipMapping.get("scmpMp"));
		//}
		//if (ipMapping.containsKey("scmpImgMp") && ipMapping.get("scmpImgMp").length() > 0) {
		//	scmpImg = scmpImg.replace(ipMapping.get("scmpImg"), ipMapping.get("scmpImgMp"));
		//}
		//if (ipMapping.containsKey("scmpMsgMp") && ipMapping.get("scmpMsgMp").length() > 0) {
		//	scmpMsg = scmpMsg.replace(ipMapping.get("scmpMsg"), ipMapping.get("scmpMsgMp"));
		//}
		//if (ipMapping.containsKey("websocket") && ipMapping.get("websocket").length() > 0) {
		//	websocket = websocket.replace(ipMapping.get("websocket"), ipMapping.get("websocketMp"));
		//}
		//if (ipMapping.containsKey("vmsMp") && ipMapping.get("vmsMp").length() > 0) {
		//	webRtcUrl = webRtcUrl.replace(ipMapping.get("vms"), ipMapping.get("vmsMp"));
		//}
		if (ipMapping.containsKey("geoMp") && ipMapping.get("geoMp").length() > 0) {
			gisUrlWms = gisUrlWms.replace(ipMapping.get("geo"), ipMapping.get("geoMp"));
			gisUrlWfs = gisUrlWfs.replace(ipMapping.get("geo"), ipMapping.get("geoMp"));
			gisUrlUti = gisUrlUti.replace(ipMapping.get("geo"), ipMapping.get("geoMp"));
		}
		//if (ipMapping.containsKey("linkoutMp") && ipMapping.get("linkoutMp").length() > 0) {
		//	linkoutUrl = linkoutUrl.replace(ipMapping.get("linkout"), ipMapping.get("linkoutMp"));
		//}

		urlMapping.put("gisUrl", gisUrl);
		urlMapping.put("gisUrlAerial", gisUrlAerial);
		//urlMapping.put("serverIp", serverIp);
		//urlMapping.put("scmpImg", scmpImg);
		//urlMapping.put("scmpMsg", scmpMsg);
		//urlMapping.put("websocket", websocket);
		//urlMapping.put("webRtcUrl", webRtcUrl);
		urlMapping.put("gisUrlWms", gisUrlWms);
		urlMapping.put("gisUrlWfs", gisUrlWfs);
		urlMapping.put("gisUrlUti", gisUrlUti);
		//urlMapping.put("linkoutUrl", linkoutUrl);
		logger.debug("--> af mp >>>> {}", urlMapping.toString());

		return urlMapping;
	}

	public String getClientIp(HttpServletRequest request) {
		String clientIp = request.getHeader("X-Forwarded-For");
		//logger.info("--> getClientIp(), clientIp0: {}", clientIp);

		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("Proxy-Client-IP");
			//logger.info("--> getClientIp(), clientIp1: {}", clientIp);
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("WL-Proxy-Client-IP");
			//logger.info("--> getClientIp(), clientIp2: {}", clientIp);
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("HTTP_CLIENT_IP");
			//logger.info("--> getClientIp(), clientIp3: {}", clientIp);
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
			//logger.info("--> getClientIp(), clientIp4: {}", clientIp);
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getRemoteAddr();
			//logger.info("--> getClientIp(), clientIp5: {}", clientIp);
		}
		logger.info("--> getClientIp(), clientIp: {}", clientIp);
		return clientIp;
	}

}
