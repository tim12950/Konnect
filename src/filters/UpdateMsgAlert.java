package filters;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import common.DBConnection;

/**
 * Servlet Filter implementation class updateMsgAlert
 */
@WebFilter("/updateMsgAlert")
public class UpdateMsgAlert implements Filter {

    /**
     * Default constructor. 
     */
    public UpdateMsgAlert() {
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
		Connection c = DBConnection.getConnection();
		String uri = req.getRequestURI();
		int uriLength = uri.length();
		
		if (cookie != null) {
			
			if (CookieRelated.getUsername(cookie) != null) {
				
				session.setAttribute("loggedin", true);
				session.setAttribute("username", CookieRelated.getUsername(cookie));
				String userID = cookie.getValue();
				
				if (uriLength >= 15 && uri.substring(0,14).equals("/messages/buy/")) {
					String msgchain = uri.substring(14);
					int chainID = 0;
					
					try {
						chainID = Integer.parseInt(msgchain);
						//of course, this chain may not exist
						
						if (chainID < 1) {
							res.sendError(404);
						}
						else {
							if (c != null) {
								PreparedStatement s = null;
								try {
									s = c.prepareStatement("UPDATE messages m JOIN chains c ON c.chainID = m.chainID AND c.buyerID = ? AND m.recipID = ? AND c.chainID = ? SET m.readStatus = 1");
									
									s.setString(1, userID);
									s.setString(2, userID);
									s.setInt(3, chainID);
									s.executeUpdate();
									
									updateReadStatus(session, c, userID, s);
									chain.doFilter(request, response);
								}
								catch (SQLException e) {
									req.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
								}
							}
							else {
								req.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
							}
						}
					}
					catch (NumberFormatException e) {
						res.sendError(404);
					}
				}
				else if (uriLength >= 16 && uri.substring(0,15).equals("/messages/sell/")) {
					String msgchain = uri.substring(15);
					int chainID = 0;
					
					try {
						chainID = Integer.parseInt(msgchain);
						
						if (chainID < 1) {
							res.sendError(404);
						}
						else {
							if (c != null) {
								PreparedStatement s = null;
								try {
									s = c.prepareStatement("UPDATE messages m JOIN chains c ON c.chainID = m.chainID AND c.sellerID = ? AND m.recipID = ? AND c.chainID = ? SET m.readStatus = 1");
									
									s.setString(1, userID);
									s.setString(2, userID);
									s.setInt(3, chainID);
									s.executeUpdate();
									
									updateReadStatus(session, c, userID, s);
									chain.doFilter(request, response);
								}
								catch (SQLException e) {
									req.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
								}
							}
							else {
								req.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
							}
						}
					}
					catch (NumberFormatException e) {
						res.sendError(404);
					}
				}
				else {
					try {
						updateReadStatus(session, c, userID, null);
						chain.doFilter(request, response);
					}
					catch (SQLException e) {
						req.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
					}
				}
			}
			else {
				//cookie without a valid userID, or db error.
				try {
					c.close();
				}
				catch (Exception e) {
					//do nothing
				}
				req.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
			}
		}
		else {
			try {
				c.close();
			}
			catch (Exception e) {
				//do nothing
			}
			chain.doFilter(request, response);
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	public void updateReadStatus(HttpSession session, Connection c, String recipID, PreparedStatement s) throws SQLException{
		
		PreparedStatement t = c.prepareStatement("SELECT msgID FROM messages WHERE readStatus = 0 AND recipID = ?");
		ResultSet r;
		
		t.setString(1, recipID);
		
		r = t.executeQuery();
		
		try {
			if (r.next()) {
				session.setAttribute("newmsg", true);
			}
			else {
				session.removeAttribute("newmsg");
			}
		}
		catch (SQLException e) {
			throw new SQLException();
		}
		finally {
			try {
				r.close();
			}
			catch (Exception e) {
				//do nothing
			}
			try {
				t.close();
			}
			catch (Exception e) {
				//do nothing
			}
			try {
				s.close();
			}
			catch (Exception e) {
				//do nothing
			}
			try {
				c.close();
			}
			catch (Exception e) {
				//do nothing
			}
		}
	}

}
