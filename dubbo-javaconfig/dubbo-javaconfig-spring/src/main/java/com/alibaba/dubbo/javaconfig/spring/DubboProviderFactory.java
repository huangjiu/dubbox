/*
 * Copyright 1999-2012 Alibaba Group.
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

import java.util.Set;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.javaconfig.annotation.Dubbo;

/**
 * AnnotationBean
 * 
 * @author william.liangf
 * @export
 */
public class DubboProviderFactory extends AbstractConfigFactory
		implements DisposableBean, BeanPostProcessor, ApplicationContextAware {

	private final Set<ServiceConfig<?>> serviceConfigs = new ConcurrentHashSet<ServiceConfig<?>>();
	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void destroy() throws Exception {
		for (ServiceConfig<?> serviceConfig : serviceConfigs) {
			try {
				serviceConfig.unexport();
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private Class<?> getDubboxInterface(Class<?> clazz) {
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> class1 : interfaces) {
			Dubbo dubbox = class1.getAnnotation(Dubbo.class);
			if (dubbox != null) {
				return class1;
			}
		}
		return null;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (!isMatchPackage(bean.getClass())) {
			return bean;
		}
		Class<?> clazz = bean.getClass();
		if (isProxyBean(bean)) {
			clazz = AopUtils.getTargetClass(bean);
		}
		Class<?> provider = getDubboxInterface(clazz);
		if (provider == null) {
			return bean;
		}

		DubboxServiceBean<Object> serviceConfig = new DubboxServiceBean<Object>();
		serviceConfig.setInterface(provider);
		if (applicationContext != null) {
			serviceConfig.setApplicationContext(applicationContext);
			try {
				serviceConfig.afterPropertiesSet();
			} catch (RuntimeException e) {
				throw (RuntimeException) e;
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalStateException(e.getMessage(), e);
			}
		}
		serviceConfig.setRef(bean);
		serviceConfigs.add(serviceConfig);
		serviceConfig.export();
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	private boolean isProxyBean(Object bean) {
		return AopUtils.isAopProxy(bean);
	}
}
