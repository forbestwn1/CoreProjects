package com.nosliw.data.core.imp.runtime.js.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.nosliw.data.core.imp.HAPOperationImp;
import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.imp.runtime.js.HAPResourceOperationImp;
import com.nosliw.data.core.imp.runtime.js.HAPJSResourceDependency;
import com.nosliw.data.core.imp.runtime.js.HAPResourceHelperImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSImp;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.js.HAPResourceIdDataType;
import com.nosliw.data.core.runtime.js.HAPResourceIdHelper;
import com.nosliw.data.core.runtime.js.HAPResourceIdLibrary;
import com.nosliw.data.core.runtime.js.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.js.HAPResourceManagerJS;
import com.nosliw.data.core.runtime.js.rhino.HAPRhinoUtility;

public class HAPJSImporter {

	private HAPDBAccess m_dbAccess = null;
	
	private String m_operationTemplate = null;
	
	private HAPResourceManagerJSImp m_resourceJSMan;
	
	public HAPJSImporter(HAPResourceManagerJSImp resourceJSMan){
		this.m_resourceJSMan = resourceJSMan;
		this.m_dbAccess = this.m_resourceJSMan.getDBAccess();
		
		this.m_dbAccess.createDBTable(HAPResourceOperationImp._VALUEINFO_NAME);
		this.m_dbAccess.createDBTable(HAPJSResourceDependency._VALUEINFO_NAME);
		this.m_dbAccess.createDBTable(HAPResourceHelperImp._VALUEINFO_NAME);
	}
	
	public void loadFromFolder(String folderPath){
		//find js operation
		List<HAPResourceOperationImp> jsout = new ArrayList<HAPResourceOperationImp>();
		List<HAPJSResourceDependency> dependency = new ArrayList<HAPJSResourceDependency>();
		this.importFromFolder(folderPath, jsout, dependency);
		this.saveOperations(jsout);
		this.saveResourceDependencys(dependency);
	}
	
