package com.wytm.UserProfile.controller;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wytm.UserProfile.model.UserProfileBean;
import com.wytm.UserProfile.repository.UserProfileRepository;
import com.wytm.UserProfile.service.UserProfileService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserProfileController {
	
	@Autowired
	UserProfileRepository userRepo;
	
	@Autowired
	UserProfileService userService;
	
	
	@GetMapping("/form")
	public ModelAndView userProfileForm() {
		
		return new ModelAndView("userProfile-form", "userObj", new UserProfileBean());
				
	}
	
	
	@PostMapping("/create")
	public String createUserProfile( @Valid @ModelAttribute("userObj") UserProfileBean user,BindingResult br,@RequestParam("file") MultipartFile file ,Model m) {
		
		
		
		String uploadDIR = System.getProperty("user.dir")+"/uploads/";
		

		
		if ( br.hasErrors()) {
			
			
		}
		
		String fileError = userService.validFile(file);
		
		if ( fileError != null) {
			m.addAttribute("fileError",fileError);
			return "userProfile-form";
		}
		
		
		/*
		 * if( file.getContentType() == null ||
		 * !file.getContentType().startsWith("/image")) {
		 * 
		 * m.addAttribute("fileError","Only Image File is allowed"); return
		 * "userProfile-form"; }
		 * 
		 * 
		 * 
		 * if(file.getSize() > 2 * 1024 ) {
		 * 
		 * System.out.println("File Size is :" + file.getSize());
		 * 
		 * m.addAttribute("fileError", "File size must be less than 2MB");
		 * 
		 * return "userProfile-form"; }
		 */
		
		
		File dir = new File(uploadDIR);
		
		if ( !dir.exists() ) {
			dir.mkdirs();
			System.out.println(dir + "Successfullly Created");
			
		}
		else {
			
			System.out.println(dir + "Dir Already Exists");
		
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
		user.setProfileImgPath("/uploads/"+profileImgName);
		
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
	public String showAllUserProfiles(Model m) {
		
		List<UserProfileBean> userList = userRepo.getAllUserProfile();
		m.addAttribute("userList",userList);
		
		return "user-list";
		
	}
	
	@GetMapping("/delete/{userId}")
	public String deleteUserProfile(@PathVariable("userId")int userId, Model m) {
		
		int i = userRepo.deleteUserById(userId);
		
		if ( i > 0) {
			
			m.addAttribute("msg","Successfully Deleted User Id" + userId);
			
		}
		else {
			
			m.addAttribute("msg","Something went wrong When Deleting user Id"+ userId);
			
		}
		
		return "redirect:/user/list";
	}
	
	
	@GetMapping("/list/{userId}")
	public String getUserById(@PathVariable("userId") int userId,Model m) {
		
		UserProfileBean user = userRepo.getUserById(userId);
		m.addAttribute("userObj",user);
		
		return "user-info";
	}
	
	@GetMapping("/edit/{userId}")
	public String editUser(@PathVariable("userId") int userId, Model m) {
		
		UserProfileBean userObj = userRepo.getUserById(userId);
		m.addAttribute("userObj",userObj);
		
		return "userEdit-form";
	}
	
	@PostMapping("/update")
	public String UpdateUser(@ModelAttribute("userObj") UserProfileBean user,@RequestParam("editFile") MultipartFile file, RedirectAttributes ra) {
		
		String uploadDIR = System.getProperty("user.dir")+"/uploads/";
		
		
		if( !file.isEmpty() ) {
			
			File dir = new File(uploadDIR);
			
				if ( !dir.exists()) {
					dir.mkdirs();
				}
				

				String fileError = userService.validFile(file);
				
				if ( fileError != null) {
			
					ra.addFlashAttribute("fileError",fileError);
					return "redirect:/user/edit/"+user.getUserId();
				}
				
				UserProfileBean oldUser = userRepo.getUserById(user.getUserId());
				userService.deleteOldFile(uploadDIR+oldUser.getProfileImgName());
				
				String imgName = file.getOriginalFilename();
				String imgType = file.getContentType();
				Path imgPath = Paths.get(uploadDIR+ imgName);
				
				user.setProfileImgName(imgName);
				user.setProfileImgType(imgType);
				user.setProfileImgPath("/uploads/"+imgName);
				
				try {
					
					Files.write(imgPath,file.getBytes());
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			
			
		}
		
		  else {
		  
		  
		  String imgName = file.getOriginalFilename();
		  String imgType =file.getContentType(); 
		  Path imgPath = Paths.get("/uploads/"+ imgName);
		  
		  user.setProfileImgName(imgName);
		  user.setProfileImgType(imgType);
		  user.setProfileImgPath(imgPath.toString());
		  
		  
		  }
		 
		
		int i = userRepo.updateUser(user);
		
		if ( i > 0 ) {
			
			return "redirect:/user/list/"+user.getUserId();
			
		}else {
			
			
			
			return "redirect:/user/edit/"+user.getUserId();
			
		}
		
		
		
		
		
	}
	

}
