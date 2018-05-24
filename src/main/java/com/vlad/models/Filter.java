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

	public boolean isMatch(User user) {
		switch (field) {
		case "name":
			if (checkByStringCriteria(user.getName())) {
				return true;
			}
			break;
		case "lastName":
			if (checkByStringCriteria(user.getLastName())) {
				return true;
			}
			break;
		case "id":
			if (checkByStringCriteria(user.getId())) {
				return true;
			}
		case "age":
			if (checkByIntCriteria(user.getAge())) {
				return true;
			}
			break;
		}
		return false;
	}

	private boolean checkByStringCriteria(String s1) {
		switch (criteria) {
		case "==":
			return s1.equals(value);
		case ">":
			return s1.compareTo(value) > 0;
		case ">=":
			return s1.compareTo(value) >= 0;
		case "<":
			return s1.compareTo(value) < 0;
		case "<=":
			return s1.compareTo(value) <= 0;
		default:
			return false;
		}
	}

	private boolean checkByIntCriteria(int i1) {
		switch (criteria) {
		case "==":
			return i1 == Integer.parseInt(value);
		case ">":
			return i1 > Integer.parseInt(value);
		case ">=":
			return i1 >= Integer.parseInt(value);
		case "<":
			return i1 < Integer.parseInt(value);
		case "<=":
			return i1 <= Integer.parseInt(value);
		default:
			return false;
		}
	}

}
