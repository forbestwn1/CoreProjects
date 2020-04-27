package com.nosliw.data.core.script.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPDefinitionReference;

public class HAPDefinitionScriptEntity extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String SCRIPT = "script";

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String REFERENCEMAPPING = "referenceMapping";

	private HAPScript m_script;
	
	private Map<String, HAPDefinitionReference> m_references;

	public HAPDefinitionScriptEntity() {
		this.m_references = new LinkedHashMap<String, HAPDefinitionReference>();
	}

	public HAPDefinitionScriptEntity(HAPScript script) {
		this();
		this.m_script = script;
	}

	public HAPScript getScript() {  return this.m_script;   }
	public void setScript(HAPScript script) {    this.m_script = script;     }
	
	public void addReference(HAPDefinitionReference reference) {  this.m_references.put(reference.getName(), reference);   }
	public Map<String, HAPDefinitionReference> getReference(){   return this.m_references;    }
	public HAPDefinitionReference getReference(String name) {   return this.m_references.get(name);    }

	public HAPDefinitionScriptEntity cloneScriptEntityDefinition() {
		HAPDefinitionScriptEntity out = new HAPDefinitionScriptEntity();
		out.m_script = this.m_script.cloneScript();
		for(String key : this.m_references.keySet()) {
			out.m_references.put(key, this.m_references.get(key).cloneReferenceDefinition());
		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_script = HAPScript.newScript(jsonObj);
		JSONArray refJsonArray = jsonObj.optJSONArray(HAPDefinitionExpression.REFERENCEMAPPING);
		if(refJsonArray!=null) {
			for(int i=0; i<refJsonArray.length(); i++) {
				JSONObject refJsonObj = refJsonArray.getJSONObject(i);
				HAPDefinitionReference ref = new HAPDefinitionReference();
				ref.buildObject(refJsonObj, HAPSerializationFormat.JSON);
				this.addReference(ref);
			}
		}
		return true;  
	}
}
