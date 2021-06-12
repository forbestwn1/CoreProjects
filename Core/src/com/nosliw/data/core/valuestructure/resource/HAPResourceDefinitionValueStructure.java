package com.nosliw.data.core.valuestructure.resource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDefinitionImp;
import com.nosliw.data.core.structure.HAPRootStructure;

public class HAPResourceDefinitionValueStructure extends HAPResourceDefinitionImp{

	private List<HAPRootStructure> m_roots;
	
	public HAPResourceDefinitionValueStructure() {
		this.m_roots = new ArrayList<HAPRootStructure>();
	}
	
	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE;  }

	public void addRoot(HAPRootStructure root) {     this.m_roots.add(root);     }
	
	public List<HAPRootStructure> getRoots(){    return this.m_roots;     }
}
