package com.classbook.cba.controller;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.classbook.cba.dao.Post;
import com.classbook.cba.dao.UserInfo;
import com.classbook.cba.db.DBAccess;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {
	@Autowired
	HttpSession httpSession;
	@GetMapping("/profile")
	public String profilePage( Model model) {
		
		//Session
		if(httpSession.getAttribute("userId")==null) {
			return "redirect:/";
		}
		
		String userId = (String) httpSession.getAttribute("userId");
		List<Post> postlist = new ArrayList<>();
		
		// User Post
		
		try {
			ResultSet rs1 = DBAccess.getUsersPost(userId);
			while (rs1.next()) {
				Post post = new Post();
				post.username = rs1.getString("newUN");
				post.posttext = rs1.getString("post_text");
				post.postId = rs1.getInt("post_id");
				post.Viewable = rs1.getInt("viewable");
				

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar dbcalendar = Calendar.getInstance();
				dbcalendar.setTime(sdf.parse(rs1.getString("created_date")));

				int dbyear = dbcalendar.get(Calendar.YEAR);
				// int dbmonth = dbcalendar.get(Calendar.MONTH) + 1; // Adding 1 because Calendar.MONTH is zero-based
				int dbday = dbcalendar.get(Calendar.DAY_OF_MONTH);
				int dbhour = dbcalendar.get(Calendar.HOUR_OF_DAY);
				int dbmin = dbcalendar.get(Calendar.MINUTE);
				// Month dbmonthName = Month.of(dbmonth);

				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 because Calendar.MONTH is zero-based
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int min = calendar.get(Calendar.MINUTE);
				Month monthName = Month.of(month);
				if ((year - dbyear) == 1) {
					post.createddate = " 1 Year Ago ";
				} else if (dbday == day) {
					if (dbhour == hour) {
						post.createddate = (min - dbmin) + " Minutes Ago";
					} else {
						post.createddate = "Today" + " On " + dbhour + ":" + dbmin;
					}
				} else if ((day - dbday) == 1) {
					post.createddate = "Yesterday" + " On " + dbhour + " : " + dbmin;
				} else {
					String dateStr = day + "," + monthName.toString() + ", " + year + " On " + hour + ":" + min;
					post.createddate = dateStr;
				}
				postlist.add(post);
			}
			model.addAttribute("posts", postlist);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// User Information
		try {
			ResultSet rs = DBAccess.getUserProfileData(userId);
			UserInfo userInfo = new UserInfo();
			if (rs.next()) {
				userInfo.userName = rs.getString("user_name");
				userInfo.userBirth = rs.getString("date_of_birth");
				userInfo.workPlace = rs.getString("workplace");
				userInfo.jobCareer = rs.getString("job_career");

				userInfo.martialStatus = rs.getInt("marital_status");
				userInfo.address1 = rs.getString("address_1");
				userInfo.address2 = rs.getString("address_2");
				userInfo.education1 = rs.getString("education_1");
				userInfo.education2 = rs.getString("education_2");
				userInfo.hobby1 = rs.getString("hobby_1");
				userInfo.hobby2 = rs.getString("hobby_2");
				userInfo.favFood = rs.getString("fav_food");
				userInfo.favMovie = rs.getString("fav_movie");
				userInfo.favSong = rs.getString("fav_song");
				userInfo.favPlace = rs.getString("fav_place");
				userInfo.createdDate = rs.getString("created_date");
				userInfo.updatedDate = rs.getString("updated_date");
				userInfo.imageBytesToString =  Base64.encodeBase64String(rs.getBytes("profile_img"));

			}
			model.addAttribute("userInfo", userInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// model.addAttribute("userName", httpSession.getAttribute("userName"));
		return "profile";
	}

	@GetMapping("/editProfile")
	public String editProfile( Model model) {
		
		//Session
		if(httpSession.getAttribute("userId")==null) {
			return "redirect:/";
		}
		String userId = (String) httpSession.getAttribute("userId");
		try {
			ResultSet rs = DBAccess.getUserProfileData(userId);
			UserInfo userInfo = new UserInfo();
			if (rs.next()) {
				userInfo.userName = rs.getString("user_name");
				userInfo.userBirth = rs.getString("date_of_birth");
				userInfo.workPlace = rs.getString("workplace");
				userInfo.jobCareer = rs.getString("job_career");

				userInfo.martialStatus = rs.getInt("marital_status");
				userInfo.address1 = rs.getString("address_1");
				userInfo.address2 = rs.getString("address_2");
				userInfo.education1 = rs.getString("education_1");
				userInfo.education2 = rs.getString("education_2");
				userInfo.hobby1 = rs.getString("hobby_1");
				userInfo.hobby2 = rs.getString("hobby_2");
				userInfo.favFood = rs.getString("fav_food");
				userInfo.favMovie = rs.getString("fav_movie");
				userInfo.favSong = rs.getString("fav_song");
				userInfo.favPlace = rs.getString("fav_place");
				userInfo.createdDate = rs.getString("created_date");
				userInfo.updatedDate = rs.getString("updated_date");
				

			}
			model.addAttribute("userInfo", userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "edit_profile";
	}
	

	@GetMapping("/updateProfileAction")
	public String updateProfileAction(@ModelAttribute UserInfo ui) {
		String userId = (String) httpSession.getAttribute("userId");
DBAccess.updateDataForEditProfile(ui,userId);
		
		
		return "redirect:profile";
	}
	
	//Upload Profile Image
	@PostMapping("/uploadProfileAction")
	public String uploadProfileAction(@RequestParam("profileImage") MultipartFile file,Model model) {
		try {
			String userId = (String) httpSession.getAttribute("userId");
			DBAccess.updateProfileImage(file,userId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return"redirect:profile";
	}
	
	@GetMapping("/viewProfile")
	public String viewProfile(Model model) {
		if(httpSession.getAttribute("userId")==null) {
			return "redirect:/";
		}
		
		String userId = (String) httpSession.getAttribute("userId");
		try {
			ResultSet rs = DBAccess.getUserProfileData(userId);
			UserInfo userInfo = new UserInfo();
			if (rs.next()) {
				userInfo.userName = rs.getString("user_name");
				userInfo.userBirth = rs.getString("date_of_birth");
				userInfo.workPlace = rs.getString("workplace");
				userInfo.jobCareer = rs.getString("job_career");

				userInfo.martialStatus = rs.getInt("marital_status");
				userInfo.address1 = rs.getString("address_1");
				userInfo.address2 = rs.getString("address_2");
				userInfo.education1 = rs.getString("education_1");
				userInfo.education2 = rs.getString("education_2");
				userInfo.hobby1 = rs.getString("hobby_1");
				userInfo.hobby2 = rs.getString("hobby_2");
				userInfo.favFood = rs.getString("fav_food");
				userInfo.favMovie = rs.getString("fav_movie");
				userInfo.favSong = rs.getString("fav_song");
				userInfo.favPlace = rs.getString("fav_place");
				userInfo.createdDate = rs.getString("created_date");
				userInfo.updatedDate = rs.getString("updated_date");

			}
			model.addAttribute("userInfo", userInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("userName", httpSession.getAttribute("userName"));
		
		return "viewProfile";
	}


}
