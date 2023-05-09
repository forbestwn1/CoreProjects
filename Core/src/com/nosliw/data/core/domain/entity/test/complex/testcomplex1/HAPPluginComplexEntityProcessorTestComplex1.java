package com.nosliw.data.core.domain.entity.test.complex.testcomplex1;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPPluginComplexEntityProcessorImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.container.HAPContainerEntityDefinition;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.domain.container.HAPElementContainerDefinition;
import com.nosliw.data.core.domain.container.HAPElementContainerExecutable;
import com.nosliw.data.core.domain.container.HAPUtilityContainerEntity;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityDefinitionContainer;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityDefinitionNormal;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.test.simple.testsimple1.HAPExecutableTestSimple1;
import com.nosliw.data.core.domain.entity.test.simple.testsimple1.HAPProcessorTestSimple1;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityValueContext;

public class HAPPluginComplexEntityProcessorTestComplex1 extends HAPPluginComplexEntityProcessorImp{

	public HAPPluginComplexEntityProcessorTestComplex1() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, HAPExecutableTestComplex1.class);
	}

	@Override
	public void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableTestComplex1 executableEntity = (HAPExecutableTestComplex1)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPExecutableEntityValueContext valueStructureComplex = executableEntity.getValueContext();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityTestComplex1 definitionEntity = (HAPDefinitionEntityTestComplex1)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		List<HAPAttributeEntityDefinition> attrs = definitionEntity.getAttributes();
		for(HAPAttributeEntityDefinition attr : attrs) {
			if(attr.getEntityType().equals(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL)){
				//normal attribute
				HAPAttributeEntityDefinitionNormal simpleAttrDef = (HAPAttributeEntityDefinitionNormal)attr;
				HAPEmbededDefinition embededAttributeDef = simpleAttrDef.getValue();
				HAPIdEntityInDomain attrEntityDefId = (HAPIdEntityInDomain)embededAttributeDef.getValue();
				HAPInfoEntityInDomainDefinition attrEntityInfo = definitionDomain.getEntityInfoDefinition(attrEntityDefId);
				if(simpleAttrDef.getValueTypeInfo().getIsComplex()) {
					this.processComplexAttribute(attr.getName(), complexEntityExecutableId, processContext);
				}
				else if(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1.equals(attrEntityInfo.getEntityType())) {
					HAPExecutableTestSimple1 simpleTest1Exe = HAPProcessorTestSimple1.process(attrEntityInfo.getEntityId(), processContext);
					executableEntity.setNormalAttribute(attr.getName(), new HAPEmbededExecutable(simpleTest1Exe), attr.getValueTypeInfo());
				}
			}
			else if(attr.getEntityType().equals(HAPConstantShared.ENTITYATTRIBUTE_TYPE_CONTAINER)) {
				//container attribute
				HAPAttributeEntityDefinitionContainer containerAttrDef = (HAPAttributeEntityDefinitionContainer)attr;
				HAPContainerEntityDefinition containerEntityDef = containerAttrDef.getValue();
				String eleType = attr.getValueTypeInfo().getValueType();
				if(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT.equals(eleType)) {
					this.processComplexAttribute(attr.getName(), complexEntityExecutableId, processContext);
				}
				else if(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1.equals(eleType)) {
					HAPContainerEntityExecutable conatinerEntityExe = HAPUtilityContainerEntity.buildExecutableContainer(containerEntityDef, processContext.getRuntimeEnvironment().getDomainEntityManager()); 
					List<HAPElementContainerDefinition> eleInfoDefs = containerEntityDef.getAllElements();
					for(HAPElementContainerDefinition eleInfoDef : eleInfoDefs) {
						HAPEmbededDefinition embededDef = eleInfoDef.getEmbededElementEntity();
						HAPInfoEntityInDomainDefinition attrEntityInfo = definitionDomain.getEntityInfoDefinition((HAPIdEntityInDomain)embededDef.getValue());

						HAPElementContainerExecutable eleInfoExe = HAPUtilityContainerEntity.buildExecutableContainerElement(eleInfoDef, eleInfoDef.getElementId());
						
						HAPExecutableTestSimple1 simpleTest1Exe = HAPProcessorTestSimple1.process(attrEntityInfo.getEntityId(), processContext);
						HAPEmbededExecutable embededExe = new HAPEmbededExecutable(simpleTest1Exe);
						eleInfoExe.setEmbededElementEntity(embededExe);
						conatinerEntityExe.addEntityElement(eleInfoExe);
					}
					executableEntity.setContainerAttribute(attr.getName(), conatinerEntityExe, attr.getValueTypeInfo());
				}
			}
		}
	}

}
