package Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;

import entity.Order;

public class OrderDBLogic {
	/**
	 * @param msg - Inserting new order to the orders table
	 * @return
	 */
	public static boolean makeNewOrder(String msg) {
		String[] cmdArr = (String[]) msg.toString().split(" ");
		try {
			System.out.println(cmdArr[7]);
			PreparedStatement preparedStmt = EchoServer.con.prepareStatement(
					"INSERT INTO orders(MemberID,OrderID,park,visitDate,visitHour,visitorsNum,email,UserType,EntryStatus,Price,MonthName,StayTime) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			preparedStmt.setString(1, cmdArr[1]);
			preparedStmt.setString(2, cmdArr[11]);
			preparedStmt.setString(3, cmdArr[2]);
			preparedStmt.setString(4, cmdArr[3]);
			preparedStmt.setString(5, cmdArr[4]);
			preparedStmt.setString(6, cmdArr[5]);
			preparedStmt.setString(7, cmdArr[6]);
			preparedStmt.setString(8, cmdArr[7]);
			preparedStmt.setString(9, "UnApproved");
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

	/**
	 * @param msg - Inserting an order to the canceled orders table
	 * @return
	 */
	public static boolean insertToCancelTable(String msg) {
		String[] cmdArr = (String[]) msg.toString().split(" ");
		try {

			PreparedStatement preparedStmt = EchoServer.con.prepareStatement(
					"INSERT INTO cancel_loss(TypeCase,MemberID,OrderID,ParkName,VisitDate,VisitorsNum,VisitorType) values(?,?,?,?,?,?,?)");
			preparedStmt.setString(1, "cancel");
			preparedStmt.setString(2, cmdArr[2]);
			preparedStmt.setString(3, cmdArr[1]);
			preparedStmt.setString(4, cmdArr[3]);
			preparedStmt.setString(5, cmdArr[4]);
			preparedStmt.setString(6, cmdArr[6]);
			preparedStmt.setString(7, cmdArr[8]);

			preparedStmt.execute();
			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param msg- Checking the amount of visitors reserved for a specific date.
	 *             Getting the park name date and time. Calculating the reserved
	 *             orders to the park on the specific date with default of 4 hour
	 *             limitation to each visit in the park
	 * @return - returns the number of the amount of visitors on the chosen date and
	 *         time of this park
	 */
	public static int checkReservedVisitsForDate(String msg) {
		int res = 0;
		String[] cmdArr = (String[]) msg.toString().split(" ");
		int currOrderHour = (LocalTime.parse(cmdArr[3]).getHour());
		try {
			String query = "SELECT visitorsNum,visitHour FROM orders WHERE park = '" + cmdArr[1] + "' AND visitDate= '"
					+ cmdArr[2] + "' AND OrderID != '" + cmdArr[4] + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				if (currOrderHour <= LocalTime.parse(resultSet.getString(2)).getHour()
						&& currOrderHour + 4 > LocalTime.parse(resultSet.getString(2)).getHour())
					res += resultSet.getInt(1);
				else if (currOrderHour >= LocalTime.parse(resultSet.getString(2)).getHour()
						&& LocalTime.parse(resultSet.getString(2)).getHour() + 4 > currOrderHour)
					res += resultSet.getInt(1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param msg- Searching an order if it exists in the canceled ordered table
	 * @return returns the member id of the order if there is one
	 */
	public static String searchInCanceledOrders(String msg) {
		String res = null;
		String[] cmdArr = (String[]) msg.toString().split(" ");

		try {
			String query = "SELECT MemberID FROM cancel_loss WHERE MemberID = '" + cmdArr[1] + "' AND ParkName= '"
					+ cmdArr[2] + "' AND VisitDate= '" + cmdArr[3] + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				res = resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param msg- Checking if a user id have an active order in the orders table
	 * @return - Order object if there is an order with the same memeber id in the
	 *         DB
	 */
	public static Order checkOrderExist(String msg) {
		try {
			String[] cmdArr = (String[]) msg.toString().split(" ");
			String query = "SELECT * FROM orders WHERE MemberID = '" + cmdArr[1] + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				Order res = new Order(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7),
						resultSet.getString(8), resultSet.getString(9), resultSet.getString(10),
						resultSet.getString(11), resultSet.getString(12));
				return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param msg- Check if the user have an active order in the waiting list
	 * @return - Order object if there is an order with the same memeber id in the
	 *         DB
	 */
	public static Order checkOrderExistWaitingList(String msg) {
		try {
			String[] cmdArr = (String[]) msg.toString().split(" ");
			String query = "SELECT * FROM waiting_list WHERE MemberID = '" + cmdArr[1] + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				Order res = new Order(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7),
						resultSet.getString(8));
				return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param msg- Getting the current order id- allocating different Ids to each
	 *             order
	 * @return the order id
	 */
	public static int getOrderID(String msg) {
		int res = 0;
		try {
			String query = "SELECT OrderID FROM orders_id";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				res = resultSet.getInt(1);
				return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;

	}

	/**
	 * @param msg- Getting the current member id- allocating different Ids to each
	 *             user
	 * @return the member id
	 */
	public static int getMemberID(String msg) {
		int res = 0;
		try {
			String query = "SELECT MemberID FROM member_id";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				res = resultSet.getInt(1);
				return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param msg- Getting the entry price for a visiting
	 * @return the price
	 */
	public static double getEntryPrice(String msg) {
		int res = 0;
		try {
			String query = "SELECT Price FROM entry_price";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				res = resultSet.getInt(1);
				return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param msg- Searching the limitation for the specific park
	 * @return the limit
	 */
	public static int checkCurrParkLimit(String msg) {
		String[] cmdArr = (String[]) msg.toString().split(" ");
		int res = 0;
		try {
			String query = "SELECT LimitNum FROM visitorlimit WHERE ParkName='" + cmdArr[1] + "' AND Status='approve'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				res = resultSet.getInt(1);
				return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;

	}

	/**
	 * @param msg- Deleting an order forn the waiting list
	 * @return
	 */
	public static boolean removeFromWaitingList(String msg) {
		try {
			String[] cmdArr = (String[]) msg.toString().split(" ");
			String query = "DELETE FROM waiting_list WHERE MemberID = '" + cmdArr[1] + "'";
			Statement statement = EchoServer.con.createStatement();
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @return- Returning an array of Orders that need to be deleted if the system's
	 *          timer passed the order time
	 */
	public static Order[] checkForExpiredWaitingList() {

		Order[] res = new Order[numOfRowsWL()];
		int i = 0;
		try {
			String query = "SELECT * FROM waiting_list";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				res[i++] = new Order(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7),
						resultSet.getString(8), resultSet.getString(9), resultSet.getString(10),
						resultSet.getString(11), resultSet.getString(12));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param msg- Deleting an order fron the orders db
	 * @return
	 */
	public static boolean cancelOrder(String msg) {
		try {
			String[] cmdArr = (String[]) msg.toString().split(" ");
			String query = "DELETE FROM orders WHERE MemberID = '" + cmdArr[1] + "'";
			Statement statement = EchoServer.con.createStatement();
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param msg- adding +1 to the overall order id- making different id for each
	 *             order
	 * @return
	 */
	public static boolean updateOrderID(String msg) {
		try {
			String[] cmdArr = (String[]) msg.toString().split(" ");
			String query = "UPDATE orders_id SET OrderID='" + cmdArr[1] + "'";
			Statement statement = EchoServer.con.createStatement();
			statement.executeUpdate(query);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param msg- Overriding an order if its already exists- prevent multiple
	 *             orders in canceled table
	 * @return
	 */
	public static boolean overrideCancelOrders(String msg) {
		try {
			String[] cmdArr = (String[]) msg.toString().split(" ");
			String query = "UPDATE cancel_loss SET OrderID='" + cmdArr[1] + "' , VisitorsNum= '" + cmdArr[2]
					+ "' WHERE MemberID= '" + cmdArr[3] + "'";
			Statement statement = EchoServer.con.createStatement();
			statement.executeUpdate(query);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param msg- checking which messages the system need to show to the user.
	 *             cnfrmMSG-when an order was made successfully waitingMSG- when an
	 *             order was accepted from the waiting list into the orders DB
	 * @return
	 */
	public static String checkForUserMsg(String msg) {
		String[] cmdArr = (String[]) msg.toString().split(" ");
		try {
			String query = "SELECT cnfrmMSG,waitingMSG from usr_msg WHERE ID= '" + cmdArr[1] + "'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				if (resultSet.getString(1).equals("1"))
					return "ConfirmMsg";
				if (resultSet.getString(2).equals("1"))
					return "WLMsg";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param msg- Check the current park discount amoubt
	 * @return
	 */
	public static String[] checkForParkDiscount(String msg) {
		String[] cmdArr = (String[]) msg.toString().split(" ");
		String[] res = new String[3];
		try {
			String query = "SELECT ActiveDate,FinishDate,Discount from discount WHERE ParkName= '" + cmdArr[1]
					+ "' AND Status= 'approve'";
			Statement statement = EchoServer.con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				res[0] = resultSet.getString(1);
				res[1] = resultSet.getString(2);
				res[2] = resultSet.getString(3);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param msg- Adding +1 to the member id - every member will get a different id
	 * @return
	 */
	public static boolean updateMemberID(String msg) {
		try {
			String[] cmdArr = (String[]) msg.toString().split(" ");
			String query = "UPDATE member_id SET MemberID='" + cmdArr[1] + "'";
			Statement statement = EchoServer.con.createStatement();
			statement.executeUpdate(query);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param msg- turning off the user message of order accepted from waiting list
	 * @return
	 */
	public static boolean removeWaitingListNotifyUsr(String msg) {
		try {
			String[] cmdArr = (String[]) msg.toString().split(" ");
			String query = "DELETE FROM usr_msg WHERE ID= '" + cmdArr[1] + "'";
			Statement statement = EchoServer.con.createStatement();
			statement.executeUpdate(query);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param msg -turning off the user message of order accepted
	 *
	 * @return
	 */
	public static boolean removeConfirmNotifyUsr(String msg) {
		try {
			String[] cmdArr = (String[]) msg.toString().split(" ");
			String query = "DELETE FROM usr_msg WHERE ID= '" + cmdArr[1] + "'";
			Statement statement = EchoServer.con.createStatement();
			statement.executeUpdate(query);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param msg- setting the new member id
	 * @return
	 */
	public static boolean setMemberID(String msg) {
		try {
			String[] cmdArr = (String[]) msg.toString().split(" ");
			String query = "UPDATE visitor SET MemberID='" + cmdArr[1] + "' WHERE ID='" + cmdArr[2] + "'";
			Statement statement = EchoServer.con.createStatement();
			statement.executeUpdate(query);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param msg - Editing the order data
	 * @return
	 */
	public static boolean saveChangesEdit(String msg) {
		String[] cmdArr = (String[]) msg.toString().split(" ");
		try {
			PreparedStatement preparedStmt = EchoServer.con.prepareStatement(
					"UPDATE orders SET park=?,visitDate=?,visitHour=?,visitorsNum=?,email=?,OrderID=? WHERE MemberID=?");
			preparedStmt.setString(1, cmdArr[2]);
			preparedStmt.setString(2, cmdArr[3]);
			preparedStmt.setString(3, cmdArr[4]);
			preparedStmt.setString(4, cmdArr[5]);
			preparedStmt.setString(5, cmdArr[6]);
			preparedStmt.setString(6, cmdArr[11]);
			preparedStmt.setString(7, cmdArr[1]);
			preparedStmt.execute();
			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * @param msg- Inserting an roder to the waitiwng list DB
	 * @return
	 */
	public static boolean insertWaitingList(String msg) {
		String[] cmdArr = (String[]) msg.toString().split(" ");
		try {
			System.out.println(cmdArr[7]);
			PreparedStatement preparedStmt = EchoServer.con.prepareStatement(
					"INSERT INTO waiting_list(MemberID,OrderID,park,visitDate,visitHour,visitorsNum,email,UserType,EntryStatus,Price,MonthName,StayTime) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			preparedStmt.setString(1, cmdArr[1]);
			preparedStmt.setString(2, cmdArr[11]);
			preparedStmt.setString(3, cmdArr[2]);
			preparedStmt.setString(4, cmdArr[3]);
			preparedStmt.setString(5, cmdArr[4]);
			preparedStmt.setString(6, cmdArr[5]);
			preparedStmt.setString(7, cmdArr[6]);
			preparedStmt.setString(8, cmdArr[7]);
			preparedStmt.setString(9, "UnApproved");
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

	/**
	 * @param msg- Creating a message for the user if his order was accepted from
	 *             the waiting list
	 * @return
	 */
	public static boolean createWaitingListNotifyUsr(String msg) {
		String[] cmdArr = (String[]) msg.toString().split(" ");
		try {
			PreparedStatement preparedStmt = EchoServer.con.prepareStatement("INSERT INTO usr_msg values(?,?,?)");
			preparedStmt.setString(1, cmdArr[1]);
			preparedStmt.setString(2, "0");
			preparedStmt.setString(3, "1");
			preparedStmt.execute();
			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param msg- Creating a message for the user if his order was accepted from
	 *             the waiting list
	 * @return
	 */
	public static boolean createConfirmNotifyUsr(String msg) {
		String[] cmdArr = (String[]) msg.toString().split(" ");
		try {
			PreparedStatement preparedStmt = EchoServer.con.prepareStatement("INSERT INTO usr_msg values(?,?,?)");
			preparedStmt.setString(1, cmdArr[1]);
			preparedStmt.setString(2, "1");
			preparedStmt.setString(3, "0");
			preparedStmt.execute();
			preparedStmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @return- gets the amount of orders in the waiting list
	 */
	public static int numOfRowsWL() {
		Statement stmt;
		ResultSet res;
		int count = 0;
		try {
			stmt = EchoServer.con.createStatement();
			res = stmt.executeQuery("SELECT * FROM waiting_list");// query
			while (res.next()) {
				count++;// counter to number of rows in the database
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	/**
	 * @param msg - This query selects the first order in the waiting list which
	 *            needs to be asserted into the order DB. We used to order by ASC in
	 *            the order id in order to select the first order in an queue form
	 * @return
	 */
	public static Order searchInWaitingList(String msg) {
		Order arrOrders = null;// create new visitor array with the number of rows in the DB
		String[] cmdArr = (String[]) msg.toString().split(" ");
		Statement stmt;
		int i = 0;
		try {
			stmt = EchoServer.con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM waiting_list WHERE park= '" + cmdArr[1]
					+ "' AND visitDate= '" + cmdArr[2] + "' ORDER BY OrderID ASC");// query
			if (rs.next()) {// get the data from the DB and save in the visitor array
				arrOrders = (new Order(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12)));

			}
			rs.close();
			return arrOrders;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrOrders;// return the visitor array that contain the data from the database
	}

}
