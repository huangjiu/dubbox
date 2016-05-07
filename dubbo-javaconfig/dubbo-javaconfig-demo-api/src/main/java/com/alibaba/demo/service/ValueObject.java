package com.alibaba.demo.service;

public class ValueObject implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String[] values;
	private byte[] bytes;

	public ValueObject() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}
