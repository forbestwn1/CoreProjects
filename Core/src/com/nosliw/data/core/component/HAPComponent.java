package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPContextGroup;

//entity info : name, description, info
//id
//context
//attachment mapping
//
public interface HAPComponent extends HAPEntityInfo, HAPWithAttachment, HAPWithLifecycleAction, HAPWithEventHanlder{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String CONTEXT = "context";
	
	String getComponentType();

	void setResourceId(HAPResourceId resourceId);
	HAPResourceId getResourceId();
	
	String getId();
	void setId(String id);

	HAPContextGroup getContext();
	void setContext(HAPContextGroup context);

	HAPChildrenComponentIdContainer getChildrenComponentId();

}
