package com.store.backend.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

	private final Cloudinary cloudinary;

	
	
	public ImageUploadServiceImpl(Cloudinary cloudinary) {
		super();
		this.cloudinary = cloudinary;
	}


	@Override
	public String uploadImage(MultipartFile file) {
		try {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
        		Map.of("folder","products")
        		);
			return uploadResult.get("secure_url").toString();
			
		} catch (Exception e) {
			 throw new RuntimeException("Cloudinary upload failed");
		}
	}
	public void deleteImage(String imageUrl) {

	    String publicId = extractPublicId(imageUrl);
	    try {
			cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String extractPublicId(String imageUrl) {

        /*
         Example imageUrl:
         https://res.cloudinary.com/demo/image/upload/v1769636631/products/abc123.jpg

         Cloudinary public_id:
         products/abc123
        */

        String[] parts = imageUrl.split("/");

        String fileName = parts[parts.length - 1];   // abc123.jpg
        String folder   = parts[parts.length - 2];   // products

        return folder + "/" + fileName.substring(0, fileName.lastIndexOf("."));
    }
}
