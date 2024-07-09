package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrick;

public class HAPManualInfoBrickProcessor {

	private HAPManualDefinitionPluginParserBrick m_parserPlugin;
	
	private HAPPluginProcessorBrick m_processPluginBrick;
	
	private HAPPluginProcessorAdapter m_processorPluginAdapter;
	
	public HAPManualInfoBrickProcessor(HAPManualDefinitionPluginParserBrick parserPlugin, HAPPluginProcessorBrick processPlugin) {
		this.m_parserPlugin = parserPlugin;
		this.m_processPluginBrick = processPlugin;
	}
	
	public HAPManualDefinitionPluginParserBrick getParserPlugin() {    return this.m_parserPlugin;     }
	
	public HAPPluginProcessorBrick getProcessorPlugin() {    return this.m_processPluginBrick;    }
	
}
