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
	
	public HAPConfigureProcessorRelative() {
		//init default value
		this.setValue(RESOLVEREFERENCE_CONFIGURE, new HAPConfigureResolveStructureElementReference());
		this.setValue(TRACKTOSOLID, false);
		this.setValue(INHERITRULE, true);
		this.setValue(TOLERATENOPARENT, false);
	}
	
	public HAPConfigureResolveStructureElementReference getResolveStructureElementReferenceConfigure() {   return (HAPConfigureResolveStructureElementReference)this.getValue(RESOLVEREFERENCE_CONFIGURE) ;      }
	
	public boolean isInheritRule() {    return (Boolean)this.getValue(INHERITRULE);	}
	
	public boolean isTrackingToSolid() {   return (Boolean)this.getValue(TRACKTOSOLID);    }
	
	public boolean isTolerantNoParentForRelative() {   return (Boolean)this.getValue(TOLERATENOPARENT);   }
	
	public void mergeHard(HAPConfigureProcessorRelative configure) {
		
	}
}
