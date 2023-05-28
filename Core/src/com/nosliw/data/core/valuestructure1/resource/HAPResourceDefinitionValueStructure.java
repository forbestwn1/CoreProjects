package com.nosliw.data.core.valuestructure1.resource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPResourceDefinitionValueStructure extends HAPResourceDefinition{

	@HAPAttribute
	public static String ROOT = "root";

	@HAPAttribute
	public static String REFERENCE = "reference";

	private List<HAPRootStructure> m_roots;
	
	private List<HAPInfoEntityReference> m_references;
	
	public HAPResourceDefinitionValueStructure() {
		this.m_roots = new ArrayList<HAPRootStructure>();
		this.m_references = new ArrayList<HAPInfoEntityReference>();
	}
	
	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE;  }

	public void addRoot(HAPRootStructure root) {     this.m_roots.add(root);     }
	
	public List<HAPRootStructure> getRoots(){    return this.m_roots;     }
	
	public List<HAPInfoEntityReference> getReferences(){   return this.m_references;    }
	
	public void addReference(HAPInfoEntityReference reference) {   this.m_references.add(reference);    }
}
