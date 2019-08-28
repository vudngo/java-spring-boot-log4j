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
        
		//Added as tags to Sentry event
		MDC.put("customKey1", "value1");
        MDC.put("customKey2", "value2");
        
        //This one is not defined in sentry.properties
        MDC.put("customKey2", "value2");
        
        
        chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
