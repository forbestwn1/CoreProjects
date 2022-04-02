package com.nosliw.data.core.domain.testing;

import org.json.JSONObject;

import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainTest1 extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainTest1(Class<? extends HAPDefinitionEntityInDomain> entityClass,
			HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityTestComplex1.class, runtimeEnv);
	}

	@Override
	public boolean isComplexEntity() {	return false;	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj,
			HAPDomainDefinitionEntity definitionDomain) {
		HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfo(entityId).getEntity();

		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			for(Object key : jsonObj.keySet()) {
				String attrName = (String)key;
				Object entityObj = jsonObj.opt(attrName);
				HAPEntityInfo entityInfo = this.parseEntityInfo(attrName);
				if(entityInfo.isContainer) {
					if(entityInfo.isComplex) {
						parseComplexContainerAttribute(jsonObj, entityId, attrName, entityInfo.entityType, entityInfo.adapterType, entityInfo.containerType, null, definitionDomain);
					}
					else {
						parseSimpleContainerAttribute(jsonObj, entityId, attrName, entityInfo.entityType, entityInfo.adapterType, entityInfo.containerType, definitionDomain);
					}
				}
				else {
					if(entityInfo.isComplex) {
						this.parseComplexEntityAttribute(jsonObj, entityId, attrName, entityInfo.entityType, entityInfo.adapterType, null, definitionDomain);
					}
					else {
						this.parseSimpleEntityAttribute(jsonObj, entityId, attrName, entityInfo.entityType, entityInfo.adapterType, definitionDomain);
					}
				}
			}
		}
		
	}
	
	private HAPEntityInfo parseEntityInfo(String attrName) {
		//name_container|containerType_entitytype_adapterType
		HAPEntityInfo out = new HAPEntityInfo();
		
		
		return out;
	}
	
	class HAPEntityInfo {
		public String entityType;
		public String attirbuteName;
		public boolean isComplex;
		public boolean isContainer;
		public String containerType;
		public String adapterType;
	}
	
}
