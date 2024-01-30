package com.classbook.cba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.classbook.cba.db.DBAccess;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChangePasswordController {
	@Autowired
	HttpSession httpSession;

	@GetMapping("/changepwd")
	public String changePasswordPage() {

		return "changepassword";
	}

	@GetMapping("/changepwdAction")
	public String changepwdAction(@RequestParam("currentPwd") String currentpwd, @RequestParam("newPwd") String newPwd,
			@RequestParam("conNewPwd") String conNewPwd) {

		String userId = (String) httpSession.getAttribute("userId");

		if (newPwd.equals(conNewPwd)) {
			DBAccess.changePassword(conNewPwd, userId, currentpwd);
			return "redirect:/profile";
		}

		return "redirect:/changepwd";
	}
}
