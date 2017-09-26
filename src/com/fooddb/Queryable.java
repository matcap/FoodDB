package com.fooddb;

import java.util.ArrayList;

public interface Queryable {
	public ArrayList<FoodInfo> search(String query);
	public Food report(FoodInfo info);
}
