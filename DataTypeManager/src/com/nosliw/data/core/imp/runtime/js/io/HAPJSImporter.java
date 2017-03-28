package com.nosliw.data.core.imp.runtime.js.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeVersion;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.imp.runtime.js.HAPJSOperation;
import com.nosliw.data.core.imp.runtime.js.HAPJSResourceDependency;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerImpJS;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPJSImporter {

	private HAPDBAccess m_dbAccess = null;
	
	private String m_operationTemplate = null;
	
	private HAPResourceManagerImpJS m_resourceJSMan;
	
	public HAPJSImporter(HAPResourceManagerImpJS resourceJSMan){
		this.m_resourceJSMan = resourceJSMan;
		this.m_dbAccess = this.m_resourceJSMan.getDBAccess();
		
		this.m_dbAccess.createDBTable(HAPJSOperation._VALUEINFO_NAME);
		this.m_dbAccess.createDBTable(HAPJSResourceDependency._VALUEINFO_NAME);
	}
	
	public void loadFromFolder(String folderPath){
		//find js operation
		List<HAPJSOperation> jsout = new ArrayList<HAPJSOperation>();
		List<HAPJSResourceDependency> dependency = new ArrayList<HAPJSResourceDependency>();
		this.importFromFolder(folderPath, jsout, dependency);
		this.saveOperations(jsout);
		this.saveResourceDependencys(dependency);
	}
	
	private void importFromFolder(String folderPath, List<HAPJSOperation> out, List<HAPJSResourceDependency> dependency){
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	File file = listOfFiles[i];
	    	if (listOfFiles[i].isFile()) {
	    		String fileName = file.getName();
	    		if(fileName.endsWith(".js")){
	    			try {
	    				Context cx = Context.enter();
	    		        Scriptable scope = cx.initStandardObjects(null);
						this.importFromFile(cx, scope, new FileInputStream(file), out, dependency);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
	    		}
	    	} else if (listOfFiles[i].isDirectory() && !listOfFiles[i].getName().equals("bin")) {
	    		this.importFromFolder(file.getPath(), out, dependency);
	    	}
	    }		
	}
	
	private List<HAPJSOperation> importFromFile(Context cx, Scriptable scope, InputStream inputStream, List<HAPJSOperation> out, List<HAPJSResourceDependency> dependency){
        String content = this.getOperationDefinition(inputStream);
	    try {
	        cx.evaluateString(scope, content, "<cmd>", 1, null);
	        Object obj = scope.get("nosliw", scope);
            
	        NativeObject nosliwObjJS = (NativeObject)obj;
            for(Object dataTypeNameKey : nosliwObjJS.keySet()){
            	String dataTypeName = (String)dataTypeNameKey;
            	NativeObject dataTypeObjJS = (NativeObject)nosliwObjJS.get(dataTypeName);
            	
            	NativeObject dataTypeIdObjJS = (NativeObject)dataTypeObjJS.get("dataType");
    			String dataTypeName = (String)dataTypeIdObjJS.get("name");
    			String dataTypeVersion = (String)dataTypeIdObjJS.get("version");
    			HAPDataTypeId dataTypeId = new HAPDataTypeId(dataTypeName, new HAPDataTypeVersion(dataTypeVersion));

    			NativeObject dataTypeRequiresObjJS = (NativeObject)dataTypeObjJS.get("requires");
    			NativeObject operationsObjJS = (NativeObject)dataTypeObjJS.get("operations");
    			for(Object operationNameKey : operationsObjJS.keySet()){
    				String operationName = (String)operationNameKey;
    				NativeObject operationObjJS = (NativeObject)operationsObjJS.get(operationName);
    				
    				//operation
    				Function operationFunJS = (Function)operationObjJS.get("operation");
                	String script = Context.toString(operationFunJS);
                	String operationId = this.getOperationId(dataTypeId, operationName);
                	out.add(new HAPJSOperation(script, operationId, dataTypeId, operationName));
                	
    				NativeObject operationRequiresObjJS = (NativeObject)operationObjJS.get("requires");
                	for(Object requiredTypeObjJS : operationRequiresObjJS.keySet()){
                		String requiredResourceType = (String)requiredTypeObjJS;
                		NativeObject requiresObjectJS = (NativeObject)operationRequiresObjJS.get(requiredResourceType);
                	}
    			}
            }
            
            
            
            
            
            NativeObject operationsObjJS = (NativeObject)obj;
            NativeObject dataTypeJS = (NativeObject)operationsObjJS.get("dataType");
			String dataTypeName = (String)dataTypeJS.get("name");
			String dataTypeVersion = (String)dataTypeJS.get("version");
			HAPDataTypeId dataTypeId = new HAPDataTypeId(dataTypeName, new HAPDataTypeVersion(dataTypeVersion));
			NativeObject operationsJS = (NativeObject)operationsObjJS.get("operations");
            for(Object key : operationsJS.keySet()){
            	String opName = (String)key;
            	Object opObjectJS = operationsJS.get(key);  
            	if(opObjectJS instanceof Function){
                	String script = Context.toString(opObjectJS);
                	String operationId = this.getOperationId(dataTypeId, (String)key);
                	out.add(new HAPJSOperation(script, operationId, dataTypeId, opName));

                	List<HAPResourceId> resources = this.discoverResources(script);
                	dependency.add(new HAPJSResourceDependency(new HAPResourceId(HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION, new HAPOperationId(dataTypeId, opName).toStringValue(HAPSerializationFormat.LITERATE)), resources));
            	}
            }
        } finally {
            // Exit from the context.
            Context.exit();
        }	
	    return out;
    }

	private void saveOperations(List<HAPJSOperation> ops){
		for(HAPJSOperation op : ops){
			this.m_dbAccess.saveEntity(op);
		}
	}

	private void saveResourceDependencys(List<HAPJSResourceDependency> dependencys){
		for(HAPJSResourceDependency dependency : dependencys){
			this.m_dbAccess.saveEntity(dependency);
		}
	}

	private String getOperationId(HAPDataTypeId dataTypeId, String operation){
		return this.m_dbAccess.getOperationInfoByName(dataTypeId, operation).getId();
	}
	
	private List<HAPResourceId> discoverResources(String script){
		List<HAPResourceId> out = new ArrayList<HAPResourceId>();
		
		String lines[] = script.split("\\r?\\n");
		for(String line : lines){
			if(line.contains(".operate(")){
				String[] segs = line.split("\"");
				String dataType = segs[1];
				String operation = segs[3];
				HAPResourceId resource = new HAPResourceId(dataType, operation);
				out.add(resource);
			}
		}
		return out;
	}
	
	private String getOperationDefinition(InputStream inputStream){
        String content = HAPFileUtility.readFile(inputStream);
        
        Map<String, String> parms = new LinkedHashMap<String, String>();
        parms.put("operations", content);
        
        String out = HAPStringTemplateUtil.getStringValue(this.getOperationTemplate(), parms);
		return out;
	}
	
	private String getOperationTemplate(){
		if(this.m_operationTemplate==null){
			InputStream inputStream = HAPFileUtility.getInputStreamOnClassPath(HAPJSImporter.class, "operationtemplate.js");
			this.m_operationTemplate = HAPFileUtility.readFile(inputStream);
		}
		return this.m_operationTemplate;
	}
	
}
