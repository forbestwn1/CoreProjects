package com.nosliw.data.core.entity.valuestructure;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;

@HAPEntityWithAttribute
public class HAPInfoValueStructureDefinition extends HAPSerializableImp{

	@HAPAttribute
	public static final String ROOT = "root";

	private Map<String, HAPRootStructure> m_roots;
	
	public HAPInfoValueStructureDefinition(Set<HAPRootStructure> roots) {
		this.m_roots = new LinkedHashMap<String, HAPRootStructure>();
		for(HAPRootStructure root : roots) {
			this.m_roots.put(root.getName(), root);
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ROOT, HAPSerializeManager.getInstance().toStringValue(m_roots, HAPSerializationFormat.JSON));
	}

}
