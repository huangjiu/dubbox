/*
 * Copyright 2006-2014 handu.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dubbo.spring.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.demo.service.DemoService;
import com.alibaba.demo.service.RefService;
import com.alibaba.demo.service.impl.DemoServiceImpl;
import com.alibaba.demo.service.impl.RefService2;
import com.alibaba.demo.service.impl.RefServiceImpl;

/**
 * @author Jinkai.Ma
 */
@Configuration
public class DubboxConfig {

	@Bean
	public DemoService aneService() {
		return new DemoServiceImpl();
	}

	@Bean
	public RefService refService() {
		return new RefServiceImpl();
	}

	
	@Bean
	public RefService2 refService2() {
		return new RefService2();
	}
}
