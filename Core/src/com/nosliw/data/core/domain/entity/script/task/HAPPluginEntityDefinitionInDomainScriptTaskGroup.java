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
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainScriptTaskGroup extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainScriptTaskGroup(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, HAPDefinitionEntityScriptTaskGroup.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJavascript(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		try {
			HAPDefinitionEntityScriptTaskGroup scriptTaskGroupDef = (HAPDefinitionEntityScriptTaskGroup)parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			
			Context cx = Context.enter();
	        Scriptable scope = cx.initStandardObjects(null);

			String varName = "varName";
			String content = "var "+varName+"="+(String)obj+";";
	        cx.evaluateString(scope, content, null, 1, null);
	        NativeObject scriptObj = (NativeObject)scope.get(varName, scope);

	        Object defScriptObj = scriptObj.get(HAPExecutableEntityScriptTaskGroup.DEFINITION);
	        if(defScriptObj!=null) {
	            JSONArray defJsonArray = (JSONArray)HAPUtilityRhinoValue.toJson(defScriptObj);
	            for(int i=0; i<defJsonArray.length(); i++) {
	            	JSONObject defJson = defJsonArray.getJSONObject(i);
	            	HAPDefinitionTaskScript def = new HAPDefinitionTaskScript();
	            	def.buildObject(defJson, HAPSerializationFormat.JSON);
	            	scriptTaskGroupDef.addDefinition(def);
	            }
	        }
	        
	        Object scriptScriptObj = scriptObj.get(HAPExecutableEntityScriptTaskGroup.SCRIPT);
	        String script = HAPUtilityRhinoValue.toJSStringValue(scriptScriptObj);
	        scriptTaskGroupDef.setScript(new HAPJsonTypeScript(script));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		try {
			HAPDefinitionEntityScriptTaskGroup scriptTaskGroupDef = (HAPDefinitionEntityScriptTaskGroup)parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
