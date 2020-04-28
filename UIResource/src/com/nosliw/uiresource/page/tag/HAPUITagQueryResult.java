package com.nosliw.uiresource.page.tag;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPUITagQueryResult {

	private Map<String, String> m_attributes;
	
	private String m_tag;
	
	public HAPUITagQueryResult(String tag) {
		this.m_tag = tag;
		this.m_attributes = new LinkedHashMap<String, String>();
	}
	
	public String getTag() {    return this.m_tag;    }
	
	public Map<String, String> getAttributes(){   return this.m_attributes;    }
	
}
