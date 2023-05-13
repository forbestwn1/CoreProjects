package com.nosliw.data.core.complex;

import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.container.HAPElementContainerExecutable;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityDefinitionNormal;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutableContainer;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutableNormal;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPInfoAdapter;
import com.nosliw.data.core.domain.entity.HAPInfoAdapterExecutable;

public abstract class HAPPluginEntityProcessorComplexImp implements HAPPluginEntityProcessorComplex{

	private Class<? extends HAPExecutableEntityComplex> m_exeEntityClass;
	
	private String m_entityType;
	
	public HAPPluginEntityProcessorComplexImp(String entityType, Class<? extends HAPExecutableEntityComplex> exeEntityClass) {
		this.m_entityType = entityType;
		this.m_exeEntityClass = exeEntityClass;
	}

	@Override
	public String getEntityType() {    return this.m_entityType;    }
	
	@Override
	public HAPExecutableEntityComplex newExecutable() {
		HAPExecutableEntityComplex out = null;
		try {
			out = this.m_exeEntityClass.newInstance();
			out.setEntityType(this.getEntityType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out; 
	}
	
	protected HAPAttributeEntityExecutable processSimpleAttribute(String attrName, HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
		
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPAttributeEntityDefinition attrDef = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity().getAttribute(attrName);

		if(attrDef.getEntityType().equals(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL)) {
			HAPAttributeEntityDefinitionNormal normalAttrDef = (HAPAttributeEntityDefinitionNormal)attrDef;
			HAPExecutableEntity entityExe = processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().processSimpleEntity((HAPIdEntityInDomain)attrDef.getValue(), processContext);

			HAPAttributeEntityExecutableNormal attrNormalExe = new HAPAttributeEntityExecutableNormal(attrName, new HAPEmbededExecutable(entityExe), attrDef.getValueTypeInfo()); 
			
			Set<HAPInfoAdapter> adapters = ((HAPEmbededDefinition)attrDef.getValue()).getAdapters();
			HAPExecutableEntityComplex complexEntityExe = processContext.getCurrentExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
			for(HAPInfoAdapter adapter : adapters) {
				Object adapterExeObj = processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().processEmbededAdapter(adapter, complexEntityExe, entityExe, processContext);
				HAPInfoAdapterExecutable adapterExe = new HAPInfoAdapterExecutable(adapter.getValueType(), adapterExeObj);
				adapter.cloneToEntityInfo(adapterExe);
				attrNormalExe.getValue().addAdapter(adapterExe);
			}
			return attrNormalExe;
		}
		
		return null;
	}
	
	protected void processComplexAttribute(String attrName, HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableEntityComplex complexEntityExe = processContext.getCurrentExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPAttributeEntityExecutable attrExe = complexEntityExe.getAttribute(attrName);
		if(attrExe.getEntityType().equals(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL)) {
			//normal attribute
			HAPAttributeEntityExecutableNormal attrNormalExe = (HAPAttributeEntityExecutableNormal)attrExe;
			//process attribute entity
			processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().processComplexEntity((HAPIdEntityInDomain)attrNormalExe.getValue().getValue(), processContext);
			
			//process attribute adapter
			HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
			HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
			
			HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
			HAPAttributeEntityDefinition attrDef = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity().getAttribute(attrName);
			Set<HAPInfoAdapter> adapters = ((HAPEmbededDefinition)attrDef.getValue()).getAdapters();

			for(HAPInfoAdapter adapter : adapters) {
				Object adapterExeObj = processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().processEmbededAdapter(adapter, complexEntityExe, attrNormalExe.getValue().getValue(), processContext);
				HAPInfoAdapterExecutable adapterExe = new HAPInfoAdapterExecutable(adapter.getValueType(), adapterExeObj);
				adapter.cloneToEntityInfo(adapterExe);
				attrNormalExe.getValue().addAdapter(adapterExe);
			}
		}
		else {
			//container attribute
			HAPAttributeEntityExecutableContainer attrContainerExe = (HAPAttributeEntityExecutableContainer)attrExe;
			List<HAPElementContainerExecutable> eles = attrContainerExe.getValue().getAllElements();
			for(HAPElementContainerExecutable ele : eles) {
				processContext.getRuntimeEnvironment().getDomainEntityExecutableManager().processComplexEntity((HAPIdEntityInDomain)((HAPEmbededExecutable)ele.getEmbededElementEntity()).getValue(), processContext);
			}
		}
	}
}
