package com.nosliw.core.application.uitag;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUITagDefinition{

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";

	
	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String BASE = "base";
	@HAPAttribute
	public static final String SCRIPT = "script";
	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";
	@HAPAttribute
	public static final String VALUESTRUCTUREEXE = "valueStructureExe";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";
	
	
	private HAPUITagValueContextDefinition m_valueContext;
	
	private HAPResourceId m_scriptResourceId;
	
	public HAPUITagValueContextDefinition getValueContext() {    return this.m_valueContext;     }
	public void setValueContext(HAPUITagValueContextDefinition valueContext) {    this.m_valueContext = valueContext;       }
	
	public HAPResourceId getScriptResourceId() {     return this.m_scriptResourceId;     }
	public void setScriptResourceId(HAPResourceId scriptResourceId) {     this.m_scriptResourceId = scriptResourceId;         }

}
