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

import com.classbook.cba.dao.Post;
import com.classbook.cba.db.DBAccess;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	HttpSession httpSession;
	
	@GetMapping("/logoutAction")
	public String logout() {
		httpSession.setAttribute("userId",null);
		return "redirect:/";
	}

	@GetMapping("/home")
	public String recivedatafrompost(Model model) {
		
		//Session
		//if someone try to come in /home in another browser by URL name , protect with session data to login first
		if(httpSession.getAttribute("userId")==null) {
			
			return "redirect:/";
		}
		
		List<Post> postlist = new ArrayList<>();
		try {
			String uId = (String)httpSession.getAttribute("userId");
			ResultSet rs1 = DBAccess.GetPostDataOnHome(uId);
			while (rs1.next()) { // records loop
				Post post = new Post();
				post.postId = rs1.getInt("post_id");
				post.username = rs1.getString("newUN");
				post.posttext = rs1.getString("post_text");
				String userId = (String) httpSession.getAttribute("userId");
				post.postOwnerFlg = userId.equals(rs1.getString("user_id"));
				post.Viewable = rs1.getInt("viewable");
				post.imageBytesToStringInPost = Base64.encodeBase64String(rs1.getBytes("profile_img"));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar dbcalendar = Calendar.getInstance();
				dbcalendar.setTime(sdf.parse(rs1.getString("created_date")));

				int dbyear = dbcalendar.get(Calendar.YEAR);
				// int dbmonth = dbcalendar.get(Calendar.MONTH) + 1; // Adding 1 because
				// Calendar.MONTH is zero-based
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
			
			ResultSet rs2 =DBAccess.returnProfileImagetoHome(uId);
			if(rs2.next()) {
				model.addAttribute("profileImg",Base64.encodeBase64String(rs2.getBytes("profile_img")));
				model.addAttribute("userName", rs2.getString("user_name"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		// model.addAttribute("userName", httpSession.getAttribute("userName"));
		return "home";
	}
}
