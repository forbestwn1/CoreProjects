package com.nosliw.core.application.common.structure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.core.application.valuestructure.HAPRootInValueStructure;

public class HAPValueStructureDefinitionImp implements HAPValueStructureDefinition{

	private Map<String, HAPRootInValueStructure> m_roots;
	
	private Object m_initValue;
	
	public HAPValueStructureDefinitionImp() {
		this.m_roots = new LinkedHashMap<String, HAPRootInValueStructure>();
	}

	public void setInitValue(Object initValue) {	this.m_initValue = initValue; 	}
	public Object getInitValue() {    return this.m_initValue;     } 
	
	public HAPRootInValueStructure addRoot(HAPRootInValueStructure root) {
		String name = root.getName();
		this.getRoots().put(name, root);
		return root;
	}

	public Map<String, HAPRootInValueStructure> getRoots(){	return this.m_roots;    }
	
	public Set<String> getRootNames(){   return this.getRoots().keySet();    }
	
	public HAPRootInValueStructure getRootByName(String rootName) {   return this.getRoots().get(rootName);  }
	
	public Set<HAPRootInValueStructure> getAllRoots(){   return new HashSet<HAPRootInValueStructure>(this.getRoots().values());      }
}
