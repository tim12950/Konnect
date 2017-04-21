package seller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.DBConnection;

/**
 * Servlet implementation class DeleteServlet
 */
@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteServlet() {
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
		
		HttpSession session = request.getSession();
		
		if (request.getAttribute("listingID") != null) {
			
			Connection c = DBConnection.getConnection();
			int listingID = (int) request.getAttribute("listingID");
			
			if (c != null) {
				
				PreparedStatement s = null;
				PreparedStatement t = null;
				PreparedStatement u = null;
				PreparedStatement v = null;
				ResultSet r = null;
				
				try {
					s = c.prepareStatement("DELETE FROM listings WHERE listingID = ?");
					t = c.prepareStatement("SELECT m.msgID FROM messages m, chains c WHERE m.chainID = c.chainID AND c.listingID = ?");
					u = c.prepareStatement("DELETE FROM chains WHERE listingID = ?");
					v = c.prepareStatement("DELETE FROM messages WHERE msgID = ?");
					
					s.setInt(1, listingID);
					s.executeUpdate();
					t.setInt(1, listingID);
					
					r = t.executeQuery();
					
					while(r.next()) {
						v.setInt(1, r.getInt("msgID"));
						v.executeUpdate();
					}
					
					u.setInt(1, listingID);
					u.executeUpdate();
					
					session.setAttribute("delAlert", "Listing and all associated messages deleted!");
					response.sendRedirect("/mylistings");
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
						u.close();
					}
					catch (Exception e) {
						//do nothing
					}
					try {
						v.close();
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
		else {
			response.sendError(404);
		}
		
	}

}
