package com.nosliw.core.application.common.valueport;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;

public class HAPValueStructure {

	@HAPAttribute
	public static final String VALUE = "value";

	private Map<String, HAPRootStructure> m_roots;
	
	public HAPValueStructure() {
		this.m_roots = new LinkedHashMap<String, HAPRootStructure>();
	}

	public HAPRootStructure addRoot(HAPRootStructure root) {
		root = root.cloneRoot();
		String name = root.getName();
		this.getRoots().put(name, root);
		return root;
	}

	public Map<String, HAPRootStructure> getRoots(){	return this.m_roots;    }
	
	public HAPRootStructure getRoot(String rootName, boolean createIfNotExist) {
		HAPRootStructure out = null;
		Map<String, HAPRootStructure> roots = this.getRoots();
		out = roots.get(rootName);
		if(createIfNotExist==true&&out==null) {
			out = new HAPRootStructure();
			roots.put(rootName, out);
		}
		return out;
	}

	public Set<String> getRootNames(){   return this.getRoots().keySet();    }
	
	public HAPRootStructure getRootByName(String rootName) {   return this.getRoots().get(rootName);  }
	
	public Set<HAPRootStructure> getAllRoots(){   return new HashSet<HAPRootStructure>(this.getRoots().values());      }

	public List<HAPRootStructure> resolveRoot(String rootName, boolean createIfNotExist) {
		HAPRootStructure root = this.getRootByName(rootName);
		if(createIfNotExist && root==null) {
			root = this.newRoot(rootName);
		}
		if(root!=null) {
			return Lists.newArrayList(root);
		} else {
			return Lists.newArrayList();
		}
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
					if(!out) {
						break;
					}
				}
				return true;
			}
		}
		return out;
	}

}
