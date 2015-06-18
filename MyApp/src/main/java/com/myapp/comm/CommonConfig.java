package com.myapp.comm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CommonConfig {
	@Value("${service.shutDownStartDt}")
	private String serviceShutDownStartDt;
	@Value("${service.shutDownEndDt}")
	private String serviceShutDownEndDt;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setLocation(new ClassPathResource("/com/myapp/comm/common.properties"));
		
		return configurer;
	}
	
	/**
	 * @return the serviceShutDownStartDt
	 */
	public String getServiceShutDownStartDt() {
		return serviceShutDownStartDt;
	}

	/**
	 * @return the serviceShutDownEndDt
	 */
	public String getServiceShutDownEndDt() {
		return serviceShutDownEndDt;
	}
}
