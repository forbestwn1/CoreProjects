package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;

//entity info : name, description, info
//id
//context
//attachment mapping
//
public interface HAPComponent extends HAPResourceDefinitionComplex, HAPWithLifecycleAction, HAPWithEventHanlder, HAPWithProcess{

	@HAPAttribute
	public static String ID = "id";
	
	String getId();
	void setId(String id);

}
