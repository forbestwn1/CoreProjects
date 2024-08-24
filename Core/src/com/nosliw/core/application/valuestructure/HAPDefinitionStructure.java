package com.nosliw.core.application.valuestructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.structure.HAPRootInStructure;

@HAPEntityWithAttribute
public class HAPDefinitionStructure extends HAPSerializableImp{

	@HAPAttribute
	public static final String ROOT = "root";

	private Map<String, HAPRootInStructure> m_roots;
	
	public HAPDefinitionStructure() {
		this.m_roots = new LinkedHashMap<String, HAPRootInStructure>();
	}
	
	public HAPDefinitionStructure(Set<HAPRootInStructure> roots) {
		this();
		for(HAPRootInStructure root : roots) {
			this.m_roots.put(root.getName(), root);
		}
	}
	
	public Set<HAPRootInStructure> getRoots(){
		return new HashSet<HAPRootInStructure>(this.m_roots.values());
	}
	
	public HAPRootInStructure getRootByName(String rootName) {   return this.m_roots.get(rootName);  }

	public Set<String> getRootNames(){   return this.m_roots.keySet();    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ROOT, HAPManagerSerialize.getInstance().toStringValue(m_roots, HAPSerializationFormat.JSON));
	}

}
