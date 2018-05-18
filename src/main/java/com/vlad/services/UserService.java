package com.vlad.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;
import com.vlad.models.User;

@Service
public class UserService {
	private static final Logger logger = LogManager.getLogger(UserService.class);
	public static final String UPLOAD_DIRECTORY = "userFiles/";
	private Gson gson = new Gson();

	public UserService() {
		// Create directory if not exist
		File file = new File(UPLOAD_DIRECTORY);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public void save(List<User> users, String fileName) {
		String lines = users.stream().map(user -> gson.toJson(user)).collect(Collectors.joining("\n"));
		try {
			Files.write(Paths.get(UPLOAD_DIRECTORY + fileName), lines.getBytes(StandardCharsets.UTF_8),
					getFileOption(fileName));
		} catch (IOException e) {
			logger.error("Unable to save user(s) to file: " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}
	}

	private OpenOption getFileOption(String fileName) {
		return Files.exists(Paths.get(UPLOAD_DIRECTORY + fileName)) ? StandardOpenOption.APPEND
				: StandardOpenOption.CREATE;
	}

	public List<User> getAll(String fileName) {
		List<User> users = new ArrayList<>();
		
		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY + fileName))) {
			users = stream.map(line -> gson.fromJson(line, User.class)).collect(Collectors.toList());
		} catch (IOException e) {
			logger.error("Unable get all users from " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}
		
		return users;
	}

	public Optional<User> getById(int id, String fileName) {

		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY + fileName))) {

			return stream.map(line -> gson.fromJson(line, User.class)).filter(u -> u.getId() == id).findAny();

		} catch (IOException e) {
			logger.error("Unable to get user by id: " + id + " from file " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}
	}

	public void delete(int id, String fileName) {

		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY + fileName))) {

			List<User> users = stream.map(line -> gson.fromJson(line, User.class)).filter(user -> user.getId() != id)
					.collect(Collectors.toList());

			deleteFile(fileName);
			save(users, fileName);
			
		} catch (IOException e) {
			logger.error("Unable to delete user with id: " + id + " from file " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}
	}

	public void update(User updatedUser, String fileName) {

		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY + fileName))) {

			List<User> users = new ArrayList<>();
			stream.map(line -> gson.fromJson(line, User.class)).forEach(user -> {
				if (user.getId() == updatedUser.getId()) {
					users.add(updatedUser);
				} else {
					users.add(user);
				}
			});

			deleteFile(fileName);
			save(users, fileName);
		} catch (IOException e) {
			logger.error("Unable to update user with id " + updatedUser.getId() + " in file " + fileName + ": "
					+ e.toString());
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

	private void deleteFile(String fileName) {
		try {
			File file = new File(UPLOAD_DIRECTORY + fileName);
			file.delete();
		} catch (Exception e) {
			logger.error("Unable to delete file " + fileName + ": " + e.toString());
			throw new RuntimeException();
		}
	}
}
