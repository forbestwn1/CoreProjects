package com.nosliw.data.core.domain.entity.dataassociation.mapping1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.structure.HAPReferenceElementInStructure;
import com.nosliw.data.core.structure.temp.HAPUtilityContextScript;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPUtilityScript2 {

	public static HAPJsonTypeScript buildDataAssociationConvertFunction(HAPExecutableDataAssociationMapping dataAssociation) {
		StringBuffer assocationScripts = new StringBuffer();
		Map<String, HAPExecutableMapping> associations = dataAssociation.getMappings();
		for(String targetName : associations.keySet()) {
			String associationScript = buildAssociationConvertFunction(associations.get(targetName)).getScript();
			assocationScripts.append("output."+targetName+"="+associationScript+"(input, utilFunction);\n");
		}
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("buildAssociations", assocationScripts.toString());
		InputStream templateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityScript2.class, "DataAssociationFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPJsonTypeScript(script);
	}
	
	public static HAPJsonTypeScript buildAssociationConvertFunction(HAPExecutableMapping association) {
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("isFlatOutput", association.isFlatOutput()+"");
		templateParms.put("isFlatInput", association.isFlatInput()+"");
		templateParms.put("rootIdSeperator", HAPConstantShared.SEPERATOR_CONTEXT_CATEGARY_NAME);
		templateParms.put("isInherit", (!HAPConstant.INHERITMODE_NONE.equals(HAPUtilityDataAssociation.getContextProcessConfigurationForDataAssociation(null).inheritMode))+"");
		
		//build init output object for mapped root
		HAPValueStructureDefinitionFlat context = new HAPValueStructureDefinitionFlat();
		HAPValueMapping daCotnext = association.getMapping();
		for(String eleName : daCotnext.getRootNames()) {
			context.addRootToCategary(eleName, daCotnext.getRoot(eleName));
		}
		JSONObject output = HAPUtilityContextScript.buildSkeletonJsonObject(context, association.isFlatOutput());
		templateParms.put("outputInit", HAPUtilityJson.formatJson(output.toString()));
		
		//build dynamic part 
		StringBuffer dynamicScript = new StringBuffer();
		Map<String, String> relativePathMapping = association.getRelativePathMappings();
		for(String targePath : relativePathMapping.keySet()) {
			String sourcePath = relativePathMapping.get(targePath);
			String script = "output = utilFunction(output, "+ buildJSArrayFromContextPath(targePath) +", input, "+ buildJSArrayFromContextPath(sourcePath) +");\n";
			dynamicScript.append(script);
		}
		templateParms.put("outputDyanimicValueBuild", dynamicScript.toString());
		
		//build cosntant assignment part
		StringBuffer constantAssignmentScript = new StringBuffer();
		Map<String, Object> constantAssignments = association.getConstantAssignments();
		for(String targePath : constantAssignments.keySet()) {
			Object constantValue = constantAssignments.get(targePath);
			String script = "output = utilFunction(output, "+ buildJSArrayFromContextPath(targePath) +", "+ HAPUtilityJson.buildJsonStringValue(constantValue, HAPSerializationFormat.JSON) +");\n";
			constantAssignmentScript.append(script);
		}
		templateParms.put("outputConstantValueBuild", constantAssignmentScript.toString());
		
		
		InputStream templateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityScript2.class, "AssociationFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPJsonTypeScript(script);
	}
 
	private static String buildJSArrayFromContextPath(String path) {
		List<String> pathSegs = new ArrayList<String>();
		HAPReferenceElementInStructure contextPath = new HAPReferenceElementInStructure(path);
		if(HAPUtilityBasic.isStringNotEmpty(contextPath.getRootReference().getCategary()))  pathSegs.add(contextPath.getRootReference().getCategary());
		pathSegs.add(contextPath.getRootReference().getName());
		pathSegs.addAll(Arrays.asList(contextPath.getPathSegments()));
		String pathSegsStr = HAPUtilityJson.buildArrayJson(pathSegs.toArray(new String[0]));
		return pathSegsStr;
	}
	
}
