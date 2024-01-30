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
public class SignUpController {
	@Autowired
	HttpSession httpSession;

	@GetMapping("/signup")
	public String signUpPage(@ModelAttribute("redirErrMsg")String redirMsg,
			@ModelAttribute("redirUserName")String reUserName,
			Model model) {
		

		try {
			ResultSet rs1 = DBAccess.GetClientSessionId(httpSession.getId());
			
			System.out.println(httpSession.getId());
			if (!rs1.next()) {
				System.out.println("there is no record");
				DBAccess.InsertSessionId(httpSession.getId());

				ResultSet rs2 = DBAccess.GetClientSessionId(httpSession.getId());
				if (rs2.next()) {
					System.out.println("One Record");
					String Uid = "cb" + rs2.getInt("user_id");
					model.addAttribute("user_id", Uid);
				}
			} else {
				String Uid = "cb" + rs1.getInt("user_id");
				model.addAttribute("user_id", Uid);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		model.addAttribute("errMsg", redirMsg);
		model.addAttribute("redUserName",reUserName);
		return "signup";
	}
	
	@GetMapping("/signupAccountAction")
	public String signupAccountAction(@RequestParam("newUserName")String nUserName,
			@RequestParam("newUserId")String nUserId,
			@RequestParam("newUserPwd")String nUserPwd,
			@RequestParam("newUserConPwd")String nUserConPwd,
			RedirectAttributes redirAttr, Model model) {
		
		if(nUserPwd.equals(nUserConPwd)) {
			DBAccess.signUpAccount(nUserId, nUserPwd, nUserName);
			DBAccess.InsertUserInfo(nUserId, nUserName);
			
			String intId = nUserId.substring(2);
			System.out.println(intId);
			int IntId = Integer.parseInt(intId);
			System.out.println(IntId);
			DBAccess.DeleteSessionId(IntId);
			
			// save to session (userName)
			// save to session (userId)
			httpSession.setAttribute("UserName", nUserName);
			httpSession.setAttribute("UserId", nUserId);
			String uId= (String)httpSession.getAttribute("UserId");
			redirAttr.addFlashAttribute("redUserId", nUserId);
			System.out.println(httpSession.getAttribute("UserName")); 
			System.out.println(uId);
			return "redirect:/";
		}
		// redirect data passing
				redirAttr.addFlashAttribute("redirUserId", nUserId);
				redirAttr.addFlashAttribute("redirUserName", nUserName);
				redirAttr.addFlashAttribute("redirErrMsg", "Password Doesn't Match");
		return "redirect:/signup";
		
	}

	// Old code for user_id and session_id record
//	@GetMapping("/signup")
//	public String signUpPage(Model model) {
//
//		try {
//			ResultSet rs1 = DBAccess.GetClientSessionId(httpSession.getId());
//			System.out.println(httpSession.getId());
//			if (!rs1.next()) {
//				System.out.println("there is no record");
//				DBAccess.InsertSessionId(httpSession.getId());
//				
//				ResultSet rs2 = DBAccess.GetClientSessionId(httpSession.getId());
//				if(rs2.next()) {
//					System.out.println("One Record");
//					model.addAttribute("user_id", rs2.getInt("user_id"));
//				}
//			}
//			if(rs1.next()) {
//				model.addAttribute("user_id", rs1.getInt("user_id"));
//			}
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		
//		return "signup";
//	}

}
