package com.store.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
	String uploadImage(MultipartFile file);
	public void deleteImage(String imageUrl);
	
}
