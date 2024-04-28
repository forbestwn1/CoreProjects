package com.nosliw.core.application.division.manual;

public class HAPManualInfoBrickProcessor {

	private HAPPluginParserBrick m_parserPlugin;
	
	private HAPPluginProcessorBlock m_processPluginBrick;
	
	private HAPPluginProcessorAdapter m_processorPluginAdapter;
	
	public HAPManualInfoBrickProcessor(HAPPluginParserBrick parserPlugin, HAPPluginProcessorBlock processPlugin) {
		this.m_parserPlugin = parserPlugin;
		this.m_processPluginBrick = processPlugin;
	}
	
	public HAPPluginParserBrick getParserPlugin() {    return this.m_parserPlugin;     }
	
	public HAPPluginProcessorBlock getProcessorPlugin() {    return this.m_processPluginBrick;    }
	
}
