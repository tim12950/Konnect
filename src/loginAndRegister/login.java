package loginAndRegister;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.DBConnection;
import loginAndRegister.LoginBean;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
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
		
		Connection c = DBConnection.getConnection();
		
		if (c != null) {
			
			LoginBean LBean = new LoginBean();
			
			String email = request.getParameter("email");
			String password = request.getParameter("pwd");
			
			LBean.setEmail(email);
			LBean.setPassword(password);
			
			LoginDAO.validateLogin(LBean,c);
			
			if (!(LBean.getSQLErrorState())) {
				if (LBean.getLoginOK()) {
					Cookie myCookie = new Cookie("konnect", LBean.getUserID());
					myCookie.setMaxAge(604800);
					myCookie.setHttpOnly(true);
					response.addCookie(myCookie);
					response.sendRedirect("/login_nocookie");
				}
				else {
					request.setAttribute("msg", "Invalid Login");
					request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
				}
			}
			else {
				request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request,response);
			}
		}
		else {
			request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request,response);
		}
	}

}
