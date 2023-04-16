package com.nosliw.data.core.complex;

import java.lang.reflect.Field;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.container.HAPElementContainerExecutable;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutableContainer;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutableNormal;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableAdapter;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPInfoAdapter;

public abstract class HAPPluginComplexEntityProcessorImp implements HAPPluginComplexEntityProcessor{

	private Class<? extends HAPExecutableEntityComplex> m_exeEntityClass;
	
	private String m_entityType;
	
	public HAPPluginComplexEntityProcessorImp(Class<? extends HAPExecutableEntityComplex> exeEntityClass) {
		this.m_exeEntityClass = exeEntityClass;
		
		//get entity type from class
		try {
			Field f = this.m_exeEntityClass.getField("ENTITY_TYPE");
			this.m_entityType = (String)f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getEntityType() {    return this.m_entityType;    }
	
	@Override
	public HAPExecutableEntityComplex newExecutable() {
		HAPExecutableEntityComplex out = null;
		try {
			out = this.m_exeEntityClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out; 
	}
	
	protected void processComplexAttribute(String attrName, HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPExecutableEntityComplex complexEntityExe = processContext.getCurrentExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
		HAPAttributeEntityExecutable attrExe = complexEntityExe.getAttribute(attrName);
		if(attrExe.getEntityType().equals(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL)) {
			//normal attribute
			HAPAttributeEntityExecutableNormal attrNormalExe = (HAPAttributeEntityExecutableNormal)attrExe;
			//process attribute entity
			processContext.getRuntimeEnvironment().getComplexEntityManager().processComplexEntity((HAPIdEntityInDomain)attrNormalExe.getValue().getValue(), processContext);
			
			//process attribute adapter
			HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
			HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
			
			HAPIdEntityInDomain complexEntityDefinitionId = currentBundle.getDefinitionEntityIdByExecutableEntityId(complexEntityExecutableId);
			HAPAttributeEntityDefinition attrDef = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity().getAttribute(attrName);
			HAPInfoAdapter adapter = ((HAPEmbededDefinition)attrDef.getValue()).getAdapterEntity();

			Object adapterExe = processContext.getRuntimeEnvironment().getComplexEntityManager().processEmbededAdapter(adapter, complexEntityExe, attrNormalExe.getValue().getValue(), processContext);
			attrNormalExe.getValue().setAdapter(new HAPExecutableAdapter(adapter.getValueType(), adapterExe));
		}
		else {
			//container attribute
			HAPAttributeEntityExecutableContainer attrContainerExe = (HAPAttributeEntityExecutableContainer)attrExe;
			List<HAPElementContainerExecutable> eles = attrContainerExe.getValue().getAllElements();
			for(HAPElementContainerExecutable ele : eles) {
				processContext.getRuntimeEnvironment().getComplexEntityManager().processComplexEntity((HAPIdEntityInDomain)((HAPEmbededExecutable)ele.getEmbededElementEntity()).getValue(), processContext);
			}
		}
	}
}
