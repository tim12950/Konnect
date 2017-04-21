package loginAndRegister;

import java.io.IOException;
import java.sql.Connection;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.DBConnection;
import common.Mail;
import loginAndRegister.RegisterBean;

/**
 * Servlet implementation class register
 */
@WebServlet("/register")
public class register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public register() {
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
		Connection d = DBConnection.getConnection();
		Connection k = DBConnection.getConnection();
		
		if (c != null && k != null && d != null) {
			
			RegisterBean RBean = new RegisterBean();
			
			String id = UUID.randomUUID().toString();
			String email = request.getParameter("email");
			String username = request.getParameter("username");
			String password = request.getParameter("pwd");
			String confirmPassword = request.getParameter("pwdA");
			
			RBean.setEmail(email);
			RBean.setUsername(username);
			RBean.setPassword(password);
			RBean.setConfirmPassword(confirmPassword);
			String code = UUID.randomUUID().toString();
			
			RegisterDAO.validateReg(RBean, c, d);
			
			if (!(RBean.getSQLErrorState())) {
				if (RBean.getRegOK()) {
					Mail.sendMail(RBean.getEmail(), id, code, RBean, k);
					if (RBean.getEverythingOK()) {
						request.setAttribute("msg", "Check your email to verify your account!");
					}
					else {
						request.setAttribute("msg", "Something went wrong. Try again later");
					}
				}
				else {
					try {
						d.close();
					}
					catch (Exception x) {
						//do nothing
					}
					try {
						k.close();
					}
					catch (Exception x) {
						//do nothing
					}
					request.setAttribute("msg", RBean.getErrorMsg());
				}
				request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
			}
			else {
				try {
					d.close();
				}
				catch (Exception x) {
					//do nothing
				}
				try {
					k.close();
				}
				catch (Exception x) {
					//do nothing
				}
				request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request,response);
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
				k.close();
			}
			catch (Exception x) {
				//do nothing
			}
			request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request,response);
		}
		
	}

}
