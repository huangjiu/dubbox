package com.alibaba.dubbo.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.alibaba.citrus.webx.context.WebxComponentsLoader;

public class WebxContextLoaderListener extends WebxComponentsLoader implements ServletContextListener  {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		this.initWebApplicationContext(sce.getServletContext());
	}

	/**
	 * Close the root web application context.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		closeWebApplicationContext(event.getServletContext());
	}



}
