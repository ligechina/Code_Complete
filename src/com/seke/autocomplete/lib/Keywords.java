package com.seke.autocomplete.lib;

import java.util.Arrays;
import java.util.HashSet;

public class Keywords {

	private static final String[] KEYWORDS = { "False", "class", "finally",
			"is", "return", "None", "continue", "for", "lambda", "try", "True",
			"def", "from", "nonlocal", "while", "and", "del", "global", "not",
			"with", "as", "elif", "if", "or", "yield", "assert", "else",
			"import", "pass", "break", "except", "in", "raise" };
	private static HashSet<String> set;

	public static boolean isKeyWord(String word) {
		if (set == null || set.isEmpty())
			set = new HashSet<>(Arrays.asList(KEYWORDS));
		return set.contains(word);
	}

}
