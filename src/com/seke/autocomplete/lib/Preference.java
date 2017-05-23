package com.seke.autocomplete.lib;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class Preference {
	private IEclipsePreferences preferences;
	private static Preference instance;
	private Preference() {
		preferences=ConfigurationScope.INSTANCE.getNode("com.seke.autocomplete");
	}
	public static Preference getInstance() {
		if (instance==null) instance=new Preference();
		return instance;
	}
	
	public void put(String key, String value) {
		preferences.put(key, value);
		flush();
	}
	
	public void put(String key, int value) {
		preferences.putInt(key, value);
	}
	
	private void flush() {
		try {preferences.flush();} catch (Exception e) {}
	}
	
	public String get(String key) {
		return get(key, null);
	}
	
	public String get(String key, String defaultValue) {
		return preferences.get(key, defaultValue);
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
	}
	
	public int getInt(String key, int defaultValue) {
		return preferences.getInt(key, defaultValue);
	}
	
}
