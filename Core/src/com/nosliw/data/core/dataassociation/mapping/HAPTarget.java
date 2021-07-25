package com.nosliw.data.core.dataassociation.mapping;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.structure.HAPReferenceRoot;
import com.nosliw.data.core.structure.HAPReferenceRootUnknowType;

public class HAPTarget {

	private HAPReferenceRoot m_rootReference;
	
	private String m_valueStructure;
	
	public HAPTarget(String leterate) {
		String[] segs = HAPUtilityNamingConversion.parseLevel1(leterate);
		if(segs.length==1) {
			this.m_rootReference = new HAPReferenceRootUnknowType(segs[0]);
		}
		else if(segs.length==2) {
			this.m_rootReference = new HAPReferenceRootUnknowType(segs[1]);
			this.m_valueStructure = segs[0];
		}
		else {}
		this.init();
	}

	private void init() {
		if(HAPBasicUtility.isStringEmpty(this.m_valueStructure)) {
			this.m_valueStructure = HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		}
	}
	
	public HAPReferenceRoot getRootNodeReference() {    return this.m_rootReference;    }
	
	public String getValueStructureName() {   return this.m_valueStructure;    }
	
}
