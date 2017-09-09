package com.seke.autocomplete.core;

import java.util.Random;
import java.util.stream.Collectors;

import com.seke.autocomplete.lib.HttpRequest;
import com.seke.autocomplete.lib.JSON;
import com.seke.autocomplete.lib.Preference;

public class Predict {

	private final static String URL = "http://162.105.89.22:23456/predict";
	private final static int TIME_OUT = 2500;

	public static String[] predict(String prefix, String current) {
		try {
			// int len=Integer.parseInt(Preference.getInstance().get("hint_length", "50"));
			
			int len=new Random().nextInt(40)+25;
			
			HttpRequest httpRequest = HttpRequest.post(URL)
					.connectTimeout(TIME_OUT).readTimeout(TIME_OUT)
					.useCaches(false).form("text", prefix)
					.form("current", current).form("max_token", len);
			String string = httpRequest.body();
			httpRequest.disconnect();
			return JSON.decode(string).getList("data").stream()
					.map(s -> s.getString()).collect(Collectors.toList())
					.toArray(new String[0]);

		} catch (Exception e) {}
		return new String[0];
	}

}
