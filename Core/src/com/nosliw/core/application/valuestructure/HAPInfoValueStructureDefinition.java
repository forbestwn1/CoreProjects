package com.nosliw.core.application.valuestructure;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

@HAPEntityWithAttribute
public class HAPInfoValueStructureDefinition extends HAPSerializableImp{

	@HAPAttribute
	public static final String ROOT = "root";

	private Map<String, HAPRootInValueStructure> m_roots;
	
	public HAPInfoValueStructureDefinition() {
		this.m_roots = new LinkedHashMap<String, HAPRootInValueStructure>();
	}
	
	public HAPInfoValueStructureDefinition(Set<HAPRootInValueStructure> roots) {
		this();
		for(HAPRootInValueStructure root : roots) {
			this.m_roots.put(root.getName(), root);
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ROOT, HAPSerializeManager.getInstance().toStringValue(m_roots, HAPSerializationFormat.JSON));
	}

}
