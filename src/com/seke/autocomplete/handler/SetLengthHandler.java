package com.seke.autocomplete.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.seke.autocomplete.core.AutoHint;
import com.seke.autocomplete.lib.Preference;

public class SetLengthHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String lenString=Preference.getInstance().get("hint_length", "50");
		InputDialog inputDialog=new InputDialog(HandlerUtil.getActiveShell(event), 
				"设置提示长度", "请输入自动补全的提示长度(0-100)。", lenString, 
				new IInputValidator() {
					public String isValid(String newText) {
						try {
							int v=Integer.parseInt(newText);
							if (v<0 || v>100) throw new Exception();
							return null;
						}
						catch (Exception e)  {
							return "请输入一个0-100以内的数字。";
						}
					}
				});
		inputDialog.setBlockOnOpen(true);
		inputDialog.open();
		String nval=inputDialog.getValue();
		if (nval!=null) {
			Preference.getInstance().put("hint_length", nval);
			try {
				AutoHint.getInstance().doDelete();
				AutoHint.getInstance().doAdd();
			}
			catch (Exception e) {}
		}
		
		return null;
	}

}
