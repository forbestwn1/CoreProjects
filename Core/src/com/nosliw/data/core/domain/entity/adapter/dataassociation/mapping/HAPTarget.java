package com.nosliw.data.core.domain.entity.adapter.dataassociation.mapping;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.structure.HAPReferenceRootInStrucutre;
import com.nosliw.data.core.structure.HAPReferenceRootUnknowType;

public class HAPTarget {

	private HAPReferenceRootInStrucutre m_rootReference;
	
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
		if(HAPUtilityBasic.isStringEmpty(this.m_valueStructure)) {
			this.m_valueStructure = HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		}
	}
	
	public HAPReferenceRootInStrucutre getRootNodeReference() {    return this.m_rootReference;    }
	
	public String getValueStructureName() {   return this.m_valueStructure;    }
	
}
