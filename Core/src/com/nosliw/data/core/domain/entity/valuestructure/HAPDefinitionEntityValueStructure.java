package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

@HAPEntityWithAttribute
public class HAPDefinitionEntityValueStructure extends HAPDefinitionEntityInDomainSimple{

	public static String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE;

	@HAPAttribute
	public static final String VALUE = "value";

	public HAPDefinitionEntityValueStructure() {
		this.setNormalAttributeObject(VALUE, new HAPEmbededDefinition(new LinkedHashMap<String, HAPRootStructure>()));
	}

	public HAPRootStructure addRoot(HAPRootStructure root) {
		root = root.cloneRoot();
		String name = root.getName();
		this.getRoots().put(name, root);
		return root;
	}

	public Map<String, HAPRootStructure> getRoots(){  
		return (Map<String, HAPRootStructure>)this.getNormalAttributeValue(VALUE, new LinkedHashMap<String, HAPRootStructure>());
	}

	public Set<String> getRootNames(){   return this.getRoots().keySet();    }
	
	public HAPRootStructure getRootByName(String rootName) {   return this.getRoots().get(rootName);  }
	
	public Set<HAPRootStructure> getAllRoots(){   return new HashSet<HAPRootStructure>(this.getRoots().values());      }

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
		return out;
	}

	public HAPDefinitionEntityValueStructure cloneValueStructure(){  return (HAPDefinitionEntityValueStructure)this.cloneEntityDefinitionInDomain();	}
	
}
