<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP</title>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="<c:url value="/resources/js/sockjs-0.3.4.js"/>"></script>
<script>
	$(function(){
		$("#sendBtn").click(function() {
			sendMessage();
		})
	});
	
	var sock;
	//웹소켓을 지정한 url과 연결
	sock = new SockJS("<c:url value="/echo"/>");
	
	//데이터가 나한테 전달될때 자동으로 실행되는 function
	sock.onmessage = onMessage;
	sock.onclose = onClose;
	function sendMessage(){
		sock.send($("#message").val());
	}
	//evt파라미터는 웹소켓을 보내준 데이터다(자동으로 들어옴)
	function onMessage(evt){
		var data = evt.data;
		$("#data").append(data+"<br/>");
		//sock.close();
	}
	function onClose(evt){
		$("#data").append("연결 끊김");
	}
	
	
	
</script>
</head>
<body>
	<input type="text" id="message" />
	<input type="button" id="sendBtn" value="전송" />
	<div id="data"></div>
</body>
</html>