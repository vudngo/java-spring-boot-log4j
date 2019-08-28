package io.sentry.example;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Component
public class CustomFilter implements Filter {


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {       
		//Defined in sentry.properties - will be added as Tags
		MDC.put("customKey1", "value1");
		MDC.put("customKey2", "value2");
		        
		//Not defined in sentry.properties - will be added to ADDITIONAL DATA 
		MDC.put("customKey3", "value3");
		          
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
