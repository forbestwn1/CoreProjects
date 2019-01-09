package com.nosliw.data.core.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.updatename.HAPUpdateNameMap;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPImporterProcessSuiteDefinition;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefEleProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPInfoRelativeContextResolve;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPUtilityProcess {

	public static HAPScript buildProcessInitScript(HAPExecutableProcess process) {
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		//build init output object 
		JSONObject output = HAPUtilityContext.buildDefaultJsonObject(process.getContext());
		templateParms.put("outputInit", HAPJsonUtility.formatJson(output.toString()));

		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityProcess.class, "ProcessInitFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPScript(script);
	}
	
	public static String buildOutputVarialbeName(String name) {
		return "nosliw_" + name;
	}
	
	public static HAPDefinitionProcessSuite getProcessSuite(String id, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionProcessSuite suite = null;
		try {
			suite = HAPImporterProcessSuiteDefinition.readProcessSuiteDefinitionFromFile(new FileInputStream(new File(HAPFileUtility.getProcessFolder()+id+".process")), activityPluginMan);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return suite;
	}
	
	//process result
	public static HAPExecutableResultActivityNormal processNormalActivityResult(
			HAPExecutableActivityNormal activity,
			String resultName, 
			HAPContextGroup parentContext,
			HAPBuilderResultContext resultContextBuilder, 
			HAPRequirementContextProcessor contextProcessRequirement) {
		HAPDefinitionResultActivityNormal resultDef = ((HAPDefinitionActivityNormal)activity.getActivityDefinition()).getResult(resultName);
		HAPExecutableResultActivityNormal resultExe = new HAPExecutableResultActivityNormal(resultDef); 
		//data association input context
		HAPContext dataAssociationInputContext = resultContextBuilder.buildResultContext(resultName, activity);
		//process data association
		HAPExecutableDataAssociationGroup outputDataAssociation = HAPProcessorDataAssociation.processDataAssociation(dataAssociationInputContext, resultDef.getOutputDataAssociation(), contextProcessRequirement);
		resultExe.setOutputDataAssociation(outputDataAssociation);

		//process result
		Map<String, String> nameMapping = new LinkedHashMap<String, String>();
		HAPContext outputContext = outputDataAssociation.getContext().getContext();
		for(String rootName : outputContext.getElementNames()) {
			//find matching variable in parent context
			HAPInfoRelativeContextResolve resolvedInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(rootName), parentContext, null, null);
			//merge back to parent context
			HAPContextDefinitionElement outputEle = outputContext.getElement(rootName).getDefinition();
			if(outputEle.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPUtilityContext.updateDataDescendant(parentContext, resolvedInfo.path.getRootElementId().getCategary(), resolvedInfo.path.getRootElementId().getName(), (HAPContextDefinitionLeafData)outputEle);
			}
			//root variable name --- root variable full name
			nameMapping.put(rootName, resolvedInfo.path.getRootElementId().getFullName());
		}
		
		//update variable names with full name 
		outputDataAssociation.updateOutputRootName(new HAPUpdateNameMap(nameMapping));
		return resultExe;
	}
}
