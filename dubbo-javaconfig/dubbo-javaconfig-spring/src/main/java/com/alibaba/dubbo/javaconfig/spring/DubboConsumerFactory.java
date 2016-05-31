package com.alibaba.dubbo.javaconfig.spring;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.ReferenceConfig;

public class DubboConsumerFactory implements ApplicationContextAware, BeanFactoryPostProcessor , DisposableBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private final ConcurrentMap<String, DubboReferenceBean<?>> referenceConfigs = new ConcurrentHashMap<String, DubboReferenceBean<?>>();
	private ApplicationContext applicationContext;
	private ConfigurableListableBeanFactory beanFactory;
	private String annotationPackage;
	private DubboConfig dubboxConfig;
	
	public DubboConsumerFactory(String annotationPackage, DubboConfig dubboxConfig) {
		super();
		this.annotationPackage = annotationPackage;
		this.dubboxConfig = dubboxConfig;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private void createReferences() {
		DubboScannerAnnotationClass scanner = new DubboScannerAnnotationClass();
		logger.debug("scan package : " + annotationPackage);
		List<Class> references = scanner.scan(annotationPackage);
		logger.debug("service length : " + references.size());
		for (Class reference : references) {
			try {
				logger.debug("reference : " + reference.getName());
				reference(reference);
			} catch (Exception e) {
				logger.debug("error create reference .", e);
			}
		}
	}

	private void reference(Class<?> referenceClass) throws Exception {
		Object service = null;
		try {
			service = this.beanFactory.getBean(referenceClass);
		} catch (Exception e) {
			logger.debug("nothing for this reference .", e);
		}
		
		if (service == null) {
			this.beanFactory.registerSingleton(referenceClass.getName(), refer(referenceClass));
		}
	}

	private Object refer(Class<?> referenceClass) { // method.getParameterTypes()[0]
		String interfaceName = referenceClass.getName();
		String key = "/" + interfaceName + ":1.0";
		DubboReferenceBean<?> referenceConfig = referenceConfigs.get(key);
		if (referenceConfig == null) {
			referenceConfig = new DubboReferenceBean<Object>();
			referenceConfig.setInterface(referenceClass);

			if (applicationContext != null) {
				referenceConfig.setApplicationContext(applicationContext);
				try {
					referenceConfig.afterPropertiesSet();
				} catch (RuntimeException e) {
					throw (RuntimeException) e;
				} catch (Exception e) {
					throw new IllegalStateException(e.getMessage(), e);
				}
			}
			referenceConfigs.putIfAbsent(key, referenceConfig);
			referenceConfig = referenceConfigs.get(key);
		}
		return referenceConfig.get();
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		createReferences();
	}

	@Override
	public void destroy() throws Exception {
		for (ReferenceConfig<?> referenceConfig : referenceConfigs.values()) {
			try {
				referenceConfig.destroy();
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		}
		
	}

}
