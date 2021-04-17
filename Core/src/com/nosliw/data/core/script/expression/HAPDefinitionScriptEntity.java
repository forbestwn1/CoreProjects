package com.nosliw.data.core.script.expression;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

public class HAPDefinitionScriptEntity extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String SCRIPT = "script";

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String REFERENCEMAPPING = "referenceMapping";

	private HAPScript m_script;
	
	public HAPDefinitionScriptEntity() {
	}

	public HAPDefinitionScriptEntity(HAPScript script) {
		this();
		this.m_script = script;
	}

	public HAPScript getScript() {  return this.m_script;   }
	public void setScript(HAPScript script) {    this.m_script = script;     }
	
	public HAPDefinitionScriptEntity cloneScriptEntityDefinition() {
		HAPDefinitionScriptEntity out = new HAPDefinitionScriptEntity();
		this.cloneToEntityInfo(out);
		out.m_script = this.m_script.cloneScript();
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_script = HAPScript.newScript(jsonObj);
		return true;  
	}
}
