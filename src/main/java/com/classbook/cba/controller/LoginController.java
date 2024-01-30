package com.classbook.cba.controller;

import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.classbook.cba.db.DBAccess;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	HttpSession httpSession;
	
	@GetMapping("/")
	public String loginPage(@ModelAttribute("redUserId") String redirectId,
			@ModelAttribute("redUserPwd") String redirectPwd,
			@ModelAttribute("redMsg") String redirectMsg, Model model) {
		
		// if session id still on browser database,no need to login => direct to /home
		if(httpSession.getAttribute("userId") != null) {
			return "redirect:/home";
		}
		
		model.addAttribute("ReErrMsg", redirectMsg);
		model.addAttribute("ReId", redirectId);
		model.addAttribute("RePwd", redirectPwd);
		return "login";
	}

	@GetMapping("/loginAction")
	public String loginAction(@RequestParam("user_id") String userId, 
			@RequestParam("user_pwd") String userPwd,RedirectAttributes redirAttr, Model model) {
		try {
			ResultSet rs = DBAccess.checkAccByIdPwd(userId, userPwd);
			if (rs.next()) {
				
				httpSession.setAttribute("userId", rs.getString("user_id"));
				httpSession.setAttribute("userName", rs.getString("user_name"));
				System.out.println(httpSession.getAttribute("userId"));
				return "redirect:/home";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		redirAttr.addFlashAttribute("redUserId", userId);
		redirAttr.addFlashAttribute("redUserPwd", userPwd);
		redirAttr.addFlashAttribute("redMsg", "UserId or Password Incorrect");

		// Login
		return "redirect:/";
	}
}
