package com.nosliw.data.core.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.erro.HAPErrorUtility;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPImporterProcessSuiteDefinition;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDataAssociationIO;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.script.expression.HAPProcessContextScriptExpression;

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

	public static void buildScriptExpressionProcessContext(HAPContextFlat context, HAPProcessContextScriptExpression expProcessContext) {
		//prepare constant value 
		expProcessContext.addConstants(context.getConstantValue());
		//prepare variables 
		expProcessContext.addDataVariables(HAPUtilityContext.discoverDataVariablesInContext(context.getContext()));
	}
	
	public static HAPContextFlat processNormalActivityInputDataAssocation(HAPExecutableActivityNormal activity, HAPContextGroup processContext, HAPRequirementContextProcessor contextProcessRequirement) {
		activity.setInputDataAssociation(HAPProcessorDataAssociation.processDataAssociation(processContext, activity.getNormalActivityDefinition().getInput(), true, contextProcessRequirement));
		//input context
		return activity.getInputContext();
	}
	
	//data variables infor in activity merge back to process context
	public static void mergeDataVariableInActivityToProcessContext(Map<String, HAPVariableInfo> activityVariablesInfo, HAPContextFlat activityContext, HAPContextGroup processContext) {
		Map<String, HAPVariableInfo> expectedVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(String varName : expectedVariablesInfo.keySet()) {
			HAPVariableInfo expectedVarInfo = activityVariablesInfo.get(varName);
			HAPContextPath varPath = new HAPContextPath(varName);
			//affect global variable 
			String globalVarRootName = activityContext.getGlobalName(varPath.getRootElementId().getFullName());
			if(globalVarRootName!=null) {
				varPath = new HAPContextPath(new HAPContextDefinitionRootId(globalVarRootName), varPath.getSubPath());
				HAPContextDefinitionRoot affectedRoot = activityContext.getContext().getElement(varPath.getRootElementId().getFullName());
				if(HAPProcessorDataAssociation.isMappedRoot(affectedRoot)) {
					//ele mapped from context variable
					HAPContextDefinitionElement currentEle = affectedRoot.getDefinition();
					String[] pathSegs = new HAPPath(varPath.getSubPath()).getPathSegs();
					int i = 0;
					while(!HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE.equals(currentEle.getType())&&currentEle!=null) {
						currentEle = currentEle.getChild(pathSegs[i]);
						i++;
					}
					HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)currentEle;
					HAPContextPath relativeElePath = relativeEle.getPath();
					String fullName = relativeElePath.getFullPath();
					for(;i<pathSegs.length; i++) {
						fullName = HAPNamingConversionUtility.buildPath(fullName, pathSegs[i]);
					}
					expectedVariablesInfo.put(fullName, expectedVarInfo);
				}
				else {
					//inhereted ele
					expectedVariablesInfo.put(varPath.getFullPath(), expectedVarInfo);
				}
			}
			else {
				//root variable does not exist, generate one
//				HAPContextDefinitionLeafData dataEle = new HAPContextDefinitionLeafData(new HAPVariableInfo(out.getScriptExpressionProcessContext().getDataVariables().get(varName).getCriteria()));
//				affectedContext.addElement(varName, dataEle);
				HAPErrorUtility.invalid("");
			}
		}

		//affect parent context
		for(String basePath : expectedVariablesInfo.keySet()) {
			HAPContextPath cpath = new HAPContextPath(basePath);
			HAPContextDefinitionLeafData affectedEle = new HAPContextDefinitionLeafData(expectedVariablesInfo.get(basePath));
			HAPUtilityContext.updateDataDescendant(processContext, cpath.getRootElementId().getCategary(), cpath.getPath(), affectedEle);
		}
		
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
		if(resultContextBuilder!=null) {
			//data association input context
			HAPDataAssociationIO dataAssociationInputContext = resultContextBuilder.buildResultContext(resultName, activity);
			//process data association
			HAPProcessorDataAssociation.processDataAssociation(dataAssociationInputContext, resultExe, parentContext, true, contextProcessRequirement);
		}
		return resultExe;
	}

}
