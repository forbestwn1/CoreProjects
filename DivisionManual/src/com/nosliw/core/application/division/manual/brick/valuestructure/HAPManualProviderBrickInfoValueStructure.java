package com.nosliw.core.application.division.manual.brick.valuestructure;

import org.springframework.stereotype.Component;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualProviderBrickInfoImp;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@Component
public class HAPManualProviderBrickInfoValueStructure extends HAPManualProviderBrickInfoImp{

	private HAPManagerDataRule m_dataRuleMan;
	
	public HAPManualProviderBrickInfoValueStructure(HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan, HAPManagerDataRule dataRuleMan) {
		super(manualBrickMan, brickMan);
		this.m_dataRuleMan = dataRuleMan;
	}
	
	@Override
	public HAPIdBrickType getBrickTypeId() {  return HAPManualEnumBrickType.VALUESTRUCTURE_100;   }

	@Override
	protected HAPManualInfoBrickType newBrickTypeInfo() {   return new HAPManualInfoBrickType(false);  }

	@Override
	protected HAPManualDefinitionPluginParserBrick newBrickParser() {
		return new HAPManualPluginParserBrickImpValueStructure(this.getManualBrickManager(), this.getBrickManager(), this.m_dataRuleMan);
	}

	@Override
	protected HAPManualPluginProcessorBrick newBrickProcessor() {   return null;  }

}
