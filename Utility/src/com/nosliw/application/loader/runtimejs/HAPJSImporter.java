package com.nosliw.application.loader.runtimejs;

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
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeVersion;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.HAPOperationImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDataJSOperationImp;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPJSResourceDependency;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDataJSConverterImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDataHelperImp;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.js.HAPResourceIdDataType;
import com.nosliw.data.core.runtime.js.HAPResourceIdJSGateway;
import com.nosliw.data.core.runtime.js.HAPResourceIdJSHelper;
import com.nosliw.data.core.runtime.js.HAPResourceIdJSLibrary;
import com.nosliw.data.core.runtime.js.rhino.HAPRhinoDataUtility;

public class HAPJSImporter {

	private HAPDataAccessRuntimeJS m_jsRuntimeDataAccess = null;
	private HAPDataAccessDataType m_dataTypeDataAccess = null;
	
	private String m_operationTemplate = null;
	
	static Map<String,String> titleToResourceType = new LinkedHashMap<String, String>();
	static {
		titleToResourceType.put("library", HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY);
		titleToResourceType.put("helper", HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER);
	}
	
	public HAPJSImporter(HAPDataAccessRuntimeJS runtimeDataAccess, HAPDataAccessDataType dataTypeDataAccess){
		this.m_jsRuntimeDataAccess = runtimeDataAccess;
		this.m_dataTypeDataAccess = dataTypeDataAccess;
		
		this.m_jsRuntimeDataAccess.createDBTable(HAPResourceDataJSOperationImp._VALUEINFO_NAME);
		this.m_jsRuntimeDataAccess.createDBTable(HAPJSResourceDependency._VALUEINFO_NAME);
		this.m_jsRuntimeDataAccess.createDBTable(HAPResourceDataHelperImp._VALUEINFO_NAME);
		this.m_jsRuntimeDataAccess.createDBTable(HAPResourceDataJSConverterImp._VALUEINFO_NAME);
	}
	
	public void loadFromFolder(String folderPath){
		//find js operation
		this.importFromFolder(folderPath);
	}
	
