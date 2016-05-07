package com.alibaba.dubbo.javaconfig.spring;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.javaconfig.annotation.EnableDubbo;

public class DubboConsumerFactory extends AbstractConfigFactory
		implements BeanFactoryPostProcessor , ApplicationContextAware, InstantiationAwareBeanPostProcessor , DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(Logger.class);
	private ConfigurableListableBeanFactory beanFactory;
	private ApplicationContext applicationContext;
	private final ConcurrentMap<String, DubboxReferenceBean<?>> referenceConfigs = new ConcurrentHashMap<String, DubboxReferenceBean<?>>();
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		if (!isMatchPackage(beanClass)) {
			return null;
		}
		
		Set<Class<?>> proxies = getProxies(beanClass);
		if (proxies != null && !proxies.isEmpty()) {
			for (Class<?> proxy : proxies) {
				if (!this.beanFactory.containsBean(proxy.getName())) {
					this.beanFactory.registerSingleton(proxy.getName(),  refer(proxy));
				}
			}
		}
		return null;
	}

	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		return true;
	}

	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean,
			String beanName) throws BeansException {
		return pvs;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private Set<Class<?>> getProxies(Class<?> beanClass) {

		Set<Class<?>> classes = new HashSet<Class<?>>();

		Method[] methods = beanClass.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (name.length() > 3 && name.startsWith("set") && method.getParameterTypes().length == 1
					&& Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
				try {
					if (method.getParameterTypes()[0].getAnnotation(EnableDubbo.class) != null
							&& method.getAnnotation(Autowired.class) != null) {
						classes.add(method.getParameterTypes()[0]);
					}
				} catch (Exception e) {
					// modified by lishen
					throw new BeanInitializationException("Failed to init remote service reference at method " + name
							+ " in class " + beanClass.getClass().getName(), e);
				}
			}
		}
		Field[] fields = beanClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().getAnnotation(EnableDubbo.class) != null && field.getAnnotation(Autowired.class) != null) {
				classes.add(field.getType());
			}

		}
		return classes;
	}
	
	private Object refer(Class<?> referenceClass) { // method.getParameterTypes()[0]
		String interfaceName = referenceClass.getName();
		String key = "/" + interfaceName + ":1.0";
		DubboxReferenceBean<?> referenceConfig = referenceConfigs.get(key);
		if (referenceConfig == null) {
			referenceConfig = new DubboxReferenceBean<Object>();
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
