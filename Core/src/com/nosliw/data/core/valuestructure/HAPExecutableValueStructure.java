package com.nosliw.data.core.valuestructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

//flat context represent result of flat a context group to context
//one root in context group named Abc will generate two element in context: local Abc and global Abc___categary
//A has local relative parent of A__categary
//Also, we can find global name by local name
//flat context is back compatible with context
@HAPEntityWithAttribute
public class HAPExecutableValueStructure extends HAPSerializableImp
{

	@HAPAttribute
	public static final String ROOT = "root";

	@HAPAttribute
	public static final String NAME2ID = "name2Id";

	private Map<String, HAPRootStructure> m_roots;
	
	private Map<String, String> m_name2Id;
	
	public HAPExecutableValueStructure() {
		this.m_roots = new LinkedHashMap<String, HAPRootStructure>();
		this.m_name2Id = new LinkedHashMap<String, String>();
	}

	public HAPRootStructure getRoot(String localId) {  return this.m_roots.get(localId); }

	public void addRoot(HAPRootStructure root) {		this.m_roots.put(root.getLocalId(), root); 	}

	public void addNameMapping(String name, String id) {  this.m_name2Id.put(name, id); 	}
	
	public Set<HAPRootStructure> getAllRoots(){	return new HashSet<HAPRootStructure>(this.m_roots.values());	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		Map<String, String> map = new LinkedHashMap<String, String>();
		for(String varId : this.m_roots.keySet()) {
			map.put(varId, HAPUtilityJson.buildJson(this.m_roots.get(varId), HAPSerializationFormat.JSON));
		}
		jsonMap.put(ROOT, HAPUtilityJson.buildMapJson(map));
		jsonMap.put(NAME2ID, HAPUtilityJson.buildMapJson(m_name2Id));
	}

	public HAPExecutableValueStructure cloneExecutableStructure() {
		HAPExecutableValueStructure out = new HAPExecutableValueStructure();
		for(String varId : this.m_roots.keySet()) {
			out.m_roots.put(varId, this.m_roots.get(varId).cloneRoot());
		}
		
		out.m_name2Id.putAll(this.m_name2Id);
		return out;
	}
}
