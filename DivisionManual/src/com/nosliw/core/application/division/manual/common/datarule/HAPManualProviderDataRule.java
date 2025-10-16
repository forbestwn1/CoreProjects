package com.nosliw.core.application.division.manual.common.datarule;

public interface HAPManualProviderDataRule {

	String getDataRuleType();
	
	HAPManualTransformerDataRule getTransformer();
	
}
