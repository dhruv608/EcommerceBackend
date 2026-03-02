package com.store.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.store.backend.service.ImageUploadService;

@RestController
@RequestMapping("/test")
public class ImageTestController {

	private final ImageUploadService imageUploadService;

	public ImageTestController(ImageUploadService imageUploadService) {
		super();
		this.imageUploadService = imageUploadService;
	}
	
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file) {
		return imageUploadService.uploadImage(file);
	}
}
