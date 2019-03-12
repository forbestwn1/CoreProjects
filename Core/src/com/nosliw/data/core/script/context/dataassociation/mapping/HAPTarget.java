package com.nosliw.data.core.script.context.dataassociation.mapping;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;

public class HAPTarget {

	private HAPContextDefinitionRootId m_rootNodeId;
	
	private String m_targetName;
	
	public HAPTarget(String leterate) {
		String[] segs = HAPNamingConversionUtility.parseLevel1(leterate);
		if(segs.length==1) {
			this.m_rootNodeId = new HAPContextDefinitionRootId(segs[0]);
		}
		else if(segs.length==2) {
			this.m_rootNodeId = new HAPContextDefinitionRootId(segs[1]);
			this.m_targetName = segs[0];
		}
		else {}
		this.init();
	}

	private void init() {
		if(HAPBasicUtility.isStringEmpty(this.m_targetName)) {
			this.m_targetName = HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		}
	}
	
	public HAPContextDefinitionRootId getRootNodeId() {    return this.m_rootNodeId;    }
	
	public String getTargetName() {   return this.m_targetName;    }
	
}
