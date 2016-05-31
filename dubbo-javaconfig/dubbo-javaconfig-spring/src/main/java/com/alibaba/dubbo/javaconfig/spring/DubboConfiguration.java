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
package com.alibaba.dubbo.javaconfig.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;

/**
 * @author Jinkai.Ma
 */
@Configuration
public class DubboConfiguration {

	@Bean
	public ApplicationConfig applicationConfig(DubboConfig dubboxConfig) {
		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setName(dubboxConfig.getApplicationName());
		return applicationConfig;
	}

	@Bean
	public RegistryConfig registryConfig(DubboConfig dubboxConfig) {
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress(dubboxConfig.getRegistryAddress());
		return registryConfig;
	}

	@Bean
	public static DubboFactory dubboxFactory(DubboConfig dubboxConfig) {
		DubboFactory dubboxFactory = new DubboFactory(dubboxConfig.getAnnotationPackage(), dubboxConfig);
		return dubboxFactory;
	}

}
