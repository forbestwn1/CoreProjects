package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;

public class HAPWrapperValueInAttributeReferenceResource extends HAPWrapperValueInAttribute implements HAPWithBrick{

	private HAPResourceId m_resourceId;
	
	private HAPReferenceBrickGlobal m_blockRef;
	
	public HAPWrapperValueInAttributeReferenceResource(HAPResourceId resourceId) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID);
		this.m_resourceId = resourceId;
	}
	
	@Override
	public Object getValue() {     return this.m_resourceId;      }

	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	public HAPInfoResourceIdNormalize getNormalizedResourceId() {   return HAPUtilityResourceId.normalizeResourceId(m_resourceId);    }
	
	public HAPBundle getReferencedBundle() {    return this.m_referBundle;     }
	
	public HAPPath getPathFromRoot() {    return  this.m_pathFromRoot;    }
	
	@Override
	public HAPBrick getBrick() {    return this.m_referBundle.getBrickByPath(m_pathFromRoot);    }

	public void solidate(HAPManagerApplicationBrick brickMan) {
		this.m_referBundle = brickMan.getBrickBundle(m_normalizedResourceId.getRootResourceIdSimple());
		this.m_pathFromRoot = this.m_normalizedResourceId.getPath();
	}
}
