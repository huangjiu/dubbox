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
import org.springframework.context.annotation.Import;

import com.alibaba.dubbo.javaconfig.spring.DubboConfig;
import com.alibaba.dubbo.javaconfig.spring.DubboConfiguration;

/**
 * @author Jinkai.Ma
 */
@Configuration
@Import(DubboConfiguration.class)
public class DubboxConsumerConfig {
	
  public static final String APPLICATION_NAME = "javaconfig-consumer-app";
  public static final String REGISTRY_ADDRESS = "redis://127.0.0.1:6379";
  public static final String ANNOTATION_PACKAGE = "com.alibaba.demo.service";
	
	@Bean
	public DubboConfig dubboConfig(){
		DubboConfig config = new DubboConfig();
		config.setApplicationName(APPLICATION_NAME);
		config.setRegistryAddress(REGISTRY_ADDRESS);
		config.setAnnotationPackage(ANNOTATION_PACKAGE);
		return config;
	}
	

//    public static final String APPLICATION_NAME = "javaconfig-consumer-app";
//
//    public static final String REGISTRY_ADDRESS = "redis://127.0.0.1:6379";
//
//    public static final String ANNOTATION_PACKAGE = "com.alibaba.demo.service";
//
//    @Bean
//    public ApplicationConfig applicationConfig() {
//        ApplicationConfig applicationConfig = new ApplicationConfig();
//        applicationConfig.setName(APPLICATION_NAME);
//        return applicationConfig;
//    }
//
//    @Bean
//    public RegistryConfig registryConfig() {
//        RegistryConfig registryConfig = new RegistryConfig();
//        registryConfig.setAddress(REGISTRY_ADDRESS);
//        return registryConfig;
//    }
//
////    @Bean
////    public DubboxAnnotationBean dubboxAnnotationBean() {
////    	DubboxAnnotationBean annotationBean = new DubboxAnnotationBean();
////        annotationBean.setPackage(ANNOTATION_PACKAGE);
////        return annotationBean;
////    }
//    
////    @Bean
////    public ConsumerProxyFactoryBean consumerProxyFactoryBean(){
////    	ConsumerProxyFactoryBean factoryBean = new ConsumerProxyFactoryBean();
////    	factoryBean.setProxyInterface(AneService.class);
////    	return factoryBean;
////    }
//    
//    @Bean
//    public DubboConsumerFactory consumerFactory(){
//    	DubboConsumerFactory factory = new DubboConsumerFactory();
//    	factory.setPackage(ANNOTATION_PACKAGE);
//    	return factory;
//    }
//    
//    @Bean
//    public DubboServiceFactory providerFactory(){
//    	DubboServiceFactory factory = new DubboServiceFactory();
//    	factory.setPackage(ANNOTATION_PACKAGE);
//    	return factory;
//    }
}
