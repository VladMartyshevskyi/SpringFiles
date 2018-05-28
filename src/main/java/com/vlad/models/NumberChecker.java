package com.vlad.models;

public class NumberChecker implements Checker {

	@Override
	public boolean check(String criteria, String value1, Object value2) {
		double d1 = Double.parseDouble(value1);
		double d2 = ((Number) value2).doubleValue();

		switch (criteria) {
			case "==":
				return d2 == d1;
			case ">":
				return d2 > d1;
			case ">=":
				return d2 >= d1;
			case "<":
				return d2 < d1;
			case "<=":
				return d2 <= d1;
			default:
				return false;
		}
	}
}
