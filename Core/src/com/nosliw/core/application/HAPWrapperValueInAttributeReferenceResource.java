package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;

public class HAPWrapperValueInAttributeReferenceResource extends HAPWrapperValueInAttribute{

	private HAPInfoResourceIdNormalize m_normalizedResourceId;
	
	//bundle related with resource
	private HAPBundle m_referBundle;
	//path to brick
	private HAPPath m_pathFromRoot;
	
	public HAPWrapperValueInAttributeReferenceResource(HAPInfoResourceIdNormalize normalizedResourceId) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID);
		this.m_normalizedResourceId = normalizedResourceId;
	}
	
	@Override
	public Object getValue() {     return this.m_normalizedResourceId;      }

	public HAPInfoResourceIdNormalize getNormalizedResourceId() {   return this.m_normalizedResourceId;    }
	
	public HAPBundle getReferencedBundle() {    return this.m_referBundle;     }
	
	public HAPPath getPathFromRoot() {    return  this.m_pathFromRoot;    }
	
	public void solidate(HAPManagerApplicationBrick brickMan) {
		this.m_referBundle = brickMan.getEntityBundle(m_normalizedResourceId.getRootResourceIdSimple());
		this.m_pathFromRoot = this.m_normalizedResourceId.getPath();
	}
}
