package com.vlad.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vlad.models.Filter;
import com.vlad.models.User;
import com.vlad.services.UserService;

@RestController
public class FileController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "{fileName}/users", method = RequestMethod.POST)
	public void add(@PathVariable String fileName, @RequestBody User user) {
		userService.save(Arrays.asList(user), fileName);
	}
	
	@RequestMapping(value = "{fileName}/filter", method = RequestMethod.POST)
	public void filter(@PathVariable String fileName, @RequestBody Filter[] filters) {
		userService.filterAndSaveToDb(fileName, filters);
	}

	@RequestMapping(value = "{fileName}/users", method = RequestMethod.GET)
	public List<User> all(@PathVariable String fileName) {
		return userService.getAll(fileName);
	}

	@RequestMapping(value = "{fileName}/users/{id}", method = RequestMethod.GET)
	public Optional<User> getById(@PathVariable String fileName, @PathVariable String id) {
		return userService.getById(id, fileName);
	}
	
	@RequestMapping(value = "{fileName}/generate/{count}", method = RequestMethod.GET)
	public void generateUsers(@PathVariable String fileName, @PathVariable long count) {
		userService.generateUsers(fileName, count);
	}

	@RequestMapping(value = "{fileName}/users/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String fileName, @PathVariable String id) {
		userService.delete(id, fileName);
	}
	@RequestMapping(value = "{fileName}/users", method = RequestMethod.PUT)
	public void update(@PathVariable String fileName, @RequestBody User user) {
		userService.update(user, fileName);
	}

	@RequestMapping(path = "download/{fileName}", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(@PathVariable String fileName) throws IOException {
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(userService.downloadFile(fileName));
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void handleFileUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		userService.loadFile(name, file);
	}
	
	

}
