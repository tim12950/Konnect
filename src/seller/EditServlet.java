package seller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CookieRelated;
import common.DBConnection;
import listings.ListingBean;
import listings.ListingDAO;

/**
 * Servlet implementation class EditServlet
 */

@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditServlet() {
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
			
			Connection c = DBConnection.getConnection();
			
			if (c != null) {
				if (request.getAttribute("listingID") != null) {
					int listingID = (int) request.getAttribute("listingID");
					ListingBean listing = new ListingBean();
					listing.setlistingID(listingID);
					listing = ListingDAO.getListing(listing, c);
					
					if (!(listing.getSQLErrorState())) {
						if (!(listing.getIOErrorState())) {
							if (listing.getListingExists()) {
								request.setAttribute("listing", listing);
								
								ArrayList<String> listOfCategories = new ArrayList<String>();
								listOfCategories.add("books");
								listOfCategories.add("carpooling");
								listOfCategories.add("electronics");
								listOfCategories.add("tutoring");
								listOfCategories.add("other");
								int length = listOfCategories.size();
								//should put constant array of categories somewhere else
								
								for (int i = 0; i < length; i++) {
									if (listOfCategories.get(i).equals(listing.getCategory())) {
										listOfCategories.remove(i);
										break;
									}
								}
								
								request.setAttribute("listOfCategories", listOfCategories);
								request.getRequestDispatcher("/WEB-INF/editPage.jsp").forward(request, response);
							}
							else {
								response.sendError(404);
							}
						}
						else {
							throw new IOException();
						}
					}
					else {
						request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request,response);
					}
				}
				else {
					response.sendError(404);
				}
			}
			else {
				request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request,response);
			}
		}
		else {
			response.sendRedirect("/login");
		}
		
	}

}
