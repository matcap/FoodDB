package com.fooddb.usda;

public class USDAFoodInfo extends com.fooddb.FoodInfo {
	public String ndbNo;
	public boolean branded;
	
	@Override
	public String toString() {
		return String.format("[USDAFoodInfo: '%s'; %s; %b]", name, ndbNo, branded);
	}
}
