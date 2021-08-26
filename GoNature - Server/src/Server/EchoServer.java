// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package Server;

import ocsf.server.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import entity.Order;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * 
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 * 
 */

public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	// final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 * 
	 */
	public static Connection con;

	public EchoServer(int port) {
		super(port);
		connectToDB();// connect to the database
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 * @param
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println((String) msg);
		String cmdArr[] = msg.toString().split(" ");
		System.out.println("Message received: " + msg + " from " + client);

		////////////////////// START OF LOGIN FUNCTIONS
		if (((String) msg).contains("LoginQuery") || ((String) msg).contains("QuickEnter")) {
			switch (cmdArr[0]) {
			case "LoginQueryUserExist":
				try {
					String ID = cmdArr[1];
					String password = cmdArr[2];
					client.sendToClient(LoginDBLogic.LoginQueryUserExist(ID, password));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "UserLoginQueryPasswordCorrect":
				try {
					String ID = cmdArr[1];
					String password = cmdArr[2];
					client.sendToClient(LoginDBLogic.UserLoginQueryPasswordCorrect(ID, password));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "WorkerLoginQueryPasswordCorrect":
				try {
					String ID = cmdArr[1];
					String password = cmdArr[2];
					client.sendToClient(LoginDBLogic.WorkerLoginQueryPasswordCorrect(ID, password));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "QuickEnterQueryUserExist":
				try {
					String ID = cmdArr[2];
					System.out.println(ID);
					client.sendToClient(LoginDBLogic.QuickEnterQueryUserExist(ID));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/////////////////////// END OF LOGIN FUNCTIONS

/////////////////////// START OF ORDER FUNCTIONS

		if (cmdArr[0].equals("CreateOrder")) {
			try {
				client.sendToClient(OrderDBLogic.makeNewOrder((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CheckOrderExist")) {
			try {
				client.sendToClient(OrderDBLogic.checkOrderExist((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CheckOrderExistWaitingList")) {
			try {
				client.sendToClient(OrderDBLogic.checkOrderExistWaitingList((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("SaveChangesEdit")) {
			try {
				client.sendToClient(OrderDBLogic.saveChangesEdit((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("CancelOrder")) {
			try {
				client.sendToClient(OrderDBLogic.cancelOrder((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("GetOrderID")) {
			try {
				client.sendToClient(OrderDBLogic.getOrderID((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("UpdateOrderID")) {
			try {
				client.sendToClient(OrderDBLogic.updateOrderID((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("UpdateMemberID")) {
			try {
				client.sendToClient(OrderDBLogic.updateMemberID((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CheckReservedVisitsForDate")) {
			try {
				client.sendToClient(OrderDBLogic.checkReservedVisitsForDate((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CheckCurrParkLimit")) {
			try {
				client.sendToClient(OrderDBLogic.checkCurrParkLimit((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("GetMemberID")) {
			try {
				client.sendToClient(OrderDBLogic.getMemberID((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("SetMemberID")) {
			try {
				client.sendToClient(OrderDBLogic.setMemberID((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("GetEntryPrice")) {
			try {
				client.sendToClient(OrderDBLogic.getEntryPrice((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("InsertToCancelTable")) {
			try {
				client.sendToClient(OrderDBLogic.insertToCancelTable((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("InsertWaitingList")) {
			try {
				client.sendToClient(OrderDBLogic.insertWaitingList((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("SearchInWaitingList")) {
			try {
				client.sendToClient(OrderDBLogic.searchInWaitingList((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CheckForParkDiscount")) {
			try {
				client.sendToClient(OrderDBLogic.checkForParkDiscount((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CreateWaitingListNotifyUsr")) {
			try {
				client.sendToClient(OrderDBLogic.createWaitingListNotifyUsr((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CreateConfirmNotifyUsr")) {
			try {
				client.sendToClient(OrderDBLogic.createConfirmNotifyUsr((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CheckForUserMsg")) {
			try {
				client.sendToClient(OrderDBLogic.checkForUserMsg((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("RemoveWaitingListNotifyUsr")) {
			try {
				client.sendToClient(OrderDBLogic.removeWaitingListNotifyUsr((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("RemoveConfirmNotifyUsr")) {
			try {
				client.sendToClient(OrderDBLogic.removeConfirmNotifyUsr((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("RemoveFromWaitingList")) {
			try {
				client.sendToClient(OrderDBLogic.removeFromWaitingList((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CheckForExpiredWaitingList")) {
			try {
				client.sendToClient(OrderDBLogic.checkForExpiredWaitingList());
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("SearchInCanceledOrders")) {
			try {
				client.sendToClient(OrderDBLogic.searchInCanceledOrders((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("OverrideCancelOrders")) {
			try {
				client.sendToClient(OrderDBLogic.overrideCancelOrders((String) msg));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
///////////////////// END OF ORDER FUNCTIONS
		//////////////////// START OF SERVICE AGNT & PARK MNGR & DEP MNGR

		if (cmdArr[0].equals("ParkOrderVisitorsReport_UsageRep")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.ParkOrderVisitorsReport_UsageRep(cmdArr[1],
						cmdArr[2], cmdArr[3]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("ParkOrderVisitorsReport_Order")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.ParkOrderVisitorsReport_Order(cmdArr[1],
						cmdArr[2], cmdArr[3]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("depNumberVisitorsReport")) {
			try {
				client.sendToClient(DepManagerDBLogic.depNumberVisitorsReport(cmdArr[1], cmdArr[2], cmdArr[3]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("OrderVisitorsReport_UsageRep")) {
			try {
				client.sendToClient(DepManagerDBLogic.OrderVisitorsReport_UsageRep(cmdArr[1], cmdArr[2], cmdArr[3]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("OrderVisitorsReport_Order")) {
			try {
				client.sendToClient(DepManagerDBLogic.OrderVisitorsReport_Order(cmdArr[1], cmdArr[2], cmdArr[3]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("ReportNumVisitorsShow")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.ShowReportNumVisitors(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("VisitorsReport")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.ShowVisitorsReport(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("VisitorsReport_Visitor")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.ShowVisitorsReport_Visitor(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("VisitorsReport_Guide")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.ShowVisitorsReport_Guide(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("VisitorsLimitation")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.UpdateVisitorLimitation(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("numOfLimitRequests")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.numOfLimitRequests(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("numOfVisitors")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.numOfVisitors(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CancellationReportCancelPark")) {
			try {
				client.sendToClient(DepManagerDBLogic.ShowCancellationReport_Park_cancel(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CancellationReportlossPark")) {
			try {
				client.sendToClient(DepManagerDBLogic.ShowCancellationReport_Park_loss(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("numOfCancelAndLoss")) {
			try {
				client.sendToClient(DepManagerDBLogic.numOfCancelAndLoss(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("VisitorsInParkNowPark")) {
			try {
				client.sendToClient(DepManagerDBLogic.VisitorsInParkNowPark(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("depVisitorsReport")) {
			try {
				client.sendToClient(DepManagerDBLogic.depVisitorsReport(cmdArr[1], cmdArr[2], cmdArr[3]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("ReportUsage")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.UsageReport(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("numOfVisitorsId")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.numOfVisitorsId((String) msg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("addVisitor")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.addVisitor((String) msg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("numOfLimitInt")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.numOfLimitInt(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("numOfRowsDiscount")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.numOfRowsDiscount());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("UpdateApprovelistVisitor")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.UpdateApprovelistVisitor());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("UpdateApprovelistDiscount")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.UpdateApprovelistDiscount());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("ApprovedVisitorLimitation")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.ApprovedVisitorLimitation(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("DeclinedVisitorLimitation")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.DeclinedVisitorLimitation(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("ApprovedDiscount")) {
			try {
				client.sendToClient(
						ServiceAgentAndParkManagerDBLogic.ApprovedDiscount(cmdArr[1], cmdArr[2], cmdArr[3]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("DeclinedDiscount")) {
			try {
				client.sendToClient(
						ServiceAgentAndParkManagerDBLogic.DeclinedDiscount(cmdArr[1], cmdArr[2], cmdArr[3]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("DiscountParkManager")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.DiscountParkManager(cmdArr));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("CheckDateDiscountParkManager")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.CheckDateDiscount(cmdArr));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("DEPReportUsage")) {
			try {
				client.sendToClient(DepManagerDBLogic.DepUsageReport(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

////////////////////END OF SERVICE AGNT & PARK MNGR & DEP MNGR

		//////////////////// START OF PARK WORKER FUNCTIONS
		if ((cmdArr[0]).equals("UserTypeForShowingPrice")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.GetUserType(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if ((cmdArr[0]).equals("SendApproveVisitorToOrderComplete")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.MoveOrderIntoOrderComplete((String) msg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if ((cmdArr[0]).equals("TakeDataFromOrdersTable")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.TakeOrdersTableData(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if ((cmdArr[0]).equals("DisplayApprovalTable")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.DisplayVisitorsForApproval(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("CheckExistVisitorID")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.CheckIfVisitorExist(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("CheckApprovedVisitorID")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.CheckIfVisitorApproved(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("ApproveVisitorID")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.ApprovalVisitor(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("AddVisitorsNumberWithOrder")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.AddVisitorsNumberWithOrder(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("ShowCurrentVisitorsNumberInPark")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.ShowCurrentVisitorsNumberinPark(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("UpdateVisitorNumber")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.UpdateVisitorsNumber(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("AddingCasualVisitors")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.AddCasualVisitors(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("ExitingVisitors")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.ExitingVisitors(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("EnterVisitorsWithMemberID")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.EnterVisitorsWithMemberID(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if ((cmdArr[0]).equals("CheckIFExistCasualVisitorMemberID")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.CheckIfCasualVisitorExist(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("CheckVisitorsNumberInPark")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.GetCurrentVisitorsNumber(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (cmdArr[0].equals("CheckIfReachLimit")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.GetParkLimit(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (cmdArr[0].equals("GetVisitorsNumberforID")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.GetVisitorsNumberForSpecificID(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (cmdArr[0].equals("CurrentVisitorsNumberInPark")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.DisplayCurrentVisitorsNumberInPark(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if ((cmdArr[0]).equals("UpdateVisitorCasualData")) {
			try {
				client.sendToClient(
						ParkWorkerDBLogic.UpdateVisitorCasualData(cmdArr[1], cmdArr[2], cmdArr[3], cmdArr[4]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("UpdateFullVisitorCasualData")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.UpdateFullVisitorCasualData(cmdArr[1], cmdArr[2], cmdArr[3]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("MoveOrderIntoUsageRep")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.MoveOrderIntoUsageRep(cmdArr[1], cmdArr[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("OrderspassTheLimit")) {
			try {
				client.sendToClient(ParkWorkerDBLogic.OrderspassTheLimit(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((cmdArr[0]).equals("WriteTheLimitInTheUsageRep")) {
			try {
				client.sendToClient(
						ParkWorkerDBLogic.WriteTheLimitInTheUsageRep(cmdArr[1], cmdArr[2], cmdArr[3], cmdArr[4]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmdArr[0].equals("ParkManagerVisitorsInParkNow")) {
			try {
				client.sendToClient(ServiceAgentAndParkManagerDBLogic.VisitorsInParkNow(cmdArr[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // ServiceAgentAndParkManagerDBLogic.VisitorsInParkNow(strArray[2])

		}

////////////////////END OF PARK WORKER FUNCTIONS

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	public static void connectToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/gonature?serverTimezone=IST", "root", "idan1993B");
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
}
