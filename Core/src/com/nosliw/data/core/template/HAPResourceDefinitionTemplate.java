package com.nosliw.data.core.template;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.component.HAPContainerChildReferenceResource;
import com.nosliw.data.core.component.HAPResourceDefinitionComplexImp;
import com.nosliw.data.core.resource.dynamic.HAPParmDefinition;

public class HAPResourceDefinitionTemplate extends HAPResourceDefinitionComplexImp{

	public static final String BUILDERID = "builderId";

	public static final String PARMDEF = "parmDef";

	private String m_builderId;
	
	private Map<String, Set<HAPParmDefinition>> m_parmDef;

	public HAPResourceDefinitionTemplate() {
		this.m_parmDef = new LinkedHashMap<String, Set<HAPParmDefinition>>();
	}
	
	public String getBuilderId() {   return this.m_builderId;     }
	public void setBuilderId(String builderId) {    this.m_builderId = builderId;     }

	public Set<HAPParmDefinition> getParmsDef(String setName){    return this.m_parmDef.get(setName);     }
	
	public void addParmDefinition(String setName, HAPParmDefinition parmDef) {
		Set<HAPParmDefinition> parmSet = this.m_parmDef.get(setName);
		if(parmSet==null) {
			parmSet = new HashSet<HAPParmDefinition>();
			this.m_parmDef.put(setName, parmSet);
		}
		parmSet.add(parmDef);
	}
	
	@Override
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {
		// TODO Auto-generated method stub
		return null;
	}
}
