package messaging;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.CookieRelated;
import common.DBConnection;
import messaging.MessagingDAO;
import messaging.Msg;

/**
 * Servlet implementation class GetMsgs
 */
@WebServlet("/GetMsgs")
public class GetMsgs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMsgs() {
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
		
		if (session.getAttribute("amsgofsorts") != null) {
			request.setAttribute("amsgofsorts", session.getAttribute("amsgofsorts"));
			session.removeAttribute("amsgofsorts");
		}
		
		if (cookie != null) {
			
			String userID = cookie.getValue();
			Connection c = DBConnection.getConnection();
			Connection d = DBConnection.getConnection();
			Connection e = DBConnection.getConnection();
			
			if (c != null && d != null && e != null) {
				if (request.getAttribute("chainID") != null) {
					
					int chainID = (int) request.getAttribute("chainID");
					
					if (request.getAttribute("genabuychain") != null) {
						
						if (MessagingDAO.userChainRelationshipExists(c, userID, chainID, "buyer").equals("no")) {
							try {
								d.close();
							}
							catch (Exception x) {
								//do nothing
							}
							try {
								e.close();
							}
							catch (Exception x) {
								//do nothing
							}
							response.sendError(403);
						}
						else if (MessagingDAO.userChainRelationshipExists(d, userID, chainID, "buyer").equals("sqlerror")) {
							try {
								e.close();
							}
							catch (Exception x) {
								//do nothing
							}
							request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
						}
						else {
							ArrayList<Msg> msgs = MessagingDAO.getABuyChain(e, userID, chainID);
							if (msgs != null) {
								if (msgs.size() != 0) {
									response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
									request.setAttribute("somemsgs", msgs);
									request.getRequestDispatcher("/WEB-INF/chainOfMsgs.jsp").forward(request, response);
								}
								else {
									response.sendError(404);
								}
							}
							else {
								request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
							}
						}
					}
					else if (request.getAttribute("genasellchain") != null) {
						
						if (MessagingDAO.userChainRelationshipExists(c, userID, chainID, "seller").equals("no")) {
							try {
								d.close();
							}
							catch (Exception x) {
								//do nothing
							}
							try {
								e.close();
							}
							catch (Exception x) {
								//do nothing
							}
							response.sendError(403);
						}
						else if (MessagingDAO.userChainRelationshipExists(d, userID, chainID, "seller").equals("sqlerror")) {
							try {
								e.close();
							}
							catch (Exception x) {
								//do nothing
							}
							request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
						}
						else {
							ArrayList<Msg> msgs = MessagingDAO.getASellChain(e, userID, chainID);
							if (msgs != null) {
								if (msgs.size() != 0) {
									response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
									request.setAttribute("somemsgs", msgs);
									request.getRequestDispatcher("/WEB-INF/chainOfMsgs.jsp").forward(request, response);
								}
								else {
									response.sendError(404);
								}
							}
							else {
								request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
							}
						}
					}
					else {
						try {
							c.close();
						}
						catch (Exception x) {
							//do nothing
						}
						try {
							d.close();
						}
						catch (Exception x) {
							//do nothing
						}
						try {
							e.close();
						}
						catch (Exception x) {
							//do nothing
						}
						response.sendError(404);
					}
				}
				else {
					try {
						c.close();
					}
					catch (Exception x) {
						//do nothing
					}
					try {
						d.close();
					}
					catch (Exception x) {
						//do nothing
					}
					try {
						e.close();
					}
					catch (Exception x) {
						//do nothing
					}
					response.sendError(404);
				}
			}
			else {
				try {
					c.close();
				}
				catch (Exception x) {
					//do nothing
				}
				try {
					d.close();
				}
				catch (Exception x) {
					//do nothing
				}
				try {
					e.close();
				}
				catch (Exception x) {
					//do nothing
				}
				request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
			}
		}
		else {
			response.sendRedirect("/login"); //should never happen
		}
		
	}

}
