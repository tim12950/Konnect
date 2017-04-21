package seller;

import javax.servlet.http.Part;

public class ValidateEdit {
	
public static String validate(Part pic, String title, String description, String price, String money) {
		
		if (pic.getSize() > 0) {
			//there's a picture
			if (pic.getHeader("content-type").equals("image/jpeg") || pic.getHeader("content-type").equals("image/png")) {
				return "good,pic";
			}
			else {
				return "image must be jpeg or png";
			}
		}
		else {
			//no picture
			return validateDelPic(title,description,price,money);
		}
	}
	
	public static String validateDelPic(String title, String description, String price, String money) {
		
		if (title.length() <= 64) {
			if (title.length() > 0) {
				if (description.length() < 65500) {
					if(description.length() > 0) {
						if (price.equals("specify")) {
							if (money.length() <= 32) {
								if (money.length() > 0) {
									return "good";
								}
								else {
									return "enter a price";
								}
							}
							else {
								return "price too long";
							}
						}
						else {
							return "good";
						}
					}
					else {
						return "please enter a description";
					}
				}
				else {
					return "description too long";
				}
			}
			else {
				return "please enter a title";
			}
		}
		else {
			return "title too long";
		}
		
	}

}
