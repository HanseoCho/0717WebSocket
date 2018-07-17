package com.java.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class EchoHandler extends TextWebSocketHandler {
	private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EchoHandler.class);
	
	private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
	/* 클라이언트 연결후 실행되는 메소드 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{
		sessionList.add(session);
		logger.info("{}연결됨",session.getId());
		logger.info("***************************************");
		String text="";
		for(WebSocketSession sess:sessionList) {
			text+=sess.getId();
			text+=" ";
		}
		logger.info(text);
		logger.info("***************************************");
		
	}
	/* 클라이언트가 웹소켓 서버로 메시지를 전송했을대 실행되는 메소드 */
	@Override
	protected void handleTextMessage(WebSocketSession session,TextMessage message) throws Exception {
		logger.info("{}로 부터 {} 받음",session.getId(),message.getPayload());
		for(WebSocketSession sess : sessionList) {
			sess.sendMessage(new TextMessage(session.getId() + " : " + message.getPayload()));
		}
	}
	@Override
	public void afterConnectionClosed(WebSocketSession session,CloseStatus status) throws Exception {
		sessionList.remove(session);
		logger.info("{} 연결 끊김",session.getId());
	}
}
