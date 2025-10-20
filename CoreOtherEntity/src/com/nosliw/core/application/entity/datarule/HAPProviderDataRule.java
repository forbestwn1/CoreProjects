package com.nosliw.core.application.entity.datarule;

public interface HAPProviderDataRule {

	String getDataRuleType();
	
	HAPPluginParserDataRule getParser();

	HAPPluginTransformerDataRule getTransformer();
}
