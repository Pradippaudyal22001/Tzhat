package com.classbook.cba.dao;

public class Post {
	public String  username;
	public int postId;

	public String posttext;
	public String createddate;
	public int Viewable;
	
	public boolean postOwnerFlg;
public String imageBytesToStringInPost; 
	
	public String getImageBytesToStringInPost() {
		return imageBytesToStringInPost;
	}

	public void setImageBytesToStringInPost(String imageBytesToStringInPost) {
		this.imageBytesToStringInPost = imageBytesToStringInPost;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getPosttext() {
		return posttext;
	}

	public void setPosttext(String posttext) {
		this.posttext = posttext;
	}

	public String getCreateddate() {
		return createddate;
	}

	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}

	public int getViewable() {
		return Viewable;
	}

	public void setViewable(int viewable) {
		Viewable = viewable;
	}

	public boolean isPostOwnerFlg() {
		return postOwnerFlg;
	}

	public void setPostOwnerFlg(boolean postOwnerFlg) {
		this.postOwnerFlg = postOwnerFlg;
	}
	
	

}
