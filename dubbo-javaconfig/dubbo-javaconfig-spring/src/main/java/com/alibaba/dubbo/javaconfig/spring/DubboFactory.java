package com.alibaba.dubbo.javaconfig.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DubboFactory
		implements ApplicationContextAware, InitializingBean, BeanFactoryPostProcessor, DisposableBean {

	private DubboConsumerFactory consumerFactory;
	private DubboProviderFactory providerFactory;

	public DubboFactory(String annotationPackage, DubboConfig dubboxConfig) {
		super();
		this.consumerFactory = new DubboConsumerFactory(annotationPackage, dubboxConfig);
		this.providerFactory = new DubboProviderFactory(annotationPackage, dubboxConfig);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.consumerFactory.postProcessBeanFactory(beanFactory);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.consumerFactory.setApplicationContext(applicationContext);
		this.providerFactory.setApplicationContext(applicationContext);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.providerFactory.afterPropertiesSet();
	}

	@Override
	public void destroy() throws Exception {
		this.providerFactory.destroy();
		this.consumerFactory.destroy();

	}

}
