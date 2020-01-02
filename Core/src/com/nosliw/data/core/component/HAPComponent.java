package com.nosliw.data.core.component;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.script.context.HAPContextGroup;

//entity info : name, description, info
//id
//context
//attachment mapping
//
public interface HAPComponent extends HAPEntityInfo, HAPWithAttachment{

//	String getComponentType();
	
	String getId();
	void setId(String id);

	HAPContextGroup getContext();
	void setContext(HAPContextGroup context);
	
	HAPChildrenComponentIdContainer getChildrenComponentId();

}
