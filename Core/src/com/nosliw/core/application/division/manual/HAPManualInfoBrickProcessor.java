package com.nosliw.core.application.division.manual;

public class HAPManualInfoBrickProcessor {

	private HAPPluginParserBrick m_parserPlugin;
	
	private HAPPluginProcessorBrickDefinition m_processPlugin;
	
	public HAPManualInfoBrickProcessor(HAPPluginParserBrick parserPlugin, HAPPluginProcessorBrickDefinition processPlugin) {
		this.m_parserPlugin = parserPlugin;
		this.m_processPlugin = processPlugin;
	}
	
	public HAPPluginParserBrick getParserPlugin() {    return this.m_parserPlugin;     }
	
	public HAPPluginProcessorBrickDefinition getProcessorPlugin() {    return this.m_processPlugin;    }
	
}
