package com.nosliw.data.core.script.context.dataassociation;

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
import com.nosliw.data.core.script.context.HAPUtilityContextScript;

public class HAPUtilityScript {

	public static HAPScript buildDataAssociationConvertFunction(HAPExecutableDataAssociationGroup dataAssociationGroup) {
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("isFlatOutput", dataAssociationGroup.isFlatOutput()+"");
		templateParms.put("isFlatInput", dataAssociationGroup.isFlatInput()+"");
		templateParms.put("rootIdSeperator", HAPContextDefinitionRootId.SEPERATOR);
		templateParms.put("isInherit", (!HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE.equals(dataAssociationGroup.getProcessConfigure().inheritMode))+"");
		
		//build init output object for mapped root
		HAPContext context = new HAPContext();
		HAPContext daCotnext = dataAssociationGroup.getContext().getContext();
		for(String eleName : daCotnext.getElementNames()) {
			if(HAPProcessorDataAssociation.isMappedRoot(daCotnext.getElement(eleName))) {
				context.addElement(eleName, daCotnext.getElement(eleName));
			}
		}
		JSONObject output = HAPUtilityContextScript.buildSkeletonJsonObject(context, dataAssociationGroup.isFlatOutput());
		templateParms.put("outputInit", HAPJsonUtility.formatJson(output.toString()));
		
		//build dynamic part 
		StringBuffer dynamicScript = new StringBuffer();
		Map<String, String> relativePathMapping = dataAssociationGroup.getPathMapping();
		for(String targePath : relativePathMapping.keySet()) {
			String sourcePath = relativePathMapping.get(targePath);
			String script = "output = utilFunction(output, "+ buildJSArrayFromContextPath(targePath) +", input, "+ buildJSArrayFromContextPath(sourcePath) +");\n";
			dynamicScript.append(script);
		}
		templateParms.put("outputDyanimicValueBuild", dynamicScript.toString());
		
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
