package com.nosliw.core.application.division.manual;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualBrickRelationAutoProcess extends HAPManualBrickRelation{

	public static String AUTOPROCESS = "autoProcess";
	
	private boolean m_autoProcess;
	
	public HAPManualBrickRelationAutoProcess() {
		super(HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS);
	}
	
	public boolean isAutoProcess() {   return this.m_autoProcess;   }
}
