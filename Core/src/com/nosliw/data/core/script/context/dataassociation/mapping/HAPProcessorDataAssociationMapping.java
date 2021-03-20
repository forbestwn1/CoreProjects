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
import com.nosliw.data.core.script.context.HAPContextDefEleProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPInfoContextElementReferenceResolve;
import com.nosliw.data.core.script.context.HAPInfoContextNode;
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

	public static void enhanceDataAssociationEndPointContext(HAPParentContext input, boolean inputEnhance, HAPDefinitionDataAssociationMapping dataAssociation, HAPParentContext output, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPContext> associations = dataAssociation.getAssociations();
		for(String targetName : associations.keySet()) {
			enhanceAssociationEndPointContext(input, inputEnhance, associations.get(targetName), output.getContext(targetName), outputEnhance, runtimeEnv);
		}
	}
	
	//enhance input and output context according to dataassociation
	private static void enhanceAssociationEndPointContext(HAPParentContext input, boolean inputEnhance, HAPContext associationDef, HAPContextStructure outputStructure, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
		associationDef = normalizeOutputNameInDataAssociation(input, associationDef, outputStructure);
		HAPInfo info = HAPUtilityDAProcess.withModifyInputStructureConfigure(null, inputEnhance);
		info = HAPUtilityDAProcess.withModifyOutputStructureConfigure(info, outputEnhance);
		HAPConfigureContextProcessor processConfigure = HAPUtilityDataAssociation.getContextProcessConfigurationForDataAssociation(info);
		List<HAPServiceData> errors = new ArrayList<HAPServiceData>();

		//process data association definition in order to find missing context data definition from input
		HAPContext daContextProcessed = HAPProcessorContext.process(associationDef, input, null, errors, processConfigure, runtimeEnv);
		
		//try to enhance input context according to error
		if(inputEnhance) {
			for(HAPServiceData error : errors) {
				String errorMsg = error.getMessage();
				if(HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE.equals(errorMsg)) {
					//enhance input context according to error
					HAPInfoContextNode contextEleInfo = (HAPInfoContextNode)error.getData();
					//find referred element defined in output
					HAPContextPath path = contextEleInfo.getContextPath();
					HAPContextDefinitionElement sourceContextEle = HAPUtilityContext.getDescendant(outputStructure.getElement(path.getRootElementId().getName(), false).getDefinition(), path.getSubPath());
					if(sourceContextEle==null)  throw new RuntimeException();
					//update input: set referred element defined in output to input
					HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)contextEleInfo.getContextElement();
					HAPContextDefinitionElement solidateSourceContextEle = sourceContextEle.getSolidContextDefinitionElement();
					if(solidateSourceContextEle==null)    throw new RuntimeException();
					HAPUtilityContext.setDescendant(input.getContext(relativeEle.getParent()), relativeEle.getPath(), solidateSourceContextEle.cloneContextDefinitionElement());
				}
				else  throw new RuntimeException();
			}
		}
		
		//try to enhance output context
		if(outputEnhance) {
			for(String eleName : associationDef.getElementNames()) {
				HAPUtilityContext.processContextDefElement(new HAPInfoContextNode(associationDef.getElement(eleName).getDefinition(), new HAPContextPath(eleName)), new HAPContextDefEleProcessor() {

					@Override
					public boolean process(HAPInfoContextNode eleInfo, Object value) {
						HAPContextStructure outputStructure = (HAPContextStructure)value;
						if(eleInfo.getContextElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							//only relative element
							HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)eleInfo.getContextElement();
							//if element path exist in output structure
							HAPInfoContextElementReferenceResolve targetResolvedInfo = HAPUtilityContext.resolveReferencedContextElement(eleInfo.getContextPath(), outputStructure);
							if(!HAPUtilityContext.isLogicallySolved(targetResolvedInfo)) {
								//target node in output according to path not exist
								//element in input structure
								HAPContextStructure sourceContextStructure = input.getContext(relativeEle.getParent());
								HAPInfoContextElementReferenceResolve sourceResolvedInfo = HAPUtilityContext.resolveReferencedContextElement(relativeEle.getPath(), sourceContextStructure);
								if(HAPUtilityContext.isLogicallySolved(sourceResolvedInfo)) {
									HAPContextDefinitionElement sourceEle = sourceResolvedInfo.resolvedNode;
									if(sourceEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
										HAPUtilityContext.setDescendant(outputStructure, eleInfo.getContextPath(), sourceEle.getSolidContextDefinitionElement());
									}
									else if(sourceEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_VALUE)) {
										
									}
									else if(sourceEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
										
									}
								}
								else  throw new RuntimeException();
							}
						}
						return false;
					}

					@Override
					public boolean postProcess(HAPInfoContextNode eleInfo, Object value) {
						return false;
					}
				}, outputStructure);
			}			
		}		
	}
	
	//make output name in da to be global name according to refference to outputStrucutre
	private static HAPContext normalizeOutputNameInDataAssociation(HAPParentContext input, HAPContext associationDef, HAPContextStructure outputStructure) {
		HAPContext out = associationDef;
		if(outputStructure instanceof HAPContextGroup) {
			//for output context group only
			//find refereed context in output
			//update root name with full name (containing categary and element name)
			HAPContext origin = associationDef;
			out = new HAPContext();
			for(String eleName : origin.getElementNames()) {
				String updatedName = eleName;
				HAPInfoContextElementReferenceResolve resolvedInfo = HAPUtilityContext.resolveReferencedContextElement(new HAPContextPath(eleName), (HAPContextGroup)outputStructure, null, null);
				if(resolvedInfo!=null) 	updatedName = resolvedInfo.path.getRootElementId().getFullName();
				out.addElement(updatedName, origin.getElement(eleName));
			}
		}
		return out;
	}

	private static HAPExecutableAssociation processAssociation(HAPParentContext input, HAPContext associationDef, HAPContextStructure outputStructure, Set<String> parentDependency, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableAssociation out = new HAPExecutableAssociation(input, associationDef, outputStructure);

		associationDef = normalizeOutputNameInDataAssociation(input, associationDef, outputStructure);
		
		//process mapping
		List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
		HAPConfigureContextProcessor processConfigure = HAPUtilityDataAssociation.getContextProcessConfigurationForDataAssociation(daProcessConfigure);
		HAPContext daContextProcessed = HAPProcessorContext.process(associationDef, input, parentDependency, errors, processConfigure, runtimeEnv);
		
		out.setMapping(daContextProcessed);
		buildPathMappingInDataAssociation(out);

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
