package com.seke.autocomplete.editors;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.*;

public class PythonPartitionScanner extends RuleBasedPartitionScanner {
	public final static String PYTHON_COMMENT = "__python_comment";
	public final static String PYTHON_KEYWORD = "__python_keyword";

	public PythonPartitionScanner() {

		IToken comment = new Token(PYTHON_COMMENT);
		//IToken keyword = new Token(PYTHON_KEYWORD);

		ArrayList<IPredicateRule> arrayList = new ArrayList<>();
		arrayList.add(new EndOfLineRule("#", comment));
		/*
		 * arrayList.add(new SingleLineRule("import", " ", keyword));
		 * arrayList.add(new SingleLineRule("as", " ", keyword));
		 * arrayList.add(new SingleLineRule("with", " ", keyword));
		 * arrayList.add(new SingleLineRule("for", " ", keyword));
		 * arrayList.add(new SingleLineRule("from", " ", keyword));
		 * arrayList.add(new SingleLineRule("print", " ", keyword));
		 */

		setPredicateRules(arrayList.toArray(new IPredicateRule[0]));
	}
}
