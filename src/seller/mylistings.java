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

import common.Bean;
import common.CookieRelated;
import common.DBConnection;
import listings.ListingBean;
import listings.ListingDAO;

/**
 * Servlet implementation class mylistings
 */
@WebServlet("/mylistings")
public class mylistings extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mylistings() {
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
			String userID = cookie.getValue();
			
			if (c != null) {
				//get specific listing:
				if (request.getAttribute("listingID") != null) {
					int listingID = (int) request.getAttribute("listingID");
					ListingBean listing = new ListingBean();
					listing.setlistingID(listingID);
					listing = ListingDAO.getListing(listing, c); //might be null
					
					if (!(listing.getSQLErrorState())) {
						if (!(listing.getIOErrorState())) {
							if (listing.getListingExists()) {
								//if reaches here, listing != null and complete
								request.setAttribute("listing", listing);
								request.getRequestDispatcher("/WEB-INF/singleListing.jsp").forward(request, response);
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
				else {//get all of my listings:
					Bean bean = new Bean();
					ArrayList<ListingBean> myListings = MyListingsDAO.getMyListOfListings(userID, bean, c);
						
					if (!(bean.getSQLErrorState())) {
						request.setAttribute("myListings", myListings);
						request.getRequestDispatcher("/WEB-INF/mylistings.jsp").forward(request, response);
					}
					else {
						request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request,response);
					}
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
