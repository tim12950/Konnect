package loginAndRegister;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CookieRelated;

/**
 * Servlet implementation class login_nocookie
 */
@WebServlet("/login_nocookie")
public class login_nocookie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login_nocookie() {
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
		
		if (cookie != null) {
			response.sendRedirect("/home");
		}
		else {
			request.setAttribute("msg", "Please enable cookies in order to login!");
			request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
		}
		
	}

}
