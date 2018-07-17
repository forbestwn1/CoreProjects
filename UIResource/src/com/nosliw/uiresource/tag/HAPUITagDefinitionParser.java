package com.nosliw.uiresource.tag;

import java.io.File;
import java.util.Iterator;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.js.rhino.HAPRhinoDataUtility;
import com.nosliw.uiresource.context.HAPContextParser;

public class HAPUITagDefinitionParser {

	public static HAPUITagDefinition parseFromFile(File file){
		HAPUITagDefinition out = null;
		try {
			Context cx = Context.enter();
	        Scriptable scope = cx.initStandardObjects(null);

			String content = "var out="+HAPFileUtility.readFile(file) + "; out;";
			NativeObject defObjJS = (NativeObject)cx.evaluateString(scope, content, file.getName(), 1, null);

			String name = (String)defObjJS.get(HAPUITagDefinition.NAME);
	    	String script = Context.toString((Function)defObjJS.get(HAPUITagDefinition.SCRIPT));
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
						out.addResourceDependency(new HAPResourceDependent(new HAPResourceId(resourceType, resourceIdLiterate), alias));
					}
				}
			}

			//attribute definition
			NativeArray attributesArrayObj = (NativeArray)defObjJS.get(HAPUITagDefinition.ATTRIBUTES);
			
			//event definition
			
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
		Boolean inherit = (Boolean)contextJson.opt(HAPUITagDefinitionContext.INHERIT);
		if(inherit!=null)  contextOut.setInherit(inherit);
		HAPContextParser.parseContextGroup(contextJson, contextOut);
	}


}
