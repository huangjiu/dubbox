package com.alibaba.demo.service.consumer;



import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.demo.service.DemoService;
import com.alibaba.demo.service.ValueObject;

//@Component
public class BussnessService implements InitializingBean {
	
	@Autowired
	private DemoService aneService;
	@Autowired
	private TestService testService;
	
	
	public void start(){
		 for( int i = 1 ; i < 100 ; i++) {
			 ValueObject value = new ValueObject();
			 value.setId(i);
			 long l =   aneService.push(value);
			 System.out.println(l);
		 }
		 
		 testService.say();
		
	}


	public void afterPropertiesSet() throws Exception {
		this.start();
		
	}

}
