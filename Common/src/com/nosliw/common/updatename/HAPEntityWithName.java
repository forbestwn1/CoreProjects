package com.nosliw.common.updatename;

import java.util.Set;

public interface HAPEntityWithName {

	Set<String> getVariableNames();
	
	Set<String> getConstantNames();
	
	void updateVariableNames(HAPUpdateName nameUpdate);	

	void updateConstantNames(HAPUpdateName nameUpdate);

}
