package com.alibaba.demo.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.demo.service.DemoService;
import com.alibaba.demo.service.RefService;
import com.alibaba.demo.service.ValueObject;

public class DemoServiceImpl implements DemoService {
	
	@Autowired
	private RefService refService;
	@Autowired
	private RefService2 refService2;

	private Map<Long, ValueObject> values = new ConcurrentHashMap<Long, ValueObject>();

	public long push(ValueObject value) {
		this.values.put(value.getId(), value);
		
		refService2.say();
		refService.say();
		return value.getId();
	}

	public boolean remove(long id) {
		if (this.values.containsKey(id)) {
			this.values.remove(id);
			return true;
		}
		return false;
	}

	public ValueObject get(long id) {
		if (this.values.containsKey(id)) {
			return this.values.get(id);
		}
		return new ValueObject();
	}

}
