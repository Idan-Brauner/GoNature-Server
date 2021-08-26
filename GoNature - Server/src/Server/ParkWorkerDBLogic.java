package Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.org.apache.bcel.internal.generic.Select;

import entity.Order;
import entity.Visitor;

/**Description of ParkWorkerDBLogic 
 * Class that contains all the queries of the Park Worker that he need to take to check various action at the park entrance
 * @author Or
 */
public class ParkWorkerDBLogic {

	/**Description of numOfRows
	 * function that execute query that sum up all the rows that exist in orders table 
	 * @return  count
	 */
	public static int numOfRows() {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT * FROM orders;");// query
			while (res.next()) {
				count++;// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	/**Description of DisplayVisitorsApproval
	 * function execute query that take data from orders table to display them on the table in VisitorsEntranceController
	 * @param msg 
	 * function gets number of park as parameter
	 * @return
	 * function return the array approveVisitor with the data that pull out from orders table
	 */
	public static Order[] DisplayVisitorsForApproval(Object msg) {
		Order[] approveVisitor = new Order[numOfRows()];// create new Order array with the number of rows in the DB
		String ParkID = (String) msg;
		Statement stmt;
		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT MemberID,visitDate,visitorsNum,visitHour,Park,EntryStatus FROM orders WHERE park ='"
							+ ParkID + "'");// query
			while (rs.next()) {// get the data from the DB and save in the Order array
				approveVisitor[i] = (new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return approveVisitor;// return the visitor array that contain the data from the database
	}

	/**Description of CheckIfVisitorExist
	 * function execute query that take MemberID from orders table 
	 * @param visitorID
	 * @return visitor member id
	 */
	public static String CheckIfVisitorExist(String visitorID) {
		String id = null;
		Statement stmt;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MemberID FROM orders Where MemberID = '" + visitorID + "'");

			while (rs.next())
				id = rs.getString(1);

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;

	}

	/**Description of CheckIfVisitorExist
	 * function execute query that take the Entry status of the visitor with the id the query gets from orders table 
	 * @param visitorID
	 * @return - Entry status of the visitor if he approved to enters the park or not 
	 */
	public static String CheckIfVisitorApproved(String visitorID) {
		String id = null;
		int i;
		Statement stmt;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT EntryStatus FROM orders Where MemberID = '" + visitorID + "'");

			while (rs.next()) {
				id = rs.getString(1);
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;

	}

	/**Description of ApprovalVisitor
	 * function execute query that update the entry status of the visitor with the ID that sends
	 * to the function from 'UnApproved' to 'Approved' and update the column EntryStatus in DB 
	 * @param visitorID - the ID sent from ApproveController when the park worker write the ID 
	 * 					  and send it to approve
	 * @return - true if the update succeeded or false if it failed
	 */
	public static boolean ApprovalVisitor(String visitorID) {

		try {
			PreparedStatement preparedStmt = EchoServer.con
					.prepareStatement("UPDATE orders SET EntryStatus = 'Approved' WHERE MemberID = ?");
			preparedStmt.setString(1, visitorID);
			preparedStmt.executeUpdate(); // The number of lines effected.

			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**Description of AddVisitorsNumberWithOrder
	 * function execute query that sum all the visitors number that exist in orders table
	 * that their Entry status is 'Approved' and their park is the park that send to as parameter to the function
	 * @param msg - get Park name to filter the tale by the park name
	 * @return - parameter count that contains all the visitors with order that entered the park
	 */
	public static int AddVisitorsNumberWithOrder(String msg) {
		String ParkID = (String) msg;
		Statement stmt;
		int count = 0;

		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT visitorsNum FROM orders WHERE EntryStatus='Approved' and park = '" + ParkID + "'");

			while (rs.next()) {
				count += rs.getInt(1);

			}

			rs.close();

			return count;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	/**Description of ShowCurrentVisitorsNumberinPark
	 * function execute query that take from visitor_number table the number of visitors in the park
	 * from the park that send as parameter to the function
	 * @param ParkID - the parameter filter the table by the park name
	 * @return - currentNum, the total number of visitor that exist in the park at the the moment
	 */
	public static int ShowCurrentVisitorsNumberinPark(String ParkID) {

		int currentNum = 0;
		try {
			Statement stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT VisitorsNumber FROM visitor_number WHERE ParkName = '" + ParkID + "'");

			if (rs.next()) {
				currentNum = rs.getInt(1);

			}

			rs.close();

			return currentNum;
		} catch (SQLException e) {
			e.printStackTrace();

			return 0;
		}
	}

	/**Description of UpdateVisitorsNumber
	 * function execute query that update visitor number in visitor_number table to be the parameter CurrentNum that send to the function
	 * where the park name is the name of park that send to the function
	 * @param CurrentNum - the new current number of  visitors that in the park right now
	 * @param ParkID - the park name that the visitors number is updated in him
	 * @return true if succeed to update or false if failed
	 */
	public static boolean UpdateVisitorsNumber(String CurrentNum, String ParkID) {

		int currentNum = (int) Integer.valueOf(CurrentNum);
		try {
			PreparedStatement preparedStmt = EchoServer.con
					.prepareStatement("UPDATE visitor_number SET VisitorsNumber = '" + currentNum
							+ "'  WHERE ParkName = '" + ParkID + "'");

			preparedStmt.execute(); // The number of lines effected.

			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			
			e.printStackTrace();

		}
		return false;
	}

	/**Description of UpdateVisitorsNumber
	 * function execute query that take all the data from visitor table where the
	 * member ID is the ID that send in the function 
	 * @param memberID 
	 * @return - parameter visID from Visitor type that contains all the data of the visitor the member id that chosen
	 */ 
	public static Visitor CheckIfCasualVisitorExist(String memberID) {

		try {
			Visitor visID = null;
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM visitor Where ID = '" + memberID + "'");

			if (resultSet.next()) {
				visID = (new Visitor(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7),
						resultSet.getString(8)));
			}
			return visID;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**Description of EnterVisitorsWithMemberID
	 * function execute query that update the new current visitors number in visitor_number table  
	 * @param Park - the park name that sent to the function to search by it in the table
	 * @param num - the number of casual visitors with member id that sent to the function that we want to add to the existing number in the table
	 * @return - true if the the update succeeded and false if it failed
	 */
	public static boolean EnterVisitorsWithMemberID(String Park, String num) {
		int CasualNum = Integer.valueOf(num);

		try {
			Statement stmt = EchoServer.con.createStatement();
			stmt.executeUpdate("UPDATE visitor_number SET VisitorsNumber = VisitorsNumber + '" + CasualNum
					+ "' WHERE ParkName = '" + Park + "'");

			return true;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return false;
	}

	/**Description of AddCasualVisitors
	 * function execute query that update the new current visitors number in visitor_number table  
	 * @param Park - the park name that sent to the function to search by it in the table
	 * @param num - the number of casual visitors without member id that sent to the function that we want to add to the existing number in the table
	 * @return - true if the the update succeeded and false if it failed
	 */
	public static boolean AddCasualVisitors(String Park, String num) {
		int CasualNum = Integer.parseInt(num);
		try {
			PreparedStatement preparedStmt = EchoServer.con
					.prepareStatement("UPDATE visitor_number SET VisitorsNumber = VisitorsNumber + '" + CasualNum
							+ "' WHERE ParkName = '" + Park + "'");

			preparedStmt.executeUpdate(); // The number of lines effected.

			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**Description of AddCasualVisitors
	 * function execute query that update the new current visitors number in visitor_number table  
	 * @param Park - the park name that sent to the function to search by it in the table
	 * @param num - the number of visitors that sent to the function that we want to decrease from the existing number in the table
	 * @return - true if the the update succeeded and false if it failed
	 */
	public static boolean ExitingVisitors(String Park, String num) {
		try {
			PreparedStatement preparedStmt = EchoServer.con
					.prepareStatement("UPDATE visitor_number SET VisitorsNumber = VisitorsNumber - '" + num
							+ "' WHERE ParkName = '" + Park + "'");

			preparedStmt.execute(); // The number of lines effected.

			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**Description of GetCurrentVisitorsNumber
	 * function execute query that take all the data from visitor_number
	 * where the park name is name of the parameter that send into the function
	 * @param park - name of the park that we search in visitor_number table
	 * @return - parameter o from Order type that contains all the data that has been taken by the query 
	 */
	public static Order GetCurrentVisitorsNumber(String park) {
		Order o = null;

		try {
			String query = "SELECT * FROM visitor_number WHERE ParkName = '" + park + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				o = (new Order(resultSet.getString(1), resultSet.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**Description of GetVisitorsNumberForSpecificID
	 * function execute query that take the number of visitors from orders table 
	 * where the member id is the equal to the parameter that sent into the function
	 * @param id - sent in to the function to search by the ID the number of visitors
	 * @return - num, the parameter that contains the number of visitors
	 */
	public static int GetVisitorsNumberForSpecificID(String id) {
		int num = 0;

		try {
			String query = "SELECT visitorsNum FROM orders WHERE MemberID = '" + id + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				num = resultSet.getInt(1);
			}
			return num;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}

	/**Description of GetParkLimit
	 * function execute query that take from visitorlimit table the number defined as the limit of visitors that can enter the park
	 * the number took where the park name is equal to the parameter park that sent into the function and the Status is 'approve'
	 * @param park - parameter that helps to filter the table as we need
	 * @return - limit, the maximize number of visitors that can enter the park
	 */
	public static int GetParkLimit(String park) {
		int limit = 0;
		try {
			String query = "SELECT LimitNum FROM visitorlimit where ParkName = '" + park + "' AND Status='approve'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				limit = resultSet.getInt(1);
			}
			return limit;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return limit;
	}

	/**Description of DisplayCurrentVisitorsNumberInPark
	 * function execute query that take current visitors number from visitor_number table
	 * @param ParkID
	 * @return - the current visitors number in the specific park that we need
	 */ 
	public static int DisplayCurrentVisitorsNumberInPark(String ParkID) {

		Statement stmt;
		int count = 0;

		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT visitorsNumber FROM visitor_number WHERE ParkName = '" + ParkID + "'");

			while (rs.next()) {
				count += rs.getInt(1);

			}

			rs.close();

		} catch (SQLException e) {
			
			e.printStackTrace();
		}

		return count;

	}

	/**Description of UpdateVisitorCasualData
	 * function execute query that insert into new row in usagerep table with that parameters sent to the function
	 * @param Date
	 * @param Hour
	 * @param ParkName
	 * @param Visitornum
	 * @return true if the insert succeeded or false if failed
	 */
	public static boolean UpdateVisitorCasualData(String Date, String Hour, String ParkName, String Visitornum) {
		String cmdArr[] = Date.toString().split("/");
		String MonthNum = null;
		if (cmdArr[1].contains("01")) {
			MonthNum = "January";
		}
		if (cmdArr[1].contains("02")) {
			MonthNum = "February";
		}
		if (cmdArr[1].contains("03")) {
			MonthNum = "March";
		}
		if (cmdArr[1].contains("04")) {
			MonthNum = "April";
		}
		if (cmdArr[1].contains("05")) {
			MonthNum = "May";
		}
		if (cmdArr[1].contains("06")) {
			MonthNum = "June";
		}
		if (cmdArr[1].contains("07")) {
			MonthNum = "July";
		}
		if (cmdArr[1].contains("08")) {
			MonthNum = "August";
		}
		if (cmdArr[1].contains("09")) {
			MonthNum = "September";
		}
		if (cmdArr[1].contains("10")) {
			MonthNum = "October";
		}
		if (cmdArr[1].contains("11")) {
			MonthNum = "November";
		}
		if (cmdArr[1].contains("12")) {
			MonthNum = "December";
		}
		try {
			PreparedStatement preparedStmt = EchoServer.con.prepareStatement(
					"INSERT INTO usagerep(ParkName, VisitorsNumber, Phour, Pdate, Pmonth) VALUES (?,?,?,?,?) ");

			preparedStmt.setString(1, ParkName);
			preparedStmt.setString(2, Visitornum);
			preparedStmt.setString(3, Hour);
			preparedStmt.setString(4, Date);
			preparedStmt.setString(5, MonthNum);

			preparedStmt.execute(); // The number of lines effected.

			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
	}

	/**Description of UpdateFullVisitorCasualData
	 * function execute query that update the column Vfull in usagerep table with the parameters 
	 * that sent into the function if the park was full at a specific date
	 * @param Date
	 * @param Hour
	 * @param ParkName
	 * @return - true if the update succeeded or false if failed  
	 */
	public static boolean UpdateFullVisitorCasualData(String Date, String Hour, String ParkName) {
		try {
			PreparedStatement preparedStmt = EchoServer.con
					.prepareStatement("UPDATE usagerep SET Vfull='Yes' WHERE Phour='" + Hour + "' AND Pdate = '" + Date
							+ "' AND ParkName = '" + ParkName + "'");

			preparedStmt.execute(); // The number of lines effected.
			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
	}

	/**Description of MoveOrderIntoUsageRep
	 * function execute query that take data from orders table where the park name and member id are equals
	 * to the parameters that sent into the function and where the Entry status is 'Approved'
	 * @param ParkID
	 * @param MemberID
	 * @return - array that called approveVisitor that contains the data that took by the query
	 */
	public static Order[] MoveOrderIntoUsageRep(String ParkID, String MemberID) {
		Order[] approveVisitor = new Order[numOfRows()];// create new Order array with the number of rows in the DB
		Statement stmt;
		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT Park,visitorsNum,visitHour,visitDate,MonthName FROM orders WHERE park ='"
							+ ParkID + "' AND EntryStatus='Approved' AND MemberID = '" + MemberID + "'");// query
			while (rs.next()) {// get the data from the DB and save in the Order array
				approveVisitor[i] = (new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5)));
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return approveVisitor;// return the visitor array that contain the data from the database
	}

	/**Description of OrderspassTheLimit
	 * function execute query that take data from orders table where the member id is equals to 
	 * parameter that sent into the function
	 * @param memberid
	 * @return - parameter o from Order type that contains all the data that took by the query
	 */
	public static Order OrderspassTheLimit(String memberid) {

		Statement stmt;
		Order o = null;

		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT visitDate,visitHour,visitorsNum,MonthName FROM orders WHERE MemberID = '" + memberid + "'");

			if (rs.next()) {
				o = new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));

			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	/**Description of WriteTheLimitInTheUsageRep
	 * function execute query that insert data that get by the parameters that get the function into usagerep table
	 * @param ParkName
	 * @param Phour
	 * @param Pdate
	 * @param Pmonth
	 * @return - true if the insert succeeded or false if failed
	 */
	public static boolean WriteTheLimitInTheUsageRep(String ParkName, String Phour, String Pdate, String Pmonth) {
		try {
			String query = "insert into usagerep(ParkName, VisitorsNumber, Phour, Pdate, Pmonth, Vfull) values (?, ?, ?,?,?,?)";

			PreparedStatement preparedStmt = EchoServer.con.prepareStatement(query);
			preparedStmt.setString(1, ParkName);
			preparedStmt.setString(2, "0");
			preparedStmt.setString(3, Phour);
			preparedStmt.setString(4, Pdate);
			preparedStmt.setString(5, Pmonth);
			preparedStmt.setString(6, "Yes");
			preparedStmt.execute();

			preparedStmt.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**Description of MoveOrderIntoOrderComplete
	 * function execute query that insert data that taken from orders table and get by the parameters that get the function into orders_complete table
	 * @param msg
	 * @return - true if the insert action succeeded or false if failed
	 */
	public static boolean MoveOrderIntoOrderComplete(Object msg) {
		String[] cmdArr = (String[]) msg.toString().split(" ");
		try {
			PreparedStatement preparedStmt = EchoServer.con.prepareStatement(
					"INSERT INTO orders_complete(MemberID,OrderID,park,visitDate,visitHour,visitorsNum,email,UserType,EntryStatus,Price,MonthName,StayTime) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			preparedStmt.setString(1, cmdArr[1]);
			preparedStmt.setString(2, cmdArr[11]);
			preparedStmt.setString(3, cmdArr[2]);
			preparedStmt.setString(4, cmdArr[3]);
			preparedStmt.setString(5, cmdArr[4]);
			preparedStmt.setString(6, cmdArr[5]);
			preparedStmt.setString(7, cmdArr[6]);
			preparedStmt.setString(8, cmdArr[7]);
			preparedStmt.setString(9, "Approved");
			preparedStmt.setString(10, cmdArr[8]);
			preparedStmt.setString(11, cmdArr[9]);
			preparedStmt.setString(12, "4");
			preparedStmt.execute();
			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**Description of MoveOrderIntoOrderComplete
	 * function execute query that take all the data from orders table where Entry Status is 'Approved' and
	 * the member id is equal to the parameter the function gets
	 * @param MemberID
	 * @return - parameter res from Order type that contains all the data that took by the query
	 */
	public static Order TakeOrdersTableData(String MemberID) {
		Order res = null;
		Statement stmt;

		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM orders WHERE EntryStatus = 'Approved' and MemberID = '" + MemberID + "' ");
			if (rs.next()) {
				res = new Order(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12));

			}

			rs.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**Description of GetUserType
	 * function execute query that take user type (visitor or guide) from visitor table where the ID is equals to the parameter the function gets
	 * @param id
	 * @return - String userType that contains the user type
	 */
	public static String GetUserType(String id) {
		String userType = null;
		try {
			String query = "SELECT UserType FROM visitor where ID = '" + id + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				userType = resultSet.getString(1);
			}
			return userType;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userType;
	}

}