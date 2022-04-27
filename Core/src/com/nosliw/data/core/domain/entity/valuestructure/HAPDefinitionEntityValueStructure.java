package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.structure.HAPRootStructure;

public class HAPDefinitionEntityValueStructure extends HAPDefinitionEntityInDomainSimple{

	public static String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE;

	@HAPAttribute
	public static final String VALUE = "value";

	private Map<String, HAPRootStructure> m_rootByName;
	
	public HAPDefinitionEntityValueStructure() {
		this.m_rootByName = new LinkedHashMap<String, HAPRootStructure>();
	}
	
	public HAPRootStructure addRoot(HAPRootStructure root) {
		root = root.cloneRoot();
		String name = root.getName();
		this.m_rootByName.put(name, root);
		return root;
	}

	public Set<String> getRootNames(){   return this.m_rootByName.keySet();    }
	
	public HAPRootStructure getRootByName(String rootName) {   return this.m_rootByName.get(rootName);  }
	
	public Set<HAPRootStructure> getAllRoots(){   return new HashSet<HAPRootStructure>(this.m_rootByName.values());      }

	public List<HAPRootStructure> resolveRoot(String rootName, boolean createIfNotExist) {
		HAPRootStructure root = this.getRootByName(rootName);
		if(createIfNotExist && root==null) 	root = this.newRoot(rootName);
		if(root!=null) return Lists.newArrayList(root);
		else return Lists.newArrayList();
	}

	public HAPRootStructure newRoot(String name) {  
		HAPRootStructure root = new HAPRootStructure();
		root.setName(name);
		return this.addRoot(root);  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String rootName : this.m_rootByName.keySet()) {
			jsonMap.put(rootName, this.m_rootByName.get(rootName).toStringValue(HAPSerializationFormat.JSON));
		}
	}
	
	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntityDefinition entityDefDomain){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPDefinitionEntityValueStructure) {
			HAPDefinitionEntityValueStructure context = (HAPDefinitionEntityValueStructure)obj;
			if(context.getRootNames().equals(this.getRootNames())) {
				for(String eleName : this.getRootNames()) {
					out = this.getRootByName(eleName).equals(context.getRootByName(eleName));
					if(!out)  
						break;
				}
				return true;
			}
		}
		return out;
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityValueStructure out = new HAPDefinitionEntityValueStructure();
		this.cloneToDefinitionEntityInDomain(out);
		for(HAPRootStructure root : this.m_rootByName.values()) {
			out.addRoot(root.cloneRoot());
		}
		return out;
	}

	public HAPDefinitionEntityValueStructure cloneValueStructure(){  return (HAPDefinitionEntityValueStructure)this.cloneEntityDefinitionInDomain();	}
	
}
