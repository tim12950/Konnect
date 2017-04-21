package listings;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CookieRelated;

/**
 * Servlet implementation class home
 */
@WebServlet("/home")
public class home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public home() {
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
			
			String username = CookieRelated.getUsername(cookie);
			
			if (username != null) { //is null iff sql error
				cookie.setMaxAge(604800);
				response.addCookie(cookie);
				//resets cookie expiration date
				request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
			}
			else {
				request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
			}
		}
		else {
			if (request.getSession().getAttribute("goodbye") != null) {
				request.getSession().removeAttribute("goodbye");
				request.setAttribute("goodbye", true);
			}
			request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
		}
		
	}

}
