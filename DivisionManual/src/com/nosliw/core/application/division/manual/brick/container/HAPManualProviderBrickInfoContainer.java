package com.nosliw.core.application.division.manual.brick.container;

import org.springframework.stereotype.Component;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualProviderBrickInfoImp;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImp;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;

@Component
public class HAPManualProviderBrickInfoContainer extends HAPManualProviderBrickInfoImp{

	public HAPManualProviderBrickInfoContainer(HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		super(manualBrickMan, brickMan);
	}
	
	@Override
	public HAPIdBrickType getBrickTypeId() {  return HAPEnumBrickType.CONTAINERLIST_100;   }

	@Override
	protected HAPManualInfoBrickType newBrickTypeInfo() {   return new HAPManualInfoBrickType(false);  }

	@Override
	protected HAPManualDefinitionPluginParserBrick newBrickParser() {
		return new HAPManualDefinitionPluginParserBrickImp(HAPEnumBrickType.CONTAINERLIST_100, HAPManualDefinitionBrickContainerList.class, this.getManualBrickManager(), this.getBrickManager());
	}

	@Override
	protected HAPManualPluginProcessorBrick newBrickProcessor() {   return new HAPManualPluginProcessorBlockContainerList();  }

}
