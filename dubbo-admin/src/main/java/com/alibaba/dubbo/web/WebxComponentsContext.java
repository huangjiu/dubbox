package com.alibaba.dubbo.web;

import static com.alibaba.citrus.util.Assert.assertNotNull;
import static com.alibaba.citrus.util.Assert.ExceptionType.ILLEGAL_STATE;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.alibaba.citrus.webx.WebxComponents;
import com.alibaba.citrus.webx.context.WebxApplicationContext;
import com.alibaba.citrus.webx.context.WebxComponentContext;
import com.alibaba.citrus.webx.context.WebxComponentsLoader;

public class WebxComponentsContext extends WebxApplicationContext{
	
	private WebxComponentsLoader componentsLoader;

    public WebxComponentsLoader getLoader() {
        return assertNotNull(componentsLoader, ILLEGAL_STATE, "no WebxComponentsLoader set");
    }

    public void setLoader(WebxComponentsLoader loader) {
        this.componentsLoader = loader;
    }

    /** 取得所有的components。 */
    public WebxComponents getWebxComponents() {
        return getLoader().getWebxComponents();
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    	
    	WebxComponentsLoader webxComponentsLoader = new WebxComponentsLoader();
        super.postProcessBeanFactory(beanFactory);
        getLoader().postProcessBeanFactory(beanFactory);
    }

    @Override
    protected void finishRefresh() {
        super.finishRefresh();
        getLoader().finishRefresh();
    }

    /** 在创建子容器时，给parent一个设置子context的机会。 */
    protected void setupComponentContext(WebxComponentContext componentContext) {
    }

}
