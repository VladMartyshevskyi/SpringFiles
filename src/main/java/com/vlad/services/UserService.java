package com.vlad.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.vlad.models.User;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Service
public class UserService {
	private static final Logger logger = LogManager.getLogger(UserService.class);
	public static final String UPLOAD_DIRECTORY = "userFiles/";
	private Gson gson = new Gson();

	public UserService() {
		File dir = new File(UPLOAD_DIRECTORY);
		if (!dir.exists())
			dir.mkdir();
	}

	public void save(List<User> users, String fileName) {
		String lines = users.stream()
				  .map(user -> gson.toJson(user))
				  .collect(Collectors.joining("\n"));
		try {
			Files.write(Paths.get(UPLOAD_DIRECTORY + fileName),
					lines.getBytes(StandardCharsets.UTF_8),
					Files.exists(Paths.get(UPLOAD_DIRECTORY + fileName)) ? StandardOpenOption.APPEND
							: StandardOpenOption.CREATE);
		} catch (IOException e) {
			logger.error("Unable to save user(s) to file: " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}
	}

	public List<User> getAll(String fileName) {
		List<User> users = null;
		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY + fileName))) {
			users = stream.map(line -> gson.fromJson(line, User.class)).collect(Collectors.toList());
		} catch (IOException e) {
			logger.error("Unable get all users from " + fileName +": " + e.toString());
			throw new RuntimeException();
		}
		return users;
	}

	public User getById(int id, String fileName) {
		User usr = null;
		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY + fileName))) {
			usr = stream.map(line -> gson.fromJson(line, User.class)).filter(user -> user.getId() == id).findAny()
					.get();
		} catch (IOException e) {
			logger.error("Unable to get user by id: " + id + " from file " + fileName +": " + e.toString());
			throw new RuntimeException();
		}
		return usr;
	}

	public void delete(int id, String fileName) {
		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY + fileName))) {
			List<User> users = stream.map(line -> gson.fromJson(line, User.class)).filter(user -> user.getId() != id)
					.collect(Collectors.toList());
			deleteAll(fileName);
			save(users, fileName);
		} catch (IOException e) {
			logger.error("Unable to delete user with id: " + id + " from file " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}
	}

	public void update(User updatedUser, String fileName) {
		int id = updatedUser.getId();
		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY + fileName))) {
			List<User> users = new ArrayList<>();
			stream.map(line -> gson.fromJson(line, User.class)).forEach(user -> {
				if (user.getId() == id) {
					user.setName(updatedUser.getName());
					user.setLastName(updatedUser.getLastName());
					user.setAge(updatedUser.getAge());
				}
				users.add(user);
			});
			deleteAll(fileName);
			save(users, fileName);
		} catch (IOException e) {
			logger.error("Unable to update user with id " + updatedUser.getId() + " in file " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}
	}

	public void deleteAll(String fileName) {
		try {
			File file = new File(UPLOAD_DIRECTORY + fileName);
			file.delete();
		} catch (Exception e) {
			logger.error("Unable to delete file " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}
	}

	public Resource downloadFile(String fileName) {
		File file = new File(UPLOAD_DIRECTORY + fileName);
		Path path = Paths.get(file.getAbsolutePath());
		ByteArrayResource resource = null;
		try {
			resource = new ByteArrayResource(Files.readAllBytes(path));
		} catch (IOException e) {
			logger.error("Unable to download file " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}
		return resource;
	}

	public void loadFile(String fileName, MultipartFile file) {
		try (BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(new File(UPLOAD_DIRECTORY + fileName)));) {
			byte[] bytes = file.getBytes();
			stream.write(bytes);
		} catch (Exception e) {
			logger.error("Unable to load file " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}

	}
}
