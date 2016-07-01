package com.alibaba.dubbo.javaconfig.spring;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ReflectionUtils;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ServiceConfig;

public class DubboProviderFactory implements ApplicationContextAware, InitializingBean, DisposableBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private final Set<DubboServiceBean<?>> serviceConfigs = new ConcurrentHashSet<DubboServiceBean<?>>();
	private ApplicationContext applicationContext;
	private String annotationPackage;
	private DubboConfig dubboxConfig;

	public DubboProviderFactory(String annotationPackage, DubboConfig dubboxConfig) {
		super();
		this.annotationPackage = annotationPackage;
		this.dubboxConfig = dubboxConfig;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private void exportProviders() {
		DubboScannerAnnotationClass scanner = new DubboScannerAnnotationClass();
		logger.debug("scan package : " + annotationPackage);
		List<Class> providers = scanner.scan(annotationPackage);
		logger.debug("provider length : " + providers.size());

		for (Class provider : providers) {
			try {
				logger.debug("provider : " + provider.getName());
				export(provider);
			} catch (Exception e) {
				logger.debug("error export provider .", e);
			}
		}
	}

	private void export(Class<?> provider) throws Exception {

		logger.debug("Loading The provider : " + provider.getName());
		DubboServiceBean<Object> serviceConfig = new DubboServiceBean<Object>();
		serviceConfig.setInterface(provider);
		if (applicationContext != null) {
			Object providerRef = applicationContext.getBean(provider);
			injection(providerRef);
			serviceConfig.setInterface(provider);
			serviceConfig.setApplicationContext(applicationContext);
			serviceConfig.setBeanName(provider.getName());
			serviceConfig.setRef(providerRef);
			this.initService(serviceConfig);
			serviceConfig.afterPropertiesSet();
			logger.debug("init service : " + provider.getName());
			serviceConfigs.add(serviceConfig);
			serviceConfig.export();
			logger.debug("export service : " + provider.getName());
		}
	}

	private void initService(DubboServiceBean<Object> serviceConfig) {
		if (this.dubboxConfig != null) {

			serviceConfig.setAsync(this.dubboxConfig.isAsync());

			if (this.dubboxConfig.getDelay() > 0) {
				serviceConfig.setDelay(this.dubboxConfig.getDelay());
			}
			if (this.dubboxConfig.getTimeout() > 0) {
				serviceConfig.setTimeout(this.dubboxConfig.getTimeout());
			}
			if (this.dubboxConfig.getRetries() > 0) {
				serviceConfig.setRetries(this.dubboxConfig.getRetries());
			}
			if (this.dubboxConfig.getConnections() > 0) {
				serviceConfig.setConnections(this.dubboxConfig.getConnections());
			}
			if (!StringUtils.isBlank(this.dubboxConfig.getVersion())) {
				serviceConfig.setVersion(this.dubboxConfig.getVersion());
			}
			if (this.dubboxConfig.getProtocol() != null) {
				serviceConfig.setProtocol(this.dubboxConfig.getProtocol());
			}
			if (!StringUtils.isBlank(this.dubboxConfig.getLoadbalance())) {
				serviceConfig.setLoadbalance(this.dubboxConfig.getLoadbalance());
			}
			if (!StringUtils.isBlank(this.dubboxConfig.getToken())) {
				serviceConfig.setToken(this.dubboxConfig.getToken());
			}
		}

	}

	private void injection(Object bean) {
		List<Field> fields = findAutowiringFields(bean.getClass());
		for (Field field : fields) {
			ReflectionUtils.makeAccessible(field);
			ReflectionUtils.setField(field, bean, applicationContext.getBean(field.getType()));
		}
	}

	private List<Field> findAutowiringFields(final Class clazz) {
		final LinkedList<Field> currElements = new LinkedList<Field>();
		Class<?> targetClass = clazz;
		ReflectionUtils.doWithLocalFields(targetClass, new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				AnnotationAttributes ann = AnnotatedElementUtils.getMergedAnnotationAttributes(field, Autowired.class);
				if (ann != null) {
					if (Modifier.isStatic(field.getModifiers())) {
						if (logger.isWarnEnabled()) {
							logger.warn("Autowired annotation is not supported on static fields: " + field);
						}
						return;
					}
					currElements.add(field);
				}
			}
		});
		return currElements;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		exportProviders();
	}

	@Override
	public void destroy() throws Exception {
		for (ServiceConfig<?> serviceConfig : serviceConfigs) {
			try {
				serviceConfig.unexport();
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
