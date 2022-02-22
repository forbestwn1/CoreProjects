package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplexImp;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureInComplex;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.resource.HAPResourceDefinitionOrId;
import com.nosliw.data.core.resource.HAPResourceId;

public abstract class HAPResourceDefinitionComplexImp extends HAPDefinitionEntityComplexImp implements HAPResourceDefinition1{

	private HAPResourceId m_resourceId;
	
	private HAPPathLocationBase m_localReferenceBase;

	public HAPResourceDefinitionComplexImp() {
	}

	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	@Override
	public String getResourceType() {   return this.getResourceId().getResourceType();  }

	@Override
	public void setResourceId(HAPResourceId resourceId) {  this.m_resourceId = resourceId;   }
	@Override
	public HAPResourceId getResourceId() {   return this.m_resourceId;   }
	
	public void setDefaultValueStructure(HAPValueStructureInComplex valueStructure) {	HAPUtilityComplexValueStructure.setValueStructureDefault(this, valueStructure);	}
	
	@Override
	public HAPResourceDefinitionOrId getChild(String path) {   return null;    }

	//path base for local resource reference
	@Override
	public HAPPathLocationBase getLocalReferenceBase() {   return this.m_localReferenceBase;	}

	@Override
	public void setLocalReferenceBase(HAPPathLocationBase localRefBase) {   this.m_localReferenceBase = localRefBase;  }
	
	//all external resource reffered by this complex resource, for debug purpose
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {	return null;	}

	@Override
	public void cloneToResourceDefinition(HAPResourceDefinition1 resourceDef) {
		resourceDef.setResourceId(this.getResourceId()==null?null:this.getResourceId().clone());
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

	void cloneToComplexResourceDefinition(HAPDefinitionResourceComplex complexEntity, boolean cloneValueStructure);

}
