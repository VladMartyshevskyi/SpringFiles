package com.vlad.models;

public class StringCheckerFactory extends CheckerFactory{

	@Override
	public Checker createChecker() {
		return new StringChecker();
	}

}
