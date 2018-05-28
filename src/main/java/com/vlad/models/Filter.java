package com.vlad.models;

public class Filter {

	private String criteria;
	private String field;
	private String value;

	public Filter(String criteria, String field, String value) {
		this.criteria = criteria;
		this.field = field;
		this.value = value;
	}

	public Filter() {

	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
