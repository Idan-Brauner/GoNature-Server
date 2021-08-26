package Server;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.CancellationReport;
import entity.DepVisitorReport;
import entity.OrderVisitorsReport;
import entity.OrderVisitorsReport2;
import entity.UsageReport;

/**
 * @author Niv Kita DB Logic for the Department manager. all the Queries and DB
 *         calcualtion doing here. all the requests coming from EchoServer.
 */
public class DepManagerDBLogic {
	static Connection conn;

	/**
	 * @return the number of rows in visitor DB table
	 */
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

	/**
	 * @return number of cancel and loss rows.
	 */
	public static int numOfcancel_loss() {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT * FROM cancel_loss");// query
			while (res.next()) {
				count++;// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	/**
	 * @return number of rows in the number of department visitor report
	 */
	public static int numOfDepVisitorReport() {
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

	/**
	 * @param ParkID - Park name
	 * @param Type   - Type of visitor(Guide/Visitor)
	 * @return the Visitor number that exist in the cancel_loss table with the
	 *         conditions
	 */
	public static int numOfCancelAndLoss(String ParkID, String Type) {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT VisitorsNum FROM cancel_loss WHERE ParkName = '" + ParkID
					+ "'  AND TypeCase = '" + Type + "'");
			while (res.next()) {
				count += res.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	/**
	 * @param ParkID - ParkName
	 * @param Type-  Type of the visitors(Guide/Visitor)
	 * @return CancellationReport object that have the data from the query
	 */
	public static CancellationReport[] ShowCancellationReport_Park_cancel(String ParkID, String Type) {
		CancellationReport[] arrOrder = new CancellationReport[numOfcancel_loss()];// create new order array
		Statement stmt;

		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT OrderID,VisitorType,VisitDate,VisitorsNum FROM cancel_loss WHERE ParkName = '"
							+ ParkID + "' AND TypeCase='" + Type + "'");// query
			while (rs.next()) {// get the data from the DB and save in the order array
				arrOrder[i] = (new CancellationReport(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return arrOrder;// return the visitor array that contain the data from the database

	}

	/**
	 * @param ParkID - Park Name
	 * @param Type   - Type of visitors (Guide/Visitor)
	 * @return array of cancellationReport that own the data from the cancel_loss
	 *         table
	 */
	public static CancellationReport[] ShowCancellationReport_Park_loss(String ParkID, String Type) {
		CancellationReport[] arrOrder = new CancellationReport[numOfcancel_loss()];// create new order array
		Statement stmt;

		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT OrderID,VisitorType,VisitDate,VisitorsNum FROM cancel_loss WHERE ParkName = '"
							+ ParkID + "' AND TypeCase='" + Type + "'");// query
			while (rs.next()) {// get the data from the DB and save in the order array
				arrOrder[i] = (new CancellationReport(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return arrOrder;// return the visitor array that contain the data from the database
	}

	/**
	 * @param ParkID - Park Name
	 * @return Visitors Number from visitor_number table in the DataBase where the
	 *         parkName equal to ParkID param.
	 */
	public static int VisitorsInParkNowPark(String ParkID) {
		try {
			int VisitorNumber = 0;
			String query = "SELECT VisitorsNumber FROM visitor_number WHERE ParkName = '" + ParkID + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				VisitorNumber = resultSet.getInt(1);
			}
			return VisitorNumber;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @param ParkNum  - Park Name
	 * @param MonthNum - Month Name
	 * @param TypeNum  - Type of visitor (Guide/Visitor)
	 * @return array of DepVisitorReport with the data, depends if Type= All or
	 *         Guide/Visitor report
	 */
	public static DepVisitorReport[] depVisitorsReport(String ParkNum, String MonthNum, String TypeNum) {
		DepVisitorReport[] arrDepVisitorReport = new DepVisitorReport[numOfDepVisitorReport()];
		Statement stmt;
		ResultSet rs;
		String app = "Approved";
		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();

			if (TypeNum.equals("All")) {
				rs = stmt.executeQuery("SELECT visitDate,StayTime FROM orders_complete WHERE park = '" + ParkNum
						+ "' AND MonthName='" + MonthNum + "' AND EntryStatus = '" + app + "'");
			} else {
				rs = stmt.executeQuery(
						"SELECT visitDate,StayTime FROM orders_complete WHERE park = '" + ParkNum + "' AND MonthName='"
								+ MonthNum + "' AND UserType = '" + TypeNum + "' AND EntryStatus = '" + app + "'");
			}
			while (rs.next()) {// get the data from the DB and save in the order array
				arrDepVisitorReport[i] = (new DepVisitorReport(rs.getString(1), rs.getString(2)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return arrDepVisitorReport;// return the visitor array that contain the data from the database

	}

	/**
	 * @param month
	 * @param ParkName
	 * @return array of UsageReport with the Dates and Number of visitors
	 */
	public static UsageReport[] DepUsageReport(String month, String ParkName) {
		UsageReport[] arr = new UsageReport[numOfUsageReport()];
		Statement stmt;
		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Pdate,VisitorsNumber FROM usagerep WHERE Pmonth ='" + month
					+ "'AND ParkName='" + ParkName + "'AND Vfull='No'");// query
			while (rs.next()) {// get the data from the DB and save in the order array
				arr[i++] = (new UsageReport(rs.getString(1)));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return arr;// return the visitor array that contain the data from the database
	}

	/**
	 * @return number of rows in the usagerep database table
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
	 * @param ParkName
	 * @param month
	 * @param Type
	 * @return OrderVisitorsReport2(same as 1 but using 2 to separate between Casual
	 *         and Order) with the sum of visitors number
	 */
	public static OrderVisitorsReport2 OrderVisitorsReport_UsageRep(String ParkName, String month, String Type) {
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
	 * @param ParkName
	 * @param month
	 * @param Type
	 * @return OrderVisitorsReport with the sum of the visitors number using orders
	 *         in the application.
	 */
	public static OrderVisitorsReport OrderVisitorsReport_Order(String ParkName, String month, String Type) {
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
	 * @param ParkName
	 * @param Month
	 * @param Type
	 * @return array of OrderVisitorsReport with the data of the type of the
	 *         visitor, Date of visit and the number of visitors.
	 */
	public static OrderVisitorsReport[] depNumberVisitorsReport(String ParkName, String Month, String Type) {
		OrderVisitorsReport[] arrOrder = new OrderVisitorsReport[numOfOrderComplete()];
		Statement stmt;
		ResultSet rs;
		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			if (Type.contains("All")) {
				rs = stmt.executeQuery("SELECT UserType,VisitDate,visitorsNum FROM orders_complete WHERE MonthName='"
						+ Month + "' AND EntryStatus='Approved' AND park='" + ParkName + "'");
			} else {
				rs = stmt.executeQuery("SELECT UserType,VisitDate,visitorsNum FROM orders_complete WHERE MonthName='"
						+ Month + "' AND EntryStatus='Approved' AND park='" + ParkName + "' AND UserType='" + Type
						+ "'");
			}
			while (rs.next()) {// get the data from the DB and save in the order array
				arrOrder[i] = (new OrderVisitorsReport(rs.getString(1), rs.getString(2), rs.getString(3)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return arrOrder;
	}

	/**
	 * @return number of rows in the database of orders_complete table.
	 */
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

	/**
	 * @return number of rows in the database of orders table.
	 */
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

}
