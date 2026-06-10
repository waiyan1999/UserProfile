package com.wytm.UserProfile.model;

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
	private String userName;
	private Integer userAge;
	private String userAddress;
	private String profileImgName;
	private String profileImgType;
	private String profileImgPath;
}
