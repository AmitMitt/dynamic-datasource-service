package com.example.dynamicdatasourceservice.util;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import com.example.dynamicdatasourceservice.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Util {

	static Log log = LogFactory.getLog(Util.class);
	
	public static Map<String, Map<String, Object>> settings = new HashMap<>();
	public static Map<String, SessionFactory> sessionFactoryMap = new HashMap<>();

	@PostConstruct
	private static void init() {	// TODO This data will be initialized through data of spring cloud config server. For now it is managed locally.

		log.info("inside init");
		
		// Domain One Settings
		String domainOneName = "0:0:0:0:0:0:0:1";
		Map<String, Object> domainOneSettings = new HashMap<>();
		domainOneSettings.put(Environment.URL, "jdbc:mysql://localhost:3306/test");
		domainOneSettings.put(Environment.USER, "root");
		domainOneSettings.put(Environment.PASS, "amit@2019");
		domainOneSettings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
		domainOneSettings.put(Environment.HBM2DDL_AUTO, "update");
		domainOneSettings.put(Environment.SHOW_SQL, "true");
		domainOneSettings.put("hibernate.hikari.connectionTimeout", "20000");// Maximum waiting time for a connection from the pool
		domainOneSettings.put("hibernate.hikari.minimumIdle", "10");         // Minimum number of ideal connections in the pool
		domainOneSettings.put("hibernate.hikari.maximumPoolSize", "20");     // Maximum number of actual connection in the pool
		domainOneSettings.put("hibernate.hikari.idleTimeout", "300000");     // Maximum time that a connection is allowed to sit ideal in the pool

		settings.put(domainOneName, domainOneSettings);

		// Domain Two Settings
		String domainTwoName = "192.168.225.55";
		Map<String, Object> domainTwoSettings = new HashMap<>();
		domainTwoSettings.put(Environment.URL, "jdbc:mysql://localhost:3306/test");
		domainTwoSettings.put(Environment.USER, "root");
		domainTwoSettings.put(Environment.PASS, "amit@123");
		domainTwoSettings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
		domainTwoSettings.put(Environment.HBM2DDL_AUTO, "update");
		domainTwoSettings.put(Environment.SHOW_SQL, "true");
		domainTwoSettings.put("hibernate.hikari.connectionTimeout", "20000");// Maximum waiting time for a connection from the pool
		domainTwoSettings.put("hibernate.hikari.minimumIdle", "10");         // Minimum number of ideal connections in the pool
		domainTwoSettings.put("hibernate.hikari.maximumPoolSize", "20");     // Maximum number of actual connection in the pool
		domainTwoSettings.put("hibernate.hikari.idleTimeout", "300000");     // Maximum time that a connection is allowed to sit ideal in the pool

		settings.put(domainTwoName, domainTwoSettings);

		log.info("exit init");
	}

	public static Session getSession(String domainName) throws Exception{
		log.info("inside getSession");
		log.info("domainName : " + domainName);

		if(sessionFactoryMap.containsKey(domainName)){
			log.info("session factory available");

			return sessionFactoryMap.get(domainName).openSession();
		}
		else if(settings.containsKey(domainName)){
			log.info("session factory not available, create new one");

			StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
			registryBuilder.applySettings(settings.get(domainName));
			StandardServiceRegistry serviceRegistry = registryBuilder.build();

			MetadataSources sources = new MetadataSources(serviceRegistry);
			sources.addAnnotatedClass(User.class);

			Metadata metadata = sources.getMetadataBuilder().build();
			SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

			sessionFactoryMap.put(domainName, sessionFactory);

			return sessionFactory.openSession();
		}
		else{
			log.info("could not create session factory, setting not available");
			throw new Exception("could not create session factory, setting not available");
		}
	}
}
