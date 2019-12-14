package com.nosliw.uiresource.page.tag;

import java.io.File;
import java.util.Iterator;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.value.HAPRhinoDataUtility;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;

public class HAPUITagDefinitionParser {

	public static HAPUITagDefinition parseFromFile(File file){
		HAPUITagDefinition out = null;
		try {
			Context cx = Context.enter();
	        Scriptable scope = cx.initStandardObjects(null);

			String content = "var out="+HAPFileUtility.readFile(file) + "; out;";
			NativeObject defObjJS = (NativeObject)cx.evaluateString(scope, content, file.getName(), 1, null);

			String name = (String)defObjJS.get(HAPUITagDefinition.NAME);
	    	String script = Context.toString(defObjJS.get(HAPUITagDefinition.SCRIPT));
			out = new HAPUITagDefinition(new HAPUITagId(name), script);

			//parse context
			HAPUITagDefinitionContext context = out.getContext();
			NativeObject contextObj = (NativeObject)defObjJS.get(HAPUITagDefinition.CONTEXT);
			JSONObject contextJson = (JSONObject)HAPRhinoDataUtility.toJson(contextObj);
			HAPUITagDefinitionParser.parseContextInTagDefinition(contextJson, context);
			
			//parse dependency
			NativeObject requiresObj = (NativeObject)defObjJS.get(HAPUITagDefinition.REQUIRES);
			if(requiresObj!=null){
				JSONObject requiresJson = (JSONObject)HAPRhinoDataUtility.toJson(requiresObj);
				Iterator<String> typeIt = requiresJson.keys();
				while(typeIt.hasNext()){
					String resourceType = typeIt.next();
					JSONObject requiresForTypeJson = requiresJson.optJSONObject(resourceType);
					Iterator<String> aliasIt = requiresForTypeJson.keys();
					while(aliasIt.hasNext()){
						String alias = aliasIt.next();
						String resourceIdLiterate = requiresForTypeJson.optString(alias);
						out.addResourceDependency(new HAPResourceDependency(HAPResourceId.newInstance(resourceType, resourceIdLiterate), alias));
					}
				}
			}

			//attribute definition
			NativeArray attributesArrayObj = (NativeArray)defObjJS.get(HAPUITagDefinition.ATTRIBUTES);
			for(int i=0; i<attributesArrayObj.size(); i++) {
				JSONObject attrDefJson = (JSONObject)HAPRhinoDataUtility.toJson(attributesArrayObj.get(i));
				HAPUITagDefinitionAttribute attrDef = new HAPUITagDefinitionAttribute();
				attrDef.buildObject(attrDefJson, HAPSerializationFormat.JSON);
				out.addAttributeDefinition(attrDef);
			}
			
			//event definition
			NativeArray eventDefObjs = (NativeArray)defObjJS.get(HAPUITagDefinition.EVENT);
			if(eventDefObjs!=null) {
				for(int i=0; i<eventDefObjs.size(); i++) {
					JSONObject eventDefJson = (JSONObject)HAPRhinoDataUtility.toJson(eventDefObjs.get(i));
					HAPDefinitionUIEvent eventDef = new HAPDefinitionUIEvent();
					eventDef.buildObject(eventDefJson, HAPSerializationFormat.JSON);
					out.addEventDefinition(eventDef);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
            // Exit from the context.
            Context.exit();
        }	

		return out;
	}
	
	//parse 
	public static void parseContextInTagDefinition(JSONObject contextJson, HAPUITagDefinitionContext contextOut){
		HAPParserContext.parseContextGroup(contextJson, contextOut);
	}
}
