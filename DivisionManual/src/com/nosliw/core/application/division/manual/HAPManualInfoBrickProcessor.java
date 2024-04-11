package com.nosliw.core.application.division.manual;

public class HAPManualInfoBrickProcessor {

	private HAPPluginParserBrick m_parserPlugin;
	
	private HAPPluginProcessorBrick m_processPlugin;
	
	public HAPManualInfoBrickProcessor(HAPPluginParserBrick parserPlugin, HAPPluginProcessorBrick processPlugin) {
		this.m_parserPlugin = parserPlugin;
		this.m_processPlugin = processPlugin;
	}
	
	public HAPPluginParserBrick getParserPlugin() {    return this.m_parserPlugin;     }
	
	public HAPPluginProcessorBrick getProcessorPlugin() {    return this.m_processPlugin;    }
	
}
