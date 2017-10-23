package bootcamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User_group {

	private int id;
	private String name;

	public User_group setName (String name) throws Exception {

		if ( name.length() < 5 ) {
			throw new Exception("Name is too short");
		} else {
			this.name = name;
			return this;
		}
	}

	public String getName () {

		return this.name;

	}

	public int getId () {

		return this.id;

	}

	User_group (String name) throws Exception{

		setName(name);

	}

	public void saveToDB() throws SQLException {

		if ( this.id == 0) {

			String query = "INSERT INTO user_group (`name`) VALUES ( ? );";
			String[] queryParams = {this.name};

			this.id = DbClient.executeUpdate(query, queryParams);

			System.out.println("Added new row, id: " + this.id);

		} else {

			String query = "UPDATE user_group SET `name` = ? where `id` = ?;";
			String id = "" + this.id;
			String[] queryParams = {this.name, id};

			this.id = DbClient.executeUpdate(query, queryParams);

			System.out.println("Update row, id: " + this.id);

		}

	}

	public void deleteUserGroup (Connection conn) throws SQLException {

		if ( this.id != 0) {
			String query = "DELETE from user_group where id = ?;";

			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, this.id);
			st.executeUpdate();

			System.out.println("Delete User_grouup, id: " + this.id);

			this.id = 0;
		}
	}

	public User_group loadById (Connection conn, int id) throws SQLException, Exception {

		String query = "Select * FROM user_group where `id` = ?;";

		PreparedStatement st = conn.prepareStatement(query);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();

		if (rs.next()) {

			User_group userGroupLoaded = new User_group(rs.getString(2));
			userGroupLoaded.id = rs.getInt(1);
			return userGroupLoaded;
		} else {
			return null;
		}
	}

	public User_group[] loadAll (Connection conn) throws SQLException, Exception{
		
		String query = "SELECT * FROM user_group";
		ArrayList<User_group> userGroupList = new ArrayList<User_group>(); 
		
		PreparedStatement st = conn.prepareStatement(query);
		ResultSet rs = st.executeQuery();
		
		while ( rs.next()) {
			
			userGroupList.add(loadById(conn, rs.getInt(1)));
			
		}
		if (userGroupList.size() > 0) {
		User_group[] userGroupArray = new User_group[userGroupList.size()];
		userGroupArray = userGroupList.toArray(userGroupArray);
		return userGroupArray;
		} else {
			return null;
		}
		
	}
	
}
