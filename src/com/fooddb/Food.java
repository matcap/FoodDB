package com.fooddb;

import java.util.HashMap;

public class Food {
	public FoodInfo info;
	public HashMap<Nutrient.Id, Nutrient> nutrients;
	
	public Food(FoodInfo info, HashMap<Nutrient.Id, Nutrient> nutrients) {
		this.info = (info == null) ? new FoodInfo() : info;
		this.nutrients = (nutrients == null) ? new HashMap<Nutrient.Id, Nutrient>() : nutrients;
	}
	
	public double valueOf(Nutrient.Id nutrient) {
		if(nutrients.containsKey(nutrient)) {
			return nutrients.get(nutrient).value;
		} else {
			return 0.0;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(String.format("[Food: %s]", info));
		for(Nutrient n : nutrients.values()) {
			builder.append(String.format("\n   %s", n));
		}
		return builder.toString();
	}
}
