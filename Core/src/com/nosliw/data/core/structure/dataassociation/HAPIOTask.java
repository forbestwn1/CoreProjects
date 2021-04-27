package com.nosliw.data.core.structure.dataassociation;

import java.util.Map;

import com.nosliw.data.core.structure.story.HAPParentContext;

//input and result of task
public interface HAPIOTask {

	HAPParentContext getInContext();

	Map<String, HAPParentContext> getOutResultContext();

}
