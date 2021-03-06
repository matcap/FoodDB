package utils;

import java.net.URLEncoder;

public class QueryBuilder {
	private String query;
	private int paramCount;
	
	public QueryBuilder(String base_url) {
		try {
			query = base_url;
			if(base_url.length() == 0 || base_url.charAt(query.length()-1) != '?') {
				query += '?';
			}
		} catch (Exception e) {
			query = "";
		}
		paramCount = 0;
	}
	
	public void addParam(String name, int value) {
		addParam(name, Integer.toString(value));
	}
	
	public void addParam(String name, long value) {
		addParam(name, Long.toString(value));
	}
	
	public void addParam(String name, double value) {
		addParam(name, Double.toString(value));
	}
	
	public void addParam(String name, String value) {
		try {
			if(paramCount > 0)
				query += "&";
			query += URLEncoder.encode(name, "utf-8");
			query += "=";
			query += URLEncoder.encode(value, "utf-8");
			paramCount++;
		} catch (Exception e) {
		}
	}
	
	@Override
	public String toString() {
		return query;
	}
	
}
