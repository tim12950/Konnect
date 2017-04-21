package filters;

import java.io.IOException;
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
import seller.MyListingsDAO;

/**
 * Servlet Filter implementation class RoutingFilter
 */
@WebFilter("/RoutingFilter")
public class RoutingFilter implements Filter {

    /**
     * Default constructor. 
     */
    public RoutingFilter() {
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
		Cookie cookie = CookieRelated.getCookie(req);
		String uri = req.getRequestURI();
		int uriLength = uri.length();
		
		//intercepts login
		if (uri.equals("/login")) {
			if (cookie != null) {
				res.sendRedirect("/home");
			}
			else {
				if (req.getParameter("email") != null && req.getParameter("pwd") != null) {
					chain.doFilter(request, response);
				}
				else {
					if (session.getAttribute("loginMsg") != null) {
						req.setAttribute("msg", session.getAttribute("loginMsg"));
						session.removeAttribute("loginMsg");
						req.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
					}
					else {
						req.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
					}
				}
			}
		}
		//intercepts register
		else if (uri.equals("/register")) {
			if (cookie != null) {
				res.sendRedirect("/home");
			}
			else {
				if (req.getParameter("email") != null && req.getParameter("pwd") != null && req.getParameter("username") != null && req.getParameter("pwdA") != null) {
					chain.doFilter(request, response);
				}
				else {
					req.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
				}
			}
		}
		//intercepts upload
		else if (uri.equals("/upload")) {
			if (cookie != null) {
				if (req.getParameter("title") != null && req.getParameter("price") != null && req.getParameter("money") != null && req.getParameter("description") != null && req.getParameter("category") != null) {
					chain.doFilter(request, response);
				}
				else {
					req.getRequestDispatcher("/WEB-INF/sellersPage.jsp").forward(request, response);
				}
			}
			else {
				session.setAttribute("loginMsg", "Please Login to Upload");
				res.sendRedirect("/login");
			}
		}
		//intercepts listings/# and listings/category
		else if (uriLength >= 11 && (uri.substring(0,10)).equals("/listings/")) {
			String listing = uri.substring(10);
			int listingID = 0;
			
			try {
				listingID = Integer.parseInt(listing);
				
				if (listingID < 10000) {
					res.sendError(404);
				}
				else {
					req.setAttribute("listingID", listingID);
					req.getRequestDispatcher("/listings").forward(request, response);
				}
			}
			catch (NumberFormatException e) {
				
				if (uri.length() >= 20 && uri.substring(10,19).equals("category/")) {
					String category = uri.substring(19);
					String[] categories = {"books", "electronics", "carpooling", "tutoring", "other" };
					int length = categories.length;
					int count = 0;
					
					for (int i = 0; i < length; i++) {
						if (category.equals(categories[i])) {
							req.setAttribute("category", category);
							req.getRequestDispatcher("/listings").forward(request, response);
							break;
						}
						count = count+1;
					}
					
					if (count == length) {
						res.sendError(404);
					}
				}
				else {
					res.sendError(404);
				}
			}
		}
		//intercepts mylistings
		else if (uri.equals("/mylistings")) {
			if (session.getAttribute("uploadSuccess") != null) { //for successful upload
				request.setAttribute("msg", session.getAttribute("uploadSuccess"));
				session.removeAttribute("uploadSuccess");
				chain.doFilter(request, response);
			}
			else if (session.getAttribute("delAlert") != null) {
				request.setAttribute("msg", session.getAttribute("delAlert"));
				session.removeAttribute("delAlert");
				chain.doFilter(request, response);
			}
			else {
				chain.doFilter(request, response);
			}
		}
		//intercepts mylistings/# and mylistings/edit/#
		else if (uri.length() >= 13 && uri.substring(0,12).equals("/mylistings/")) {
			
			if (session.getAttribute("updateMsg") != null) {
				String s = (String) session.getAttribute("updateMsg");
				if (s.equals("Update Successful!")) { //successful edit:
					int listingID = Integer.parseInt(uri.substring(12));
					request.setAttribute("listingID", listingID);
					request.setAttribute("msg", s);
					session.removeAttribute("updateMsg");
					req.getRequestDispatcher("/mylistings").forward(request, response);
				}
				else { //edit error:
					int listingID = Integer.parseInt(uri.substring(17));
					request.setAttribute("listingID", listingID);
					request.setAttribute("msg", s);
					session.removeAttribute("updateMsg");
					req.getRequestDispatcher("/EditServlet").forward(request, response);
				}
			}
			else {
				try { //takes you to mylistings/# if you're authorized:
					String listing = uri.substring(12);
					int listingID = 0;
					listingID = Integer.parseInt(listing);
					
					if (listingID < 10000) {
						res.sendError(404);
					}
					else {
						
						if (cookie != null) {
							String userID = cookie.getValue();
							String output = MyListingsDAO.verifyUserListingRelationship(listingID, userID);
							
							if (output.equals("good")) {
								req.setAttribute("listingID", listingID);
								req.getRequestDispatcher("/mylistings").forward(request, response);
							}
							else if (output.equals("notfound")) {
								res.sendError(404);
							}
							else if (output.equals("notgood")) {
								res.sendError(403);
							}
							else {
								req.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
							}
						}
						else {
							res.sendRedirect("/login");
						}
					}
				}
				catch (NumberFormatException e) { //takes you to mylistings/edit/#
					if (uri.length() >= 18 && uri.substring(0,17).equals("/mylistings/edit/")) {
						String listing = uri.substring(17);
						int listingID = 0;
						
						try {
							listingID = Integer.parseInt(listing);
							
							if (listingID < 10000) {
								res.sendError(404);
							}
							else {
								if (cookie != null) {
									String userID = cookie.getValue();
									String output = MyListingsDAO.verifyUserListingRelationship(listingID, userID);
									
									if (output.equals("good")) {
										req.setAttribute("listingID", listingID);
										req.getRequestDispatcher("/EditServlet").forward(request, response);
									}
									else if (output.equals("notfound")) {
										res.sendError(404);
									}
									else if (output.equals("notgood")) {
										res.sendError(403);
									}
									else {
										req.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
									}
								}
								else {
									res.sendRedirect("/login");
								}
							}
						}
						catch (NumberFormatException t) {
							res.sendError(404);
						}
					}
					else if (uri.length() >= 20 && uri.substring(0, 19).equals("/mylistings/delete/")) { //takes you to mylistings/delete/#
						String listing = uri.substring(19);
						int listingID = 0;
						
						try {
							listingID = Integer.parseInt(listing);
							
							if (listingID < 10000) {
								res.sendError(404);
							}
							else {
								if (cookie != null) {
									String userID = cookie.getValue();
									String output = MyListingsDAO.verifyUserListingRelationship(listingID, userID);
									
									if (output.equals("good")) {
										req.setAttribute("listingID", listingID);
										req.getRequestDispatcher("/DeleteServlet").forward(request, response);
									}
									else if (output.equals("notgood")) {
										res.sendError(403);
									}
									else {
										req.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
									}
								}
								else {
									res.sendRedirect("/login");
								}
							}
						}
						catch (NumberFormatException t) {
							res.sendError(404);
						}
					}
					else {
						res.sendError(404);
					}
				}
			}
		}
		//everything else:
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
