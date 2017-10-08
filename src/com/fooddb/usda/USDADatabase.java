package com.fooddb.usda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.fooddb.Food;
import com.fooddb.FoodInfo;
import com.fooddb.Nutrient;
import com.fooddb.Queryable;

import utils.QueryBuilder;

public class USDADatabase implements Queryable {
	
	private static final String SEARCH_URL = "https://api.nal.usda.gov/ndb/search/";
	private static final String REPORT_URL = "https://api.nal.usda.gov/ndb/reports/";
	
	private String apiKey;
	
	public USDADatabase(String apiKey) {
		this.apiKey = apiKey;
	}
	
	@Override
	public ArrayList<FoodInfo> search(String query) {
		ArrayList<FoodInfo> result =  new ArrayList<>();
		
		QueryBuilder builder = new QueryBuilder(SEARCH_URL);
		builder.addParam("api_key", apiKey);
		builder.addParam("format", "json");
		builder.addParam("q", query);
		builder.addParam("ds", "Standard Reference");
		builder.addParam("sort", "r");
		builder.addParam("max", "20");
		
		JsonValue response = request(builder.toString());		
		if(response == null || response.isNull()) {
			return result;
		}
		
		JsonArray foodList = null;
		try {
			foodList = response
					.asObject().get("list")
					.asObject().get("item")
					.asArray();
		} catch (Exception e) {
			return result;
		}
		
		for(JsonValue food : foodList ) {
			USDAFoodInfo info = new USDAFoodInfo();
			info.name = food.asObject().getString("name", "");
			info.ndbNo = food.asObject().getString("ndbno", "-1");
			info.branded = food.asObject().getString("ds", "SR").equals("BL");
			if(!info.ndbNo.equals("-1")) {
				result.add(info);				
			}
		}
		
		return result;
	}

	@Override
	public Food report(FoodInfo info) {
		QueryBuilder builder = new QueryBuilder(REPORT_URL);
		builder.addParam("api_key", apiKey);
		builder.addParam("format", "json");
		builder.addParam("ndbno", ((USDAFoodInfo) info).ndbNo);
		builder.addParam("type", "b");
		
		JsonValue response = request(builder.toString());
		if(response == null || response.isNull()) {
			return null;
		}
		Food food = new Food(info, null);
		
		JsonArray nutrientList = null;
		try {
			nutrientList = response
					.asObject().get("report")
					.asObject().get("food")
					.asObject().get("nutrients")
					.asArray();
		} catch (Exception e) {
			return null;
		}
		
		for(JsonValue nutrient : nutrientList) {
			USDANutrient n = new USDANutrient();
			n.ndbNo = nutrient.asObject().getString("nutrient_id", "-1");
			n.id = fromNutrientNdbNo(n.ndbNo);
			n.unit = nutrient.asObject().getString("unit", "");
			n.value = Double.parseDouble(nutrient.asObject().getString("value", "0"));
			
			if(n.id != null) {
				food.nutrients.put(n.id, n);
			}
		}
		
		return food;
	}
	
	private static Nutrient.Id fromNutrientNdbNo(String ndbno) {
		switch(ndbno) {
		case "255": return Nutrient.Id.WATER;
		case "208": return Nutrient.Id.ENERGY;
		case "203": return Nutrient.Id.PROTEIN;
		case "204": return Nutrient.Id.FAT;
		case "205": return Nutrient.Id.CARBOHYDRATE;
		case "291": return Nutrient.Id.FIBER;
		case "269": return Nutrient.Id.SUGAR;
		case "301": return Nutrient.Id.CALCIUM;
		case "303": return Nutrient.Id.IRON;
		case "304": return Nutrient.Id.MAGNESIUM;
		case "305": return Nutrient.Id.PHOSPHORUS;
		case "306": return Nutrient.Id.POTASSIUM;
		case "307": return Nutrient.Id.SODIUM;
		case "309": return Nutrient.Id.ZINC;
		case "606": return Nutrient.Id.FAT_SATURATED;
		case "645": return Nutrient.Id.FAT_MONOUNSATURATED;
		case "656": return Nutrient.Id.FAT_POLYUNSATURATED;
		case "601": return Nutrient.Id.CHOLESTEROL;
		case "262": return Nutrient.Id.CAFFEINE;
		default: return null;
		}
	}
	
	private static JsonValue request(String query) {
		try {
			URL url = new URL(query);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			JsonValue json = Json.parse(new InputStreamReader(conn.getInputStream()));
			conn.disconnect();
			return json;
		} catch (IOException e) {
			return null;
		}
	}
	
}
