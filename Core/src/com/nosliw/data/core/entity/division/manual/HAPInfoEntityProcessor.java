package com.nosliw.data.core.entity.division.manual;

public class HAPInfoEntityProcessor {

	private HAPPluginParserEntity m_parserPlugin;
	
	private HAPPluginProcessorEntityDefinition m_processPlugin;
	
	public HAPInfoEntityProcessor(HAPPluginParserEntity parserPlugin, HAPPluginProcessorEntityDefinition processPlugin) {
		this.m_parserPlugin = parserPlugin;
		this.m_processPlugin = processPlugin;
	}
	
	public HAPPluginParserEntity getParserPlugin() {    return this.m_parserPlugin;     }
	
	public HAPPluginProcessorEntityDefinition getProcessorPlugin() {    return this.m_processPlugin;    }
	
}
