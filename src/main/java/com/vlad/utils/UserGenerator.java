package com.vlad.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vlad.models.User;

public class UserGenerator {

	private List<String> names;
	private List<String> lastNames;
	private static UserGenerator instance;

	private UserGenerator() {
		names = loadData("src/main/resources/static/names.txt");
		lastNames = loadData("src/main/resources/static/lastNames.txt");
	}

	public List<User> generateUsers(long count) {
		List<User> users = new ArrayList<>();

		Random random = new Random();
		for (long i = 0; i < count; i++) {
			String name = names.get(random.nextInt(names.size()));
			String lastName = names.get(random.nextInt(lastNames.size()));
			int age = 16 + random.nextInt(47);
			users.add(new User(String.valueOf(i), name, lastName, age));
		}
		return users;
	}

	public static UserGenerator getInstance() {
		if (instance == null) {
			instance = new UserGenerator();
		}
		return instance;
	}

	private List<String> loadData(String fileName) {
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			return stream.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

}
