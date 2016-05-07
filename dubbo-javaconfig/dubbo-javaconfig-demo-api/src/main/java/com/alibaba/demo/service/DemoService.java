package com.alibaba.demo.service;

import com.alibaba.dubbo.javaconfig.annotation.EnableDubbo;

@EnableDubbo
public interface DemoService {

	long push(ValueObject value);

	boolean remove(long id);

	ValueObject get(long id);

}
