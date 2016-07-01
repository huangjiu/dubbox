package com.alibaba.demo.service;

import com.alibaba.dubbo.javaconfig.annotation.Dubbo;
import com.alibaba.dubbo.javaconfig.annotation.DubboDeploy;

@Dubbo
@DubboDeploy
public interface RefService {

	void say();

}
