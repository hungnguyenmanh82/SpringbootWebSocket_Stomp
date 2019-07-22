package example.com.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import example.com.websocket.model.ChatMessage;

/**
@controller: bản chất là đăng ký nhận Event từ client (vd: http event, STOMP event với websocket)
@MessageMapping(): là đăng ký nhận Event từ STOMP request
 */

@Controller
public class WebSocketController {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
 
 
	/**
	 * @MessageMapping(stomp_url): là đăng ký nhận Event STOMP message với stomp_url = "/chat.sendMessage" từ client 
	 * => ChatMessage param là thông tin trong body của STOMP message từ client  (định dạng JSON)
	 * 
	 * @sendTo(stomp_url): đăng ký gửi thong tin STOMP message trả về cho Client với stomp_url= "/topic/publicChatRoom"
	 * phia client phải đăng ký nhận stomp_url như trên 
	 * return ChatMessage: là thông tin STOMP message body từ Server (định dạng JSON)
	 */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/publicChatRoom")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
    	logger.info("sendMessage: " + chatMessage.toString());
        return chatMessage;
    }
 
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/publicChatRoom")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    	logger.info("addUser: " + chatMessage.toString());
    	// Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
 
}