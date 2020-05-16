package com.nosliw.uiresource.page.story;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPHtmlTag extends HAPHtml{

	private String m_tag;
	
	private Map<String, HAPTagAttribute> m_attributes;
	
	private HAPHtmlSegment m_html;

	public HAPHtmlTag(String tag) {
		this.m_attributes = new LinkedHashMap<String, HAPTagAttribute>();
		this.m_tag = tag;
	}
	
	public String getTag() {   return this.m_tag;    }
	
	public HAPHtmlSegment getHtmlContent() {  return this.m_html;    }
	
	public void addAttribute(HAPTagAttribute attribute) {  this.m_attributes.put(attribute.getName(), attribute);     }
	public Map<String, HAPTagAttribute> getAttributes(){  return this.m_attributes;   }

	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append("<");
		out.append(this.m_tag);
		out.append(" ");
		
		//attributes in tag
		for(String attrName : this.m_attributes.keySet()) {
			out.append(attrName);
			out.append("=\"");
			out.append(this.m_attributes.get(attrName).getValue());
			out.append("\" ");
		}
		out.append(">");
		out.append("\n");

		//tag content
		if(this.m_html!=null) {
			out.append(this.m_html.toString());
		}
		out.append("\n");
		
		//tag end
		out.append("</");
		out.append(this.m_tag);
		out.append(">");
		out.append("\n");
		return out.toString();
	}
}
