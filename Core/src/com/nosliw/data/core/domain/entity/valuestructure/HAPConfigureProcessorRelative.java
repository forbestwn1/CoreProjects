package com.nosliw.data.core.domain.entity.valuestructure;

import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.data.core.structure.reference.HAPConfigureResolveStructureElementReference;

public class HAPConfigureProcessorRelative extends HAPInfoImpSimple{
	
	//how to resolve reference
	private static final String RESOLVEREFERENCE_CONFIGURE = "resolveReference";

	//how to handle rule defined in parent node
	private static final String INHERITRULE = "inheritRule";

	//relative can track to sold parent
	private static final String TRACKTOSOLID = "trackToSolid";

	//whether throw error when cannot find proper parent context item for relative item
	private static final String TOLERATENOPARENT = "tolerateNoParent";
	
	
	//how to resolve reference
	private HAPConfigureResolveStructureElementReference m_resolveStructureElementReferenceConfigure;

	//how to handle rule defined in parent node
	private boolean m_inheritRule = false;
	
	//relative can track to sold parent
	public boolean m_trackingToSolid = false;
	
	//whether throw error when cannot find proper parent context item for relative item
	public boolean m_tolerantNoParentForRelative = false;

	
	public HAPConfigureResolveStructureElementReference getResolveStructureElementReferenceConfigure() {   return (HAPConfigureResolveStructureElementReference)this.getValue(RESOLVEREFERENCE_CONFIGURE) ;      }
	
	public boolean isInheritRule() {    return this.m_inheritRule;	}
	
	public boolean isTrackingToSolid() {   return this.m_trackingToSolid;    }
	
	public boolean isTolerantNoParentForRelative() {   return this.m_tolerantNoParentForRelative;   }
	
	public void mergeHard(HAPConfigureProcessorRelative configure) {
		
	}
}
