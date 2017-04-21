package messaging;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.CookieRelated;
import common.DBConnection;
import messaging.ComposeMsgsDAO;
import messaging.Msg;

/**
 * Servlet implementation class ComposeMsgs
 */
@WebServlet("/ComposeMsgs")
public class ComposeMsgs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ComposeMsgs() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Cookie cookie = CookieRelated.getCookie(request);
		
		if (cookie != null) {
			
			String userID = cookie.getValue();
			
			if (request.getAttribute("composebrandnewmsg") != null) {
				if (request.getAttribute("listingID") !=  null) {
					int listingID = (int) request.getAttribute("listingID");
					Connection c = DBConnection.getConnection();
					//check if listing exists and ensure user is not owner of listing
					if (c != null) {
						Msg msg = new Msg();
						msg.setMsg(request.getParameter("newcontactmsg"));
						msg.setBuyerID(userID);
						msg.setListingID(listingID);
						ComposeMsgsDAO.sendBrandNewMsgtoDB(c, msg);
						int chainID = msg.getChainID();
						if (!(msg.getSQLErrorState())) {
							if (msg.getUploadSuccess()) {
								//session.setAttribute("msgerror", "Message sent successfully!"); //maybe include html that's a button to messages/buy/chainid
								//session.setAttribute("amsgofsorts", "Message sent successfully!");
								response.sendRedirect("/messages/buy/" + chainID);
								//session.setAttribute("listingID", listingID);
								//response.sendRedirect(Path.getRoot() + "listings/" + listingID);
							}
							else {
								String errormsg = msg.getErrorMsg();
								if (errormsg.equals("chainexists")) {
									//session.setAttribute("msgerror", "Please use the existing chain for further contact."); //maybe include html that's a button to messages/buy/chainid
									//session.setAttribute("listingID", listingID);
									//response.sendRedirect(Path.getRoot() + "listings/" + listingID);
									session.setAttribute("amsgofsorts", "Please use the existing chain for further contact.");
									response.sendRedirect("/messages/buy/" + chainID);
								}
								else if (errormsg.equals("cantbuyfromhimself")) {
									session.setAttribute("msgerror", "You can't contact yourself!");
									session.setAttribute("listingID", listingID);
									response.sendRedirect("/listings/" + listingID);
								}
								else {
									response.sendError(404);
								}
							}
						}
						else {
							request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
						}
					}
					else {
						request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
					}
				}
				else {
					response.sendError(404);
				}
			}
			else {
				response.sendError(404);
			}
			
		}
		else {
			response.sendRedirect("/login");
		}
		
	}

}
