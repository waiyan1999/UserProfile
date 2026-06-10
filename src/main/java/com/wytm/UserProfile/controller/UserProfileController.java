package com.wytm.UserProfile.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.wytm.UserProfile.model.UserProfileBean;
import com.wytm.UserProfile.repository.UserProfileRepository;

@Controller
@RequestMapping("/user")
public class UserProfileController {
	
	@Autowired
	UserProfileRepository userRepo;
	
	
	
	@GetMapping("/form")
	public ModelAndView userProfileForm() {
		
		return new ModelAndView("userProfile-form", "userObj", new UserProfileBean());
				
	}
	
	@PostMapping("/create")
	public String createUserProfile(@ModelAttribute("userObj") UserProfileBean user,@RequestParam("file") MultipartFile file, Model m) {
		
		/* String uploadDIR = "src\\main\\resources\\upload\\"; */
		
		String uploadDIR = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\upload\\";
		
		System.out.println("uloadDir :" + uploadDIR);
		
		File dir = new File(uploadDIR);
		
		if ( !dir.exists() ) {
			dir.mkdirs();
			System.out.println("Successfullly Createe");
		}
		else {
			System.out.println("Dir Already Exists");
		}
		
		
		
		
		
		String profileImgName = file.getOriginalFilename();
		String profileImgType = file.getContentType();
		
		
		
		Path imgPath = Paths.get(uploadDIR + profileImgName );
		
		try {
			
			Files.write(imgPath, file.getBytes());
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
		
		user.setProfileImgName(profileImgName);
		user.setProfileImgType(profileImgType);
		user.setProfileImgPath("/upload/"+profileImgName);
		
		/*
		 * System.out.println(profileImgName + profileImgType + imgPath);
		 * System.out.println("obj: " + user.getProfileImgName()+
		 * user.getProfileImgType()+ user.getProfileImgPath());
		 */
		
		
		int i = userRepo.createUserProfile(user);
		
		if ( i != 0 ) {
			
			m.addAttribute("msg","Successfully Created User Profile");
			return "redirect:list";
		}
		else {
			m.addAttribute("msg","Something went wrong when creating user Profile");
			return "redirect:form";
		}
		
		
	}
	
	
	@GetMapping("/list")
	public String showUserProfiles(Model m) {
		
		List<UserProfileBean> userList = userRepo.getAllUserProfile();
		m.addAttribute("userList",userList);
		
		return "user-list";
		
	}

}
