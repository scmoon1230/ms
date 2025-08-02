package kr.co.ucp.swip.cmn.websocket.handler;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

public class WsEvtHandler extends TextWebSocketHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

	// 클라이언트와 연결 이후에 실행되는 메서드
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Map<String, Object> map = session.getAttributes();
		Object obj = map.get("LoginVO");
		
		if (obj != null && obj.getClass().isAssignableFrom(LoginVO.class)) {
			LoginVO vo = (LoginVO) obj;
			sessionList.add(session);
			String httpSessionId = EgovStringUtil.nullConvert(map.get("HTTP.SESSION.ID"));
			String userId = vo.getUserId();
			logger.info("--> 연결됨 : {}, {}", userId, httpSessionId);
			List<NameValuePair> params = URLEncodedUtils.parse(session.getUri(), StandardCharsets.UTF_8);
			
			if (!params.isEmpty()) {
				for (NameValuePair param : params) {
					if ("page".equals(param.getName()) && "login".equals(param.getValue())) {
						// 중복로그인불가
						if ("N".equals(vo.getLoginDplctnYn())) {
							// 세션에 같은 유저아이디와 다른 세션아이디가 있는지 체크
							JSONObject jsonObject = new JSONObject();
							boolean isDuplicate = false;
							for (WebSocketSession wss : sessionList) {
								Map<String, Object> wssAttributes = wss.getAttributes();
								LoginVO wssLoginVO = (LoginVO) wssAttributes.get("LoginVO");
								String wssHttpSessionId = EgovStringUtil.nullConvert(wssAttributes.get("HTTP.SESSION.ID"));
								if (userId.equals(wssLoginVO.getUserId()) && !httpSessionId.equals(wssHttpSessionId)) {
									isDuplicate = true;
								}
							}
							if (isDuplicate) {
								jsonObject.put("session", 0);
							} else {
								jsonObject.put("session", 1);
							}
							session.sendMessage(new TextMessage(jsonObject.toString()));
						}
					}
				}
			}
			
		}
	}

	// 클라이언트가 서버로 메시지를 전송했을 때 실행되는 메서드
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		logger.info("--> {}로 부터 {} 받음, {}", session.getId(), message.getPayload(), sessionList.size());
		
		List<NameValuePair> params = URLEncodedUtils.parse(session.getUri(), StandardCharsets.UTF_8);
		if (!params.isEmpty()) {
			for (NameValuePair param : params) {
				
				if ("page".equals(param.getName()) && "login".equals(param.getValue())) {
					if ("LOGOUT".equals(payload)) {
						Map<String, Object> map = session.getAttributes();
						if (map.containsKey("LoginVO")) {
							LoginVO vo = (LoginVO) map.get("LoginVO");
							String httpSessionId = EgovStringUtil.nullConvert(map.get("HTTP.SESSION.ID"));
							String userId = vo.getUserId();
							for (WebSocketSession wss : sessionList) {
								Map<String, Object> wssAttributes = wss.getAttributes();
								LoginVO wssLoginVO = (LoginVO) wssAttributes.get("LoginVO");
								String wssHttpSessionId = EgovStringUtil.nullConvert(wssAttributes.get("HTTP.SESSION.ID"));
								if (userId.equals(wssLoginVO.getUserId()) && !httpSessionId.equals(wssHttpSessionId)) {
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("evtId", payload);
									wss.sendMessage(new TextMessage(jsonObject.toString()));
								}
							}
						}
					}
					
				} else if ("page".equals(param.getName()) && "prvdApprv".equals(param.getValue())) {	// 승인요청
					for (WebSocketSession wss : sessionList) {
						Map<String, Object> wssAttributes = wss.getAttributes();
						LoginVO wssLoginVO = (LoginVO) wssAttributes.get("LoginVO");
						logger.info("--> 로긴한 사용자 정보 : {}, {}", wssLoginVO.getGrpId(), wssLoginVO.getAuthLvl());
						
						if ("PVECENTER".equals(wssLoginVO.getGrpId()) && ("1".equals(wssLoginVO.getAuthLvl()) || "2".equals(wssLoginVO.getAuthLvl()) ) ) {
							// 영상반출센터 관리자,승인자레벨 알림전송
							wss.sendMessage(new TextMessage(payload));
							logger.info("--> WsEvtHandler.handleTextMessage: send message {} ( {} )", wssLoginVO.getUserNmKo(), wssLoginVO.getUserId());
						}
					}
					
				} else if ("page".equals(param.getName()) && "maskCmplt".equals(param.getValue())) {	// 마스킹완료
					for (WebSocketSession wss : sessionList) {
						Map<String, Object> wssAttributes = wss.getAttributes();
						LoginVO wssLoginVO = (LoginVO) wssAttributes.get("LoginVO");
						logger.info("--> 로긴한 사용자 정보 : {}, {}", wssLoginVO.getGrpId(), wssLoginVO.getAuthLvl());

						if ("PVECENTER".equals(wssLoginVO.getGrpId()) && ("1".equals(wssLoginVO.getAuthLvl()) || "2".equals(wssLoginVO.getAuthLvl()) ) ) {
							// 영상반출센터 관리자,승인자레벨 알림전송
							wss.sendMessage(new TextMessage(payload));
							logger.info("--> WsEvtHandler.handleTextMessage: send message {} ( {} )", wssLoginVO.getUserNmKo(), wssLoginVO.getUserId());
						}
					}
					
				}
				
			}
		} else {
			for (WebSocketSession sess : sessionList) {
				sess.sendMessage(new TextMessage(payload));
			}
		}
	}

	// 클라이언트와 연결을 끊었을 때 실행되는 메소드
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Map<String, Object> map = session.getAttributes();
		Object obj = map.get("LoginVO");
		if (obj != null && obj.getClass().isAssignableFrom(LoginVO.class)) {
			LoginVO vo = (LoginVO) obj;
			sessionList.remove(session);
			logger.info("--> 연결 끊김 : {} ", vo.getUserId());
		}

		logger.info("--> WsEvtHandler.afterConnectionClosed");
	}
	
}
