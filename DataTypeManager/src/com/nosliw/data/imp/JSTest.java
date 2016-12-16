package com.nosliw.data.imp;

import java.io.InputStream;
import java.util.Set;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.nosliw.common.utils.HAPFileUtility;

public class JSTest {

	public static void main(String[] args) throws JavaScriptException{
		
		InputStream inputStream = HAPFileUtility.getInputStreamOnClassPath(JSTest.class, "operations.js");
		String content = HAPFileUtility.readFile(inputStream);
		
	    Context cx = Context.enter();
	    try {
	    	// Initialize the standard objects (Object, Function, etc.)
	        // This must be done before scripts can be executed. Returns
	        // a scope object that we use in later calls.
	        Scriptable scope = cx.initStandardObjects(null);

	        // Collect the arguments into a single string.
	        // Now evaluate the string we've colected.
            NativeObject operationsObj = (NativeObject)cx.evaluateString(scope, content, "<cmd>", 1, null);

            Object dataTypeName = operationsObj.get("dataType");
            
            Set<Object> keys = operationsObj.keySet();
            for(Object key : keys){
            	Object attrObj = ScriptableObject.getProperty(operationsObj, (String)key);
            	if(attrObj instanceof Function){
                	System.out.println(key + "  " + cx.decompileFunction((Function)attrObj, 5));
                	System.out.println(Context.toString(attrObj));
            	}
            	else{
            		if(key.equals("dataType")){
            			NativeObject dataType = (NativeObject)attrObj;
            			Object name = dataType.get("name");
            			Object version = dataType.get("version");
                    	System.out.println(name + "   " + version);
            		}
            	}
            }
	            
            // Convert the result to a string and print it.
            System.err.println(Context.toString(operationsObj));
        } finally {
            // Exit from the context.
            Context.exit();
        }	
	}
}
