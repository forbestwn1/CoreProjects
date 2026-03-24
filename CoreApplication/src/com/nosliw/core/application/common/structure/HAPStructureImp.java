package com.nosliw.core.application.common.structure;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPStructureImp extends HAPSerializableImp implements HAPStructure{

	private Map<String, HAPRootInStructure> m_roots;
	
	public HAPStructureImp() {
		this.m_roots = new LinkedHashMap<String, HAPRootInStructure>();
	}
	
	public HAPStructureImp(Set<HAPRootInStructure> roots) {
		this();
		for(HAPRootInStructure root : roots) {
			this.m_roots.put(root.getName(), root);
		}
	}
	
	@Override
	public HAPRootInStructure addRoot(HAPRootInStructure root) {
		String name = root.getName();
		this.getRoots().put(name, root);
		return root;
	}

	@Override
	public HAPRootInStructure updateRoot(String name, HAPRootInStructure root) {
		m_roots.remove(name);
		if(HAPUtilityEntityInfo.isEnabled(root)){
			return this.addRoot(root);
		}
		else {
			return null;
		}
	}

	@Override
	public Map<String, HAPRootInStructure> getRoots(){	return this.m_roots;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ROOT, HAPManagerSerialize.getInstance().toStringValue(m_roots, HAPSerializationFormat.JSON));
	}

}
