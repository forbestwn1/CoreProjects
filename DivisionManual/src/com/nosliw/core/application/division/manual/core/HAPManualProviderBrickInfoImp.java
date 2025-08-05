package com.nosliw.core.application.division.manual.core;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;

public abstract class HAPManualProviderBrickInfoImp implements HAPManualProviderBrickInfo{

	private HAPManualManagerBrick m_manualBrickMan;
	
	private HAPManagerApplicationBrick m_brickMan;
	
	private HAPManualInfoBrickType m_brickTypeInfo;
	
	private HAPManualDefinitionPluginParserBrick m_parser;
	
	private HAPManualPluginProcessorBrick m_processor;
	
	public HAPManualProviderBrickInfoImp(HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		this.m_manualBrickMan = manualBrickMan;
		this.m_brickMan = brickMan;
	}
	
	@Override
	public HAPManualDefinitionPluginParserBrick getBrickParser() {   
		if(this.m_parser==null) {
			this.m_parser = this.newBrickParser();
		}
		return this.m_parser;
	}
	abstract protected HAPManualDefinitionPluginParserBrick newBrickParser();
	
	@Override
	public HAPManualPluginProcessorBrick getBrickProcessor() {
		if(this.m_processor==null) {
			this.m_processor = this.newBrickProcessor();
		}
		return this.m_processor;
	}
	abstract protected HAPManualPluginProcessorBrick newBrickProcessor();

	@Override
	public HAPManualInfoBrickType getBrickTypeInfo() {
		if(this.m_brickTypeInfo==null) {
			this.m_brickTypeInfo = this.newBrickTypeInfo();
		}
		return this.m_brickTypeInfo;
	}
	abstract protected HAPManualInfoBrickType newBrickTypeInfo();

	protected HAPManualManagerBrick getManualBrickManager() {    return this.m_manualBrickMan;     }
	
	protected HAPManagerApplicationBrick getBrickManager() {    return this.m_brickMan;      } 

}
