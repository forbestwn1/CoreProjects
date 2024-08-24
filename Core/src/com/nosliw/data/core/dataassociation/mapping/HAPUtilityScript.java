package com.nosliw.data.core.dataassociation.mapping;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationMapping;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafConstant;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPElementStructureNode;
import com.nosliw.core.application.common.structure.HAPRootStructure;
import com.nosliw.data.core.structure.temp.HAPUtilityContextInfo;

public class HAPUtilityScript {

	public static HAPJsonTypeScript buildDataAssociationConvertFunction(HAPDataAssociationMapping dataAssociation) {
		StringBuffer assocationScripts = new StringBuffer();
		Map<String, HAPExecutableValueMapping> associations = dataAssociation.getMappings();
		for(String targetName : associations.keySet()) {
			String associationScript = buildAssociationConvertFunction(associations.get(targetName)).getScript();
			assocationScripts.append("output."+targetName+"="+associationScript+"(input, utilFunction);\n");
		}
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("buildAssociations", assocationScripts.toString());
		InputStream templateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityScript.class, "DataAssociationFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPJsonTypeScript(script);
	}
	
	public static HAPJsonTypeScript buildAssociationConvertFunction(HAPExecutableValueMapping association) {
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		//build init output object for mapped root
		HAPDefinitionValueMapping valueMappinig = association.getMapping();
		JSONObject output = buildSkeletonJsonObject(valueMappinig);
		templateParms.put("outputInit", HAPUtilityJson.formatJson(output.toString()));
		
		//build dynamic part 
		StringBuffer dynamicScript = new StringBuffer();
		Map<String, String> relativePathMapping = association.getTunnels();
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
		
		
		InputStream templateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityScript.class, "AssociationFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPJsonTypeScript(script);
	}
 
	private static String buildJSArrayFromContextPath(String path) {
		List<String> pathSegs = new ArrayList<String>();
		HAPComplexPath contextPath = new HAPComplexPath(path);
		pathSegs.add(contextPath.getRoot());
		pathSegs.addAll(Arrays.asList(contextPath.getPath().getPathSegments()));
		String pathSegsStr = HAPUtilityJson.buildArrayJson(pathSegs.toArray(new String[0]));
		return pathSegsStr;
	}

	//build skeleton, it is used for data mapping operation
	public static JSONObject buildSkeletonJsonObject(HAPDefinitionValueMapping valueMapping) {
		JSONObject output = new JSONObject();
		Map<String, HAPRootStructure> elements = valueMapping.getItems();
		for(String targetId : elements.keySet()) {
			HAPRootStructure root = elements.get(targetId);
			if(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
				HAPElementStructure contextDefEle = root.getDefinition();
				Object contextEleJson = buildJsonValue(contextDefEle);
				JSONObject parentJsonObj = output;
				parentJsonObj.put(targetId, contextEleJson);
			}
		}
		
		return output;
	}
	
	private static Object buildJsonValue(HAPElementStructure contextDefEle) {
		switch(contextDefEle.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT:
		{
			HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)contextDefEle;
			return constantEle.getValue();
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
		{
			HAPElementStructureNode nodeEle = (HAPElementStructureNode)contextDefEle;
			JSONObject out = new JSONObject();
			for(String childName : nodeEle.getChildren().keySet()) {
				Object childJsonValue = buildJsonValue(nodeEle.getChild(childName));
				if(childJsonValue!=null) {
					out.put(childName, childJsonValue);
				}
			}
			return out;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
		{
			HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)contextDefEle;
			return new JSONObject();
		}
		default:
			return null;
		}
	}

}
