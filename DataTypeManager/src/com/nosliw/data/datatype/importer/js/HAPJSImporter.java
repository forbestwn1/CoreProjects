package com.nosliw.data.datatype.importer.js;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.datatype.importer.HAPDBAccess;
import com.nosliw.data.datatype.importer.HAPDataTypeIdImp;
import com.nosliw.data.datatype.importer.HAPDataTypeVersionImp;
import com.nosliw.data.datatype.importer.HAPResourceDataOperationImp;
import com.nosliw.data.datatype.importer.HAPResourceIdImp;

public class HAPJSImporter {

	private HAPDBAccess m_dbAccess = null;
	
	private String m_operationTemplate = null;
	
	public HAPJSImporter(){
		this.m_dbAccess = HAPDBAccess.getInstance();
		
		HAPValueInfoManager.getInstance().importFromXML(HAPJSImporter.class, new String[]{
				"jsoperation.xml"
		});
		
		this.m_dbAccess.createDBTable("data.operation.js");
	}
	
	public void loadFromFolder(String folderPath){
		//find js operation
		List<HAPJSOperation> jsout = new ArrayList<HAPJSOperation>();
		this.importFromFolder(folderPath, jsout);
		this.saveOperations(jsout);
	}
	
	private void importFromFolder(String folderPath, List<HAPJSOperation> out){
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
	
	private List<HAPJSOperation> importFromFile(InputStream inputStream){
        List<HAPJSOperation> out = new ArrayList<HAPJSOperation>();

        String content = this.getOperationDefinition(inputStream);
		Context cx = Context.enter();
	    try {
	        Scriptable scope = cx.initStandardObjects(null);

	        System.out.println(content);
	        
	        Object obj = cx.evaluateString(scope, content, "<cmd>", 1, null);
            NativeObject operationsObjJS = (NativeObject)obj;

            NativeObject dataTypeJS = (NativeObject)operationsObjJS.get("dataType");
			String dataTypeName = (String)dataTypeJS.get("name");
			String dataTypeVersion = (String)dataTypeJS.get("version");
			HAPDataTypeIdImp dataTypeId = new HAPDataTypeIdImp(dataTypeName, new HAPDataTypeVersionImp(dataTypeVersion));
			NativeObject operationsJS = (NativeObject)operationsObjJS.get("operations");
            for(Object key : operationsJS.keySet()){
            	String opName = (String)key;
            	Object opObjectJS = operationsJS.get(key);  
            	if(opObjectJS instanceof Function){
                	String script = Context.toString(opObjectJS);
                	String operationId = this.getOperationId(dataTypeId, (String)key);
                	List<HAPResourceId> resources = this.discoverResources(script);
                	HAPJSOperation opInfo = new HAPJSOperation(script, operationId, dataTypeId, opName, resources);
                	out.add(opInfo);
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
			this.m_dbAccess.saveOperationJS(op);
		}
	}
	
	private String getOperationId(HAPDataTypeIdImp dataTypeId, String operation){
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
				HAPResourceDataOperationImp resource = new HAPResourceDataOperationImp(dataType, operation);
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
