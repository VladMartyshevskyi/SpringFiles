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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.vlad.models.User;
import com.google.gson.Gson;


@Service
public class UserService {
	
	public static final String UPLOAD_DIRECTORY = "userFiles/";
	private Gson gson = new Gson();
	
	public void loadFile(String name, MultipartFile file) {
		try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(UPLOAD_DIRECTORY + name)));
            stream.write(bytes);
            stream.close();         
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public void save(User user, String fileName) {
		try {
			Files.write(Paths.get(UPLOAD_DIRECTORY+fileName), (gson.toJson(user) + "\n").getBytes(StandardCharsets.UTF_8),
					Files.exists(Paths.get(UPLOAD_DIRECTORY+fileName)) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public List<User> getAll(String fileName) {
		List<User> users = null;
		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY+fileName))) {
			users = stream.map(line -> gson.fromJson(line, User.class)).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return users;
	}

	public User get(int id, String fileName) {
		User usr = null;
		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY+fileName))) {
			usr = stream.map(line -> gson.fromJson(line, User.class))
					.filter(user -> user.getId() == id)
					.findAny()
					.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return usr;
	}

	public void delete(int id, String fileName) {
		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY+fileName))) {
			List<User> users = stream.map(line -> gson.fromJson(line, User.class))
					.filter(user -> user.getId() != id)
					.collect(Collectors.toList());
			deleteAll(fileName);
			users.forEach(user -> save(user, fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(User updatedUser, String fileName) {
		int id = updatedUser.getId();
		try (Stream<String> stream = Files.lines(Paths.get(UPLOAD_DIRECTORY+fileName))) {
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
			users.forEach(user -> save(user, fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteAll(String fileName) {
		try {
			File file = new File(UPLOAD_DIRECTORY+fileName);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Resource downloadFile(String fileName) {
		File file = new File(UPLOAD_DIRECTORY+fileName);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = null;
        try {
        	resource = new ByteArrayResource(Files.readAllBytes(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
        return resource;
	}
}
