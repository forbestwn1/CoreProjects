package com.nosliw.uiresource.page.template;

import java.util.ArrayList;
import java.util.List;

public class HAPHtmlSegment extends HAPHtml{

	private List<HAPHtml> m_segments;
	
	public HAPHtmlSegment() {
		this.m_segments = new ArrayList<HAPHtml>();
	}
	
	public void addSegment(HAPHtml seg) {
		this.m_segments.add(seg);
	}
	
	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		for(HAPHtml seg : this.m_segments) {
			out.append(seg.toString());
			out.append("\n");
		}
		return out.toString();
	}
	
}
