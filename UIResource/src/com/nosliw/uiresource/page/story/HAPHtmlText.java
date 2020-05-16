package com.nosliw.uiresource.page.story;

public class HAPHtmlText extends HAPHtml{

	private String m_text;
	
	public HAPHtmlText(String text) {
		this.m_text = text;
	}
	
	public String getText() {   return this.m_text;   }
	
	@Override
	public String toString() {
		return this.m_text;
	}
	
}
