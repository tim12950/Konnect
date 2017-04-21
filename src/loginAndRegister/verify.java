package loginAndRegister;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CookieRelated;
import common.DBConnection;

/**
 * Servlet implementation class verify
 */
@WebServlet("/verify/*")
public class verify extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public verify() {
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
		
		String uri = request.getRequestURI();
		int length = uri.length();
		Cookie cookie = CookieRelated.getCookie(request);
		Connection c = DBConnection.getConnection();
		
		if (cookie == null) {
			
			if (c != null) {
				
				if (request.getParameter("code") != null && request.getParameter("pwd") != null) {
					
					String code = request.getParameter("code");
					String password = request.getParameter("pwd");
					PreparedStatement s = null;
					PreparedStatement t = null;
					ResultSet r = null;
					
					try {
						s = c.prepareStatement("SELECT userID FROM users WHERE code = ? AND password = ? AND verified = 0");
						t = c.prepareStatement("UPDATE users SET verified = 1 WHERE code = ?");
						
						s.setString(1, code);
						s.setString(2, password);
						
						r = s.executeQuery();
						
						if (r.next()) {
							t.setString(1, code);
							t.executeUpdate();
							
							Cookie myCookie = new Cookie("konnect", r.getString("userID"));
							myCookie.setMaxAge(604800);
							myCookie.setHttpOnly(true);
							myCookie.setPath("/");
							response.addCookie(myCookie);
							
							//session.setAttribute("loginMsg", "Account Verified! Enable cookies, then login:");
							response.sendRedirect("/verify_nocookie");
						}
						else {
							//session.setAttribute("verificationfailed", true);
							//response.sendRedirect("/verify/" + code);
							request.setAttribute("verificationmsg", "Verification failed. Try again:");
							request.setAttribute("code", code);
							request.getRequestDispatcher("/WEB-INF/verify.jsp").forward(request, response);
						}
					}
					catch (SQLException e) {
						request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
					}
					finally {
						try {
							r.close();
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
							t.close();
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
				else {
					
					if (length == 44) {
						
						String code = uri.substring(8, 44);
						PreparedStatement s = null;
						ResultSet r = null;
						
						try {
							s = c.prepareStatement("SELECT code FROM users WHERE code = ? AND verified = 0");
							
							s.setString(1, code);
							r = s.executeQuery();
							
							if (r.next()) {
								request.setAttribute("code", code);
								request.getRequestDispatcher("/WEB-INF/verify.jsp").forward(request, response);
							}
							else {
								response.sendError(404);
							}
						}
						catch (SQLException e) {
							request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
						}
						finally {
							try {
								r.close();
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
			try {
				c.close();
			}
			catch (Exception e) {
				//do nothing
			}
			response.sendRedirect("/home");
		}
	}
	
}
