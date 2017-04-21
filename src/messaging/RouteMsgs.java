package messaging;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CookieRelated;

/**
 * Servlet implementation class RouteMsgs
 */
@WebServlet("/RouteMsgs")
public class RouteMsgs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RouteMsgs() {
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
		//Connection c = DBconnection.getConnection();
		
		if (cookie != null) {
			if (request.getAttribute("proceedto") != null) {
				
				String dest = (String) request.getAttribute("proceedto");
				
				if (dest.equals("seemsgs")) {
					request.setAttribute("genchains", true);
					request.getRequestDispatcher("/GetListOfMsgs").forward(request, response);
				}
				else if (dest.equals("seeabuychain")) {
					
					request.setAttribute("genabuychain", true);
					//chainid att set already
					request.getRequestDispatcher("/GetMsgs").forward(request, response);
				}
				else if (dest.equals("seeasellchain")) {
					request.setAttribute("genasellchain", true);
					//request.setAttribute("chainID", request.getAttribute("chainID"));
					request.getRequestDispatcher("/GetMsgs").forward(request, response);
				}
				else if (dest.equals("brandnewmsg")) {
					request.setAttribute("composebrandnewmsg", true);
					//listingid att set
					request.getRequestDispatcher("/ComposeMsgs").forward(request, response);
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
