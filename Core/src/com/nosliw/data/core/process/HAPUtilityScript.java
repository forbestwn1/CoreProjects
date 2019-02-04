package com.nosliw.data.core.process;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPExecutableDataAssociationGroup;
import com.nosliw.data.core.script.context.HAPProcessorDataAssociation;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPUtilityScript {
	
	
	public static HAPScript buildDataAssociationConvertFunction(HAPExecutableDataAssociationGroup dataAssociationGroup) {
		String isInherit = "false";
		if(!HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE.equals(HAPUtilityContext.getContextGroupInheritMode(dataAssociationGroup.getInfo()))){
			//inherit from input first
			isInherit = "true";
		}
		else {
			isInherit = "false";
		}
		
		//build init output object
		HAPContext context = new HAPContext();
		for(String eleName : dataAssociationGroup.getContext().getContext().getElementNames()) {
			if(HAPProcessorDataAssociation.isMappedRoot(dataAssociationGroup.getContext().getContext().getElement(eleName))) {
				context.addElement(eleName, dataAssociationGroup.getContext().getContext().getElement(eleName));
			}
		}
		
		JSONObject output = HAPUtilityContext.buildSkeletonJsonObject(context, dataAssociationGroup.isFlatOutput());
		
		//build dynamic part 
		StringBuffer dynamicScript = new StringBuffer();
		Map<String, String> relativePathMapping = dataAssociationGroup.getPathMapping();
		for(String targePath : relativePathMapping.keySet()) {
			String sourcePath = relativePathMapping.get(targePath);
			String script = "output = utilFunction(output, "+ buildJSArrayFromContextPath(targePath) +", input, "+ buildJSArrayFromContextPath(sourcePath) +");\n";
			dynamicScript.append(script);
		}
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("outputInit", HAPJsonUtility.formatJson(output.toString()));
		templateParms.put("outputDyanimicValueBuild", dynamicScript.toString());
		templateParms.put("isFlat", dataAssociationGroup.isFlatOutput()+"");
		templateParms.put("isInherit", isInherit);
		templateParms.put("rootIdSeperator", HAPContextDefinitionRootId.SEPERATOR);
		
		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityScript.class, "DataAssociationFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPScript(script);
	}

	private static String buildJSArrayFromContextPath(String path) {
		List<String> pathSegs = new ArrayList<String>();
		HAPContextPath contextPath = new HAPContextPath(path);
		if(HAPBasicUtility.isStringNotEmpty(contextPath.getRootElementId().getCategary()))  pathSegs.add(contextPath.getRootElementId().getCategary());
		pathSegs.add(contextPath.getRootElementId().getName());
		pathSegs.addAll(Arrays.asList(contextPath.getPathSegments()));
		String pathSegsStr = HAPJsonUtility.buildArrayJson(pathSegs.toArray(new String[0]));
		return pathSegsStr;
	}
	

}
