package com.vlad.models;

public class NumberCheckerFactory extends CheckerFactory{
	@Override
	public Checker createChecker() {
		return new NumberChecker();
	}
}
