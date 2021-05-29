package com.nosliw.data.core.valuestructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.structure.HAPRoot;

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

	private Map<String, HAPRoot> m_roots;
	
	public HAPExecutableValueStructure() {
		this.m_roots = new LinkedHashMap<String, HAPRoot>();
	}

	public HAPRoot getRoot(String localId) {  return this.m_roots.get(localId); }

	public void addRoot(HAPRoot root) {
		this.m_roots.put(root.getName(), root);
	}

	public Set<HAPRoot> getAllRoots(){	return new HashSet<HAPRoot>(this.m_roots.values());	}
	
	public Set<String> getAllNames(){
		Set<String> out = new HashSet<String>();
		out.addAll(this.m_roots.keySet());
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		Map<String, String> map = new LinkedHashMap<String, String>();
		for(String varId : this.m_roots.keySet()) {
			map.put(varId, HAPJsonUtility.buildJson(this.m_roots.get(varId), HAPSerializationFormat.JSON));
		}
	}

	public HAPExecutableValueStructure cloneExecutableStructure() {
		HAPExecutableValueStructure out = new HAPExecutableValueStructure();
		for(String varId : this.m_roots.keySet()) {
			out.m_roots.put(varId, this.m_roots.get(varId).cloneRoot());
		}
		return out;
	}
}
