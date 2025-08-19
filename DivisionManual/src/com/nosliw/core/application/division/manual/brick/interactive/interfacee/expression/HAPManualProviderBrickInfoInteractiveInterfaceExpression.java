package com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression;

import org.springframework.stereotype.Component;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualProviderBrickInfoImp;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;

@Component
public class HAPManualProviderBrickInfoInteractiveInterfaceExpression extends HAPManualProviderBrickInfoImp{

	public HAPManualProviderBrickInfoInteractiveInterfaceExpression(HAPManualManagerBrick manualBrickMan,
			HAPManagerApplicationBrick brickMan) {
		super(manualBrickMan, brickMan);
	}

	@Override
	public HAPIdBrickType getBrickTypeId() {   return HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100;   }

	@Override
	protected HAPManualDefinitionPluginParserBrick newBrickParser() {
		return new HAPManualPluginParserBlockSimpleInteractiveInterfaceExpression(this.getManualBrickManager(), this.getBrickManager());
	}

	@Override
	protected HAPManualPluginProcessorBrick newBrickProcessor() {
		return new HAPManualPluginProcessorBlockSimpleInteractiveInterfaceExpression();
	}

	@Override
	protected HAPManualInfoBrickType newBrickTypeInfo() {  return new HAPManualInfoBrickType(false);  }

}
