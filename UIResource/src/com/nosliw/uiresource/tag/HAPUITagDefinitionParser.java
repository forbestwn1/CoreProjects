package com.nosliw.uiresource.tag;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.js.rhino.HAPRhinoDataUtility;
import com.nosliw.uiresource.context.HAPContextParser;

public class HAPUITagDefinitionParser {

	public static HAPUITagDefinition parseFromFile(String fileName){
		HAPUITagDefinition out = null;
		try {
			Context cx = Context.enter();
	        Scriptable scope = cx.initStandardObjects(null);

			String content = HAPFileUtility.readFile(fileName);
			NativeObject defObjJS = (NativeObject)cx.evaluateString(scope, content, fileName, 1, null);

			String name = (String)defObjJS.get(HAPUITagDefinition.NAME);
	    	String script = Context.toString((Function)defObjJS.get(HAPUITagDefinition.SCRIPT));
			out = new HAPUITagDefinition(new HAPUITagId(name), script);

			//parse context
			HAPUITagDefinitionContext context = out.getContext();
			NativeObject contextObj = (NativeObject)defObjJS.get(HAPUITagDefinition.CONTEXT);
			JSONObject contextJson = (JSONObject)HAPRhinoDataUtility.toJson(contextObj);
			HAPContextParser.parseContextInTagDefinition(contextJson, context);
			
			NativeArray attributesArrayObj = (NativeArray)defObjJS.get(HAPUITagDefinition.ATTRIBUTES);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
            // Exit from the context.
            Context.exit();
        }	

		return out;
	}
	
}
