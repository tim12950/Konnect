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
 * Servlet implementation class ReplyMsg
 */
@WebServlet("/ReplyMsg")
public class ReplyMsg extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReplyMsg() {
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
		
		if (request.getAttribute("genareply") != null && request.getAttribute("chainID") != null) {
			
			if (cookie != null) {
				
				HttpSession session = request.getSession();
				String userID = cookie.getValue();
				int chainID = (int) request.getAttribute("chainID");
				Connection c = DBConnection.getConnection();
				String message = request.getParameter("newreplymsg");
						
				Msg msg = new Msg();
				msg.setChainID(chainID);
				msg.setMsg(message);
				
				ComposeMsgsDAO.validateReply(c, msg, userID);
				
				if (!(msg.getSQLErrorState())) {
					if (msg.getUploadSuccess()) {
						String buyerOrSeller = msg.getIsSenderBuyerOrSeller();
						response.sendRedirect("/messages/" + buyerOrSeller + "/" + chainID);
					}
					else {
						if (msg.getErrorMsg().equals("notauth")) {
							response.sendError(403);
						}
						else if (msg.getErrorMsg().equals("chaindne")) {
							response.sendError(404);
						}
						else if (msg.getErrorMsg().equals("empty")) {
							String buyerOrSeller = msg.getIsSenderBuyerOrSeller();
							session.setAttribute("amsgofsorts", "Reply cannot be empty!");
							response.sendRedirect("/messages/" + buyerOrSeller + "/" + chainID);
						}
						else if (msg.getErrorMsg().equals("toolong")) {
							String buyerOrSeller = msg.getIsSenderBuyerOrSeller();
							session.setAttribute("amsgofsorts", "Reply is too long!");
							response.sendRedirect("/messages/" + buyerOrSeller + "/" + chainID);
						}
					}
				}
				else {
					request.getRequestDispatcher("/WEB-INF/SQLerror.jsp").forward(request, response);
				}
			}
			else {
				response.sendRedirect("/login");
			}
		}
		else {
			response.sendError(404);
		}
		
	}

}
