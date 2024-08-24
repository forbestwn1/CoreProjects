package com.nosliw.core.application.common.structure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HAPValueStructureDefinitionImp implements HAPValueStructureDefinition{

	private Map<String, HAPRootInStructure> m_roots;
	
	private Object m_initValue;
	
	public HAPValueStructureDefinitionImp() {
		this.m_roots = new LinkedHashMap<String, HAPRootInStructure>();
	}

	public void setInitValue(Object initValue) {	this.m_initValue = initValue; 	}
	public Object getInitValue() {    return this.m_initValue;     } 
	
	public HAPRootInStructure addRoot(HAPRootInStructure root) {
		String name = root.getName();
		this.getRoots().put(name, root);
		return root;
	}

	public Map<String, HAPRootInStructure> getRoots(){	return this.m_roots;    }
	
	public Set<String> getRootNames(){   return this.getRoots().keySet();    }
	
	public HAPRootInStructure getRootByName(String rootName) {   return this.getRoots().get(rootName);  }
	
	public Set<HAPRootInStructure> getAllRoots(){   return new HashSet<HAPRootInStructure>(this.getRoots().values());      }
}
