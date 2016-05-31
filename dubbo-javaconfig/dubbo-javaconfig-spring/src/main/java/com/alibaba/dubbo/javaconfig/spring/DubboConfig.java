package com.alibaba.dubbo.javaconfig.spring;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ProtocolConfig;

public class DubboConfig implements InitializingBean {

	private String applicationName;
	private String registryAddress;
	private String annotationPackage;

	private ProtocolConfig protocol;
	private String version;
	private int delay;
	private int timeout;
	private int retries;
	private int connections;
	private String loadbalance;
	private String token;
	private boolean async;

	public DubboConfig() {
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public String getAnnotationPackage() {
		return annotationPackage;
	}

	public void setAnnotationPackage(String annotationPackage) {
		this.annotationPackage = annotationPackage;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getConnections() {
		return connections;
	}

	public void setConnections(int connections) {
		this.connections = connections;
	}

	public String getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(String loadbalance) {
		this.loadbalance = loadbalance;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public ProtocolConfig getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolConfig protocol) {
		this.protocol = protocol;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		if (StringUtils.isBlank(this.applicationName)) {
			throw new IllegalArgumentException("The applicationName is required.");
		}

		if (StringUtils.isBlank(this.registryAddress)) {
			throw new IllegalArgumentException("The registryAddress is required.");
		}

		if (StringUtils.isBlank(this.annotationPackage)) {
			throw new IllegalArgumentException("The annotationPackage is required.");
		}

	}

}
