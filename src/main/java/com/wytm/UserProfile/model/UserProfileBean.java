package com.wytm.UserProfile.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor 
@NoArgsConstructor
public class UserProfileBean {

	private Integer userId;
	
	@NotEmpty(message = "Please Enter Your Name")
	@Size(min = 1,max = 15,message = "Name Character Must be between 1 and 15")
	private String userName;
	
	@NotEmpty(message = "Please Enter Your E-mail")
	private String userEmail;
	
	@NotNull(message = "Please Enter Your Age")
	@Max(value = 50,message = "Age Must be under 50")
	@Min(value = 18,message = "Age Must be after 18")
	private Integer userAge;
	
	@NotEmpty(message = "Please Enter Your Address")
	private String userAddress;
	
	private String profileImgName;
	private String profileImgType;
	private String profileImgPath;
}
