package com.nosliw.data.core.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPImporterProcessSuiteDefinition;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
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

	public static void buildScriptExpressionProcessContext(HAPContext context, HAPProcessContextScriptExpression expProcessContext) {
		//prepare constant value 
		expProcessContext.addConstants(context.getConstantValue());
		//prepare variables 
		expProcessContext.addDataVariables(HAPUtilityContext.discoverDataVariablesInContext(context));
	}
	
	public static void processNormalActivityInputDataAssocation(HAPExecutableActivityNormal activity, HAPContextGroup processContext, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociation da = HAPProcessorDataAssociation.processDataAssociation(
				HAPParentContext.createDefault(processContext), 
				activity.getNormalActivityDefinition().getInputMapping(), 
				HAPParentContext.createDefault(activity.getNormalActivityDefinition().getInputContextStructure(processContext)), 
				null, 
				contextProcessRequirement);
		activity.setInputDataAssociation(da);
	}
	
	//data variables infor in activity merge back to process context
	public static void mergeDataVariableInActivityToProcessContext(Map<String, HAPVariableInfo> activityVariablesInfo, HAPContext activityContext, HAPContextGroup processContext) {
		Map<String, HAPVariableInfo> expectedVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(String varName : expectedVariablesInfo.keySet()) {
			HAPVariableInfo expectedVarInfo = activityVariablesInfo.get(varName);
			HAPContextPath varPath = new HAPContextPath(varName);
			//affect global variable 
			HAPContextDefinitionRoot affectedRoot = activityContext.getElement(varPath.getRootElementId().getFullName());
			if(affectedRoot!=null) {
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
			HAPContextStructure dataAssociationInputContext = resultContextBuilder.buildResultContext(resultName, activity);
			//process data association
			HAPExecutableDataAssociation outputDataAssociation = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(dataAssociationInputContext), resultDef.getOutputDataAssociation(), HAPParentContext.createDefault(parentContext), null, contextProcessRequirement);
			resultExe.setDataAssociation(outputDataAssociation);
		}
		return resultExe;
	}

	//build task wrapper for activity has task in it
	//all the input and result output for activity is mirror 
	public static HAPDefinitionWrapperTask parseTaskDefinition(HAPDefinitionActivityNormal activity, JSONObject jsonObj) {
		HAPDefinitionWrapperTask out = new HAPDefinitionWrapperTask();
		activity.setInputMapping(new HAPDefinitionDataAssociationMirror());
		
		out.buildMapping(jsonObj);
		
		Map<String, HAPDefinitionResultActivityNormal> results = activity.getResults();
		for(String resultName : results.keySet()) {
			HAPDefinitionResultActivityNormal result = results.get(resultName);
			HAPDefinitionDataAssociation dataAssociation = result.getOutputDataAssociation();
			out.addOutputMapping(resultName, dataAssociation.cloneDataAssocation());
			result.setOutputDataAssociation(new HAPDefinitionDataAssociationMirror());
		}
		return out;
	}
	
}
