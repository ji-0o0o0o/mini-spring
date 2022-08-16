//package com.hanghea99.minispring.security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//	@Override
//	public void addCorsMappings(CorsRegistry registry){
//		registry.addMapping("/**")
//				.exposedHeaders("*")
//				.allowCredentials(true)
//				.allowedOrigins("GET", "POST", "PATCH", "DELETE", "OPTIONS")
//				.allowedOrigins("https://devtools-fe.vercel.app/**")
//				.allowedOrigins("/**")
//				.allowedMethods(
//						HttpMethod.GET.name(),
//						HttpMethod.POST.name(),
//						HttpMethod.PATCH.name(),
//						HttpMethod.DELETE.name(),
//						HttpMethod.OPTIONS.name());
//	}
//}