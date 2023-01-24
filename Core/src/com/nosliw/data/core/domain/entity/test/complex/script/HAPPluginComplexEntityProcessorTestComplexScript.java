package com.nosliw.data.core.domain.entity.test.complex.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPPluginComplexEntityProcessorImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.script.HAPDefinitionEntityScript;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityValueContext;
import com.nosliw.data.core.structure.reference.HAPCandidatesValueContext;
import com.nosliw.data.core.structure.reference.HAPConfigureResolveStructureElementReference;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;

public class HAPPluginComplexEntityProcessorTestComplexScript extends HAPPluginComplexEntityProcessorImp{

	public HAPPluginComplexEntityProcessorTestComplexScript() {
		super(HAPExecutableTestComplexScript.class);
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

		HAPIdEntityInDomain scriptEntityId = definitionEntity.getNormalAttributeWithId(HAPDefinitionEntityTestComplexScript.ATTR_SCRIPT).getValue().getEntityId();
		HAPDefinitionEntityScript scriptDef = (HAPDefinitionEntityScript)globalDomain.getEntityInfoDefinition(scriptEntityId).getEntity();
		
		executableEntity.setScript(scriptDef.getScript());
		
		Map<String, Object> parms = definitionEntity.getParms();
		executableEntity.setParms(parms);
	
		Object variables = parms.get(HAPExecutableTestComplexScript.VARIABLE);
		if(variables!=null) {
			JSONArray varJsonArray = (JSONArray)variables;
			List<HAPInfoReferenceResolve> resolvedVars = new ArrayList<HAPInfoReferenceResolve>();
			for(int i=0; i<varJsonArray.length(); i++) {
				HAPReferenceElementInValueContext ref = new HAPReferenceElementInValueContext();
				ref.buildObject(varJsonArray.get(i), HAPSerializationFormat.JSON);
				
				HAPInfoReferenceResolve resolve = HAPUtilityStructureElementReference.resolveElementReference(ref, new HAPCandidatesValueContext(valueStructureComplex, valueStructureComplex), new HAPConfigureResolveStructureElementReference(), valueStructureDomain);
				resolvedVars.add(resolve);
			}
			executableEntity.setVariables(resolvedVars);
		}
		
//		
//		System.out.println(new HAPIdVariable(resolve.structureId, variable).toStringValue(HAPSerializationFormat.JSON));

	
	}
}
