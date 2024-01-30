package com.classbook.cba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.classbook.cba.db.DBAccess;

import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {
	@Autowired
	HttpSession httpSession;

	@GetMapping("/createpost")
	public String createPostPage() {

		// Session
		if (httpSession.getAttribute("userId") == null) {
			return "redirect:/";
		}

		return "create_post";
	}

	@GetMapping("/createPostAction")
	public String createPostAction(@RequestParam("postcontent") String postcontent,
			@RequestParam("viewable") int viewable, Model model) {
		
		// Session
		if (httpSession.getAttribute("userId") == null) {
			return "redirect:/";
		}

		String userId = (String) httpSession.getAttribute("userId");
		String userName = (String) httpSession.getAttribute("userName");

		DBAccess.insertDataToCreatePost(userId, userName, postcontent, viewable);

		return "redirect:/home";
	}

	@GetMapping("/editpost")
	public String editPostPage(@RequestParam("postId") int postId, @RequestParam("postText") String postText,
			@RequestParam("viewable") int viewAble, RedirectAttributes redirect, Model model) {

		// Session
		if (httpSession.getAttribute("userId") == null) {
			return "redirect:/";
		}

		model.addAttribute("pId", postId);
		model.addAttribute("pText", postText);
		model.addAttribute("pViewable", viewAble);

		String sessionId = (String) httpSession.getAttribute("userId");
		if (sessionId == null || sessionId == "") {
			redirect.addFlashAttribute("errLoginMsg", "Login First");

			return "redirect:/";
		}

		return "edit_post";
	}

	@GetMapping("/updatePostAction")
	public String updatePostAction(@RequestParam("post_Id") int postId, @RequestParam("post_Text") String postText,
			@RequestParam("viewable") int Viewable, Model model) {
		DBAccess.updateDataToPost(Viewable, postText, postId);
		return "redirect:/home";
	}

	// edit post in profile to return page profile Start
	@GetMapping("/editpostPf")
	public String editPostInProfile(@RequestParam("postId") int postId, @RequestParam("postText") String postText,
			@RequestParam("viewable") int viewAble, RedirectAttributes redirect, Model model) {

		// Session
		if (httpSession.getAttribute("userId") == null) {
			return "redirect:/";
		}

		model.addAttribute("pId", postId);
		model.addAttribute("pText", postText);
		model.addAttribute("pViewable", viewAble);

		String sessionId = (String) httpSession.getAttribute("userId");
		if (sessionId == null || sessionId == "") {
			redirect.addFlashAttribute("errLoginMsg", "Login First");

			return "redirect:/";
		}

		return "edit_post_pf";
	}

	@GetMapping("/updatePostActionToProfilePage")
	public String updatePostActionToProfilePage(@RequestParam("post_Id") int postId,
			@RequestParam("post_Text") String postText, @RequestParam("viewable") int Viewable, Model model) {
		DBAccess.updateDataToPost(Viewable, postText, postId);
		return "redirect:/profile";
	}

	// edit post in profile to return page profile End

	@GetMapping("/deletePostActionByPostId")
	public String deletePostActionByPostId(@RequestParam("PostId") int pId, Model model) {
		DBAccess.deleteDataFromPost(pId);

		return "redirect:profile";
	}

	@GetMapping("/deletePostActionByPostIdInHome")
	public String deletePostActionByPostIdInHome(@RequestParam("PostId") int pId, Model model) {
		DBAccess.deleteDataFromPost(pId);

		return "redirect:home";
	}

}
