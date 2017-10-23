package bootcamp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbClient {

	static String db_conn = "jdbc:mysql://127.0.0.1:3306/";
	static String db_name = "workshop_2";
	static String db_user = "root";
	static String db_password = "coderslab";

	protected static Connection connect() throws SQLException {

		return DriverManager.getConnection(db_conn + db_name + "?useSSL=false", db_user, db_password) ;
	}

	public static void execute(String query, String[] queryParams) throws SQLException {
		try (Connection conn = DbClient.connect()) {

			PreparedStatement st = conn.prepareStatement(query);

			for ( int i = 0; i < queryParams.length ; i++) {
				st.setString(i+1, queryParams[i]);
			}

			st.execute();

		}catch (SQLException e) {
			throw e;
		}
	}

	public static int executeUpdate(String query, String[] queryParams) throws SQLException {

		int id = 0;

		Connection conn = DbClient.connect();

		PreparedStatement st = null;
		st = conn.prepareStatement(query, st.RETURN_GENERATED_KEYS);

		for ( int i = 0; i < queryParams.length ; i++) {
			st.setString(i+1, queryParams[i]);
		}

		st.executeUpdate();

		ResultSet rs = st.getGeneratedKeys();

		while (rs.next()) {

			id = rs.getInt(1);

		}

		return id;
	}

}
