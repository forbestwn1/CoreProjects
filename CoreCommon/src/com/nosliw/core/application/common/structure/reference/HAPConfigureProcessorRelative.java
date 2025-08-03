package com.nosliw.core.application.common.structure.reference;

import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.core.xxx.application.valueport.HAPConfigureResolveElementReference;

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
		this.setValue(RESOLVEREFERENCE_CONFIGURE, new HAPConfigureResolveElementReference());
		this.setValue(TRACKTOSOLID, false);
		this.setValue(INHERITRULE, true);
		this.setValue(TOLERATENOPARENT, false);
	}
	
	public HAPConfigureResolveElementReference getResolveStructureElementReferenceConfigure() {   return (HAPConfigureResolveElementReference)this.getValue(RESOLVEREFERENCE_CONFIGURE) ;      }
	
	public boolean isInheritRule() {    return (Boolean)this.getValue(INHERITRULE);	}
	
	public boolean isTrackingToSolid() {   return (Boolean)this.getValue(TRACKTOSOLID);    }
	
	public boolean isTolerantNoParentForRelative() {   return (Boolean)this.getValue(TOLERATENOPARENT);   }
	
	public void mergeHard(HAPConfigureProcessorRelative configure) {
		
	}
}
