package com.nosliw.common.pattern;

public abstract class HAPPatternProcessorImp implements HAPPatternProcessor{
	@Override
	public String getName(){
		return this.getClass().getName();
	}

	public String getTestSuiteName(){
		return "PatternProcessor";
	}
	
	public String getTestSuiteDescription(){
		return "Processing string patterns";
	}

}
