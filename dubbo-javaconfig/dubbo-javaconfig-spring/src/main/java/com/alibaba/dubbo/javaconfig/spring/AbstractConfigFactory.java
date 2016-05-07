package com.alibaba.dubbo.javaconfig.spring;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

public abstract class AbstractConfigFactory {
 
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private String annotationPackage;
	private String[] annotationPackages;

	public String getPackage() {
		return annotationPackage;
	}

	public void setPackage(String annotationPackage) {
		this.annotationPackage = annotationPackage;
		this.annotationPackages = (annotationPackage == null || annotationPackage.length() == 0) ? null
				: Constants.COMMA_SPLIT_PATTERN.split(annotationPackage);
	}

	protected boolean isMatchPackage(Class<?> beanClass) {
		if (annotationPackages == null || annotationPackages.length == 0) {
			return true;
		}
		String beanClassName = beanClass.getName();
		for (String pkg : annotationPackages) {
			if (beanClassName.startsWith(pkg)) {
				return true;
			}
		}
		return false;
	}

}
