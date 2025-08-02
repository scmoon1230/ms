package kr.co.ucp.swip.cmn.websocket.handler;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.mntr.cmm.util.SessionUtil;

public class WsMsgHandler extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

    // 클라이언트와 연결 이후에 실행되는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> map = session.getAttributes();

        Object obj = map.get("LoginVO");
        if (obj != null && obj.getClass().isAssignableFrom(LoginVO.class)) {
            sessionList.add(session);
			/*
			LoginVO vo = (LoginVO) obj;
			String userId = vo.getUserId();
			String sessionId = session.getId();
			URI uri = session.getUri();
			String query = uri.getQuery();
			JSONObject json = new JSONObject();
			json.put("type", "join");
			json.put("userId",  userId);
			json.put("sessionId", sessionId);
			String payload = json.toString();
			session.sendMessage(new TextMessage(payload));
			logger.info("{}, {}, {} 연결됨", userId, sessionId, query);
			*/
        }
    }

    // 클라이언트가 서버로 메시지를 전송했을 때 실행되는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        URI uri = session.getUri();
        String query = uri.getQuery();
        if (query != null && !"".equals(query)) {
            String payload = message.getPayload();
            logger.info("===== handleTextMessage =====> {}", payload);
            JSONObject json = new JSONObject(payload);
            String role = json.getString("role");
            logger.info("===== ROLE : {}", role);
            if (!"CHAT".equals(role)) {
                String userList = json.getString("userList");
                String roomNm = json.getString("roomNm");
                String userId = json.getString("userId");
                String userNm = json.getString("userNm");
                if ("INVITE".equals(role)) {
                    userList += "," + userId;
                    roomNm += "," + userNm;
                } else if ("LEAVE".equals(role)) {
                    if (userList.startsWith(userId)) {
                        userList = userList.replace(userId + ",", "");
                        roomNm = roomNm.replace(userNm + ",", "");
                    } else {
                        userList = userList.replace("," + userId, "");
                        roomNm = roomNm.replace("," + userNm, "");
                    }
                }
                logger.info("===== userList, roomNm : {}, {}", userList, roomNm);
                json.put("userList", userList);
                json.put("roomNm", roomNm);
            }

            if (!json.isNull("userList") && !json.isNull("talkDscrt")) {
                String[] userArray = json.getString("userList").split(",");
                if (userArray.length != 0) {
                    List<String> userList = new ArrayList(Arrays.asList(userArray));
                    for (WebSocketSession sess : sessionList) {
                        LoginVO loginVO = (LoginVO) sess.getAttributes().get("LoginVO");
                        String userId = loginVO.getUserId();
                        if (query.equals(sess.getUri().getQuery())) {
                            sess.sendMessage(new TextMessage(json.toString()));
                            userList.remove(userId);
                        }
                    }
                    for (WebSocketSession sess : sessionList) {
                        LoginVO loginVO = (LoginVO) sess.getAttributes().get("LoginVO");
                        String userId = loginVO.getUserId();
                        for (String recipientUserId : userList) {
                            if (userId.equals(recipientUserId) && "CHAT".equals(role)) {
                                sess.sendMessage(new TextMessage(json.toString()));
                            }
                        }
                    }
                }
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
            logger.info("{} 연결 끊김", vo.getUserId());
        }
    }
}
