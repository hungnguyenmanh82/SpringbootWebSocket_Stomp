package example.com.websocket.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import example.com.websocket.model.ChatMessage;

/**
//================================ Handshake via http protocol =======================
Step1:  client gửi http-request theo định dạng Http- request 
Step2: client nhận http-response theo định dạng http-request.

//=========================== websocket protocol ====================================
Step3: client và server giao tiếp qua Message theo 2 chiều (ko phân biệt client- server). 
Message = header + body  -> theo format của websocket protocol. https://tools.ietf.org/html/rfc6455 (ko cần đọc)
Chú ý: websocket protocol là chuẩn riêng Khác hẳn http protocol (tất nhiên vẫn base trên TCP protocol).
Sau khi thiết lập handshake qua http protocol, nó hoàn toàn tuân thủ websocket protocol về gửi nhận dữ liệu (khác với tcp protocol). 

*/

@Component
public class WebSocketEventListener {
 
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
 
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
 
    
    /**
     * @EventListener: là đăng ký nhận Event đc chuẩn hóa thông qua Kiểu Params từ Springboot
     * Tên function ko quan trọng. Quan trọng là Event đăng ký với Springboot "SessionConnectedEvent"
     * chú ý package name của "SessionConnectedEvent" class
     * 
     * Handle này đc gọi khi Websocket
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }
 
    /**
     * @EventListener: là đăng ký nhận Event đc chuẩn hóa thông qua Kiểu Params từ Springboot
     * Tên function ko quan trọng. Quan trọng là Event đăng ký với Springboot "SessionDisconnectEvent"
     * chú ý package name của "SessionDisconnectEvent" class
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
 
        String username = (String) headerAccessor.getSessionAttributes().get("username");
         
        if(username != null) {
            logger.info("User Disconnected : " + username);
 
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
 
            messagingTemplate.convertAndSend("/topic/publicChatRoom", chatMessage);
        }
    }
     
}
