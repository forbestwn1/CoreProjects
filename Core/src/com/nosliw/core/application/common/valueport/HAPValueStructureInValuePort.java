package com.nosliw.core.application.common.valueport;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueStructure;

public class HAPValueStructureInValuePort extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String ROOT = "root";

	private Map<String, HAPRootStructureInValuePort> m_roots;
	
	public HAPValueStructureInValuePort() {
		this.m_roots = new LinkedHashMap<String, HAPRootStructureInValuePort>();
	}

	public HAPRootStructureInValuePort addRoot(HAPRootStructureInValuePort root) {
		String name = root.getName();
		this.getRoots().put(name, root);
		return root;
	}

	public Map<String, HAPRootStructureInValuePort> getRoots(){	return this.m_roots;    }
	
	public HAPRootStructureInValuePort getRoot(String rootName) {
		HAPRootStructureInValuePort out = null;
		Map<String, HAPRootStructureInValuePort> roots = this.getRoots();
		out = roots.get(rootName);
		return out;
	}

	public Set<String> getRootNames(){   return this.getRoots().keySet();    }
	
	public HAPRootStructureInValuePort getRootByName(String rootName) {   return this.getRoots().get(rootName);  }
	
	public Set<HAPRootStructureInValuePort> getAllRoots(){   return new HashSet<HAPRootStructureInValuePort>(this.getRoots().values());      }

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPManualBrickValueStructure) {
			HAPManualBrickValueStructure context = (HAPManualBrickValueStructure)obj;
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
