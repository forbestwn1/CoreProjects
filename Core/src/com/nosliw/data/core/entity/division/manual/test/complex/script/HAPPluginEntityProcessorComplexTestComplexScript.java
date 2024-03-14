package com.nosliw.data.core.entity.division.manual.test.complex.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.HAPDomainAttachment;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityValueContextReference;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentImpEntity;
import com.nosliw.data.core.domain.entity.test.complex.script.HAPDefinitionVariableExpected;
import com.nosliw.data.core.domain.entity.test.complex.script.HAPExecutableTestComplexScript;
import com.nosliw.data.core.domain.entity.test.complex.script.HAPExecutableVariableExpected;
import com.nosliw.data.core.domain.entity.test.complex.script.HAPInfoAttachmentResolve;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityProcessRelativeElement;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;
import com.nosliw.data.core.domain.valueport.HAPReferenceElementInValueStructure;
import com.nosliw.data.core.entity.HAPEntityBundle;
import com.nosliw.data.core.entity.HAPEntityExecutable;
import com.nosliw.data.core.entity.HAPEntityExecutableComplex;
import com.nosliw.data.core.entity.division.manual.HAPContextProcess;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPManualInfoEntity;
import com.nosliw.data.core.entity.division.manual.HAPPluginProcessorEntityDefinitionComplexImp;
import com.nosliw.data.core.entity.division.manual.HAPUtilityDefinitionEntity;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.reference.HAPConfigureResolveStructureElementReference;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;

public class HAPPluginEntityProcessorComplexTestComplexScript extends HAPPluginProcessorEntityDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexTestComplexScript() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT, HAPExecutableTestComplexScript.class);
	}

	@Override
	public void processEntity(HAPPath pathFromRoot, HAPContextProcess processContext) {
		
		HAPEntityBundle bundle = processContext.getCurrentBundle();
		
		Pair<HAPManualEntity, HAPEntityExecutable> entityPair = HAPUtilityDefinitionEntity.getEntityPair(pathFromRoot, bundle);
		HAPDefinitionEntityTestComplexScript definitionEntity = (HAPDefinitionEntityTestComplexScript)entityPair.getLeft();
		HAPExecutableTestComplexScript executableEntity = (HAPExecutableTestComplexScript)entityPair.getRight();
		
		Map<String, Object> parms = definitionEntity.getParms();

		executableEntity.setScriptName(definitionEntity.getScriptName());
		
		executableEntity.setParms(parms);
	
		//normal variable
		Object variables = parms.get(HAPExecutableTestComplexScript.VARIABLE);
		if(variables!=null) {
			JSONArray varJsonArray = (JSONArray)variables;
			List<HAPInfoReferenceResolve> resolvedVars = new ArrayList<HAPInfoReferenceResolve>();
			List<HAPReferenceElementInValueStructure> unknownVars = new ArrayList<HAPReferenceElementInValueStructure>();
			for(int i=0; i<varJsonArray.length(); i++) {
				HAPReferenceElementInValueStructure ref = new HAPReferenceElementInValueStructure();
				ref.buildObject(varJsonArray.get(i), HAPSerializationFormat.JSON);
				
				HAPInfoReferenceResolve resolve = HAPUtilityProcessRelativeElement.resolveElementReference(ref, new HAPConfigureResolveStructureElementReference(), processContext);
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

	
	
	
	
	@Override
	public void processValueContext(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPExecutableTestComplexScript executableEntity = (HAPExecutableTestComplexScript)complexEntityExecutable;
		
		HAPIdEntityInDomain complexEntityDefinitionId = executableEntity.getDefinitionEntityId();
		HAPDefinitionEntityTestComplexScript definitionEntity = (HAPDefinitionEntityTestComplexScript)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		Map<String, Object> parms = definitionEntity.getParms();
		
		//extended variable
		Object variablesExtension = parms.get(HAPExecutableTestComplexScript.VARIABLEEXTENDED);
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
		
		HAPExecutableTestComplexScript executableEntity = (HAPExecutableTestComplexScript)complexEntityExecutable;
		HAPExecutableEntityValueContext valueStructureComplex = executableEntity.getValueContext();
		
		HAPIdEntityInDomain complexEntityDefinitionId = complexEntityExecutable.getDefinitionEntityId();
		HAPDefinitionEntityTestComplexScript definitionEntity = (HAPDefinitionEntityTestComplexScript)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();

		
		HAPEntityBundle bundle = processContext.getCurrentBundle();
		HAPUtilityEntityDefinition.getEntityDefinitionFromExeTreeNodeE(null, bundle)
//		HAPUtilityEntity.getEntityDefinitionFromExeTreeNodeE(bundle., bundle)
		
		
		Map<String, Object> parms = definitionEntity.getParms();

		executableEntity.setScriptName(definitionEntity.getScriptName());
		
		executableEntity.setParms(parms);
	
		//normal variable
		Object variables = parms.get(HAPExecutableTestComplexScript.VARIABLE);
		if(variables!=null) {
			JSONArray varJsonArray = (JSONArray)variables;
			List<HAPInfoReferenceResolve> resolvedVars = new ArrayList<HAPInfoReferenceResolve>();
			List<HAPReferenceElementInValueStructure> unknownVars = new ArrayList<HAPReferenceElementInValueStructure>();
			for(int i=0; i<varJsonArray.length(); i++) {
				HAPReferenceElementInValueStructure ref = new HAPReferenceElementInValueStructure();
				ref.buildObject(varJsonArray.get(i), HAPSerializationFormat.JSON);
				
				HAPInfoReferenceResolve resolve = HAPUtilityProcessRelativeElement.resolveElementReference(ref, new HAPConfigureResolveStructureElementReference(), processContext);
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
		Object attachmentsObj = parms.get(HAPExecutableTestComplexScript.ATTACHMENT);
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
		
		HAPExecutableTestComplexScript executableEntity = (HAPExecutableTestComplexScript)complexEntityExecutable;
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
		HAPExecutableTestComplexScript executableEntity = (HAPExecutableTestComplexScript)complexEntityExecutable;
		
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
}
