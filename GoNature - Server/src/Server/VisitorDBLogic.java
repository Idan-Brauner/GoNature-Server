package Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Visitor;

public class VisitorDBLogic {

	public static int numOfRows() {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT * FROM visitor;");//query
		      while (res.next()) {
		        count++;//counter to number of rows in the database
		      }  
		      
			} catch (SQLException e) {
			e.printStackTrace();
			}
			return count;

	}

	public static Visitor[] showVisitors() {
		Visitor[] arrVisitor = new Visitor[numOfRows()];//create new visitor array with the number of rows  in the DB
		Statement stmt;
		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM visitor;");//query
			while (rs.next()) {//get the data from the DB and save in the visitor array
				arrVisitor[i] = (new Visitor(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrVisitor;//return the visitor array that contain the data from the database
	}

	public static boolean updateEmail(Object msg) {
		msg = (String) msg;// Casting from object
		String msg_id = "";// initialize msg id
		int msg_id_num = 0;// initialize msg id number
		String msg_email = "";// initialize msg email
		int index = 0;
		while (msg.toString().charAt(index) != ' ') {// loop to get the email until ' ' char by char
			msg_id += msg_id + msg.toString().charAt(index); //add to msg_id the email characters
			index++;//count the number of char's
		}
		msg_email = msg.toString().substring(index + 1); // msg email get the string +1 for '/0'
		msg_id_num = Integer.valueOf(msg_id); // msg id number get the value of msg id to know the number
		try {
			PreparedStatement preparedStmt = EchoServer.con.prepareStatement("UPDATE visitor SET Email = ? WHERE ID = ?");
			preparedStmt.setString(1, msg_email);
			preparedStmt.setInt(2, msg_id_num);
			int result = preparedStmt.executeUpdate(); // The number of lines effected.
			if (result != 1) // ID need to be main key so that's why only one line need to be effected.
			{
				preparedStmt.close();
				return true;
			}
			preparedStmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
