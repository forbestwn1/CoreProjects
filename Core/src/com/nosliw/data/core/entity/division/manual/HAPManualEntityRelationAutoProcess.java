package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualEntityRelationAutoProcess extends HAPManualEntityRelation{

	public static String AUTOPROCESS = "autoProcess";
	
	private boolean m_autoProcess;
	
	public HAPManualEntityRelationAutoProcess() {
		super(HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS);
	}
	
	public boolean isAutoProcess() {   return this.m_autoProcess;   }
}
