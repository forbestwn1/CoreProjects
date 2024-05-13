package com.nosliw.core.application.division.manual.brick.test.complex.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.test.complex.script.HAPBlockTestComplexScript;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualUtilityBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockComplexImp;

public class HAPManualPluginProcessorBlockComplexTestComplexScript extends HAPPluginProcessorBlockComplexImp{

	public HAPManualPluginProcessorBlockComplexTestComplexScript() {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100);
	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		
		HAPBundle bundle = processContext.getCurrentBundle();
		
		Pair<HAPManualBrick, HAPBrick> entityPair = HAPManualUtilityBrick.getEntityPair(pathFromRoot, bundle);
		HAPManualBrickTestComplexScript definitionEntity = (HAPManualBrickTestComplexScript)entityPair.getLeft();
		HAPBlockTestComplexScript executableEntity = (HAPBlockTestComplexScript)entityPair.getRight();
		
		Map<String, Object> parms = definitionEntity.getParms();

		executableEntity.setScriptName(definitionEntity.getScriptName());
		
		executableEntity.setParms(parms);
	
		//normal variable
		Object variables = parms.get(HAPBlockTestComplexScript.VARIABLE);
		if(variables!=null) {
			JSONArray varJsonArray = (JSONArray)variables;
			List<HAPResultReferenceResolve> resolvedVars = new ArrayList<HAPResultReferenceResolve>();
			List<HAPReferenceElement> unknownVars = new ArrayList<HAPReferenceElement>();
			for(int i=0; i<varJsonArray.length(); i++) {
				HAPReferenceElement ref = new HAPReferenceElement();
				ref.buildObject(varJsonArray.get(i), HAPSerializationFormat.JSON);
				ref.setValuePortRef(HAPUtilityValuePort.normalizeValuePortReference(ref.getValuePortRef(), pathFromRoot, bundle, processContext.getRuntimeEnv().getResourceManager(), processContext.getRuntimeEnv().getRuntime().getRuntimeInfo()));
				HAPResultReferenceResolve resolve  = HAPUtilityStructureElementReference.analyzeElementReference(ref, new HAPConfigureResolveElementReference(), bundle, processContext.getRuntimeEnv().getResourceManager(), processContext.getRuntimeEnv().getRuntime().getRuntimeInfo());
				
				if(resolve!=null) {
					resolvedVars.add(resolve);
				} else {
					unknownVars.add(ref);
				}
			}
			executableEntity.setVariables(resolvedVars);
			executableEntity.setUnknowVariable(unknownVars);
		}
		
		
		
	}

	
