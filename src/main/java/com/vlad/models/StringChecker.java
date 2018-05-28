package com.vlad.models;

public class StringChecker implements Checker {

	public boolean check(String criteria, String value1, Object value2) {
		String s2 = (String) value2;
		
		switch (criteria) {
			case "==":
				return s2.equals(value1);
			case ">":
				return s2.compareTo(value1) > 0;
			case ">=":
				return s2.compareTo(value1) >= 0;
			case "<":
				return s2.compareTo(value1) < 0;
			case "<=":
				return s2.compareTo(value1) <= 0;
			default:
				return false;
		}
	}

}
