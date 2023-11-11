package com.nosliw.data.core.domain.entity.script.task;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.value.HAPUtilityRhinoValue;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainScriptTaskGroup extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainScriptTaskGroup(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, HAPDefinitionEntityScriptTaskGroup.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		try {
			HAPDefinitionEntityScriptTaskGroup scriptTaskGroupDef = (HAPDefinitionEntityScriptTaskGroup)parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			
			String content = (String)obj;
			
			Context cx = Context.enter();
	        Scriptable scope = cx.initStandardObjects(null);

	        cx.evaluateString(scope, content, null, 1, null);
	        NativeObject scriptObj = (NativeObject)scope.get("nosliw", scope);

	        Object defScriptObj = scriptObj.get(HAPDefinitionEntityScriptTaskGroup.DEFINITION);
	        if(defScriptObj!=null) {
	            JSONArray defJsonArray = (JSONArray)HAPUtilityRhinoValue.toJson(defScriptObj);
	            for(int i=0; i<defJsonArray.length(); i++) {
	            	JSONObject defJson = defJsonArray.getJSONObject(i);
	            	HAPDefinitionTaskScript def = new HAPDefinitionTaskScript();
	            	def.buildObject(defJson, HAPSerializationFormat.JSON);
	            	scriptTaskGroupDef.addDefinition(def);
	            }
	        }
	        
	        Object scriptScriptObj = scriptObj.get(HAPDefinitionEntityScriptTaskGroup.SCRIPT);
	        scriptTaskGroupDef.setScript(new HAPJsonTypeScript(scriptScriptObj.toString()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