	private void importFromFolder(String folderPath){
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
						this.importFromFile(cx, scope, new FileInputStream(file), file.getAbsolutePath());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
	    		    finally {
	    	            // Exit from the context.
	    	            Context.exit();
	    	        }	
	    		}
	    	} else if (listOfFiles[i].isDirectory() && !listOfFiles[i].getName().equals("bin")) {
	    		this.importFromFolder(file.getPath());
	    	}
	    }		
	}
	
	private void importFromFile(Context cx, Scriptable scope, InputStream inputStream, String name){
        String content = this.getOperationDefinition(inputStream);
        cx.evaluateString(scope, content, name, 1, null);
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
			Set<HAPResourceDependent> dataTypeResources = new HashSet<HAPResourceDependent>(); 
        	for(Object requiredTypeObjJS : dataTypeRequiresObjJS.keySet()){
        		String requiredResourceType = (String)requiredTypeObjJS;
        		NativeObject requiresTypeObjectJS = (NativeObject)dataTypeRequiresObjJS.get(requiredResourceType);
        
        		for(Object requiredResourceKey : requiresTypeObjectJS.keySet()){
        			String requiredResourceName = (String) requiredResourceKey;
        			Object requiredResourceObjJS = requiresTypeObjectJS.get(requiredResourceName);
        			HAPResourceDependent resourceId = this.processDependentResource(requiredResourceType, requiredResourceObjJS, requiredResourceName);
        			this.addDependentResourceIdToSet(resourceId, dataTypeResources);
        		}
        	}
			
        	//operation definition
			NativeObject operationsObjJS = (NativeObject)dataTypeObjJS.get("operations");
			for(Object operationNameKey : operationsObjJS.keySet()){
				//get resources for operation
				String operationName = (String)operationNameKey;
				NativeObject operationObjJS = (NativeObject)operationsObjJS.get(operationName);
				HAPJSResourceDependency dep = this.processOperationObject(operationObjJS, dataTypeId, operationName, dataTypeResources, HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION);
				this.m_jsRuntimeDataAccess.saveEntity(dep);
			}
			
			//convert to
			NativeObject convertToObjJS = (NativeObject)dataTypeObjJS.get(HAPConstant.DATAOPERATION_TYPE_CONVERTTO);
			if(convertToObjJS!=null){
				HAPJSResourceDependency toDep = this.processOperationObject(convertToObjJS, dataTypeId, HAPConstant.DATAOPERATION_TYPE_CONVERTTO, dataTypeResources, HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER);
				this.m_jsRuntimeDataAccess.saveEntity(toDep);
			}
			
			//convert from
			NativeObject convertFromObjJS = (NativeObject)dataTypeObjJS.get(HAPConstant.DATAOPERATION_TYPE_CONVERTFROM);
			if(convertFromObjJS!=null){
				HAPJSResourceDependency fromDep = this.processOperationObject(convertFromObjJS, dataTypeId, HAPConstant.DATAOPERATION_TYPE_CONVERTFROM, dataTypeResources, HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER);
				this.m_jsRuntimeDataAccess.saveEntity(fromDep);
			}
        }
    }
	
	private HAPJSResourceDependency processOperationObject(NativeObject operationObjJS, HAPDataTypeId dataTypeId, String operationName, Set<HAPResourceDependent> dataTypeResources, String resourceType){
		
		//operation
		Function operationFunJS = (Function)operationObjJS.get("operation");
    	String script = Context.toString(operationFunJS);
    	
    	switch(resourceType){
    	case HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION:
        	String operationId = this.getOperationId(dataTypeId, operationName);
    		this.m_jsRuntimeDataAccess.saveEntity(new HAPResourceDataJSOperationImp(script, operationId, dataTypeId, operationName));
    		break;
    	case HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER:
    		this.m_jsRuntimeDataAccess.saveEntity(new HAPResourceDataJSConverterImp(script, dataTypeId, operationName));
    		break;
    	}
    	
		NativeObject operationRequiresObjJS = (NativeObject)operationObjJS.get("requires");
		Set<HAPResourceDependent> operationResources = new HashSet<HAPResourceDependent>();
		//add data type requires first
		for(HAPResourceDependent dataTypeResourceId : dataTypeResources)	this.addDependentResourceIdToSet(dataTypeResourceId.clone(), operationResources);

    	//get resource from script
    	List<HAPResourceDependent> discoverResources = discoverResources(script);
    	for(HAPResourceDependent discoverResource : discoverResources)  this.addDependentResourceIdToSet(discoverResource, dataTypeResources);
		
    	for(Object requiredTypeObjJS : operationRequiresObjJS.keySet()){
    		String requiredResourceType = (String)requiredTypeObjJS;
    		NativeObject requiresTypeObjectJS = (NativeObject)operationRequiresObjJS.get(requiredResourceType);
    
    		for(Object requiredResourceKey : requiresTypeObjectJS.keySet()){
    			String requiredResourceName = (String) requiredResourceKey;
    			Object requiredResourceObjJS = requiresTypeObjectJS.get(requiredResourceName);
    			HAPResourceDependent resourceId = this.processDependentResource(requiredResourceType, requiredResourceObjJS, requiredResourceName);
    			this.addDependentResourceIdToSet(resourceId, operationResources);
    		}
    	}

    	HAPResourceId baseResourceId = null;
    	switch(resourceType){
    	case HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION:
    		baseResourceId = HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPOperationId(dataTypeId, operationName));
    		break;
    	case HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER:
    		baseResourceId = HAPResourceHelper.getInstance().buildResourceIdFromIdData(new HAPDataTypeConverter(dataTypeId, operationName));
    		break;
    	}
    	HAPJSResourceDependency dep = new HAPJSResourceDependency(baseResourceId, new ArrayList(operationResources));
		return dep;
	}

	private void addDependentResourceIdToSet(HAPResourceDependent resourceId, Set<HAPResourceDependent> resourceIdSet){
		Set<String> alias = resourceId.getAlias();
		
		boolean added = false;
		for(HAPResourceDependent resourceIdEle : resourceIdSet){
			if(resourceIdEle.equals(resourceId)){
				resourceIdEle.addAlias(resourceId.getAlias());
				added = true;
				break;
			}
			else{
				if(resourceId.getId().getType().equals(resourceIdEle.getId().getType())){
					//if found alias under same type, remove it
					for(String aliasEle : alias){
						resourceIdEle.removeAlias(aliasEle);
					}
				}
			}
		}
		if(!added)  resourceIdSet.add(resourceId);
	}
	
	private HAPResourceDependent processDependentResource(String type, Object resourceObjJS, String alais){
		String resourceType = getResourceTypeByResourceTitle(type);
		HAPResourceId resourceId = null;
		switch(resourceType){
		case HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION:
			String operationIdLiterate = (String)resourceObjJS;
			resourceId = new HAPResourceIdOperation(operationIdLiterate);
			break;
		case HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY:
			String libraryIdLiterate = (String)resourceObjJS;
			resourceId = new HAPResourceIdJSLibrary(libraryIdLiterate);
			break;
		case HAPConstant.RUNTIME_RESOURCE_TYPE_DATATYPE:
			String dataTypeIdLiterate = (String)resourceObjJS;
			resourceId = new HAPResourceIdDataType(dataTypeIdLiterate);
			break;
		case HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER:
			String helperScript = new HAPRhinoDataUtility().toJson(resourceObjJS)+""; 
			helperScript = HAPJsonUtility.unescape(helperScript);
			HAPResourceDataHelperImp helperResource = new HAPResourceDataHelperImp(helperScript);
			helperResource = (HAPResourceDataHelperImp)this.m_jsRuntimeDataAccess.saveEntity(helperResource);
			resourceId = new HAPResourceIdJSHelper(helperResource.getId());
			break;
		case HAPConstant.RUNTIME_RESOURCE_TYPE_JSGATEWAY:
			String gatewayIdLiterate = (String)resourceObjJS;
			resourceId = new HAPResourceIdJSGateway(gatewayIdLiterate);
			break;
		}
		return new HAPResourceDependent(resourceId, alais);
	}
	
	private String getOperationId(HAPDataTypeId dataTypeId, String operationName){
		HAPOperationImp operation = this.m_dataTypeDataAccess.getOperationBasicInfoByName(dataTypeId, operationName);
		return operation.getId();
	}
	
	private List<HAPResourceDependent> discoverResources(String script){
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		
		String lines[] = script.split("\\r?\\n");
		for(String line : lines){
			if(line.contains(".executeOperation(")){
				String[] segs = line.split("\"");
				String dataType = segs[1];
				String operation = segs[3];
				HAPResourceId resourceId = new HAPResourceIdOperation(new HAPOperationId(dataType, operation));
				out.add(new HAPResourceDependent(resourceId));
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
	
	//Sometimes, the title for particular required resource is not the same as resource name. 
	//For instance, resource type "jshelper", we use "helper" as title  
	private String getResourceTypeByResourceTitle(String title){
		String out = titleToResourceType.get(title);
		if(out!=null)   return out;
		else return title;
	}
	
	public static void main(String[] args){

		HAPModuleRuntimeJS runtimeJSModule = new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance());;

		HAPJSImporter jsImporter = new HAPJSImporter(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess());
		jsImporter.loadFromFolder("C:\\Users\\ewaniwa\\Desktop\\MyWork\\CoreProjects\\DataType");
		jsImporter.loadFromFolder("C:\\Users\\ewaniwa\\Desktop\\MyWork\\CoreProjects\\DataTypeTest");

//		HAPDBAccess.getInstance().close();
	}
	
}
