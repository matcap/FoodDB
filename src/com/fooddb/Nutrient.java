package com.fooddb;

public class Nutrient {
	public enum Id {
		// Generic nutrients
		WATER,
		ENERGY,
		PROTEIN,
		FAT,
		CARBOHYDRATE,
		FIBER,
		SUGAR,
		// Minerals
		CALCIUM,
		IRON,
		MAGNESIUM,
		PHOSPHORUS,
		POTASSIUM,
		SODIUM,
		ZINC,
		// Vitamins
		VITAMIN_C,
		VITAMIN_B6,
		VITAMIN_B12,
		// Lipids
		FAT_SATURATED,
		FAT_MONOUNSATURATED,
		FAT_POLYUNSATURATED,
		CHOLESTEROL,
		// Other
		CAFFEINE
	}
	
	public Id id;
	public String unit;
	public double value;
}
