package com.fooddb.usda;

public class USDANutrient extends com.fooddb.Nutrient {
	public String ndbNo;
	
	@Override
	public String toString() {
		return String.format("[USDANutrient: %s; %s; %s; %f]", this.id, this.ndbNo, this.unit, this.value);
	}
	
}
