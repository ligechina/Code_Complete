package com.seke.autocomplete;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.seke.autocomplete.core.AutoHint;
import com.seke.autocomplete.listener.PythonEditorListener;

public class StartUp implements IStartup {

	@Override
	public void earlyStartup() {
		
		final IWorkbench wb = PlatformUI.getWorkbench();
		final Display display = wb.getDisplay();
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
				if (window!=null) {
					window.getPartService().addPartListener(new PythonEditorListener());
					IWorkbenchPage activePage = window.getActivePage();
					if (activePage!=null) {
						PythonEditorListener.addListener(activePage);
						AutoHint.getInstance().setKeyWordColor();
						AutoHint.getInstance().setCompletedColor();
						
					}
				}
				
			}
		});

	}

}
