package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import org.springframework.stereotype.Component;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualProviderBrickInfoImp;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;

@Component
public class HAPManualProviderBrickInfoDataExpressionElementInLibrary extends HAPManualProviderBrickInfoImp{

	private HAPParserDataExpression m_dataExpressionParser;
	
	public HAPManualProviderBrickInfoDataExpressionElementInLibrary(HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan, HAPParserDataExpression dataExpressionParser) {
		super(manualBrickMan, brickMan);
		this.m_dataExpressionParser = dataExpressionParser;
	}
	
	@Override
	public HAPIdBrickType getBrickTypeId() {  return HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100;   }

	@Override
	protected HAPManualInfoBrickType newBrickTypeInfo() {   return new HAPManualInfoBrickType(true);  }

	@Override
	protected HAPManualDefinitionPluginParserBrick newBrickParser() {
		return new HAPManualPluginParserBlockDataExpressionElementInLibrary(this.getManualBrickManager(), this.getBrickManager(), this.m_dataExpressionParser);
	}

	@Override
	protected HAPManualPluginProcessorBrick newBrickProcessor() {   return new HAPManualPluginProcessorBlockDataExpressionElementInLibrary();  }

}
