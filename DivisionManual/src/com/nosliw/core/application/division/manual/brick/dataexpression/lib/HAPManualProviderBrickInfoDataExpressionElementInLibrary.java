package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import org.springframework.stereotype.Component;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualProviderBrickInfoImp;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@Component
public class HAPManualProviderBrickInfoDataExpressionElementInLibrary extends HAPManualProviderBrickInfoImp{

	private HAPParserDataExpression m_dataExpressionParser;
	
	private HAPManagerDataRule m_dataRuleMan;
	
	private HAPManagerWithVariablePlugin m_withVariableMan;
	
	public HAPManualProviderBrickInfoDataExpressionElementInLibrary(
			HAPManualManagerBrick manualBrickMan, 
			HAPManagerApplicationBrick brickMan, 
			HAPParserDataExpression dataExpressionParser,
			HAPManagerWithVariablePlugin withVariableMan,
			HAPManagerDataRule dataRuleMan) {
		super(manualBrickMan, brickMan);
		this.m_dataExpressionParser = dataExpressionParser;
		this.m_withVariableMan = withVariableMan;
		this.m_dataRuleMan = dataRuleMan;
	}
	
	@Override
	public HAPIdBrickType getBrickTypeId() {  return HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100;   }

	@Override
	protected HAPManualInfoBrickType newBrickTypeInfo() {   return new HAPManualInfoBrickType(true);  }

	@Override
	protected HAPManualDefinitionPluginParserBrick newBrickParser() {
		return new HAPManualPluginParserBlockDataExpressionElementInLibrary(this.getManualBrickManager(), this.getBrickManager(), this.m_dataExpressionParser, this.m_dataRuleMan);
	}

	@Override
	protected HAPManualPluginProcessorBrick newBrickProcessor() {   return new HAPManualPluginProcessorBlockDataExpressionElementInLibrary(this.m_withVariableMan);  }

}