/*	
	
	
	@Override
	public void processValueContext(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPBlockTestComplexScript executableEntity = (HAPBlockTestComplexScript)complexEntityExecutable;
		
		HAPIdEntityInDomain complexEntityDefinitionId = executableEntity.getDefinitionEntityId();
		HAPManualBrickTestComplexScript definitionEntity = (HAPManualBrickTestComplexScript)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		Map<String, Object> parms = definitionEntity.getParms();
		
		//extended variable
		Object variablesExtension = parms.get(HAPBlockTestComplexScript.VARIABLEEXTENDED);
		if(variablesExtension!=null) {
			JSONArray varJsonArray = (JSONArray)variablesExtension;
			List<HAPExecutableVariableExpected> resolvedVars = new ArrayList<HAPExecutableVariableExpected>();
			for(int i=0; i<varJsonArray.length(); i++) {
				
				JSONObject varJson = varJsonArray.getJSONObject(i);
				HAPDefinitionVariableExpected varDef = new HAPDefinitionVariableExpected();
				varDef.buildObject(varJson, HAPSerializationFormat.JSON);

				HAPExecutableVariableExpected varExe = new HAPExecutableVariableExpected(varDef);
				resolvedVars.add(varExe);
			}
			executableEntity.setExtendedVariables(resolvedVars);
		}
	}
	
	@Override
	public void processEntity(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPBlockTestComplexScript executableEntity = (HAPBlockTestComplexScript)complexEntityExecutable;
		HAPExecutableEntityValueContext valueStructureComplex = executableEntity.getValueContext();
		
		HAPIdEntityInDomain complexEntityDefinitionId = complexEntityExecutable.getDefinitionEntityId();
		HAPManualBrickTestComplexScript definitionEntity = (HAPManualBrickTestComplexScript)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();

		
		HAPEntityBundle bundle = processContext.getCurrentBundle();
		HAPUtilityEntityDefinition.getEntityDefinitionFromExeTreeNodeE(null, bundle)
//		HAPUtilityBrick.getEntityDefinitionFromExeTreeNodeE(bundle., bundle)
		
		
		Map<String, Object> parms = definitionEntity.getParms();

		executableEntity.setScriptName(definitionEntity.getScriptName());
		
		executableEntity.setParms(parms);
	
		//normal variable
		Object variables = parms.get(HAPBlockTestComplexScript.VARIABLE);
		if(variables!=null) {
			JSONArray varJsonArray = (JSONArray)variables;
			List<HAPResultReferenceResolve> resolvedVars = new ArrayList<HAPResultReferenceResolve>();
			List<HAPReferenceElement> unknownVars = new ArrayList<HAPReferenceElement>();
			for(int i=0; i<varJsonArray.length(); i++) {
				HAPReferenceElement ref = new HAPReferenceElement();
				ref.buildObject(varJsonArray.get(i), HAPSerializationFormat.JSON);
				
				HAPResultReferenceResolve resolve = HAPUtilityProcessRelativeElement.resolveElementReference(ref, new HAPConfigureResolveElementReference(), processContext);
				if(resolve!=null) {
					resolvedVars.add(resolve);
				} else {
					unknownVars.add(ref);
				}
			}
			executableEntity.setVariables(resolvedVars);
			executableEntity.setUnknowVariable(unknownVars);
		}
	
		HAPDomainAttachment attachmentDomain = processContext.getCurrentBundle().getAttachmentDomain();
		Object attachmentsObj = parms.get(HAPBlockTestComplexScript.ATTACHMENT);
		if(attachmentsObj!=null) {
			List<HAPInfoAttachmentResolve> attachments = new ArrayList<HAPInfoAttachmentResolve>(); 
			JSONArray attachmentsArray = (JSONArray)attachmentsObj;
			for(int i=0; i<attachmentsArray.length(); i++) {
				String attIdStr = (String)attachmentsArray.get(i);
				String[] segs = HAPUtilityNamingConversion.parseLevel2(attIdStr);
				String valueType = segs[0];
				String itemName = segs[1];
				HAPAttachmentImpEntity attachment = (HAPAttachmentImpEntity)attachmentDomain.getAttachment(executableEntity.getAttachmentContainerId(), valueType, itemName);
				String entityStr = attachment.getEntity().toExpandedJsonString(definitionDomain);
				attachments.add(new HAPInfoAttachmentResolve(valueType, itemName, attachment, entityStr));
			}
			executableEntity.setAttachment(attachments);
		}

//		System.out.println(new HAPIdVariable(resolve.structureId, variable).toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public void processValueContextExtension(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		super.processValueContextExtension(complexEntityExecutable, processContext);
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPBlockTestComplexScript executableEntity = (HAPBlockTestComplexScript)complexEntityExecutable;
		HAPExecutableEntityValueContext valueStructureComplex = executableEntity.getValueContext();
		
		List<HAPExecutableVariableExpected> expectedVars = executableEntity.getExtendedVariables();
		if(expectedVars!=null) {
			for(HAPExecutableVariableExpected extendedVar : expectedVars) {
				HAPDefinitionVariableExpected varDef = extendedVar.getDefinition();
				HAPIdVariable idVariable = HAPUtilityValueContextReference.resolveVariableName(varDef.getVariableName(), valueStructureComplex, varDef.getGroup(), valueStructureDomain, null);
				extendedVar.setVariableId(idVariable);
			}
		}
	}
	
	@Override
	public void processValueContextDiscovery(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		super.processValueContextDiscovery(complexEntityExecutable, processContext);
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		HAPBlockTestComplexScript executableEntity = (HAPBlockTestComplexScript)complexEntityExecutable;
		
		List<HAPExecutableVariableExpected> expectedVars = executableEntity.getExtendedVariables();
		if(expectedVars!=null) {
			for(HAPExecutableVariableExpected extendedVar : expectedVars) {
				HAPDefinitionVariableExpected varDef = extendedVar.getDefinition();
				HAPElementStructureLeafData dataStructureEle = (HAPElementStructureLeafData)HAPUtilityValueContext.getStructureElement(extendedVar.getVariableId(), valueStructureDomain);
				
				HAPMatchers matchers = HAPUtilityCriteria.mergeVariableInfo(dataStructureEle.getCriteriaInfo(), new HAPDataTypeCriteriaId(varDef.getDataTypeId(), null), processContext.getRuntimeEnvironment().getDataTypeHelper());
				extendedVar.setMarchers(matchers);
			}
		}
	}
	
*/	
}
