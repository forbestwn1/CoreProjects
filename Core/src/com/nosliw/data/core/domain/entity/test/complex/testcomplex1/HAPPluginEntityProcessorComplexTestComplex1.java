package com.nosliw.data.core.domain.entity.test.complex.testcomplex1;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityDefinitionNormal;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.domain.entity.HAPUtilityEntityProcess;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;

public class HAPPluginEntityProcessorComplexTestComplex1 extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexTestComplex1() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, HAPExecutableTestComplex1.class);
	}

	@Override
	public void postProcess(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPExecutableTestComplex1 executableEntity = (HAPExecutableTestComplex1)currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPExecutableEntityValueContext valueStructureComplex = executableEntity.getValueContext();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPDefinitionEntityTestComplex1 definitionEntity = (HAPDefinitionEntityTestComplex1)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		
		List<HAPAttributeEntityDefinition> attrs = definitionEntity.getAttributes();
		for(HAPAttributeEntityDefinition attr : attrs ) {
			if(attr.getEntityType().equals(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL)){
				String valueType = attr.getValueTypeInfo().getValueType();
				if(!(valueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)||valueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT))) {
					//normal attribute
					HAPAttributeEntityDefinitionNormal simpleAttrDef = (HAPAttributeEntityDefinitionNormal)attr;
					HAPEmbededDefinition embededAttributeDef = simpleAttrDef.getValue();
					HAPIdEntityInDomain attrEntityDefId = (HAPIdEntityInDomain)embededAttributeDef.getValue();
					HAPInfoEntityInDomainDefinition attrEntityInfo = definitionDomain.getEntityInfoDefinition(attrEntityDefId);
					if(simpleAttrDef.getValueTypeInfo().getIsComplex()) {
						//complex attribute
						HAPUtilityEntityProcess.processComplexAttribute(attr.getName(), complexEntityExecutableId, processContext);
					}
					else {
						HAPAttributeEntityExecutable attrExe = HAPUtilityEntityProcess.processSimpleAttribute(attr.getName(), complexEntityExecutableId, processContext);
						executableEntity.setAttribute(attrExe);
					}
				}
			}
		}
	}
}
