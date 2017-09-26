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
import java.util.function.Consumer;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.fooddb.Food;
import com.fooddb.FoodInfo;
import com.fooddb.Queryable;

import utils.QueryBuilder;

public class USDADatabase implements Queryable {
	
	private final String SEARCH_URL =  "https://api.nal.usda.gov/ndb/search/";
	
	private String apiKey;
	
	public USDADatabase(String cfgFile) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cfgFile)));
			HashMap<String, String> config = new HashMap<>();
			for(String line = reader.readLine(); line != null; line = reader.readLine()) {
				config.put(
					line.split("=")[0].trim(), 
					line.split("=")[1].trim()
				);
			}
			if(config.containsKey("api_key")) {
				apiKey = config.get("api_key");
			} else {
				apiKey = "";
			}
			
		} catch (IOException e) {
			apiKey = "";
		}
		
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
		JsonArray foodList = response.asObject().get("list").asObject().get("item").asArray();
		if(foodList == null) {
			return result;
		}
		
		for(JsonValue food : foodList ) {
			USDAFoodInfo info = new USDAFoodInfo();
			info.name = food.asObject().getString("name", "");
			info.name = info.name.substring(1, info.name.length() - 1);
			info.ndbNo = Integer.parseInt(food.asObject().getString("ndbno", "-1"));
			info.branded = food.asObject().getString("ds", "SR") == "BL";
			result.add(info);
		}
		
		return result;
	}

	@Override
	public Food report(FoodInfo info) {
		return null;
	}
	
	private JsonValue request(String query) {
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
