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

import common.CookieRelated;
import common.DBConnection;
import messaging.Chain;
import messaging.MessagingDAO;

/**
 * Servlet implementation class GetListOfMsgs
 */
@WebServlet("/GetListOfMsgs")
public class GetListOfMsgs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetListOfMsgs() {
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
		
		Cookie cookie = CookieRelated.getCookie(request);
		Connection c = DBConnection.getConnection();
		Connection k = DBConnection.getConnection();
		
		if (c != null && k != null) {
			
			if (cookie != null) {
				if (request.getAttribute("genchains") != null) {
					
					String userID = cookie.getValue();
					
					ArrayList<Chain> buychains = MessagingDAO.getChains(c, userID, "buyer");
					if (buychains != null) {
						if (buychains.size() != 0) {
							request.setAttribute("buychains", buychains);
						}
						else {
							//no buy messages, so buychains = null on jsp page
						}
					}
					else {
						request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
					}
					
					ArrayList<Chain> sellchains = MessagingDAO.getChains(k, userID, "seller");
					if (sellchains != null) {
						if (sellchains.size() != 0) {
							request.setAttribute("sellchains", sellchains);
						}
						else {
							//no sell messages, so sellchains = null on jsp page
						}
					}
					else {
						request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
					}
					
					//response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
					request.getRequestDispatcher("/WEB-INF/msgPage.jsp").forward(request, response);
				}
				else {
					try {
						c.close();
					}
					catch (Exception e) {
						//do nothing
					}
					try {
						k.close();
					}
					catch (Exception e) {
						//do nothing
					}
				}
			}
			else {
				try {
					c.close();
				}
				catch (Exception e) {
					//do nothing
				}
				try {
					k.close();
				}
				catch (Exception e) {
					//do nothing
				}
				response.sendRedirect("/login"); //this should never happen because caught by routemsgs servlet
			}
		}
		else {
			try {
				c.close();
			}
			catch (Exception e) {
				//do nothing
			}
			try {
				k.close();
			}
			catch (Exception e) {
				//do nothing
			}
			request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
		}
	}

}
