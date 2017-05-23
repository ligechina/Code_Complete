package com.seke.autocomplete.listener;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.seke.autocomplete.core.AutoHint;

public class KeyDownListener implements Listener {
	private static KeyDownListener listener;
	private KeyDownListener() {}
	public static KeyDownListener getListener() {
		if (listener==null) listener=new KeyDownListener();
		return listener;
	}
	public void handleEvent(Event event) {
		try {
			if (event.widget instanceof StyledText) {
				StyledText styledText=(StyledText) event.widget;
				if (styledText.isFocusControl())
					AutoHint.getInstance().doDelete();
				
			}
		}
		catch (Exception e) {}
	}
	
}
