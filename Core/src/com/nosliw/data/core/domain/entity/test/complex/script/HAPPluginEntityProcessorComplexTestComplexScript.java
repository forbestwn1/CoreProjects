package com.nosliw.data.core.domain.entity.test.complex.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainAttachment;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityProcessRelativeElement;
import com.nosliw.data.core.structure.reference.HAPCandidatesValueContext;
import com.nosliw.data.core.structure.reference.HAPConfigureResolveStructureElementReference;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;

public class HAPPluginEntityProcessorComplexTestComplexScript extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexTestComplexScript() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT, HAPExecutableTestComplexScript.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
		HAPDomainEntityDefinitionGlobal globalDomain = processContext.getCurrentDefinitionDomain();
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableTestComplexScript executableEntity = (HAPExecutableTestComplexScript)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPExecutableEntityValueContext valueStructureComplex = executableEntity.getValueContext();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityTestComplexScript definitionEntity = (HAPDefinitionEntityTestComplexScript)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();

//		HAPIdEntityInDomain scriptEntityId = definitionEntity.getNormalAttributeWithId(HAPDefinitionEntityTestComplexScript.ATTR_SCRIPT).getValue().getEntityId();
//		HAPDefinitionEntityScript scriptDef = (HAPDefinitionEntityScript)globalDomain.getEntityInfoDefinition(scriptEntityId).getEntity();
//		executableEntity.setScript(scriptDef.getScript());
		
		executableEntity.setScriptName(definitionEntity.getScriptName());
		
		Map<String, Object> parms = definitionEntity.getParms();
		executableEntity.setParms(parms);
	
		Object variables = parms.get(HAPExecutableTestComplexScript.VARIABLE);
		if(variables!=null) {
			JSONArray varJsonArray = (JSONArray)variables;
			List<HAPInfoReferenceResolve> resolvedVars = new ArrayList<HAPInfoReferenceResolve>();
			List<HAPReferenceElementInValueContext> unknownVars = new ArrayList<HAPReferenceElementInValueContext>();
			for(int i=0; i<varJsonArray.length(); i++) {
				HAPReferenceElementInValueContext ref = new HAPReferenceElementInValueContext();
				ref.buildObject(varJsonArray.get(i), HAPSerializationFormat.JSON);
				
				HAPInfoReferenceResolve resolve = HAPUtilityProcessRelativeElement.resolveElementReference(ref, new HAPCandidatesValueContext(valueStructureComplex, valueStructureComplex), new HAPConfigureResolveStructureElementReference(), valueStructureDomain);
				if(resolve!=null)		resolvedVars.add(resolve);
				else unknownVars.add(ref);
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
				HAPAttachment attachment = attachmentDomain.getAttachment(executableEntity.getAttachmentContainerId(), valueType, itemName);
				String entityStr = definitionDomain.getEntityInfoDefinition(attachment.getEntityId()).getEntity().toExpandedJsonString(definitionDomain);
				attachments.add(new HAPInfoAttachmentResolve(valueType, itemName, attachment, entityStr));
			}
			executableEntity.setAttachment(attachments);
		}

		
//		
//		System.out.println(new HAPIdVariable(resolve.structureId, variable).toStringValue(HAPSerializationFormat.JSON));

	
	}
}
