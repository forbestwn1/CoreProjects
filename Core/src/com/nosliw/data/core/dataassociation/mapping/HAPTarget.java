package com.nosliw.data.core.dataassociation.mapping;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.structure.HAPIdContextDefinitionRoot;

public class HAPTarget {

	private HAPIdContextDefinitionRoot m_rootNodeId;
	
	private String m_targetName;
	
	public HAPTarget(String leterate) {
		String[] segs = HAPNamingConversionUtility.parseLevel1(leterate);
		if(segs.length==1) {
			this.m_rootNodeId = new HAPIdContextDefinitionRoot(segs[0]);
		}
		else if(segs.length==2) {
			this.m_rootNodeId = new HAPIdContextDefinitionRoot(segs[1]);
			this.m_targetName = segs[0];
		}
		else {}
		this.init();
	}

	private void init() {
		if(HAPBasicUtility.isStringEmpty(this.m_targetName)) {
			this.m_targetName = HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		}
	}
	
	public HAPIdContextDefinitionRoot getRootNodeId() {    return this.m_rootNodeId;    }
	
	public String getTargetName() {   return this.m_targetName;    }
	
}
