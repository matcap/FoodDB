import java.util.ArrayList;

import com.fooddb.FoodInfo;
import com.fooddb.usda.USDADatabase;

public class Main {

	public static void main(String[] args) {
		USDADatabase db = new USDADatabase("config/usda_api.cfg");
		
		ArrayList<FoodInfo> info = db.search("butter");
		
		System.out.println(info);
	}

}
