package com.vlad.models;

public class Matcher {
	private Filter filter;
	
	public Matcher(Filter filter) {
		this.filter = filter;
	}
	
	public boolean isMatch(User user) {
		Checker stringChecker = new StringCheckerFactory().createChecker();
		Checker numberChecker = new NumberCheckerFactory().createChecker();
		switch (filter.getField()) {
			case "name":
				return stringChecker.check(filter.getCriteria(), filter.getValue(), user.getName());
			case "lastName":
				return stringChecker.check(filter.getCriteria(), filter.getValue(), user.getLastName());
			case "id":
				return stringChecker.check(filter.getCriteria(), filter.getValue(), user.getId());
			case "age":
				return numberChecker.check(filter.getCriteria(), filter.getValue(), user.getAge());
			default:
				return false;
		}
	}
	
	public boolean isNotMatch(User user) {
		return !isMatch(user);
	}
	
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	public Filter getFilter() {
		return filter;
	}
}
