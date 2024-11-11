package com.nosliw.core.application.common.structure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPValueStructureDefinitionImp extends HAPSerializableImp implements HAPValueStructureDefinition{

	private Map<String, HAPRootInStructure> m_roots;
	
	private Object m_initValue;
	
	public HAPValueStructureDefinitionImp() {
		this.m_roots = new LinkedHashMap<String, HAPRootInStructure>();
	}

	@Override
	public void setInitValue(Object initValue) {	this.m_initValue = initValue; 	}
	@Override
	public Object getInitValue() {    return this.m_initValue;     } 
	
	@Override
	public HAPRootInStructure addRoot(HAPRootInStructure root) {
		String name = root.getName();
		this.getRoots().put(name, root);
		return root;
	}

	@Override
	public Map<String, HAPRootInStructure> getRoots(){	return this.m_roots;    }
	
	public Set<String> getRootNames(){   return this.getRoots().keySet();    }
	
	public HAPRootInStructure getRootByName(String rootName) {   return this.getRoots().get(rootName);  }
	
	public Set<HAPRootInStructure> getAllRoots(){   return new HashSet<HAPRootInStructure>(this.getRoots().values());      }


	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ROOT, HAPManagerSerialize.getInstance().toStringValue(this.m_roots, HAPSerializationFormat.JSON));
		jsonMap.put(INITVALUE, HAPManagerSerialize.getInstance().toStringValue(this.m_initValue, HAPSerializationFormat.JSON));
		
	}


}
