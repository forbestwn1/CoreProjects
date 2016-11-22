package com.nosliw.data.datatype.importer.js;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.datatype.importer.HAPResourceDataOperationImp;
import com.nosliw.data.datatype.util.HAPDBAccess;

public class HAPJSImporter {

	private HAPDBAccess m_dbAccess = null;
	
	private static String HEAD = null;
	
	public HAPJSImporter(HAPDBAccess dbAccess){
		this.m_dbAccess = dbAccess;
	}
	
	public void importFromFolder(String folderPath, List<HAPJSOperationInfo> out){
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	File file = listOfFiles[i];
	    	if (listOfFiles[i].isFile()) {
	    		String fileName = file.getName();
	    		if(fileName.endsWith(".js")){
	    			try {
						out.addAll(this.importFromFile(new FileInputStream(file)));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
	    		}
	    	} else if (listOfFiles[i].isDirectory()) {
	    		this.importFromFolder(file.getPath(), out);
	    	}
	    }		
	}
	
	public List<HAPJSOperationInfo> importFromFile(InputStream inputStream){
        List<HAPJSOperationInfo> out = new ArrayList<HAPJSOperationInfo>();

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
	    return out;
    }

	private void saveOperations(List<HAPJSOperationInfo> ops){
		for(HAPJSOperationInfo op : ops){
			this.m_dbAccess.saveOperationInfoJS(op);
		}
	}
	
	private String getOperationId(String dataTypeName, String dataTypeVersion, String operation){
		return this.m_dbAccess.getOperationId(dataTypeName, dataTypeVersion, operation);
	}
	
	private String getResources(String script){
		List<HAPResourceDataOperationImp> out = new ArrayList<HAPResourceDataOperationImp>();
		
		String lines[] = script.split("\\r?\\n");
		for(String line : lines){
			if(line.contains(".operate(")){
				String[] segs = line.split("\"");
				String dataType = segs[1];
				String operation = segs[3];
				HAPResourceDataOperationImp resource = new HAPResourceDataOperationImp(dataType, operation);
				out.add(resource);
			}
		}
		return HAPLiterateManager.getInstance().valueToString(out);
	}
	
	private String getHead(){
		if(HEAD==null){
			InputStream inputStream = HAPFileUtility.getInputStreamOnClassPath(HAPJSImporter.class, "head.js");
			HEAD = HAPFileUtility.readFile(inputStream);
		}
		return HEAD;
	}
}
