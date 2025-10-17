package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualProviderBrickInfo;
import com.nosliw.core.application.division.manual.core.HAPManualProviderBrickInfoImp;
import com.nosliw.core.application.division.manual.core.HAPManualProviderBrickInfoMultiple;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;
import com.nosliw.core.application.entity.uitag.HAPManagerUITag;

@Component
public class HAPManualProviderBrickInfoMultipleUIContent implements HAPManualProviderBrickInfoMultiple{

	private List<HAPManualProviderBrickInfo> m_providers;
	
	private HAPManualManagerBrick m_manualBrickMan; 
	private HAPManagerApplicationBrick m_brickMan;
	private HAPParserDataExpression m_dataExpressionParser; 
	private HAPManagerWithVariablePlugin m_withVariableMan;
	private HAPManagerUITag m_uiTagMan;
	private HAPManagerDataRule m_dataRuleMan;
	
	public HAPManualProviderBrickInfoMultipleUIContent(
			HAPManualManagerBrick manualBrickMan, 
			HAPManagerApplicationBrick brickMan, 
			HAPParserDataExpression dataExpressionParser, 
			HAPManagerWithVariablePlugin withVariableMan,
			HAPManagerUITag uiTagMan,
			HAPManagerDataRule dataRuleMan) {
		this.m_providers = new ArrayList<HAPManualProviderBrickInfo>();
		this.m_manualBrickMan = manualBrickMan;
		this.m_brickMan = brickMan;
		this.m_dataExpressionParser = dataExpressionParser;
		this.m_withVariableMan = withVariableMan;
		this.m_uiTagMan = uiTagMan;
		this.m_dataRuleMan = dataRuleMan;
		
		m_providers.add(new HAPManualProviderBrickInfoImp(m_manualBrickMan, m_brickMan) {
			@Override
			public HAPIdBrickType getBrickTypeId() {   return HAPEnumBrickType.UICONTENT_100;  }
			@Override
			protected HAPManualDefinitionPluginParserBrick newBrickParser() {  return new HAPManualPluginParserBlockComplexUIContent(m_manualBrickMan, m_brickMan, m_dataRuleMan);  }
			@Override
			protected HAPManualPluginProcessorBrick newBrickProcessor() {  return new HAPManualPluginProcessorBlockUIContent(m_dataExpressionParser, m_withVariableMan);	}
			@Override
			protected HAPManualInfoBrickType newBrickTypeInfo() {  return new HAPManualInfoBrickType(true);  }
		});
		
		m_providers.add(new HAPManualProviderBrickInfoImp(manualBrickMan, brickMan) {
			@Override
			public HAPIdBrickType getBrickTypeId() {   return HAPEnumBrickType.UIPAGE_100; 	}
			@Override
			protected HAPManualDefinitionPluginParserBrick newBrickParser() {  return new HAPManualPluginParserBlockComplexUIPage(m_manualBrickMan, m_brickMan);	}
			@Override
			protected HAPManualPluginProcessorBrick newBrickProcessor() {    return new HAPManualPluginProcessorBlockUIPage();	}
			@Override
			protected HAPManualInfoBrickType newBrickTypeInfo() {  return new HAPManualInfoBrickType(true);  }
		});
		
		m_providers.add(new HAPManualProviderBrickInfoImp(manualBrickMan, brickMan) {
			@Override
			public HAPIdBrickType getBrickTypeId() {  return HAPEnumBrickType.UICUSTOMERTAG_100;	}
			@Override
			protected HAPManualDefinitionPluginParserBrick newBrickParser() {	return new HAPManualPluginParserBlockComplexUICustomerTag(m_manualBrickMan, m_brickMan, m_uiTagMan);	}
			@Override
			protected HAPManualPluginProcessorBrick newBrickProcessor() {   return new HAPManualPluginProcessorBlockUICustomerTag();	}
			@Override
			protected HAPManualInfoBrickType newBrickTypeInfo() {  return new HAPManualInfoBrickType(true);		}
		});
		
		m_providers.add(new HAPManualProviderBrickInfoImp(manualBrickMan, brickMan) {
			@Override
			public HAPIdBrickType getBrickTypeId() {  return HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100;	}
			@Override
			protected HAPManualDefinitionPluginParserBrick newBrickParser() {	return new HAPManualPluginParserBlockComplexUICustomerTagDebugger(m_manualBrickMan, m_brickMan, m_uiTagMan);	}
			@Override
			protected HAPManualPluginProcessorBrick newBrickProcessor() {   return new HAPManualPluginProcessorBlockUICustomerTagDebugger(); 	}
			@Override
			protected HAPManualInfoBrickType newBrickTypeInfo() {  return new HAPManualInfoBrickType(true);	}
		});
	}
	
	@Override
	public List<HAPManualProviderBrickInfo> getProviders() {
		return this.m_providers;
	}

}
