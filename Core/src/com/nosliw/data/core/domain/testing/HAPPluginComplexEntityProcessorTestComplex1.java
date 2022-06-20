package com.nosliw.data.core.domain.testing;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPPluginComplexEntityProcessorImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPBundleComplexResource;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityComplexValueStructure;
import com.nosliw.data.core.structure.reference.HAPCandidatesValueStructureComplex;
import com.nosliw.data.core.structure.reference.HAPConfigureResolveStructureElementReference;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInStructureComplex;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;

public class HAPPluginComplexEntityProcessorTestComplex1 extends HAPPluginComplexEntityProcessorImp{

	public HAPPluginComplexEntityProcessorTestComplex1() {
		super(HAPExecutableTestComplex1.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
		HAPBundleComplexResource currentPackage = processContext.getCurrentComplexResourcePackage();
		HAPDomainValueStructure valueStructureDomain = currentPackage.getValueStructureDomain();
		
		HAPExecutableTestComplex1 executableEntity = (HAPExecutableTestComplex1)currentPackage.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPExecutableEntityComplexValueStructure valueStructureComplex = executableEntity.getValueStructureComplex();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentPackage.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityTestComplex1 definitionEntity = (HAPDefinitionEntityTestComplex1)currentPackage.getDefinitionDomain().getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		String variable = definitionEntity.getVariable();
		
		HAPInfoReferenceResolve resolve = HAPUtilityStructureElementReference.resolveElementReference(new HAPReferenceElementInStructureComplex(variable), new HAPCandidatesValueStructureComplex(valueStructureComplex, valueStructureComplex), new HAPConfigureResolveStructureElementReference(), valueStructureDomain);
		
		System.out.println(new HAPIdVariable(resolve.structureId, variable).toStringValue(HAPSerializationFormat.JSON));
		
	}

}
