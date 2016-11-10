package com.nosliw.data.datatype.importer.js;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.datatype.util.HAPDBAccess;

public class HAPJSImporter {

	private HAPDBAccess m_dbAccess = null;
	
	private static String HEAD = null;
	
	public void importFromFile(InputStream inputStream){
		String content = this.getHead() + HAPFileUtility.readFile(inputStream);
		Context cx = Context.enter();
	    try {
	        Scriptable scope = cx.initStandardObjects(null);

            NativeObject operationsObj = (NativeObject)cx.evaluateString(scope, content, "<cmd>", 1, null);

            //Get data type info
            String dataTypeName = null;
            String dataTypeVersion = null;
            Set<Object> keys = operationsObj.keySet();
            for(Object key : keys){
            	Object attrObj = ScriptableObject.getProperty(operationsObj, (String)key);
            	if(attrObj instanceof NativeObject && key.equals("dataType")){
        			NativeObject dataType = (NativeObject)attrObj;
        			dataTypeName = (String)dataType.get("name");
        			dataTypeVersion = (String)dataType.get("version");
                	break;
            	}
            }

            List<HAPJSOperationInfo> out = new ArrayList<HAPJSOperationInfo>();
            String dataTypeId = null;
            if(dataTypeName!=null){
                Set<Object> keys1 = operationsObj.keySet();
                for(Object key : keys1){
                	Object attrObj = ScriptableObject.getProperty(operationsObj, (String)key);
                	if(attrObj instanceof Function){
                    	String script = Context.toString(attrObj);
                    	String resources = this.getResources(script);
                    	String operationId = this.getOperationId(dataTypeName, dataTypeVersion, (String)key);
                    	HAPJSOperationInfo opInfo = new HAPJSOperationInfo(script, resources, operationId);
                    	out.add(opInfo);
                	}
                }
            }
            
        } finally {
            // Exit from the context.
            Context.exit();
        }	
    }

	private void saveOperations(List<HAPJSOperationInfo> ops){
		
	}
	
	private String getOperationId(String dataTypeName, String dataTypeVersion, String operation){
		return null;
	}
	
	private String getResources(String script){
		return null;
	}
	
	private String getHead(){
		if(HEAD==null){
			InputStream inputStream = HAPFileUtility.getInputStreamOnClassPath(HAPJSImporter.class, "head.js");
			HEAD = HAPFileUtility.readFile(inputStream);
		}
		return HEAD;
	}
}
