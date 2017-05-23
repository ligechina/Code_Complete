package com.seke.autocomplete.core;

import java.util.stream.Collectors;

import com.seke.autocomplete.lib.HttpRequest;
import com.seke.autocomplete.lib.JSON;
import com.seke.autocomplete.lib.Preference;

public class Predict {

	private final static String URL = "http://162.105.81.216:23456/predict";
	private final static int TIME_OUT = 3000;

	public static String[] predict(String prefix, String current) {
		try {
			int len=Integer.parseInt(Preference.getInstance().get("hint_length", "50"));
			
			//long start = System.currentTimeMillis();
			HttpRequest httpRequest = HttpRequest.post(URL)
					.connectTimeout(TIME_OUT).readTimeout(TIME_OUT)
					.useCaches(false).form("text", prefix)
					.form("current", current).form("max_token", len);
			String string = httpRequest.body();
			httpRequest.disconnect();
			//long end = System.currentTimeMillis();
			// System.out.println(String.format("Time used: %.5fs",
			// (end-start)/1000.));
			return JSON.decode(string).getList("data").stream()
					.map(s -> s.getString()).collect(Collectors.toList())
					.toArray(new String[0]);

		} catch (Exception e) {
			e.printStackTrace();
			return new String[0];
		}
	}

}
