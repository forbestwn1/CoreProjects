package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPParentContext {

	private Map<String, HAPContextStructure> m_parents;
	private List<String> m_parentNames;
	
	public HAPParentContext() {
		this.m_parents = new LinkedHashMap<String, HAPContextStructure>();
		this.m_parentNames = new ArrayList<String>();
	}
	
	static public HAPParentContext createDefault(HAPContextStructure parent) {
		HAPParentContext out = new HAPParentContext();
		if(parent!=null)	out.addContext(null, parent);
		return out;
	}
	
	public HAPParentContext addContext(String name, HAPContextStructure context) {
		if(context==null)  return this;
		if(this.isSelf(name))  return this;   //ignore self parent
		
		if(HAPBasicUtility.isStringEmpty(name))  name = HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		this.m_parents.put(name, context);
		this.m_parentNames.add(name);
		return this;
	}
	
	public boolean isEmpty() {  return this.m_parentNames.isEmpty();  }
	
	public HAPContextStructure getContext(String name) {	return this.m_parents.get(name);	}

	public HAPContextStructure getContext() {	return this.m_parents.get(HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT);	}

	public List<String> getNames(){  return this.m_parentNames;  }	

	private boolean isSelf(String name) {	return HAPConstant.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name);  }
	
	public HAPParentContext cloneParentContext() {
		HAPParentContext out = new HAPParentContext();
		for(String name : this.getNames()) {
			out.addContext(name, this.getContext(name));
		}
		return out;
	}
}
