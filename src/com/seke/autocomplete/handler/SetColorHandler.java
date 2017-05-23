package com.seke.autocomplete.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.handlers.HandlerUtil;

import com.seke.autocomplete.core.AutoHint;
import com.seke.autocomplete.lib.ColorConstants;
import com.seke.autocomplete.lib.Preference;

public class SetColorHandler extends AbstractHandler {
	
	static private final int COLORDIALOG_WIDTH = 222; 
	static private final int COLORDIALOG_HEIGHT = 306; 

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String name=event.getParameter("color");
		Shell parentShell=HandlerUtil.getActiveShell(event);
		final Shell centerShell = new Shell(parentShell, SWT.NO_TRIM); 
		centerShell.setLocation( 
				(parentShell.getSize().x - COLORDIALOG_WIDTH) / 2, 
				(parentShell.getSize().y - COLORDIALOG_HEIGHT) / 2); 
		ColorDialog colorDialog = new ColorDialog(centerShell, 
					SWT.APPLICATION_MODAL); 
		colorDialog.setText("«Î—°‘Ò—’…´");
		String color=Preference.getInstance().get(name);
		colorDialog.setRGB(ColorConstants.getRGB(color));
		RGB ncolor=colorDialog.open();
		if (ncolor!=null) {
			Preference.getInstance().put(name, ColorConstants.toString(ncolor));
			if (name.contains("hint")) {
				try {
					AutoHint.getInstance().doDelete();
					AutoHint.getInstance().doAdd(false);
				} catch (Exception e) {}
			}
			else {
				try {
					AutoHint.getInstance().setKeyWordColor();
					AutoHint.getInstance().setCompletedColor();
				}catch (Exception e) {}
			}
		}
		return null;
	}

}
