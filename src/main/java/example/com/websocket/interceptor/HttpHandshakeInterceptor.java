package example.com.websocket.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

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
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
 
    private static final Logger logger = LoggerFactory.getLogger(HttpHandshakeInterceptor.class);
     
    /**
     * this function là interceptor trc khi websocket-request đến Controller => tại đây đã nhận đc đầy đủ http request rồi
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
         
        logger.info("******Call beforeHandshake: Uri = " + request.getURI());
         
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            
            if( session.isNew()){
            	logger.info("new session");
            	attributes.put("sessionId", session.getId());
            }else{
            	logger.info("old sessionId");
            }
            
        }else{
        	logger.info("request != ServletServerHttpRequest");
        }
        
        return true;
    }
 
    /**
     * this function là interceptor sau khi xử lý ở controller=> 
     * trc đó controller đã add thông tin vào Http-response rồi. Tại đây chuẩn bị gửi http-response trả lại Client
     */
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception ex) {
        logger.info("-------Call afterHandshake");
    }
     
}