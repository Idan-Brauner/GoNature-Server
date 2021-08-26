package Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Visitor;
import entity.Worker;

public class LoginDBLogic {

	public static boolean LoginQueryUserExist(String tableName, String ID) // For worker table and visitor table.
	{
		try {
			String query = "SELECT * FROM " + tableName + " WHERE ID = ?";
			PreparedStatement preparedStatement = EchoServer.con.prepareStatement(query);
			preparedStatement.setString(1, ID); // In the first question mark put IDtxt.
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Visitor UserLoginQueryPasswordCorrect(String ID, String Password) {
		try {
			Visitor vis = null;
			// Visitor visitor = null;
			String query = "SELECT * FROM visitor WHERE ID = '" + ID + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				vis = (new Visitor(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7),
						resultSet.getString(8), resultSet.getString(9), resultSet.getString(10),
						resultSet.getString(11)));
			}
			if (vis != null && vis.getPassword().equals(Password))
				return vis;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Worker WorkerLoginQueryPasswordCorrect(String ID, String Password) {
		try {
			// Worker worker = null;

			Worker work = null;
			String query = "SELECT * FROM worker WHERE ID = '" + ID + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				work = (new Worker(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
						resultSet.getString(7)));

			}
			if (work != null && work.getPassword().equals(Password) && !(work.getRole().isEmpty())) {
				return work;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Visitor QuickEnterQueryUserExist(String ID) {
		try {
			Visitor vis = null;
			String query = "SELECT * FROM visitor WHERE ID = '" + ID + "'" + " OR MemberID = '" + ID + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				vis = (new Visitor(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7),
						resultSet.getString(8)));

			}
			return vis;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
