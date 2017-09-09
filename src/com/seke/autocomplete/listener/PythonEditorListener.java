package com.seke.autocomplete.listener;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;

import com.seke.autocomplete.core.AutoHint;

public class PythonEditorListener implements IPartListener2 {

	@Override
	public void partActivated(IWorkbenchPartReference arg0) {
		addListener(arg0.getPage());
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference arg0) {
		addListener(arg0.getPage());
	}

	@Override
	public void partClosed(IWorkbenchPartReference arg0) {
		removeListener();
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference arg0) {
		removeListener();
	}

	@Override
	public void partHidden(IWorkbenchPartReference arg0) {
		//removeListener();
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference arg0) {
	}

	@Override
	public void partOpened(IWorkbenchPartReference arg0) {
		addListener(arg0.getPage());
	}

	@Override
	public void partVisible(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		addListener(arg0.getPage());
	}
	
	public static void addListener(IWorkbenchPage activePage) {
		// check valid
		if (activePage==null) return;
		IEditorPart editorPart = activePage.getActiveEditor();
		if (editorPart==null) return;
		/*if (!"com.seke.autocomplete.editors.PythonEditor".equals(editorPart.getEditorSite().getId()))
			return;*/
		
		String id = editorPart.getEditorSite().getId();
		if (id == null || !id.startsWith("com.seke.autocomplete.editors"))
			return;
		
		Display display=PlatformUI.getWorkbench().getDisplay();
		removeListener();
		try {
			StyledText styledText=AutoHint.getInstance().getStyledText();
			if (styledText==null || styledText.isDisposed() || !styledText.isEnabled()) {
				Control control=(Control) editorPart.getAdapter(Control.class);
				if (control instanceof StyledText) {
					StyledText nText = (StyledText) control;
					AutoHint.getInstance().setStyledText(nText);
				}
			}
			display.addFilter(SWT.KeyDown, KeyDownListener.getListener());
			display.addFilter(SWT.KeyUp, KeyUpListener.getListener());
		}
		catch (Exception e) {
		}
	}
	
	public static void removeListener() {
		Display display=PlatformUI.getWorkbench().getDisplay();
		display.removeFilter(SWT.KeyDown, KeyDownListener.getListener());
		display.removeFilter(SWT.KeyUp, KeyUpListener.getListener());
		try {
			AutoHint.getInstance().doDelete();
		}
		catch (Exception e) {}
		AutoHint.getInstance().setStyledText(null);
	}

}
