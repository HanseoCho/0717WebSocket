package com.java.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.util.JSONPObject;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Component
public class EchoHandler extends TextWebSocketHandler {
	private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EchoHandler.class);
	
	private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

	//방구분을 List안의 List트로 해보는것도 좋은 시도일것같음
	//private List<ArrayList<WebSocketSession>> sessionList = new ArrayList<ArrayList<WebSocketSession>>();
	
	@Autowired
	SqlSession sls;	
	
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
		for(WebSocketSession sess:sessionList) {
			sess.sendMessage(new TextMessage(session.getId()));
		}
		
		logger.info("***************************************");
		
	}
	/* 클라이언트가 웹소켓 서버로 메시지를 전송했을대 실행되는 메소드 */
	@Override
	protected void handleTextMessage(WebSocketSession session,TextMessage message) throws Exception {
		/* 클라이언트에서 값을 보내주고 원하는 값뽑기 */
		JSONObject jObj = JSONObject.fromObject(message.getPayload());
		
		System.out.println(1);
		System.out.println("message : "+jObj.get("message"));
		System.out.println("number : "+jObj.get("number"));
		
		/* DB연결확인용 (실제가능하면 메소드를 따로 빼두어야함 웹소켓 클래스에서 활동폭 확인 */
		System.out.println(sls.selectList("test.selectTest"));
		// db사용가능
		
		logger.info("{}로 부터 {} 받음",session.getId(),message.getPayload());
		for(WebSocketSession sess : sessionList) {
			HashMap<String, Object> map = new HashMap<String,Object>();
			map.put("test", "test");
			map.put("message", session.getId() + " : " + message.getPayload());
			//텍스트를 보내는 기존방식 
			//sess.sendMessage(new TextMessage(session.getId() + " : " + message.getPayload()));
			//jsonObject전송하기
			JSONObject j = new JSONObject();
			j = JSONObject.fromObject(JSONSerializer.toJSON(map));
			System.out.println(j.toString());
			sess.sendMessage(new TextMessage(j.toString()));
		}
	}
	/* 나가면 연결이 끓긴다. */
	@Override
	public void afterConnectionClosed(WebSocketSession session,CloseStatus status) throws Exception {
		sessionList.remove(session);
		logger.info("{} 연결 끊김",session.getId());
	}
	
	// 
	public void sayserver(HashMap<String, Object> param) {
		
	}

	public void conObj (WebSocketSession sess, HashMap<String, Object> param) throws Exception {
		JSONObject j = new JSONObject();
		j = JSONObject.fromObject(JSONSerializer.toJSON(param));
		System.out.println(j.toString());
		sess.sendMessage(new TextMessage(j.toString()));
	}
}
