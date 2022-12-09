package com.nosliw.data.core.domain.entity.test;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainDynamic extends HAPPluginEntityDefinitionInDomainImp{

	public final static String PREFIX_IGNORE = "ignore";
	
	private boolean m_isComplex;
	
	public HAPPluginEntityDefinitionInDomainDynamic(String entityType, Class<? extends HAPDefinitionEntityInDomain> entityClass, boolean isComplex, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, entityClass, runtimeEnv);
		this.m_isComplex = isComplex;
	}

	public HAPPluginEntityDefinitionInDomainDynamic(Class<? extends HAPDefinitionEntityInDomain> entityClass, boolean isComplex, HAPRuntimeEnvironment runtimeEnv) {
		super(entityClass, runtimeEnv);
		this.m_isComplex = isComplex;
	}

	@Override
	public boolean isComplexEntity() {	return this.m_isComplex;	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		JSONArray jsonArray = null;
		if(obj instanceof JSONArray) jsonArray = (JSONArray)obj;
		else if(obj instanceof String)  jsonArray = new JSONArray((String)obj);
		
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			JSONObject infoJsonObj = jsonObj.getJSONObject("info");
			HAPEntityInfo info = HAPUtilityEntityInfo.buildEntityInfoFromJson(infoJsonObj);

			if(HAPUtilityEntityInfo.isEnabled(info)) {
				String attrName = info.getName();
				System.out.println(attrName);
				if(!attrName.startsWith(PREFIX_IGNORE)) {
					if(attrName.equals(HAPWithAttachment.ATTACHMENT)) {
						this.parseNormalSimpleEntityAttributeSelf(jsonObj, entityId, attrName, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
					}
					else if(attrName.equals(HAPWithValueStructure.VALUESTRUCTURE)) {
						this.parseNormalSimpleEntityAttributeSelf(jsonObj, entityId, attrName, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURECOMPLEX, null, parserContext);
					}
					else {
						Object entityObj = jsonObj.opt(attrName);
						if(isAttributeEnabled(entityObj)) {
							HAPAttributeEntityInfo attributeInfo = this.parseAttributeInfo(attrName);
							if(attributeInfo.isContainer) {
								if(attributeInfo.isComplex) {
									parseContainerComplexAttributeSelf(jsonObj, entityId, attrName, attributeInfo.entityType, attributeInfo.adapterType, attributeInfo.containerType, null, parserContext);
								}
								else {
									parseContainerSimpleAttributeSelf(jsonObj, entityId, attrName, attributeInfo.entityType, attributeInfo.adapterType, attributeInfo.containerType, parserContext);
								}
							}
							else {
								if(attributeInfo.isComplex) {
									this.parseNormalComplexEntityAttributeSelf(jsonObj, entityId, attrName, attributeInfo.entityType, attributeInfo.adapterType, null, parserContext);
								}
								else {
									this.parseNormalSimpleEntityAttributeSelf(jsonObj, entityId, attrName, attributeInfo.entityType, attributeInfo.adapterType, parserContext);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private boolean isAttributeEnabled(Object entityObj) {
		boolean out = true;
		if(entityObj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)entityObj;
			JSONObject extraJsonObj = jsonObj.optJSONObject("extra");
			if(extraJsonObj!=null) {
				return HAPUtilityEntityInfo.isEnabled(extraJsonObj);
			}
		}
		
		return out;
	}
	
	//name_(none|set|list|container)_entitytype_adapterType
	private HAPAttributeEntityInfo parseAttributeInfo(String attrName) {
		HAPAttributeEntityInfo out = new HAPAttributeEntityInfo();

		String str = attrName;
		
		{
			if(HAPUtilityBasic.isStringNotEmpty(str)) {
				Pair<String, String> pair = this.parseString(str);
				out.attirbuteName = pair.getLeft();
				str = pair.getRight();
			}
		}

		{
			if(HAPUtilityBasic.isStringNotEmpty(str)) {
				Pair<String, String> pair = this.parseString(str);
				String container = pair.getLeft();
				if(container.equals("none"))  out.isContainer = false;
				else if(container.equals("container"))  out.isContainer = true;
				else {
					out.isContainer = true;
					out.containerType = container;
				}
				str = pair.getRight();
			}
		}

		{
			if(HAPUtilityBasic.isStringNotEmpty(str)) {
				Pair<String, String> pair = this.parseString(str);
				out.entityType = pair.getLeft();
				str = pair.getRight();
			}
		}

		{
			if(HAPUtilityBasic.isStringNotEmpty(str)) {
				Pair<String, String> pair = this.parseString(str);
				out.adapterType = pair.getLeft();
				str = pair.getRight();
			}
		}

		out.isComplex = this.getRuntimeEnvironment().getDomainEntityManager().isComplexEntity(out.entityType);
		
		return out;
	}
	
	private Pair<String, String> parseString(String str){
		String seperator = "_";
		int index = str.indexOf(seperator);
		if(index!=-1) {
			return Pair.of(str.substring(0, index), str.substring(index+1));
		}
		else {
			return Pair.of(str, null);
		}
	}
	
	class HAPAttributeEntityInfo {
		public String entityType;
		public String attirbuteName;
		public boolean isComplex;
		public boolean isContainer;
		public String containerType;
		public String adapterType;
	}
	
}
