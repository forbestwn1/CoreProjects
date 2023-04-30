package com.nosliw.data.core.structure;

import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPPathElementMapping {

	private Object m_fromConstant;
	
	private HAPMatchers m_matchers;
	
	private String m_path;
	
	public HAPPathElementMapping(String path, HAPMatchers matchers) {
		this.m_path = path;
		this.m_matchers = matchers;
	}
	
	public HAPPathElementMapping(Object fromConstant, HAPMatchers matchers) {
		this.m_fromConstant = fromConstant;
		this.m_matchers = matchers;
	}
	
	public String getPath() {    return this.m_path;      }
	
	public HAPMatchers getMatcher() {   return this.m_matchers;    }
	
	public Object getFromConstant() {    return this.m_fromConstant;     }
	
}
