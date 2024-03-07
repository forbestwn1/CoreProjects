package com.nosliw.data.core.domain.entity;

import java.util.Set;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.entity.division.manual.HAPManualAttribute;

public class HAPUtilityEntityProcess {

	public static void processComplexAttribute(String attrName, HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableEntityComplex complexEntityExe = processContext.getCurrentExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPAttributeEntityExecutable attrExe = complexEntityExe.getAttribute(attrName);

		//process attribute entity
		processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().processComplexEntity((HAPIdEntityInDomain)attrExe.getValue().getValue(), processContext);
		
		//process attribute adapter
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		HAPManualAttribute attrDef = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity().getAttribute(attrName);
		Set<HAPInfoAdapter> adapters = attrDef.getValue().getAdapters();

		for(HAPInfoAdapter adapter : adapters) {
			Object adapterExeObj = processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().processEmbededAdapter(adapter, complexEntityExe, attrExe.getValue().getValue(), processContext);
			HAPInfoAdapterExecutable adapterExe = new HAPInfoAdapterExecutable(adapter.getValueType(), adapterExeObj);
			adapter.cloneToEntityInfo(adapterExe);
			attrExe.getValue().addAdapter(adapterExe);
		}
	}
	
	public static HAPAttributeEntityExecutable processSimpleAttribute(String attrName, HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPManualAttribute attrDef = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity().getAttribute(attrName);

		HAPManualAttribute normalAttrDef = attrDef;
		HAPExecutableEntity entityExe = processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().processSimpleEntity((HAPIdEntityInDomain)normalAttrDef.getValue().getValue(), processContext);

		HAPAttributeEntityExecutable attrNormalExe = new HAPAttributeEntityExecutable(attrName, new HAPEmbededExecutable(entityExe), attrDef.getValueTypeInfo()); 
		
		Set<HAPInfoAdapter> adapters = attrDef.getValue().getAdapters();
		HAPExecutableEntityComplex complexEntityExe = processContext.getCurrentExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		for(HAPInfoAdapter adapter : adapters) {
			Object adapterExeObj = processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().processEmbededAdapter(adapter, complexEntityExe, entityExe, processContext);
			HAPInfoAdapterExecutable adapterExe = new HAPInfoAdapterExecutable(adapter.getValueType(), adapterExeObj);
			adapter.cloneToEntityInfo(adapterExe);
			attrNormalExe.getValue().addAdapter(adapterExe);
		}
		return attrNormalExe;
	}
	
}
