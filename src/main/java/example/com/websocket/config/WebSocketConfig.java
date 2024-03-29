package example.com.websocket.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import example.com.websocket.interceptor.HttpHandshakeInterceptor;

/**
 * dùng STOMP:  Streaming Text Oriented Messaging Protocol trên nền websocket để truyền dữ liệu
 * 
 * STOMP: cấu trúc Header dùng URL giống với http là UTF-8 có thể đọc trực tiếp như http header=> vì thế mới dùng Controller giống http.
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
     
    //@Autowired: khoi tạo 1 biến 
	@Autowired
    private HttpHandshakeInterceptor handshakeInterceptor;
 
	/**
	 * Khai báo Websocket URL cho thư viện Sockjs
	 * Sockjs: là lib xử lý websocket
	 * 
	 */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	/**
    	 *  websocket uri = "/socket_uri"
    	 *  handshakeInterceptor:  là Handler bắt event Handshake
    	 */
        registry.addEndpoint("/socket_uri").withSockJS().setInterceptors(handshakeInterceptor);
    }
 
    /**
     * Khai báo config cho STOMP protocol
     * phần nay lib cua STOMP sẽ xu ly TextFrame của websocket va trigger Event tới WebsocketController.java
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    	// Xem source code main.js sẽ hiểu ngay
    	
    	/**
    	 *  stomp_url context đăng ký nhận STOMP url phía server
    	 *  Prefix = "/app"
    	 *   @MessageMapping("/chat.sendMessage")  client gửi:  stomp_url = "/app/chat.sendMessage"
    	 */ 	
        registry.setApplicationDestinationPrefixes("/app");
        
        /**
         * đây là Security filter STOMP message thôi (ko có cũng ko sao)
         * Server chi cho phép STOM url = "/topic/*" đc gưi đi. Các URL khac sẽ ko cho phep gui đi
         * vd: STOMP url = "/topic/publicChatRoom" cho phép đi qua
         * vd: STOMP url = "/abc/publicChatRoom sẽ ko cho qua
         * 
         */
        registry.enableSimpleBroker("/topic");
    }
 
}
