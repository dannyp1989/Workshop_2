package bootcamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Excercise {

	private int id;
	private String title;
	private String description;

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Excercise setTitle(String title) throws Exception{
		if (title.length() > 5) {
			this.title = title;
			return this;
		} else {
			throw new Exception("Title is too short");
		}
	}

	public String getDescription() {
		return description;
	}

	public Excercise setDescription(String description) throws Exception{
		if (description.length() > 5) {
			this.description = description;
			return this;
		}else {
			throw new Exception("Description is too short");
		}

	}

	Excercise (String title, String description) throws Exception{

		setTitle(title);
		setDescription(description);

	}

	public void saveToDB() throws SQLException {

		if ( this.id == 0) {
			String query = "INSERT INTO excercise (`title`, `description`) VALUES (?, ?);";
			String[] queryParams = {this.title, this.description};

			this.id = DbClient.executeUpdate(query, queryParams);

			System.out.println("Added new Excercise, id: " + this.id);

		}else {

			String query = "UPDATE excercise SET `title` = ?, `description` = ? where `id` = ?;";
			String idString = "" + this.id;
			String[] queryParams = {this.title, this.description, idString};
			
			DbClient.executeUpdate(query, queryParams);
			
			System.out.println("Update row, id: " + this.id);

		}

	}

	public void deleteExcercise(Connection conn) throws SQLException {
		
		String query = "DELETE FROM excercise where `id` = ?;";
		
		PreparedStatement st = conn.prepareStatement(query);
		st.setInt(1, this.id);
		st.executeUpdate();
		
		System.out.println("Delete Excercise, id: " + this.id);
		
		this.id = 0;
		 
	}

	public static Excercise loadById(Connection conn, int id) throws SQLException, Exception{
		
		String query = "SELECT * FROM excercise where id = ?;";
		
		PreparedStatement st = conn.prepareStatement(query);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		
		if (rs.next()) {
			Excercise excerciseLoaded = new Excercise(rs.getString(2),rs.getString(3));
			excerciseLoaded.id = rs.getInt(1);
			return excerciseLoaded;
		} else {
			return null;
		}
		
	}

	public static Excercise[] loadAll(Connection conn) throws SQLException, Exception{
		
		String query = "SELECT * FROM excercise;";
		ArrayList<Excercise> excerciseList = new ArrayList<Excercise>();
		
		PreparedStatement st = conn.prepareStatement(query);
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {

			excerciseList.add(Excercise.loadById(conn, rs.getInt(1)));
		
		}
		
		if ( excerciseList.size() > 0) {
			
			Excercise[] excerciseArray = new Excercise[excerciseList.size()];
			excerciseArray = excerciseList.toArray(excerciseArray);
			return excerciseArray;
			
		} else {
			return null;
		}
		
	}
	
}
