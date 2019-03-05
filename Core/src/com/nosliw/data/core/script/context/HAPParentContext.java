package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPParentContext {

	private Map<String, HAPContextGroup> m_parents;
	private List<String> m_parentNames;
	
	public HAPParentContext() {
		this.m_parents = new LinkedHashMap<String, HAPContextGroup>();
		this.m_parentNames = new ArrayList<String>();
	}
	
	static public HAPParentContext createDefault(HAPContextGroup parent) {
		HAPParentContext out = new HAPParentContext();
		out.addContext(null, parent);
		return out;
	}
	
	public HAPParentContext addContext(String name, HAPContextGroup context) {
		if(this.isSelf(name))  return this;   //ignore self parent
		
		if(HAPBasicUtility.isStringEmpty(name))  name = HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		this.m_parents.put(name, context);
		this.m_parentNames.add(name);
		return this;
	}
	
	public HAPContextGroup getContext(String name, HAPContextGroup self) {
		if(this.isSelf(name))   return self;
		return this.m_parents.get(name);	
	}
	
	public List<String> getNames(){  return this.m_parentNames;  }	

	private boolean isSelf(String name) {	return HAPConstant.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name);  }
}
