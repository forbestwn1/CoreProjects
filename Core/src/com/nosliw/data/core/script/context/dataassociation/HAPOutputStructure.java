package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPContextStructure;

public class HAPOutputStructure {

	private Map<String, HAPContextStructure> m_contextStructrue;

	public HAPOutputStructure() {
		this.m_contextStructrue = new LinkedHashMap<String, HAPContextStructure>();
	}

	public Set<String> getNames(){   return this.m_contextStructrue.keySet();   };
	
	public Map<String, HAPContextStructure> getOutputStructures() {		return this.m_contextStructrue;	}
	
	public HAPContextStructure getOutputStructure(String name) {   return this.m_contextStructrue.get(name);   }

	public HAPContextStructure getOutputStructure() {	return this.m_contextStructrue.get(HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT);	}
	
	public void addOutputStructure(String name, HAPContextStructure structure) {   this.m_contextStructrue.put(name, structure);   }

	public void addOutputStructure(HAPContextStructure structure) {   this.m_contextStructrue.put(HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT, structure);   }
}
