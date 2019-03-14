package com.example.dynamicdatasourceservice.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.dynamicdatasourceservice.model.User;
import com.example.dynamicdatasourceservice.util.Util;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/doctor")
public class UserController {

    
	static Log log = LogFactory.getLog(UserController.class);
    

    @GetMapping("/test")
    public Object getUsers(HttpServletRequest request) throws Exception {
    	log.info("inside getUsers");

		String domainName = request.getRemoteAddr();
		log.info("domainName: " + domainName);

		List<User> users = null;
		try(Session session = Util.getSession(domainName)){
			users = session.createQuery("FROM User").list();
			log.info("size : " + users.size());
		}

    	log.info("exit getUsers");
    	return users;
    }
}