	private void importFromFolder(String folderPath, List<HAPResourceOperationImp> out, List<HAPJSResourceDependency> dependency){
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
	    		    finally {
	    	            // Exit from the context.
	    	            Context.exit();
	    	        }	
	    		}
	    	} else if (listOfFiles[i].isDirectory() && !listOfFiles[i].getName().equals("bin")) {
	    		this.importFromFolder(file.getPath(), out, dependency);
	    	}
	    }		
	}
	
	private List<HAPResourceOperationImp> importFromFile(Context cx, Scriptable scope, InputStream inputStream, List<HAPResourceOperationImp> out, List<HAPJSResourceDependency> dependency){
        String content = this.getOperationDefinition(inputStream);
        cx.evaluateString(scope, content, "<cmd>", 1, null);
        Object obj = scope.get("nosliw", scope);
        
        NativeObject nosliwObjJS = (NativeObject)obj;
        Function getDataTypesFun = (Function)nosliwObjJS.get("getDataTypes");
        NativeObject dataTypesObjJS = (NativeObject)getDataTypesFun.call(cx, scope, nosliwObjJS, null);
        for(Object dataTypeNameKeyObj : dataTypesObjJS.keySet()){
        	String dataTypeNameKey = (String)dataTypeNameKeyObj;
        	NativeObject dataTypeObjJS = (NativeObject)dataTypesObjJS.get(dataTypeNameKey);
        	
        	NativeObject dataTypeIdObjJS = (NativeObject)dataTypeObjJS.get("dataType");
			String dataTypeName = (String)dataTypeIdObjJS.get("name");
			String dataTypeVersion = (String)dataTypeIdObjJS.get("version");
			HAPDataTypeId dataTypeId = new HAPDataTypeId(dataTypeName, new HAPDataTypeVersion(dataTypeVersion));

			//get all resources required by data type
			NativeObject dataTypeRequiresObjJS = (NativeObject)dataTypeObjJS.get("requires");
			Set<HAPResourceId> dataTypeResources = new HashSet<HAPResourceId>(); 
        	for(Object requiredTypeObjJS : dataTypeRequiresObjJS.keySet()){
        		String requiredResourceType = (String)requiredTypeObjJS;
        		NativeObject requiresTypeObjectJS = (NativeObject)dataTypeRequiresObjJS.get(requiredResourceType);
        
        		for(Object requiredResourceKey : requiresTypeObjectJS.keySet()){
        			String requiredResourceName = (String) requiredResourceKey;
        			Object requiredResourceObjJS = requiresTypeObjectJS.get(requiredResourceName);
        			HAPResourceId resourceId = this.processResource(cx, scope, requiredResourceType, requiredResourceObjJS, requiredResourceName);
        			this.addResourceIdToSet(resourceId, dataTypeResources);
        		}
        	}
			
			NativeObject operationsObjJS = (NativeObject)dataTypeObjJS.get("operations");
			for(Object operationNameKey : operationsObjJS.keySet()){
				//get resources for operation
				String operationName = (String)operationNameKey;
				NativeObject operationObjJS = (NativeObject)operationsObjJS.get(operationName);
				
				//operation
				Function operationFunJS = (Function)operationObjJS.get("operation");
            	String script = Context.toString(operationFunJS);
            	String operationId = this.getOperationId(dataTypeId, operationName);
            	out.add(new HAPResourceOperationImp(script, operationId, dataTypeId, operationName));
            	
				NativeObject operationRequiresObjJS = (NativeObject)operationObjJS.get("requires");
				Set<HAPResourceId> operationResources = new HashSet<HAPResourceId>();
				//add data type requires first
				for(HAPResourceId dataTypeResourceId : dataTypeResources)	this.addResourceIdToSet(dataTypeResourceId.clone(), dataTypeResources);

            	//get resource from script
            	List<HAPResourceId> discoverResources = discoverResources(script);
            	for(HAPResourceId discoverResource : discoverResources)  this.addResourceIdToSet(discoverResource, dataTypeResources);
				
            	for(Object requiredTypeObjJS : operationRequiresObjJS.keySet()){
            		String requiredResourceType = (String)requiredTypeObjJS;
            		NativeObject requiresTypeObjectJS = (NativeObject)operationRequiresObjJS.get(requiredResourceType);
            
            		for(Object requiredResourceKey : requiresTypeObjectJS.keySet()){
            			String requiredResourceName = (String) requiredResourceKey;
            			Object requiredResourceObjJS = requiresTypeObjectJS.get(requiredResourceName);
            			HAPResourceId resourceId = this.processResource(cx, scope, requiredResourceType, requiredResourceObjJS, requiredResourceName);
            			this.addResourceIdToSet(resourceId, operationResources);
            		}
            	}
            	HAPResourceId baseResourceId = this.getResourceManagerJS().buildResourceIdFromIdData(new HAPOperationId(dataTypeId, operationName), null);
            	
            	System.out.println(baseResourceId.toStringValue(HAPSerializationFormat.LITERATE));
            	
            	HAPJSResourceDependency dep = new HAPJSResourceDependency(baseResourceId, new ArrayList(operationResources));
            	
            	dependency.add(dep);
			}
        }
	    return out;
    }

	private void addResourceIdToSet(HAPResourceId resourceId, Set<HAPResourceId> resourceIdSet){
		Set<String> alias = resourceId.getAlias();
		
		boolean added = false;
		for(HAPResourceId resourceIdEle : resourceIdSet){
			if(resourceIdEle.equals(resourceId)){
				resourceIdEle.addAlias(resourceId.getAlias());
				added = true;
				break;
			}
			else{
				if(resourceId.getType().equals(resourceIdEle.getType())){
					//if found alias under same type, remove it
					for(String aliasEle : alias){
						resourceIdEle.removeAlias(aliasEle);
					}
				}
			}
		}
		if(!added)  resourceIdSet.add(resourceId);
	}
	
	private HAPResourceId processResource(Context cx, Scriptable scope, String type, Object resourceObjJS, String alais){
		HAPResourceId out = null;
		switch(type){
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION:
			String operationIdLiterate = (String)resourceObjJS;
			out = new HAPResourceIdOperation(operationIdLiterate, alais);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_LIBRARY:
			String libraryIdLiterate = (String)resourceObjJS;
			out = new HAPResourceIdLibrary(libraryIdLiterate, alais);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPE:
			String dataTypeIdLiterate = (String)resourceObjJS;
			out = new HAPResourceIdDataType(dataTypeIdLiterate, alais);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_HELPER:
			String helperScript = new HAPRhinoUtility().toJSONString(resourceObjJS); 
			HAPResourceHelperImp helperResource = new HAPResourceHelperImp(helperScript);
			helperResource = (HAPResourceHelperImp)this.m_dbAccess.saveEntity(helperResource);
			out = new HAPResourceIdHelper(helperResource.getId(), alais);
			break;
		}
		return out;
	}
	
	private void saveOperations(List<HAPResourceOperationImp> ops){
		for(HAPResourceOperationImp op : ops){
			this.m_dbAccess.saveEntity(op);
		}
	}

	private void saveResourceDependencys(List<HAPJSResourceDependency> dependencys){
		for(HAPJSResourceDependency dependency : dependencys){
			{
				int kkkk = 555;
				kkkk++;
				HAPResourceId resourceId = dependency.getResourceId();
			}

			this.m_dbAccess.saveEntity(dependency);
		}
	}

	private String getOperationId(HAPDataTypeId dataTypeId, String operationName){
		HAPOperationImp operation = this.m_dbAccess.getOperationInfoByName(dataTypeId, operationName);
		return operation.getId();
	}
	
	private List<HAPResourceId> discoverResources(String script){
		List<HAPResourceId> out = new ArrayList<HAPResourceId>();
		
		String lines[] = script.split("\\r?\\n");
		for(String line : lines){
			if(line.contains(".executeOperation(")){
				String[] segs = line.split("\"");
				String dataType = segs[1];
				String operation = segs[3];
				HAPResourceId resource = new HAPResourceIdOperation(new HAPOperationId(dataType, operation), null);
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
	
	private HAPResourceManagerJS getResourceManagerJS(){		return this.m_resourceJSMan;	}
	
}
