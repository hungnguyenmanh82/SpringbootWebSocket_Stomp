package example.com.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
https://o7planning.org/vi/10719/tao-ung-dung-chat-don-gian-voi-spring-boot-va-websocket
 */
/**
Step1:  client gửi http-request theo định dạng Http- request 
Step2: client nhận http-response theo định dạng http-request.
Step3: client và server giao tiếp qua Message theo 2 chiều (ko phân biệt client- server). 
Message = header + body  -> theo format của websocket protocol. https://tools.ietf.org/html/rfc6455 (ko cần đọc)
Chú ý: websocket protocol là chuẩn riêng Khác hẳn http protocol (tất nhiên vẫn base trên TCP protocol).
 Sau khi thiết lập handshake qua http protocol, nó hoàn toàn tuân thủ websocket protocol về gửi nhận dữ liệu (khác với tcp protocol). 

 */
@SpringBootApplication
public class SpringBootWebSocketApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebSocketApplication.class, args);
    }
     
}
