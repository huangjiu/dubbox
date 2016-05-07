package com.alibaba.demo.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.demo.service.DemoService;
import com.alibaba.demo.service.ValueObject;

public class DemoServiceImpl implements DemoService {

	private Map<Long, ValueObject> values = new ConcurrentHashMap<Long, ValueObject>();

	public long push(ValueObject value) {
		this.values.put(value.getId(), value);
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
