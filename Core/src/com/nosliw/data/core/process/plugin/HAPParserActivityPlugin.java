package com.nosliw.data.core.process.plugin;

import java.io.File;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.utils.HAPFileUtility;

public class HAPParserActivityPlugin {

	public static HAPPluginActivity parseFromFile(File file) {
		HAPPluginActivityImp out = new HAPPluginActivityImp();
		
		try {
			Context cx = Context.enter();
	        Scriptable scope = cx.initStandardObjects(null);

			String content = "var out="+HAPFileUtility.readFile(file) + "; out;";
			NativeObject defObjJS = (NativeObject)cx.evaluateString(scope, content, file.getName(), 1, null);

			String type = (String)defObjJS.get(HAPPluginActivity.TYPE);
			out.setType(type);
			String processorClsName = (String)defObjJS.get(HAPPluginActivityImp.PROCESSOR);
			out.setProcessorClass(processorClsName);
			String activityDefName = (String)defObjJS.get(HAPPluginActivityImp.DEFINITION);
			out.setActivityClass(activityDefName);
			
			NativeObject scriptObj = (NativeObject)defObjJS.get(HAPPluginActivity.SCRIPT);
			for(Object key : scriptObj.keySet()) {
				String scriptName = (String)key;
				String script = Context.toString((Function)scriptObj.get(scriptName));
				out.addScript(scriptName, script);
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
}
