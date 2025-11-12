package com.nosliw.core.application.entity.datarule.mandatory;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.brick.dataexpression.standalone.HAPBlockDataExpressionStandAloneImp;
import com.nosliw.core.application.entity.datarule.HAPDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginTransformerDataRule;

public class HAPPluginTransformerDataRuleMandatory implements HAPPluginTransformerDataRule{

	public HAPPluginTransformerDataRuleMandatory() {
	}
	
	@Override
	public HAPEntityOrReference transformDataRule(HAPDataRule dataRule, HAPDomainValueStructure valueStructureDomian) {
		return new HAPBlockDataExpressionStandAloneImp();
	}

}
