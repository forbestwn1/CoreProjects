package com.nosliw.data.core.domain.testing.testcomplex1;

import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPPluginComplexEntityProcessorImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPEmbededDefinitionWithId;
import com.nosliw.data.core.domain.HAPEmbededExecutableWithEntity;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.container.HAPContainerEntityDefinition;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.domain.container.HAPElementContainerDefinition;
import com.nosliw.data.core.domain.container.HAPElementContainerExecutable;
import com.nosliw.data.core.domain.container.HAPUtilityContainerEntity;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityComplexValueStructure;
import com.nosliw.data.core.domain.testing.testsimple1.HAPExecutableTestSimple1;
import com.nosliw.data.core.domain.testing.testsimple1.HAPProcessorTestSimple1;

public class HAPPluginComplexEntityProcessorTestComplex1 extends HAPPluginComplexEntityProcessorImp{

	public HAPPluginComplexEntityProcessorTestComplex1() {
		super(HAPExecutableTestComplex1.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableTestComplex1 executableEntity = (HAPExecutableTestComplex1)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPExecutableEntityComplexValueStructure valueStructureComplex = executableEntity.getValueStructureComplex();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityTestComplex1 definitionEntity = (HAPDefinitionEntityTestComplex1)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		//normal attribute
		Map<String, HAPEmbededDefinitionWithId> simpleAttrs = definitionEntity.getSimpleAttributes();
		for(String simpleAttrName : simpleAttrs.keySet()) {
			HAPIdEntityInDomain attrEntityId = simpleAttrs.get(simpleAttrName).getEntityId();
			HAPInfoEntityInDomainDefinition attrEntityInfo = definitionDomain.getEntityInfoDefinition(attrEntityId);
			if(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1.equals(attrEntityInfo.getEntityType())) {
				HAPExecutableTestSimple1 simpleTest1Exe = HAPProcessorTestSimple1.process(attrEntityInfo.getEntityId(), processContext);
				executableEntity.setAttribute(simpleAttrName, new HAPEmbededExecutableWithEntity(simpleTest1Exe, attrEntityInfo.getEntityType(), attrEntityInfo.isComplexEntity()));
			}
		}
		
		//container attribute
		Map<String, HAPContainerEntityDefinition> containerAttrs = definitionEntity.getContainerAttributes();
		for(String attrName : containerAttrs.keySet()) {
			HAPContainerEntityDefinition containerEntityDef = containerAttrs.get(attrName);

			if(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1.equals(containerEntityDef.getElementType())) {
				HAPContainerEntityExecutable conatinerEntityExe = HAPUtilityContainerEntity.buildExecutableContainer(containerEntityDef, processContext.getRuntimeEnvironment().getDomainEntityManager()); 
				List<HAPElementContainerDefinition> eleInfoDefs = containerEntityDef.getAllElements();
				for(HAPElementContainerDefinition eleInfoDef : eleInfoDefs) {
					HAPEmbededDefinitionWithId embededDef = (HAPEmbededDefinitionWithId)eleInfoDef.getEmbededElementEntity();
					HAPInfoEntityInDomainDefinition attrEntityInfo = definitionDomain.getEntityInfoDefinition(embededDef.getEntityId());

					HAPElementContainerExecutable eleInfoExe = HAPUtilityContainerEntity.buildExecutableContainerElement(eleInfoDef, eleInfoDef.getElementId());
					
					HAPExecutableTestSimple1 simpleTest1Exe = HAPProcessorTestSimple1.process(attrEntityInfo.getEntityId(), processContext);
					HAPEmbededExecutableWithEntity embededExe = new HAPEmbededExecutableWithEntity(simpleTest1Exe, containerEntityDef.getElementType(), containerEntityDef.getIsComplex());
					eleInfoExe.setEmbededElementEntity(embededExe);
					conatinerEntityExe.addEntityElement(eleInfoExe);
				}
				executableEntity.setAttribute(attrName, conatinerEntityExe);
			}
		}
		
		
		
		
//		String variable = definitionEntity.getVariable();
//		
//		HAPInfoReferenceResolve resolve = HAPUtilityStructureElementReference.resolveElementReference(new HAPReferenceElementInStructureComplex(variable), new HAPCandidatesValueStructureComplex(valueStructureComplex, valueStructureComplex), new HAPConfigureResolveStructureElementReference(), valueStructureDomain);
//		
//		System.out.println(new HAPIdVariable(resolve.structureId, variable).toStringValue(HAPSerializationFormat.JSON));
		
	}

}
