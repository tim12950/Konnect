package listings;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.Bean;
import common.DBConnection;
import listings.ListingBean;
import listings.ListingDAO;

/**
 * Servlet implementation class listings
 */
@WebServlet("/listings")
public class listings extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public listings() {
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
		
		Connection c = DBConnection.getConnection();
		
		if (c != null) {
			
			//get specific listing:
			if (request.getAttribute("listingID") != null || session.getAttribute("listingID") != null) {
				int listingID = 0;
				
				if (request.getAttribute("listingID") != null) {
					listingID = (int) request.getAttribute("listingID");
				}
				else if (session.getAttribute("listingID") != null) {
					listingID = (int) session.getAttribute("listingID");
					session.removeAttribute("listingID");
				}
				
				ListingBean listing = new ListingBean();
				listing.setlistingID(listingID);
				listing = ListingDAO.getListing(listing, c);
				
				if (!(listing.getSQLErrorState())) {
					if (!(listing.getIOErrorState())) {
						if (listing.getListingExists()) {
							if (session.getAttribute("msgerror") != null) {
								request.setAttribute("msgerror", session.getAttribute("msgerror"));
								session.removeAttribute("msgerror");
							}
							//if we reach this point, listing must be a complete listing object
							request.setAttribute("listing", listing);
							request.getRequestDispatcher("/WEB-INF/individualListing.jsp").forward(request, response);
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
			//get all listings in a category:
			else if (request.getAttribute("category") != null) {
				
				String category = (String) request.getAttribute("category");
				Bean bean = new Bean();
				ArrayList<ListingBean> listings = ListingDAO.getListingsInCategory(bean, c, category); //is null iff no listings, or sql or io error
				
				if (!(bean.getSQLErrorState())) {
					if (!(bean.getIOErrorState())) {
						request.setAttribute("listings", listings);
						request.getRequestDispatcher("/WEB-INF/listings.jsp").forward(request, response);
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
				//get all listings:
				Bean bean = new Bean();
				ArrayList<ListingBean> listings = ListingDAO.getListings(bean, c);
				
				if (!(bean.getSQLErrorState())) {
					if (!(bean.getIOErrorState())) {
						request.setAttribute("listings", listings);
						request.getRequestDispatcher("/WEB-INF/listings.jsp").forward(request, response);
					}
					else {
						throw new IOException();
					}
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

}
