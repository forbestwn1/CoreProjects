package com.nosliw.data.core.task;

public interface HAPExecutableTask extends HAPExecutable{

	void setId(String id);
	
	String getName();
	
//	Map<String, HAPMatchers> getVariableMatchers();
	
}
