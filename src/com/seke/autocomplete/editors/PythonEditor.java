package com.seke.autocomplete.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class PythonEditor extends TextEditor {

	public PythonEditor() {
		super();
		setSourceViewerConfiguration(new PythonConfiguration());
		setDocumentProvider(new PythonDocumentProvider());
	}

	public void dispose() {
		super.dispose();
	}

}
