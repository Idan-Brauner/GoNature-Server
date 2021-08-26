package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.UsageReport;
import entity.Visitor;
import entity.ApproveList;
import entity.OrderTotalNumberOfVisitorsReport;
import entity.OrderVisitorsReport;
import entity.OrderVisitorsReport2;

public class ServiceAgentAndParkManagerDBLogic {
	static Connection conn;

	public static int numOfRows() {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT * FROM visitor;");// query
			while (res.next()) {
				count++;// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	public static int numOfRowsVisitLimit() {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT * FROM visitorlimit;");// query
			while (res.next()) {
				count++;// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}
	public static int numOfOrderComplete() {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT * FROM orders_complete");// query
			while (res.next()) {
				count++;// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	public static int numOfOrder() {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT * FROM orders");// query
			while (res.next()) {
				count++;// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	/**
	 * @return
	 * return the number of visitor in the DB query
	 * 
	 */
	public static int numOfVisitors(String ParkID) {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT visitorsNum FROM orders WHERE park = '" + ParkID + "'");// query
			while (res.next()) {
				count += res.getInt(1);// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	public static int numOfLimitRequests(String ParkID) {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT LimitNum FROM visitorlimit WHERE ParkName = '" + ParkID + "'");// query
			while (res.next()) {
				count = res.getInt(1);// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	public static boolean UpdateDiscount(Object msg) {
		Statement stmt;

		String[] check = (String[]) msg;

		try {
			stmt = EchoServer.con.createStatement();
			String query = " insert into discount(ActiveDate, FinishDate, Discount)" + " values (?, ?, ?,?,?)";

			PreparedStatement preparedStmt = EchoServer.con.prepareStatement(query);
			preparedStmt.setString(1, check[1]);
			preparedStmt.setString(2, check[2]);
			preparedStmt.setString(3, check[3]);
			preparedStmt.execute();

			preparedStmt.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param The function accepts the values of active date and finish date and the
	 *            discount and the name of the park and insert the data to the DB
	 *            and feeds on each request "wait".
	 * @return true if succeed to insert or return false if fail.
	 */
	public static boolean DiscountParkManager(Object msg) {
		Statement stmt;

		String[] check = (String[]) msg;

		try {
			stmt = EchoServer.con.createStatement();
			String query = " insert into discount(ActiveDate, FinishDate, Discount,ParkName,Status)"
					+ " values (?, ?, ?, ?, ?)";

			PreparedStatement preparedStmt = EchoServer.con.prepareStatement(query);
			preparedStmt.setString(1, check[1]);// Value of active date.
			preparedStmt.setString(2, check[2]);// Value of finish date.
			preparedStmt.setString(3, check[3]);// Value of discount.
			preparedStmt.setString(4, check[4]);// Value of Park name.
			preparedStmt.setString(5, check[5]);// Value of status.
			preparedStmt.execute();

			preparedStmt.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		}
	}

	/**
	 * @param Num
	 * @param ParkID
	 * @return
	 * this function set up an visitor request update for the department manager to approve in the DB
	 * by changing the status of the request to wait from decline or any previous wait status.
	 * 
	 */
	public static boolean UpdateVisitorLimitation(String Num, String ParkID) {
		Statement stmt;
		String wait = "wait";
		String decline = "decline";
		try {

			stmt = EchoServer.con.createStatement();
			stmt.executeUpdate("UPDATE visitorlimit SET LimitNum = '" + Num + "', Status= '" + wait
					+ "' WHERE ParkName= '" + ParkID + "' AND (Status= '" + wait + "' OR Status= '" + decline + "')");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	public static OrderTotalNumberOfVisitorsReport[] ShowReportNumVisitors(String ParkID) {
		OrderTotalNumberOfVisitorsReport[] arrOrder = new OrderTotalNumberOfVisitorsReport[numOfOrder()];// create new
																											// order
																											// array
		Statement stmt;

		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT UserType,visitDate,visitorsNum FROM orders WHERE park= '" + ParkID + "'");// query
			while (rs.next()) {
				arrOrder[i] = (new OrderTotalNumberOfVisitorsReport(rs.getString(1), rs.getString(2), rs.getString(3)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return arrOrder;

	}

	/**
	 * The function get park name and month and return from order table the type of
	 * visitor that approved to enter to the park.
	 * 
	 * 
	 */
	public static OrderVisitorsReport[] ShowVisitorsReport_Visitor(String ParkName, String Month) {
		OrderVisitorsReport[] arrOrder = new OrderVisitorsReport[numOfOrderComplete()];
		Statement stmt;

		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT UserType,VisitDate,visitorsNum FROM orders_complete WHERE UserType = 'Visitor' AND EntryStatus='Approved' AND MonthName='"
							+ Month + "' AND park='" + ParkName + "'");// query
			while (rs.next()) {// get the data from the DB and save in the order array
				arrOrder[i] = (new OrderVisitorsReport(rs.getString(1), rs.getString(2), rs.getString(3)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return arrOrder;// return the visitor array that contain the data from the database
	}

	/**
	 * The function get park name and month and return from order table the type of
	 * guide that approved to enter to the park.
	 * 
	 * 
	 */
	public static OrderVisitorsReport[] ShowVisitorsReport_Guide(String ParkName, String Month) {
		OrderVisitorsReport[] arrOrder = new OrderVisitorsReport[numOfOrderComplete()];// create new order array
		Statement stmt;

		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT UserType,VisitDate,visitorsNum FROM orders_complete WHERE UserType = 'Guide' AND EntryStatus='Approved' AND MonthName='"
							+ Month + "' AND park='" + ParkName + "'");// query
			while (rs.next()) {// get the data from the DB and save in the order array
				arrOrder[i] = (new OrderVisitorsReport(rs.getString(1), rs.getString(2), rs.getString(3)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return arrOrder;// return the visitor array that contain the data from the database
	}

	/**
	 * The function get park name and month, return from the DB the values of user
	 * type visit date and visitor number with status approved.
	 * 
	 * 
	 */
	public static OrderVisitorsReport[] ShowVisitorsReport(String ParkName, String Month) {
		OrderVisitorsReport[] arrOrder = new OrderVisitorsReport[numOfOrderComplete()];// create new order array
		Statement stmt;

		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT UserType,VisitDate,visitorsNum FROM orders_complete WHERE MonthName='"
					+ Month + "' AND EntryStatus='Approved' AND park='" + ParkName + "'");// query
			while (rs.next()) {// get the data from the DB and save in the order array
				arrOrder[i] = (new OrderVisitorsReport(rs.getString(1), rs.getString(2), rs.getString(3)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return arrOrder;// return the visitor array that contain the data from the database
	}

	/**
	 * The function gets the value of the name of the park and the month and
	 * searches in DB and shows when the park was not full according to the
	 * parameters.
	 *
	 * @return the data from the DB or catch exception.
	 */
	public static UsageReport[] UsageReport(String month, String ParkName) {
		UsageReport[] arr = new UsageReport[numOfUsageReport()];// create new order array
		Statement stmt;

		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Pdate FROM usagerep WHERE Pmonth='" + month
					+ "' AND ParkName='" + ParkName + "' AND Vfull='No'");// query
			while (rs.next()) {// get the data from the DB and save in the order array
				arr[i] = (new UsageReport(rs.getString(1)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return arr;// return the visitor array that contain the data from the database

	}

	/**
	 * @return number of rows in the database
	 */
	public static int numOfUsageReport() {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT * FROM usagerep");// query
			while (res.next()) {
				count++;// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}
	
	
	/**
	 * @param msg
	 * @return
	 * This 
	 * try to add a visitor and return boolean confirmation if a visitor added correctly to the visitor query in the DB
	 * 
	 */
	public static boolean addVisitor(String msg) {
		Statement stmt;
		String arr[] = msg.toString().split(" ");

		try {
			stmt = EchoServer.con.createStatement();
			stmt.executeUpdate("INSERT INTO Visitor " + "VALUES('" + arr[1].toString() + "', '" + arr[2].toString()
					+ "', '" + arr[3].toString() + "', '" + arr[4].toString() + "', '" + arr[5].toString() + "', '"
					+ arr[6].toString() + "'," + null + "," + " '" + arr[8].toString() + "', '" + arr[9].toString()
					+ "', '" + arr[10].toString() + "', '" + arr[11].toString() + "');");

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param msg
	 * @return
	 * return the right information if the ID is exist or not in the visitor query 
	 * 
	 */
	public static boolean numOfVisitorsId(String msg) {
		Statement stmt;
		ResultSet resultSet;
		Visitor count = null;
		try {
			stmt = EchoServer.con.createStatement();
			resultSet = stmt.executeQuery("SELECT * FROM visitor WHERE ID = '" + msg + "'");// query
			while (resultSet.next()) {
				count = new Visitor(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7),
						resultSet.getString(8), resultSet.getString(9), resultSet.getString(10),
						resultSet.getString(11));
			}
			if (count != null && count.getVisitorID().equals(msg))
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * @param ParkID
	 * @return
	 * This function receive the current visitor Limitation in the park
	 * 
	 */
	public static int numOfLimitInt(String ParkID) {
		Statement stmt;
		ResultSet res;
		String approve = "approve";
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery(
					"SELECT LimitNum FROM visitorlimit WHERE ParkName= '" + ParkID + "' AND Status= '" + approve + "'");// query
			while (res.next()) {
				count = res.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * @return
	 * This function receive the number of rows in the discount DB query
	 * 
	 */
	public static int numOfRowsDiscount() {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT * FROM discount;");// query
			while (res.next()) {
				count++;// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	/**
	 * @return
	 * This function return an array of requests to the approve list table from visitor limitation type.
	 * 
	 */
	public static ApproveList[] UpdateApprovelistVisitor() {
		Statement stmt;
		ApproveList[] reqs = new ApproveList[numOfRowsVisitLimit()];
		int i = 0;
		String wait = "wait";
		try {

			// Visitor visitor = null;
			String query = "SELECT* FROM visitorlimit WHERE Status= '" + wait + "'";
			stmt = EchoServer.con.createStatement();
			ResultSet resultSet = stmt.executeQuery(query);
			while (resultSet.next()) {
				reqs[i++] = (new ApproveList(resultSet.getString(1), resultSet.getString(2), "-", "-", wait));

			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reqs;
	}

	/**
	 * @return
	 * This function return an array of request to the approve list table from discount type.
	 * 
	 */
	public static ApproveList[] UpdateApprovelistDiscount() {
		Statement stmt;
		ApproveList[] reqs = new ApproveList[numOfRowsDiscount()];
		int i = 0;
		String wait = "wait";
		try {
			String query = "SELECT* FROM discount WHERE Status= '" + wait + "'";
			stmt = EchoServer.con.createStatement();
			ResultSet resultSet = stmt.executeQuery(query);
			while (resultSet.next()) {
				reqs[i++] = (new ApproveList(resultSet.getString(4), resultSet.getString(3), resultSet.getString(1),
						resultSet.getString(2), wait));

			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reqs;
	}

	/**
	 * @param ParkID
	 * @param Num
	 * @return
	 * This function approve the request that the Department manager select to approve in the list of the 
	 * Visitor Limitation table option and update it to approve from wait in the Visitor Limitation query in the DB
	 * 
	 * 
	 */
	public static boolean ApprovedVisitorLimitation(String ParkID, String Num) {

		Statement stmt;
		String approve = "approve";
		String wait = "wait";
		String decline = "decline";
		try {

			stmt = EchoServer.con.createStatement();
			stmt.executeUpdate("UPDATE visitorlimit SET LimitNum = '" + Num + "' WHERE ParkName= '" + ParkID
					+ "' AND Status= '" + approve + "'");
			stmt.executeUpdate("UPDATE visitorlimit SET Status = '" + decline + "' WHERE ParkName= '" + ParkID
					+ "' AND Status= '" + wait + "'");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	/**
	 * @param ParkID
	 * @return
	 * This function decline the request that the Department manager select to decline in the list of the 
	 * Visitor Limitation table option and update it to decline from wait in the Visitor Limitation query in the DB
	 * 
	 */
	public static boolean DeclinedVisitorLimitation(String ParkID) {
		Statement stmt;
		String wait = "wait";
		String decline = "decline";
		try {

			stmt = EchoServer.con.createStatement();
			stmt.executeUpdate("UPDATE visitorlimit SET Status = '" + decline + "' WHERE ParkName= '" + ParkID
					+ "' AND Status= '" + wait + "'");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param ParkID
	 * @param StartDate
	 * @param FinishDate
	 * @return
	 * This function approve the request that the Department manager select to approve in the list of the 
	 * Discount table option and update it to approve from wait in the Discount query in the DB
	 * 
	 */
	public static boolean ApprovedDiscount(String ParkID, String StartDate, String FinishDate) {

		Statement stmt;
		String approve = "approve";
		String wait = "wait";
		try {

			stmt = EchoServer.con.createStatement();
			stmt.executeUpdate(
					"UPDATE discount SET Status = '" + approve + "' WHERE ParkName= '" + ParkID + "' AND ActiveDate= '"
							+ StartDate + "'AND FinishDate= '" + FinishDate + "' AND Status= '" + wait + "'");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param ParkID
	 * @param StartDate
	 * @param FinishDate
	 * @return
	 * This function Decline the request that the Department manager select to decline in the list of the 
	 * Discount table option and remove it in the Discount query in the DB
	 * 
	 */
	public static boolean DeclinedDiscount(String ParkID, String StartDate, String FinishDate) {
		Statement stmt;
		String wait = "wait";
		try {

			stmt = EchoServer.con.createStatement();
			stmt.executeUpdate("DELETE FROM discount WHERE ParkName= '" + ParkID + "' AND ActiveDate= '" + StartDate
					+ "'AND FinishDate= '" + FinishDate + "' AND Status= '" + wait + "'");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * The function get park name,month and type and return the sum of visitor number from the DB.
	 * 
	 * @return
	 */
	public static OrderVisitorsReport2 ParkOrderVisitorsReport_UsageRep(String ParkName, String month, String Type) {
		OrderVisitorsReport2 num = null;
		Statement stmt;
		ResultSet rs = null;
		try {
			stmt = EchoServer.con.createStatement();

			rs = stmt.executeQuery("SELECT sum(VisitorsNumber) FROM usagerep WHERE (Pmonth = '" + month
					+ "' AND ParkName='" + ParkName + "' AND Vfull = 'No')");

			if (rs.next()) {// get the data from the DB and save in the order array
				num = (new OrderVisitorsReport2(rs.getString(1)));
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return num;
	}

	/**
	 * The function get park name,type and month and return the sum of visitors number in order where the status approved.
	 * 
	 * 
	 * 
	 */
	public static OrderVisitorsReport ParkOrderVisitorsReport_Order(String ParkName, String month, String Type) {
		OrderVisitorsReport num = null;
		Statement stmt;
		ResultSet rs;
		try {
			stmt = EchoServer.con.createStatement();
			if (Type.contains("All")) {
				rs = stmt.executeQuery("SELECT sum(visitorsNum) FROM orders_complete WHERE (MonthName ='" + month
						+ "' AND park='" + ParkName + "'AND EntryStatus='Approved');");
			} else {
				rs = stmt.executeQuery("SELECT sum(visitorsNum) FROM orders_complete WHERE (MonthName ='" + month
						+ "' AND park='" + ParkName + "'AND EntryStatus='Approved' AND UserType ='" + Type + "');");
			}
			if (rs.next()) {// get the data from the DB and save in the order array
				num = (new OrderVisitorsReport(rs.getString(1)));
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return num;
	}

	/**
	 * The function get active date and finish date and park name, return if the
	 * date with discount exist in DB.
	 * 
	 */
	public static int CheckDateDiscount(Object msg) {
		Statement stmt;
		String[] check = (String[]) msg;

		int res = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Discount FROM discount WHERE ActiveDate ='" + check[1]
					+ "'OR FinishDate='" + check[2] + "'AND ParkName='" + check[4] + "'");// query
			if (rs.next()) {
				res = rs.getInt(1);

			}
			rs.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param The function gets the value of the name of the park and searches it in
	 *            DB and displays the amount of visitors of that park.
	 * @return
	 */
	public static String VisitorsInParkNow(String ParkID) {
		try {
			String VisitorNumber = null;
			String query = "SELECT * FROM visitor_number WHERE ParkName = '" + ParkID + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				VisitorNumber = resultSet.getString(2);

			}
			if (VisitorNumber != null)// Check if the value exist.
				return VisitorNumber;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
