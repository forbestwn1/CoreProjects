package com.nosliw.core.application.division.manual.common.valuecontext;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.m.HAPManualBrick;
import com.nosliw.core.application.valueport.HAPValuePort;
import com.nosliw.core.xxx.application1.valuestructure.HAPDomainValueStructure;

public class HAPManualValuePortValueContext extends HAPValuePort{

	private HAPManualValueContext m_valueContext;
	private HAPDomainValueStructure m_valueStructureDomain;
	
	public HAPManualValuePortValueContext(HAPManualBrick complexEntityExe, HAPDomainValueStructure valueStructureDomain) {
		super(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT, HAPConstantShared.IO_DIRECTION_BOTH);
		this.m_valueContext = complexEntityExe.getManualValueContext();
		this.m_valueStructureDomain = valueStructureDomain;
	}
}
