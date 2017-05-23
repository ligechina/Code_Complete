package com.seke.autocomplete.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.seke.autocomplete.core.AutoHint;

public class KeyUpListener implements Listener {

	private static KeyUpListener listener;
	private KeyUpListener() {}
	public static KeyUpListener getListener() {
		if (listener==null) listener=new KeyUpListener();
		return listener;
	}
	
	public void handleEvent(Event event) {
		try {
			if (event.widget instanceof StyledText) {
				StyledText styledText=(StyledText) event.widget;
				if (styledText.isFocusControl()) {
					if (Character.isLetterOrDigit(event.character)
							|| "`~!@#$%^&*()_+-=[]{}\\|;:'\"<>?,./"
								.indexOf(event.character) >= 0) {
						AutoHint.getInstance().doAdd();
					}
					else if (event.keyCode == SWT.BS) {
						AutoHint.getInstance().updateCompleteMap();
					}
				}
			}
		}
		catch (Exception e) {}
	}
	
}
