package com.nosliw.data.core.script.context.dataassociation.mapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPInfoContextElementReferenceResolve;
import com.nosliw.data.core.script.context.HAPInfoContextLeaf;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.HAPUtilityContextInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPUtilityDAProcess;

public class HAPProcessorDataAssociationMapping {

	public static HAPExecutableDataAssociationMapping processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociationMapping dataAssociation, HAPParentContext output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociationMapping out = new HAPExecutableDataAssociationMapping(dataAssociation, input);
		processDataAssociation(out, input, dataAssociation, output, daProcessConfigure, runtimeEnv);
		return out;
	}
	
	//process input configure for activity and generate flat context for activity
	public static void processDataAssociation(HAPExecutableDataAssociationMapping out, HAPParentContext input, HAPDefinitionDataAssociationMapping dataAssociation, HAPParentContext output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPContext> associations = dataAssociation.getAssociations();
		for(String targetName : associations.keySet()) {
			HAPExecutableAssociation associationExe = processAssociation(input, associations.get(targetName), output.getContext(targetName), out.getInputDependency(), daProcessConfigure, runtimeEnv);
			out.addAssociation(targetName, associationExe);
		}
	}
	
	private static HAPExecutableAssociation processAssociation(HAPParentContext input, HAPContext associationDef, HAPContextStructure outputStructure, Set<String> parentDependency, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableAssociation out = new HAPExecutableAssociation(input, associationDef, outputStructure);

		if(outputStructure instanceof HAPContextGroup) {
			//update root name with full name (containing categary and element name)
			HAPContext origin = associationDef;
			HAPContext updated = new HAPContext();
			for(String eleName : origin.getElementNames()) {
				String updatedName = eleName;
				HAPInfoContextElementReferenceResolve resolvedInfo = HAPUtilityContext.resolveReferencedContextElement(new HAPContextPath(eleName), (HAPContextGroup)outputStructure, null, null);
				if(resolvedInfo!=null) 	updatedName = resolvedInfo.path.getRootElementId().getFullName();
				updated.addElement(updatedName, origin.getElement(eleName));
			}
			associationDef = updated;
		}
		
		//process mapping
		List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
		HAPConfigureContextProcessor processConfigure = HAPUtilityDataAssociation.getContextProcessConfigurationForDataAssociation(daProcessConfigure);
		HAPContext daContextProcessed = HAPProcessorContext.process(associationDef, input, parentDependency, errors, processConfigure, runtimeEnv);
		
		//try to enhance input context
		if(HAPUtilityDAProcess.ifModifyInputStructure(daProcessConfigure)) {
			boolean needReprocess = false;
			for(HAPServiceData error : errors) {
				String errorMsg = error.getMessage();
				if(HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE.equals(errorMsg)) {
					needReprocess = true;
					HAPInfoContextLeaf contextEleInfo = (HAPInfoContextLeaf)error.getData();
					//find referred element defined in output
					HAPContextDefinitionElement targetContextEle = HAPUtilityContext.getDescendant(outputStructure.getElement(contextEleInfo.getContextPath().getRootElementId().getName(), false).getDefinition(), contextEleInfo.getContextPath().getSubPath());
					//set referred element defined in output to input
					HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)contextEleInfo.getContextElement();
					HAPUtilityContext.setDescendant(input.getContext(relativeEle.getParent()), relativeEle.getPath(), targetContextEle);
				}
				else  throw new RuntimeException();
			}
		}
		
		out.setMapping(daContextProcessed);
		buildPathMappingInDataAssociation(out);

		//try to enhance output context
		if(HAPUtilityDAProcess.ifModifyOutputStructure(daProcessConfigure)) {
			for(String eleName : associationDef.getElementNames()) {
				HAPContextPath path = new HAPContextPath(eleName);
				HAPInfoContextElementReferenceResolve solve = HAPUtilityContext.resolveReferencedContextElement(path, outputStructure);
				if(!HAPUtilityContext.isLogicallySolved(solve)) {
					//cannot find in output context
					HAPUtilityContext.setDescendant(outputStructure, path, solve.resolvedNode);
				}
			}
		}		
		
		//matchers to output
		switch(outputStructure.getType()) {
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT:
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT:
			HAPContext mapping = out.getMapping();
			for(String rootName : mapping.getElementNames()) {
				//merge back to context variable
				if(outputStructure.getElement(rootName, false)!=null) {
					Map<String, HAPMatchers> matchers = HAPUtilityContext.mergeContextRoot(outputStructure.getElement(rootName, false), mapping.getElement(rootName), HAPUtilityDAProcess.ifModifyOutputStructure(daProcessConfigure), runtimeEnv);
					//matchers when merge back to context variable
					for(String matchPath :matchers.keySet()) {
						out.addOutputMatchers(new HAPContextPath(new HAPContextDefinitionRootId(rootName), matchPath).getFullPath(), HAPMatcherUtility.reversMatchers(matchers.get(matchPath)));
					}
				}
			}
			break;
		}

		return out;
	}

	private static void buildPathMappingInDataAssociation(HAPExecutableAssociation dataAssociationExe) {
		//build path mapping according for mapped element only
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : dataAssociationExe.getMapping().getElementNames()) {
			HAPContextDefinitionRoot root = dataAssociationExe.getMapping().getElement(eleName);
			//only physical root do mapping
			if(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
				pathMapping.putAll(HAPUtilityDataAssociation.buildRelativePathMapping(root, buildRootNameAccordingToFlat(eleName, dataAssociationExe.isFlatOutput()), dataAssociationExe.isFlatInput()));
			}
		}
		dataAssociationExe.setPathMapping(pathMapping);
	}
	
	//if flat, aaa__bbb
	//if not flat, aaa.bbb
	private static String buildRootNameAccordingToFlat(String eleName, boolean isFlatOutput) {
		HAPContextDefinitionRootId eleId = new HAPContextDefinitionRootId(eleName);
		if(isFlatOutput)  return eleId.getFullName();
		else return eleId.getPath();
	}
}
