package com.nosliw.core.runtime.js;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.script.HAPJSScriptInfo;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.entity.js.library.HAPResourceDataJSLibrary;
import com.nosliw.core.resource.HAPConfigureResource;
import com.nosliw.core.resource.HAPResource;
import com.nosliw.core.resource.HAPResourceInfo;
import com.nosliw.core.resource.imp.js.HAPResourceDataJSValue;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.core.system.HAPSystemFolderUtility;

public class HAPUtilityRuntimeJsScriptResource {

	public static List<HAPJSScriptInfo> buildScriptForResource(HAPResourceInfo resourceInfo, HAPResource resource){
		List<HAPJSScriptInfo> out = new ArrayList<HAPJSScriptInfo>();
		//build library script info first
		if(resource.getId().getResourceTypeId().getResourceType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY)){
			out.addAll(buildScriptInfoForLibrary(resourceInfo, resource));
		}
		
		if(HAPRuntimeImpRhino.ADDTORESOURCEMANAGER.equals(resourceInfo.getInfo().getValue(HAPRuntimeImpRhino.ADDTORESOURCEMANAGER))) {
			return out;
		}
		
		//build script for resource with data
		out.add(buildScriptInfoForResourceWithScript(resourceInfo, resource));
		
//		if(resource.getResourceData() instanceof HAPResourceDataJSValue){
//			out.add(buildScriptInfoForResourceWithScript(resourceInfo, resource, ((HAPResourceDataJSValue)resource.getResourceData()).getValue()));
//		}
		return out;
	}
	
	private static HAPJSScriptInfo buildScriptInfoForResourceWithScript(HAPResourceInfo resourceInfo, HAPResource resource){
		HAPJSScriptInfo out = null;
		String script = buildImportResourceScriptForResource(resourceInfo, resource);
		
		String loadPattern = (String)resource.getInfoValue(HAPConfigureResource.RESOURCE_LOADPATTERN);
		if(loadPattern==null) {
			loadPattern = HAPConfigureResource.RESOURCE_LOADPATTERN_VALUE;
		}
		switch(loadPattern){
		case HAPConfigureResource.RESOURCE_LOADPATTERN_FILE:
			//load as file, create temp file first
			String name = resource.getId().toStringValue(HAPSerializationFormat.LITERATE);
			name = name.replace(";", "_");
			String resourceFile = HAPSystemFolderUtility.getResourceTempFileFolder() + name + ".js";
			resourceFile = HAPUtilityFile.writeFile(resourceFile, script);
			out = HAPJSScriptInfo.buildByFile(resourceFile, name);
			
			break;
		case HAPConfigureResource.RESOURCE_LOADPATTERN_VALUE:
			//load as value
			out = HAPJSScriptInfo.buildByScript(script.toString(), "Resource__"+resource.getId().toStringValue(HAPSerializationFormat.LITERATE));
			break;
		}

		return out;
	}
	
	private static String buildImportResourceScriptForResource(HAPResourceInfo resourceInfo, HAPResource resource){
		StringBuffer script = new StringBuffer();
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("resourceInfo", resourceInfo.toStringValue(HAPSerializationFormat.JSON));
		
		String infoJson = HAPManagerSerialize.getInstance().toStringValue(resource.getInfo(), HAPSerializationFormat.JSON);
		if(HAPUtilityBasic.isStringEmpty(infoJson)){
			templateParms.put(HAPResource.INFO, "undefined");
		}
		else{
			templateParms.put(HAPResource.INFO, infoJson);
		}

		String valueScript = resource.getResourceData().toStringValue(HAPSerializationFormat.JAVASCRIPT);
		if(valueScript==null) {
			valueScript = "undefined";
		}
		templateParms.put(HAPResourceDataJSValue.VALUE, valueScript);

		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJsScriptResource.class, "ImportResource.temp");
		script.append("\n");
		String resoruceDataScript = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		script.append(resoruceDataScript);
		script.append("\n");
		
		HAPUtilityFile.writeFile("c:\\Temp\\test.js", script.toString());
		
		return script.toString();
	}
	
	private static List<HAPJSScriptInfo> buildScriptInfoForLibrary(HAPResourceInfo resourceInfo, HAPResource resource){
		List<HAPJSScriptInfo> out = new ArrayList<HAPJSScriptInfo>();
		
		HAPResourceDataJSLibrary resourceLibrary = (HAPResourceDataJSLibrary)resource.getResourceData();
		List<URI> uris = resourceLibrary.getURIs();
		for(URI uri : uris){
			File file = new File(uri);
			String fileFullName = file.getAbsolutePath().replaceAll("\\\\", "/");
			HAPJSScriptInfo scriptInfo = HAPJSScriptInfo.buildByFile(fileFullName, "Library__" + resource.getId().getCoreIdLiterate() + "__" + file.getName());
			scriptInfo.setType(resource.getId().getResourceTypeId().getResourceType());
			out.add(scriptInfo);
		}
		out.add(buildScriptInfoForResourceWithScript(resourceInfo, resource));
		return out;
	}
}
