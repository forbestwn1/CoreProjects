package com.nosliw.data.core.script.context.dataassociation;

import java.util.Map;

import com.nosliw.data.core.script.context.HAPParentContext;

//input and result of task
public interface HAPIOTask {

	HAPParentContext getInContext();

	Map<String, HAPParentContext> getOutResultContext();

}
