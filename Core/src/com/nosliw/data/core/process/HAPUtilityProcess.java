package com.nosliw.data.core.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.updatename.HAPUpdateNameMap;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.expression.HAPMatcherUtility;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPImporterProcessSuiteDefinition;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPInfoRelativeContextResolve;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPUtilityProcess {

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
			Map<String, HAPMatchers> matchers = HAPUtilityContext.mergeContextRoot(parentContext.getElement(resolvedInfo.path.getRootElementId()), outputContext.getElement(rootName), true, contextProcessRequirement);
			for(String matchPath :matchers.keySet()) {
				outputDataAssociation.addMatchers(matchPath, HAPMatcherUtility.reversMatchers(matchers.get(matchPath)));
			}
			
//			HAPContextDefinitionElement outputEle = outputContext.getElement(rootName).getDefinition();
			
//			HAPUtilityContext.setDescendant(parentContext, resolvedInfo.path.getRootElementId().getCategary(), resolvedInfo.path.getRootElementId().getName(), outputEle);
			
//			if(outputEle.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_DATA)) {
//				HAPUtilityContext.updateDataDescendant(parentContext, resolvedInfo.path.getRootElementId().getCategary(), resolvedInfo.path.getRootElementId().getName(), (HAPContextDefinitionLeafData)outputEle);
//			}
			//root variable name --- root variable full name
			nameMapping.put(rootName, resolvedInfo.path.getRootElementId().getFullName());
		}
		
		//update variable names with full name 
		outputDataAssociation.updateOutputRootName(new HAPUpdateNameMap(nameMapping));
		return resultExe;
	}
}
