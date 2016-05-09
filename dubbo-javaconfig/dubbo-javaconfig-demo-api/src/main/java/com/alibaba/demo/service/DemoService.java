package com.alibaba.demo.service;

import com.alibaba.dubbo.javaconfig.annotation.Dubbo;

@Dubbo
public interface DemoService {

	long push(ValueObject value);

	boolean remove(long id);

	ValueObject get(long id);

}
