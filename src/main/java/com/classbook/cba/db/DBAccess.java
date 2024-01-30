package com.classbook.cba.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.web.multipart.MultipartFile;

import com.classbook.cba.dao.UserInfo;

public class DBAccess {

	/**
	 * login query
	 * 
	 * @param userId
	 * @param userPwd
	 * @return
	 */
	public static ResultSet checkAccByIdPwd(String userId, String userPwd) {
		try {
			Connection con = DBConnector.getConnection();
			PreparedStatement pstm = con.prepareStatement("SELECT * FROM account WHERE user_id = ? AND user_pwd = ?");
//			PreparedStatement pstm = 
//					con.prepareStatement("SELECT account.*,user_info.user_name as newUN FROM account LEFT JOIN user_info ON account.user_id = user_info.user_id "
//							+ "WHERE account.user_id = ? AND account.user_pwd = ?");
			pstm.setString(1, userId);
			pstm.setString(2, userPwd);

			ResultSet rs = pstm.executeQuery();

			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static ResultSet getUsersPost(String userId) {
		try {
			Connection con = DBConnector.getConnection();
			PreparedStatement pstm = con.prepareStatement("SELECT post.*,user_info.user_name as newUN FROM post LEFT JOIN user_info ON post.user_id = user_info.user_id"
					+ " WHERE post.user_id = ? ORDER BY created_date DESC");
			pstm.setString(1, userId);

			ResultSet rs = pstm.executeQuery();
			
			return rs;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * from sql table user_info select query for profile
	 * 
	 * @param uid
	 * @return
	 */
	public static ResultSet getUserProfileData(String uid) {
		try {
			Connection con = DBConnector.getConnection();
			PreparedStatement pstm = con.prepareStatement("SELECT * FROM user_info WHERE user_id = ?");
			pstm.setString(1, uid);

			ResultSet rs = pstm.executeQuery();
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static void updateDataForEditProfile(UserInfo userInfo, String userId) {
		try {
			Connection con = DBConnector.getConnection();
			PreparedStatement pstm = con.prepareStatement(
					"UPDATE user_info SET user_name = ?,date_of_birth = ?,marital_status = ?,workplace = ?,job_career = ?,address_1 = ?,address_2 = ?,education_1 = ?,education_2 = ?,hobby_1 = ?,hobby_2 = ?,fav_food = ?,fav_movie = ?,fav_song = ?,fav_place = ? WHERE user_id = ?");

			pstm.setString(1, userInfo.userName);
			pstm.setString(2, userInfo.userBirth);
			pstm.setInt(3, userInfo.martialStatus);
			pstm.setString(4, userInfo.workPlace);
			pstm.setString(5, userInfo.jobCareer);
			pstm.setString(6, userInfo.address1);
			pstm.setString(7, userInfo.address2);
			pstm.setString(8, userInfo.education1);
			pstm.setString(9, userInfo.education2);
			pstm.setString(10, userInfo.hobby1);
			pstm.setString(11, userInfo.hobby2);
			pstm.setString(12, userInfo.favFood);
			pstm.setString(13, userInfo.favMovie);
			pstm.setString(14, userInfo.favSong);
			pstm.setString(15, userInfo.favPlace);
			pstm.setString(16, userId);

			pstm.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();

		}

	}


	/**
	 * from sql table post insert query for create post
	 * 
	 * @param uid
	 * @param uNm
	 * @param pT
	 * @param view
	 */
	public static void insertDataToCreatePost(String uid, String uNm, String pT, int view) {
		try {
			Connection con = DBConnector.getConnection();
			PreparedStatement pstm = con.prepareStatement(
					"INSERT INTO post(user_id,user_name,post_text,viewable,created_date) VALUES (?,?,?,?,sysdate())");
			pstm.setString(1, uid);
			pstm.setString(2, uNm);
			pstm.setString(3, pT);
			pstm.setInt(4, view);

			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * from sql table post update query for edit post
	 * 
	 * @param viewable
	 * @param postText
	 * @param pid
	 * @return ResultSet
	 */
	public static ResultSet updateDataToPost(int viewable, String postText, int pid) {
		try {
			Connection con = DBConnector.getConnection();
			PreparedStatement pstm = con.prepareStatement(
					"UPDATE post SET post_text = ? ,viewable = ? ,updated_date = sysdate() WHERE post_id = ?");
			pstm.setString(1, postText);
			pstm.setInt(2, viewable);
			pstm.setInt(3, pid);

			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * from sql table post delete query
	 * 
	 * @param pid
	 */

	public static void deleteDataFromPost(int p_id) {
		try {
			Connection con = DBConnector.getConnection();
			PreparedStatement pstm = con.prepareStatement("DELETE FROM post where post_id = ?");
			pstm.setInt(1, p_id);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	// Sign up DBAccess Start
	public static ResultSet GetClientSessionId(String sessionId) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DBConnector.getConnection();
			String sql = "SELECT * FROM tmp_account WHERE client_server_data_tmp_data = ?";
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.setString(1, sessionId);
			ResultSet rs1 = pstm.executeQuery();
			return rs1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static void InsertSessionId(String sessionId) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DBConnector.getConnection();
			String sql = "INSERT INTO tmp_account (client_server_data_tmp_data,created_date) VALUE (?,sysdate())";
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.setString(1, sessionId);
			pstm.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void signUpAccount(String newId, String newPwd, String newUserName) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DBConnector.getConnection();
			String sql = "INSERT INTO account(user_id,user_pwd,user_name,created_date) VALUES(?,?,?,sysdate())";
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.setString(1, newId);
			pstm.setString(2, newPwd);
			pstm.setString(3, newUserName);
			pstm.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void InsertUserInfo(String newId, String newUserName) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DBConnector.getConnection();
			String sql = "INSERT INTO user_info(user_id,user_name,created_date) VALUES(?,?,sysdate())";
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.setString(1, newId);
			pstm.setString(2, newUserName);
			pstm.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void DeleteSessionId(int Id) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DBConnector.getConnection();
			String sql = "DELETE FROM tmp_account WHERE user_id= ? ";
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.setInt(1, Id);
			pstm.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	// Sign up DBAccess End
	
//	public static ResultSet updatePost(String postId) {
//		try {
//			Connection con = DBConnector.getConnection();
//			PreparedStatement pstm = con.prepareStatement("select * from post where post_id = ?");
//			pstm.setString(1, postId);
//			ResultSet rs1 = pstm.executeQuery();
//			return rs1;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return null;
//	}

	public static ResultSet GetPostDataOnHome(String userId) {
		try {
			Connection con = DBConnector.getConnection();
			String sql = "SELECT post.*,user_info.user_name as newUN,user_info.profile_img FROM post LEFT JOIN user_info ON post.user_id = user_info.user_id"
					+ " WHERE post.user_id = ? OR viewable = 0 ORDER BY created_date DESC";
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.setString(1, userId);
			
			/* System.out.println(pstm.toString()); */
			
			ResultSet rs1 = pstm.executeQuery();
			return rs1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	//profile Image
	public static ResultSet returnProfileImagetoHome(String userid) {
		try {
			Connection con = DBConnector.getConnection();
			PreparedStatement pstm= con.prepareStatement("select profile_img,user_name from user_info where user_id = ?");
			pstm.setString(1,userid );
			ResultSet rs2 = pstm.executeQuery();
			
			return rs2;
			
		} catch (Exception e) {
		e.printStackTrace();
		}
		return null;
	}
	
	public static void updateProfileImage(MultipartFile file,String userId) {
		try {
			Connection con = DBConnector.getConnection();
			PreparedStatement pstm = con.prepareStatement("UPDATE user_info SET profile_img = ?  WHERE user_id = ?");
			pstm.setBytes(1, file.getBytes());
			pstm.setString(2,userId );
			pstm.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// Change password
	public static void changePassword(String newPwd, String uId, String currentpwd) {
		try {
			Connection conn = DBConnector.getConnection();
			String sql = "UPDATE account SET user_pwd = ? WHERE user_id = ? AND user_pwd = ?";
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, newPwd);
			pstm.setString(2, uId);
			pstm.setString(3, currentpwd);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
