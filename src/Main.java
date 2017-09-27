import java.util.ArrayList;

import com.fooddb.Food;
import com.fooddb.FoodInfo;
import com.fooddb.usda.USDADatabase;

public class Main {

	public static void main(String[] args) {
		USDADatabase db = new USDADatabase("config/usda_api.cfg");
		
		ArrayList<FoodInfo> info = db.search("cheddar");
		for(FoodInfo f : info) {
			System.out.println(f.toString());			
		}
		
		Food butter = db.report(info.get(0));
		
		System.out.println(butter);
	}

}
