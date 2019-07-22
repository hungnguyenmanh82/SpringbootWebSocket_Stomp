package example.com.websocket.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import example.com.websocket.interceptor.HttpHandshakeInterceptor;

/**
Phần này xử lý các http request thông thường, ko liên quan tới websocket cả

@controller: bản chất là đăng ký nhận Event
@RequestMapping(): là đăng ký nhận Event từ Http request
*/
@Controller
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model) {
    	logger.info("/");
        String username = (String) request.getSession().getAttribute("username");
  
        if (username == null || username.isEmpty()) {
            return "redirect:/login"; //redirect to login.html
        }
 
        return "redirect:/chat";  //chat.html
    }
    
    /**
     *  chat.html: sẽ dùng javascript lib sau de connect tơi Websocket trên server
			sockjs.js
			stomp.js
     */
    @RequestMapping("/chat")
    public String chat(HttpServletRequest request, Model model) {
    	logger.info("/chat");
        String username = (String) request.getSession().getAttribute("username");
  
        if (username == null || username.isEmpty()) {
            return "redirect:/login"; //redirect to login.html
        }
        
        model.addAttribute("username", username);
 
        return "chat";  //chat.html
    }
 
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String showLoginPage() {
    	logger.info("GET:  /login");
        return "login";
    }
 
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, @RequestParam(defaultValue = "") String username) {
    	logger.info("POST:  /login");
        username = username.trim();
 
        if (username.isEmpty()) {
            return "login";
        }
        request.getSession().setAttribute("username", username);
 
        return "redirect:/chat";
    }
 
    @RequestMapping(path = "/logout")
    public String logout(HttpServletRequest request) {
    	logger.info("/logout");
        request.getSession(true).invalidate();
         
        return "redirect:/login";
    }
     
}

