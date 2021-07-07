package com.nosliw.data.core.task;

public class HAPInfoTask {

	private HAPParserTask m_parser;
	
	private HAPProcessorTask m_processor;
	
	public HAPInfoTask(HAPParserTask parser, HAPProcessorTask processor) {
		this.m_parser = parser;
		this.m_processor = processor;
	}
	
	public HAPParserTask getParser() {	return this.m_parser;	}
	
	public HAPProcessorTask getProcessor() {   return this.m_processor;  }
	
}
