package com.seke.autocomplete.core;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;

import com.seke.autocomplete.lib.ColorConstants;
import com.seke.autocomplete.lib.Keywords;
import com.seke.autocomplete.lib.Preference;

public class AutoHint {
	private int start = 0, end = 0, ne = 0;

	private StyledText styledText;
	private String[] candidates;

	private static AutoHint hint;
	private TreeMap<Integer, Integer> map;

	public static AutoHint getInstance() {
		if (hint == null)
			hint = new AutoHint();
		return hint;
	}

	private AutoHint() {
		map=new TreeMap<>();
	}

	public void setStyledText(StyledText styledText) {
		this.styledText = styledText;
		start=end=ne=0; candidates=null;
		map=new TreeMap<>();
	}

	public StyledText getStyledText() {
		return styledText;
	}

	public void doDelete() {
		if (styledText == null)
			return;
		try {
			int offset = styledText.getCaretOffset();
			if (offset <= start && end > start && start >= 0) {
				String text = styledText.getText();
				styledText.replaceTextRange(start, text.length()-start, text.substring(end));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		start = end = ne = 0;
	}

	public void doAdd(boolean request) {
		if (styledText == null)
			return;
		int offset = styledText.getCaretOffset();
		
		Color hintColor=ColorConstants.getColor(
				Preference.getInstance().get("color_hint", 
						ColorConstants.toString(ColorConstants.darkGray)));
		Color hintColorBg=ColorConstants.getColor(
				Preference.getInstance().get("color_hint_bg", 
						ColorConstants.toString(ColorConstants.lightGray)));
		if ("#FFFFFF".equals(ColorConstants.toString(hintColorBg)))
			hintColorBg=null;
		
		try {
			String text=styledText.getText();
			int pos = offset - 1;
			while (pos >= 0 && isCharacter(text.charAt(pos)) && !Character.isDigit(text.charAt(pos)))
				pos--;
			pos++;
			String prefix = text.substring(0, pos);
			String word = text.substring(pos, offset);
			
			if (request || candidates == null)
				if (word.length()<=2)
					predict(prefix, word);
			

			if (candidates!=null && candidates.length > 0 && !(request && word.length()>2)) {

				// current indent
				int line_start = prefix.lastIndexOf('\n') + 1;
				int indent = 0;
				while (line_start < prefix.length()
						&& text.charAt(line_start) == ' ') {
					line_start++;
					indent++;
				}

				String[] temp = candidates.clone();
				
				if (!word.isEmpty() && candidates[0].startsWith(word)) {
					if (candidates[0].equals(word))
						candidates = Arrays.copyOfRange(candidates, 1,
								candidates.length);
					else {
						//if (word.length()==1)
							candidates[0] = candidates[0].substring(word.length());
						//else 
						//	candidates = new String[] {candidates[0].substring(word.length())};
					}
				}
				
				StringBuilder builder = new StringBuilder();
				for (String wd : candidates) {
					if (wd.equals("<ENTER>")) {
						builder.append("\n");
						for (int i = 0; i < indent; i++)
							builder.append(' ');
					} else if (wd.equals("<IND>")) {
						indent += 4;
						builder.append("    ");
					} else if (wd.equals("<UNIND>")) {
						if (builder.length() >= 4
								&& "    ".equals(builder.substring(builder
										.length() - 4))) {
							builder = builder.replace(builder.length() - 4,
									builder.length(), "");
							indent -= 4;
						}
					} else {
						builder.append(wd);
						builder.append(" ");
					}
				}
				
				
				String fill = builder.toString();
				start = offset;
				int le=start; if (end!=0) le=end;
				
				end = start + fill.length();
				if (fill.charAt(0) == '\n' || fill.charAt(0) == ' ') {
					ne = 0;
					while (ne < fill.length()
							&& (fill.charAt(ne) == '\n' || fill.charAt(ne) == ' ')) {
						ne++;
					}
					ne += start;
				} else {
					ne = start + candidates[0].length();
					if (temp.length > 1
							&& !temp[1].matches("[.({\\[,:)\\]}]")
							&& !isSpace(temp[1])
							&& !temp[0].matches("[.({\\[:]"))
						ne++;
					if (temp.length > 1
							&& temp[1].equals("[")
							&& !isCharacter(temp[0].charAt(temp[0].length() - 1)))
						ne++;
				}
				styledText.replaceTextRange(start, text.length() - start, fill
						+ text.substring(le));
				StyleRange styleRange = new StyleRange(start, fill.length(),
						hintColor, hintColorBg);
				styledText.setStyleRange(styleRange);
			}
			else {
				start = end = ne = 0;
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			start = end = ne = 0;
		} finally {
			styledText.setCaretOffset(offset);
			String text=styledText.getText().substring(0, offset);
			int from=0;
			int nl1=text.lastIndexOf('\n');
			if (nl1>0) {
				from=nl1+1;
			}
			setKeyWordColor(from);
			setCompletedColor(from);
		}
	}
	
	private void predict(String prefix, String word) {
		try {

			// `'` and `"`
			int q1 = 0, q2 = 0;
			for (char c : prefix.toCharArray()) {
				if (c == '\'')
					q1++;
				if (c == '"')
					q2++;
			}
			if (q1 % 2 != 0 || q2 % 2 != 0) {
				// in quote: do not hint
				start = end = ne = 0;
				candidates=null;
				return;
			}
			candidates = Predict.predict(prefix, word);
			
		}
		catch (Exception e) {candidates=null;}
	}

	public void doAdd() {
		doAdd(true);
	}

	private boolean isSpace(String word) {
		return word.equals("<ENTER>") || word.equals("<IND>")
				|| word.equals("<UNIND>");
	}
	
	private boolean isCharacter(char c) {
		return Character.isLetterOrDigit(c) || c=='_';
	}

	public void doInsert() {
		if (styledText == null || start == ne || candidates == null)
			return;
		styledText.setCaretOffset(ne);

		if (isSpace(candidates[0])) {
			int index = 0;
			while (index < candidates.length && isSpace(candidates[index]))
				index++;
			candidates = Arrays.copyOfRange(candidates, index,
					candidates.length);
		} else {
			candidates = Arrays.copyOfRange(candidates, 1, candidates.length);
			if (styledText.getText().charAt(ne - 1) == ' ')
				ne--;
			map.put(start, ne);

		}
		doAdd(false);
	}
	
	public void setKeyWordColor(int from) {
		int offset = styledText.getCaretOffset();
		String text = styledText.getText().substring(0, offset);
		int l = offset, s = from, e = from;
		
		Color keywordColor=ColorConstants.getColor(
				Preference.getInstance().get("color_keyword", "#0000FF"));
		
		while (true) {
			if (e < l && isCharacter(text.charAt(e))) {
				e++;
				continue;
			}
			if (s != e) {
				String word = text.substring(s, e);
				if (Keywords.isKeyWord(word)) {
					try {
						styledText.setStyleRange(new StyleRange(s, e - s,
								keywordColor, null));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				else {
					styledText.setStyleRange(new StyleRange(s, e-s, null, null));
				}
			} else {
				
				while (e<l && !isCharacter(text.charAt(e)))
					e++;
				styledText.setStyleRange(new StyleRange(s, e-s, null, null));
			}
			s = e;
			if (e >= l)
				break;
		}
	}

	public void setKeyWordColor() {
		try {
			setKeyWordColor(0);}
		catch (Exception e) {}
	}
	
	public void setCompletedColor(int from) {
		int offset = styledText.getCaretOffset();
		String text = styledText.getText();
		
		Color completedColorBg=ColorConstants.getColor(
				Preference.getInstance().get("color_completed_bg", "#FFFFFF"));
		Color completedColorBg2=ColorConstants.getColor(
				Preference.getInstance().get("color_completed_bg2", "#FFFFFF"));
		if ("#FFFFFF".equals(ColorConstants.toString(completedColorBg)))
			completedColorBg=null;
		if ("#FFFFFF".equals(ColorConstants.toString(completedColorBg2)))
			completedColorBg2=null;

		Map<Integer, Integer> subMap=new TreeMap<Integer, Integer>(map.subMap(from, offset));
		for (Map.Entry<Integer, Integer> entry: subMap.entrySet()) {
			int st=entry.getKey(), ed=entry.getValue();
			ed=Math.min(ed, offset);
			if (ed!=entry.getValue())
				map.put(st, ed);
			try {
				Color color=completedColorBg;
				if (isCharacter(text.charAt(st)) && st>0 && isCharacter(text.charAt(st-1))) {
					color=completedColorBg2;
				}
				
				if (ed < offset && text.charAt(ed) == ' ') {
					styledText.setStyleRange(new StyleRange(ed, 1, null,
							null));
				}
				StyleRange[] styleRanges = styledText.getStyleRanges(
						st, ed - st);
				StyleRange curRange = new StyleRange(st, ed
						- st, null, color);
				if (styleRanges != null && styleRanges.length > 0) {
					if (ColorConstants.toString(styleRanges[0].background)
							.equals(ColorConstants.toString(completedColorBg)))
						continue;
					curRange.foreground = styleRanges[0].foreground;
				}
				styledText.setStyleRange(curRange);
			} catch (Exception e) {}
		}
	}

	public void setCompletedColor() {
		try {
		setCompletedColor(0);
		}catch (Exception e) {}
	}
	
	public void updateCompleteMap() {
		if (map==null || map.size()==0) return;
		Map.Entry<Integer, Integer> entry = map.lastEntry();
		map.put(entry.getKey(), Math.min(entry.getValue(), styledText.getCaretOffset()));
	}

	class Interval implements Comparable<Interval> {
		int start, end;

		public Interval(int s, int e) {
			start = s;
			end = e;
		}

		public int compareTo(Interval o) {
			return start - o.start;
		}
	}

}
