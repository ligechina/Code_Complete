package com.seke.autocomplete.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import com.seke.autocomplete.lib.ColorConstants;

public class PythonConfiguration extends SourceViewerConfiguration {

	public PythonConfiguration() {
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				PythonPartitionScanner.PYTHON_COMMENT,
				PythonPartitionScanner.PYTHON_KEYWORD };
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		/*NonRuleBasedDamagerRepairer keyword = new NonRuleBasedDamagerRepairer(
				new TextAttribute(ColorConstants.blue));
		reconciler.setDamager(keyword, PythonPartitionScanner.PYTHON_KEYWORD);
		reconciler.setRepairer(keyword, PythonPartitionScanner.PYTHON_KEYWORD);*/

		NonRuleBasedDamagerRepairer comment = new NonRuleBasedDamagerRepairer(
				new TextAttribute(ColorConstants.gray));
		reconciler.setDamager(comment, PythonPartitionScanner.PYTHON_COMMENT);
		reconciler.setRepairer(comment, PythonPartitionScanner.PYTHON_COMMENT);

		return reconciler;
	}

}