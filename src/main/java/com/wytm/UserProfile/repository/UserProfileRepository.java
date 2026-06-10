package com.wytm.UserProfile.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wytm.UserProfile.model.UserProfileBean;

@Repository
public class UserProfileRepository {
	
	@Autowired
	JdbcTemplate jdbc;
	
	public int createUserProfile(UserProfileBean user) {
		int result = 0;
		
		String sql = " insert into user_profile (name,age,address,p_img_name,p_img_type,p_img_path) values (?,?,?,?,?,?)";
		
		result = jdbc.update(sql,
					
					user.getUserName(),
					user.getUserAge(),
					user.getUserAddress(),
					user.getProfileImgName(),
					user.getProfileImgType(),
					user.getProfileImgPath()
				
				);
		
		return result;
		
	}
	
	
	public List<UserProfileBean> getAllUserProfile() {
		
		List<UserProfileBean> userList = new ArrayList<>();
		
		String sql = " select * from user_profile where delete_flag=0";
		
		userList = jdbc.query(sql,
				(rs,rowCount) -> new UserProfileBean(
						
						rs.getInt("id"),
						rs.getString("name"),
						rs.getInt("age"),
						rs.getString("address"),
						rs.getString("p_img_name"),
						rs.getString("p_img_type"),
						rs.getString("p_img_path")
						
						)
				
				);
		
		return userList;
	}
	
	

}
