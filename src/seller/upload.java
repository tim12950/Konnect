package seller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import common.CookieRelated;
import common.DBConnection;
import listings.ListingBean;
import seller.EditDAO;
import seller.MyListingsDAO;
import seller.UploadDAO;
import seller.ValidateEdit;

/**
 * Servlet implementation class upload
 */
@MultipartConfig
@WebServlet("/upload")
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public upload() {
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
		Cookie cookie = CookieRelated.getCookie(request);
		Connection c = DBConnection.getConnection();
		
		if (c != null) {
			
			if (request.getParameter("listingID") != null) {
				//edit: first verify user has permission to edit a particular listing
				
				int listingID = 0;
				try{
					listingID = Integer.parseInt(request.getParameter("listingID")); //parsing must be successful if coming from edit page; user can enter parameters on his own though
				}
				catch (NumberFormatException e) {
					response.sendError(404);
					return;
				}
				
				String userID = cookie.getValue();
				String result = MyListingsDAO.verifyUserListingRelationship(listingID, userID);
				
				if (result.equals("notgood")) {
					response.sendError(403);
					return;
				}
				else if (result.equals("sqlerror")) {
					request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
					return;
				}
				
				InputStream input = null;
				Part pic = request.getPart("pic"); //throws IO,Servlet error, caught by doPost
				String title = request.getParameter("title");
				String description = request.getParameter("description");
				String price = request.getParameter("price");
				String money = request.getParameter("money");
				String category = request.getParameter("category");
				
				if (request.getParameter("deletePic") != null) {
					String output = ValidateEdit.validateDelPic(title, description, price, money);
					if (output.equals("good")) {
						ListingBean listing = new ListingBean();
						EditDAO.editDeletePic(listingID, c, listing, title, price, money, description, category);
						
						if (!(listing.getSQLErrorState())) {
							session.setAttribute("updateMsg", "Update Successful!");
							response.sendRedirect("/mylistings/" + listingID);
						}
						else {
							request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
						}
					}
					else {
						session.setAttribute("updateMsg", output);
						response.sendRedirect("/mylistings/edit/" + listingID);
					}
				}
				else {
					String output = ValidateEdit.validate(pic, title, description, price, money);
					
					if (output.equals("good,pic")) {
						ListingBean listing = new ListingBean();
						input = pic.getInputStream(); //throws IO error, caught by doPost
						
						EditDAO.edit(listingID, c, listing, title, 1, price, money, description, category, input);
						
						if (!(listing.getSQLErrorState())) {
							if (listing.getUploadSuccess()) {
								session.setAttribute("updateMsg", "Update Successful!");
								response.sendRedirect("/mylistings/" + listingID);
							}
						}
						else {
							request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
						}
					}
					else if (output.equals("good")) {
						ListingBean listing = new ListingBean();
						
						EditDAO.edit(listingID, c, listing, title, 0, price, money, description, category, null);
						
						if (!(listing.getSQLErrorState())) {
							if (listing.getUploadSuccess()) {
								session.setAttribute("updateMsg", "Update Successful!");
								response.sendRedirect("/mylistings/" + listingID);
							}
						}
						else {
							request.getRequestDispatcher("/WEB-INF/SQLError.jsp").forward(request, response);
						}
					}
					else {
						session.setAttribute("updateMsg", output);
						response.sendRedirect("/mylistings/edit/" + listingID);
					}
				}
				
			}
			else {
				//new upload:
				ListingBean listing = new ListingBean();
				
				InputStream input = null;
				Part pic = request.getPart("pic"); //throws io,servlet exceptions, caught by doPost
				
				if (pic == null) {
					request.getRequestDispatcher("/WEB-INF/sellersPage.jsp").forward(request, response);
					return;
				}
				
				long imageSize = pic.getSize();
				String type = pic.getHeader("content-type");
				
				int titleLength = request.getParameter("title").length();
				int priceLength = request.getParameter("price").length();
				int descriptionLength = ((request.getParameter("description")).getBytes()).length;
				String category = request.getParameter("category");
				String userID = cookie.getValue();
				
				if (imageSize < 102400) {
					if (titleLength > 0) {
						if (priceLength > 0) { //mistake, always > 0, but that's ok
							if (descriptionLength > 0) {
								if (!(category.equals("select"))) {
									if (imageSize > 0) {
										if (type.equals("image/jpeg") || type.equals("image/png")) {
											// more input validation
											if ((check(titleLength, descriptionLength)).equals("good")) {
												String title = request.getParameter("title");
												String description = request.getParameter("description");
												input = pic.getInputStream(); //throws ioexception, caught by doPost
												
												String s= request.getParameter("price");
												
												if (s.equals("free")) {
													String price = "free";
													UploadDAO.upload(c, listing, 1, title, price, description, category, userID, input);
												}
												else if (s.equals("uponRequest")) {
													String price = "uponRequest";
													UploadDAO.upload(c, listing, 1, title, price, description, category, userID, input);
												}
												else if (s.equals("specify")) {
													String t = checkPrice(request.getParameter("money"));
													if (t.equals("good")) {
														String price = request.getParameter("money");
														UploadDAO.upload(c, listing, 1, title, price, description, category, userID, input);
													}
													else {
														request.setAttribute("msg", t);
													}
												}
											}
											else {
												request.setAttribute("msg", check(titleLength, descriptionLength));
											}
										}
										else {
											request.setAttribute("msg", "image must be jpeg or png");
										}
									}
									else {
										//no picture
										if ((check(titleLength, descriptionLength)).equals("good")) {
											String title = request.getParameter("title");
											String description = request.getParameter("description");
											
											String s= request.getParameter("price");
											
											if (s.equals("free")) {
												String price = "free";
												UploadDAO.upload(c, listing, 0, title, price, description, category, userID, input);
											}
											else if (s.equals("uponRequest")) {
												String price = "uponRequest";
												UploadDAO.upload(c, listing, 0, title, price, description, category, userID, input);
											}
											else if (s.equals("specify")) {
												String t = checkPrice(request.getParameter("money"));
												if (t.equals("good")) {
													String price = request.getParameter("money");
													UploadDAO.upload(c, listing, 0, title, price, description, category, userID, input);
												}
												else {
													request.setAttribute("msg", t);
												}
											}
										}
										else {
											request.setAttribute("msg", check(titleLength, descriptionLength));
										}
									}
								}
								else {
									request.setAttribute("msg", "select a category");
								}
							}
							else {
								request.setAttribute("msg", "enter a description");
							}
						}
						else {
							request.setAttribute("msg", "enter a price");
						}
					}
					else {
						request.setAttribute("msg", "enter a title");
					}
				}
				else {
					request.setAttribute("msg", "image too large");
				}
				
				if (!(listing.getSQLErrorState())) {
					if (listing.getUploadSuccess()) {
						session.setAttribute("uploadSuccess", "Listing Uploaded Successfully!");
						response.sendRedirect("/mylistings");
					}
					else {
						request.getRequestDispatcher("/WEB-INF/sellersPage.jsp").forward(request, response);
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
	
	public static String check(int title, int description) {
		if (title > 64) {
			return "title must be less than 64 characters";
		}
		else if (description > 65500) {
			return "description too long";
		}
		return "good";
	}
	
	public static String checkPrice(String s) {
		if (s.length() == 0) {
			return "you must enter a price";
		}
		else if (s.length() > 32) {
			return "price too long";
		}
		return "good";
	}

}
