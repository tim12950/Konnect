package filters;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.CookieRelated;
import messaging.ComposeMsgsDAO;

/**
 * Servlet Filter implementation class msgFilter
 */
@WebFilter("/msgFilter")
public class msgFilter implements Filter {

    /**
     * Default constructor. 
     */
    public msgFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String uri = req.getRequestURI();
		int uriLength = uri.length();
		
		if (uri.equals("/messages")) {
			request.setAttribute("proceedto", "seemsgs");
			request.getRequestDispatcher("/RouteMsgs").forward(request, response);
		}
		else if ((uriLength >= 15) && uri.substring(0, 14).equals("/messages/buy/")) {
			try {
				String msgchain = uri.substring(14);
				int chainID = 0;
				chainID = Integer.parseInt(msgchain);
				
				if (chainID < 1) {
					res.sendError(404);
				}
				else {
					request.setAttribute("proceedto", "seeabuychain");
					request.setAttribute("chainID", chainID);
					request.getRequestDispatcher("/RouteMsgs").forward(request, response);
				}
			}
			catch (NumberFormatException e) {
				res.sendError(404);
			}
		}
		else if ((uriLength >= 16) && uri.substring(0, 15).equals("/messages/sell/")) {
			try {
				String msgchain = uri.substring(15);
				int chainID = 0;
				chainID = Integer.parseInt(msgchain);
				
				if (chainID < 1) {
					res.sendError(404);
				}
				else {
					request.setAttribute("proceedto", "seeasellchain");
					request.setAttribute("chainID", chainID);
					request.getRequestDispatcher("/RouteMsgs").forward(request, response);
				}
			}
			catch (NumberFormatException e) {
				res.sendError(404);
			}
		}
		else if ((uriLength >= 15) && uri.substring(0, 14).equals("/messages/new/")) {
			//brand new message w/ listing id; should check for cookie here FIRST
			
			Cookie cookie = CookieRelated.getCookie(req);
			
			if (cookie != null) {
				if (request.getParameter("newcontactmsg") != null) {
					
					String msg = request.getParameter("newcontactmsg");
					int msglength = msg.length();
					
					try {
						String listing = uri.substring(14);
						int listingID = 0;
						listingID = Integer.parseInt(listing);
						
						if (listingID < 10000) {
							res.sendError(404);
						}
						else {
							if (msglength > 0 && msglength < 65500) {
								request.setAttribute("proceedto", "brandnewmsg");
								request.setAttribute("listingID", listingID);
								request.getRequestDispatcher("/RouteMsgs").forward(request, response);
							}
							else if (msglength == 0) {
								if (ComposeMsgsDAO.checkiflistingexists(listingID)) {
									session.setAttribute("msgerror", "Message cannot be empty!");
									res.sendRedirect("/listings/" + listingID);
								}
								else {
									res.sendError(404);
								}
							}
							else if (msglength >= 65500) {
								if (ComposeMsgsDAO.checkiflistingexists(listingID)) {
									session.setAttribute("msgerror", "Your message is too long!");
									res.sendRedirect("/listings/" + listingID);
								}
								else {
									res.sendError(404);
								}
							}
						}
					}
					catch (NumberFormatException e) {
						res.sendError(404);
					}
					catch (SQLException e) {
						req.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
					}
				}
				else {
					res.sendError(404);
				}
			}
			else {
				session.setAttribute("loginMsg", "Please login in order to contact sellers");
				res.sendRedirect("/login");
			}
		}
		else if ((uriLength >= 17) && uri.substring(0, 16).equals("/messages/reply/")) {
			//new reply msg with chainid; verify that userid is buyer or seller id
			if (request.getParameter("newreplymsg") != null) {
				
				try {
					String msgchain = uri.substring(16);
					int chainID = 0;
					chainID = Integer.parseInt(msgchain);
					
					if (chainID < 1) {
						res.sendError(404);
					}
					else {
						request.setAttribute("genareply", true);
						request.setAttribute("chainID", chainID);
						req.getRequestDispatcher("/ReplyMsg").forward(request, response);
					}
				}
				catch (NumberFormatException e) {
					res.sendError(404);
				}
			}
			else {
				res.sendError(404);
			}
		}
		else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
