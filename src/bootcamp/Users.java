package bootcamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Users {

	private Integer id;
	private String userName;
	private String email;
	private String password;
	private String person_group_id;

	public Users hashPassword(String password) throws Exception{

		if (password.length() < 5) {

			throw new Exception("Your password is too short");

		}else {

			this.password = BCrypt.hashpw(password,BCrypt.gensalt());
			return this;
			
		}
	}

	public boolean verifyPassword(String password) { // need to be end - only name of that method!

		return true;

	}

	public Users setUserName (String userName) throws Exception{

		if (userName.length() < 5) {

			throw new Exception("Your user name is too short");

		}else {

			this.userName = userName;
			return this;
		}
	}

	public String getUserName() {
		return this.userName;
	}
	
	public String getPerson_group_id() {
		return person_group_id;
	}

	public Users setPerson_group_id(String person_group_id) {
		this.person_group_id = person_group_id;
		return this;
	}

	public String getEmail() {
		return this.email;
	}

	public Users setEmail (String email) throws Exception{

		if (email.indexOf('@') == -1 || email.indexOf('@') == 0) {

			throw new Exception("It's not a email address");

		}else {

			this.email = email;
			return this;

		}
	}

	Users(String userName, String email, String password) throws Exception {

		try {
			this.id = 0;
			setUserName(userName);
			setEmail(email);
			hashPassword(password);
			this.person_group_id = "1";
		}catch (Exception e) {
			throw e;
		}

	}

	public void saveToDB() throws SQLException {

		if ( this.id == 0) {

			String query = "INSERT INTO users VALUES (username,email,password,person_group_id) VALUES (?,?,?,?);";
			String[] queryParams = {this.userName, this.email, this.password, this.person_group_id};

			this.id = DbClient.executeUpdate(query, queryParams);
			System.out.println("Added new row, id: " + this.id);
			
		} else {
			
			String id = "" + this.id;
			String query = "UPDATE users SET `username` =?, `email` = ?, `password` = ? , `person_group_id` = ? where `id` = ?;";
			String[] queryParams = {this.userName, this.email, this.password, this.person_group_id , id};
			
			System.out.println("Update user, id: " + DbClient.executeUpdate(query, queryParams));
			
		}
	}

	public static Users loadById( Connection conn , int id) throws SQLException, Exception {
		
		String query = "SELECT * FROM users where `id` = ?;";
		
		PreparedStatement st = conn.prepareStatement(query);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		
		if (rs.next()) {
			Users loadedUser = new Users(rs.getString(2), rs.getString(3), rs.getString(4));
			
			loadedUser.id = rs.getInt(1);
			loadedUser.person_group_id = rs.getString(5);
			
			return loadedUser;	
		} else {
			return null;
		}
	}

	public static Users[] loadAll(Connection conn) throws SQLException, Exception {
		
		
		ArrayList<Users> users = new ArrayList<Users>();
		String query = "SELECT * FROM users";
		
		PreparedStatement st = conn.prepareStatement(query);
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			users.add(Users.loadById(conn, rs.getInt(1)));
		}
		if (users.size() > 0) {
		Users[] usersArray = new Users[users.size()];
		usersArray = users.toArray(usersArray);
		
		return usersArray;
		} else {
			return null;
		}
		
	}

	public void deleteUser(Connection conn) throws SQLException{
		
		if (this.id != 0) {
			
			String query = "DELETE FROM users where id = ?;";
			
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, this.id);
			st.executeUpdate();
			
			System.out.println("Delete User, id: " + this.id);
			
			this.id = 0;
			
		}
		
	}
	
	public static void main(String[] arg) {

		Users user1 = null;

		try {
			
			user1 = new Users("Daniel", "dan@wp.pl", "haslo");
			
			user1.saveToDB();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println(user1.id);
	}

}


