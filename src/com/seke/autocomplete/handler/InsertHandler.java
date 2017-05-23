package com.seke.autocomplete.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import com.seke.autocomplete.core.AutoHint;

public class InsertHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {

		AutoHint hint = AutoHint.getInstance();
		if (hint.getStyledText() == null) {
			IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (activeWorkbenchWindow == null) {
				return null;
			}
			IEditorPart activeEditor = activeWorkbenchWindow.getActivePage()
					.getActiveEditor();
			if (activeEditor instanceof AbstractDecoratedTextEditor) {
				AbstractDecoratedTextEditor editor = (AbstractDecoratedTextEditor) activeEditor;
				Object adapter = (Control) editor.getAdapter(Control.class);
				if (adapter instanceof Control) {
					Control control = (Control) adapter;
					if (control instanceof StyledText) {
						StyledText styledText = (StyledText) control;
						hint.setStyledText(styledText);
					}
				}
			}
		}
		hint.doInsert();

		return null;
	}

}
