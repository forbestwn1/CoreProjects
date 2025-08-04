package com.nosliw.core.application.division.manua;

import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrick;

public class HAPManualInfoBrickProcessor {

	private HAPManualDefinitionPluginParserBrick m_parserPlugin;
	
	private HAPManualPluginProcessorBrick m_processPluginBrick;
	
	private HAPManualPluginProcessorAdapter m_processorPluginAdapter;
	
	public HAPManualInfoBrickProcessor(HAPManualDefinitionPluginParserBrick parserPlugin, HAPManualPluginProcessorBrick processPlugin) {
		this.m_parserPlugin = parserPlugin;
		this.m_processPluginBrick = processPlugin;
	}
	
	public HAPManualDefinitionPluginParserBrick getParserPlugin() {    return this.m_parserPlugin;     }
	
	public HAPManualPluginProcessorBrick getProcessorPlugin() {    return this.m_processPluginBrick;    }
	
}
