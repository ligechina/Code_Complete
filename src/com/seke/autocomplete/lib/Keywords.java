package com.seke.autocomplete.lib;

import java.util.Arrays;
import java.util.HashSet;

public class Keywords {

	private static final String[] KEYWORDS = { 
			/*  PYTHON  */
			"False", "class", "finally","is", "return", "None", "continue", "for",
			"lambda", "try", "True", "def", "from", "nonlocal", "while", "and",
			"del", "global", "not", "with", "as", "elif", "if", "or", "yield",
			"assert", "else", "import", "pass", "break", "except", "in", "raise",
			
			/* JAVA */
			"abstract","continue", "for", "new", "switch", "assert", "default", 
			"goto", "package", "synchronized", "boolean", "do", "if", "private", 
			"this","break", "double", "implements", "protected", "throw", "byte", 
			"else","import", "public", "throws", "case", "enum", "instanceof", 
			"transient", "catch", "extends", "int", "short", "try", "char", "final",
			"interface", "static", "void", "class", "finally", "long", "strictfp",
			"volatile", "const", "float", "native", "super", "while", "return",
			
			/* C */
			"auto", "double", "int", "struct", "break", "else", "long", "switch",
			"case", "enum", "register", "typedef", "char", "extern", "return",
			"union", "const", "float", "short", "unsigned", "continue", "for",
			"signed", "void", "default", "goto", "sizeof", "volatile", "do", "if",
			"while", "static", "include"
			
	};
	private static HashSet<String> set;

	public static boolean isKeyWord(String word) {
		if (set == null || set.isEmpty())
			set = new HashSet<>(Arrays.asList(KEYWORDS));
		return set.contains(word);
	}

}
