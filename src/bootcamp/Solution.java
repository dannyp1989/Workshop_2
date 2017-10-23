package bootcamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Solution {

	private int id;
	private String created;
	private String updated;
	private String description;
	private int excercise_id;
	private int users_id;

	public int getId() {
		return id;
	}

	public String getCreated() {
		return created;
	}

	public String getUpdated() {
		return updated;
	}

	public String getDescription() {
		return description;
	}

	public int getExcercise_id() {
		return excercise_id;
	}

	public int getUsers_id() {
		return users_id;
	}

	public Solution setCreated(String created) {
		this.created = created;
		return this;
	}

	public Solution setUpdated(String updated) {
		this.updated = updated;
		return this;
	}

	public Solution setDescription(String description) {
		this.description = description;
		return this;
	}

	public Solution setExcercise_id(int excercise_id) {
		this.excercise_id = excercise_id;
		return this;
	}

	public Solution setUsers_id(int users_id) {
		this.users_id = users_id;
		return this;
	}

	Solution(String created, String updated, String description, int excercise_id, int users_id) {

		setCreated(created);
		setUpdated(updated);
		setDescription(description);
		setExcercise_id(excercise_id);
		setUsers_id(users_id);

	}

	public void saveToDB() throws SQLException {

		if (this.id == 0) {
			String query = "INSERT INTO solution (`created`, `update`, `description`, `excercise_id`, `users_id`) VALUES ( ?,?,?,?,?);";
			String excerciseId = "" + this.excercise_id;
			String usersId = "" + this.users_id;
			String[] queryParams = { this.created, this.updated, this.description, excerciseId, usersId };

			this.id = DbClient.executeUpdate(query, queryParams);

			System.out.println("Added new row, id: " + this.id);

		} else {

			String query = "UPDATE solution SET `created` = ?, `update` = ?, `description` = ?, `excercise_id` =?, `users_id` = ? where `id` = ?;";
			String excerciseId = "" + this.excercise_id;
			String usersId = "" + this.users_id;
			String Id = "" + this.id;
			String[] queryParams = { this.created, this.updated, this.description, excerciseId, usersId, Id };

			System.out.println("Update solution, id: " + DbClient.executeUpdate(query, queryParams));

		}
	}

	public void deleteSolution(Connection conn) throws SQLException {

		if (this.id != 0) {
			String query = "DELETE FROM solution where id = ?;";

			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, this.id);
			st.executeUpdate();

			System.out.println("Delete Solution, id: " + this.id);
			this.id = 0;
		}

	}

	public static Solution loadById(Connection conn, int id) throws SQLException {

		String query = "SELECT * FROM solution where `id` = ?;";

		PreparedStatement st = conn.prepareStatement(query);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			Solution solutionLoaded = new Solution(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5),
					rs.getInt(6));
			solutionLoaded.id = rs.getInt(1);
			return solutionLoaded;
		} else {
			return null;
		}
	}

	public static Solution[] loadAll(Connection conn) throws SQLException {

		String query = "SELECT * FROM solution;";
		ArrayList<Solution> solutionList = new ArrayList<Solution>();

		PreparedStatement st = conn.prepareStatement(query);
		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			solutionList.add(loadById(conn, rs.getInt(1)));
		}
		if (solutionList.size() > 0) {
			Solution[] solutionArray = new Solution[solutionList.size()];
			solutionArray = solutionList.toArray(solutionArray);
			return solutionArray;
		} else {
			return null;
		}

	}

}
