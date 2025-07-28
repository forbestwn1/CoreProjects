package com.nosliw.uiresource.page.story.design;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPHtmlTag extends HAPHtml{

	private String m_tag;
	
	private Map<String, HAPTagAttribute> m_attributes;
	
	private HAPHtmlSegment m_body;

	public HAPHtmlTag(String tag) {
		this.m_body = new HAPHtmlSegment();
		this.m_attributes = new LinkedHashMap<String, HAPTagAttribute>();
		this.m_tag = tag;
	}
	
	public String getTag() {   return this.m_tag;    }
	
	public HAPHtmlSegment getBody() {  return this.m_body;    }
	public void addBodySegment(HAPHtml html) {     this.m_body.addSegment(html);    }
	
	public void addAttribute(HAPTagAttribute attribute) {  this.m_attributes.put(attribute.getName(), attribute);     }
	public Map<String, HAPTagAttribute> getAttributes(){  return this.m_attributes;   }

	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append("<");
		out.append("nosliw-"+this.m_tag);
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
		if(this.m_body!=null) {
			out.append(this.m_body.toString());
		}
		out.append("\n");
		
		//tag end
		out.append("</");
		out.append("nosliw-"+this.m_tag);
		out.append(">");
		out.append("\n");
		return out.toString();
	}
}
