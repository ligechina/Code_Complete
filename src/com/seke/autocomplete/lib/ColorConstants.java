package com.seke.autocomplete.lib;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ColorConstants {
	
	public static final Color white = new Color(null, 255, 255, 255);
	public static final Color lightGray = new Color(null, 192, 192, 192);
	public static final Color gray = new Color(null, 128, 128, 128);
	public static final Color darkGray = new Color(null, 64, 64, 64);
	public static final Color black = new Color(null, 0, 0, 0);
	public static final Color red = new Color(null, 255, 0, 0);
	public static final Color orange = new Color(null, 255, 196, 0);
	public static final Color yellow = new Color(null, 255, 255, 0);
	public static final Color green = new Color(null, 0, 255, 0);
	public static final Color lightGreen = new Color(null, 96, 255, 96);
	public static final Color darkGreen = new Color(null, 0, 127, 0);
	public static final Color cyan = new Color(null, 0, 255, 255);
	public static final Color lightBlue = new Color(null, 127, 127, 255);
	public static final Color blue = new Color(null, 0, 0, 255);
	public static final Color darkBlue = new Color(null, 0, 0, 127);
	
	public static Color getColor(int r, int g, int b) {
		RGB rgb=getRGB(r, g, b);
		if (rgb==null) return null;
		return new Color(null, rgb);
	}
	
	public static Color getColor(String rgb) {
		RGB _rgb=getRGB(rgb);
		if (_rgb==null) return null;
		return new Color(null, _rgb);
	}
	
	public static RGB getRGB(int r, int g, int b) {
		if (r<0 || g<0 || b<0 || r>255 || g>255 || b>255) return null;
		return new RGB(r, g, b);
	}
	
	public static RGB getRGB(String rgb) {
		try {
			if (rgb==null || rgb.isEmpty()) return null;
			rgb=rgb.toUpperCase();
			if (!rgb.matches("#([A-Za-z0-9]){6}")) return null;
			int r=Integer.parseInt(rgb.substring(1, 3), 16);
			int g=Integer.parseInt(rgb.substring(3, 5), 16);
			int b=Integer.parseInt(rgb.substring(5, 7), 16);
			return getRGB(r, g, b);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static String toString(RGB rgb) {
		if (rgb==null) return "null";
		int r=rgb.red, g=rgb.green, b=rgb.blue;
		String red=Integer.toHexString(r); 
		if (red.length()==1) red="0"+red;
		String green=Integer.toHexString(g); 
		if (green.length()==1) green="0"+green;
		String blue=Integer.toHexString(b); 
		if (blue.length()==1) blue="0"+blue;
		return ("#"+red+green+blue).toUpperCase();
	}
	
	public static String toString(Color color) {
		if (color==null) return "null";
		return toString(color.getRGB());
	}
	
}
