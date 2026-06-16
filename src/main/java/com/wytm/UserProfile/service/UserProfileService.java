package com.wytm.UserProfile.service;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserProfileService {

	private final String UploadDIR = System.getProperty("user.dir") + "/uploads";

	public String validFile(MultipartFile file) {

		if (file.isEmpty()) {
			return "Please Select File";
		}

		if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {

			System.out.println(file.getContentType());
			System.out.println(file.getSize());

			return "Only Image File is allowed";

		}

		if (file.getSize() > 2 * 1024 * 1024) {

			System.out.println(file.getContentType());
			System.out.println(file.getSize());

			return "File Size Must Be Less Than 2MB";
		}

		return null;
	}

	public void deleteOldFile(String oldFilePath) {

		System.out.println("calling menthod " + oldFilePath);

		if (oldFilePath == null || oldFilePath.isEmpty()) {
			return;
		}

		File file = new File(oldFilePath);

		if (file.exists()) {
			System.out.println("Old Img File" + oldFilePath);
			file.delete();
			System.out.println("Successfully deleted previous File" + oldFilePath);
		}

	}

}
