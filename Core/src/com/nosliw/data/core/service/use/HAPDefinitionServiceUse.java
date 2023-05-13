package com.nosliw.data.core.service.use;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;

@HAPEntityWithAttribute
public class HAPDefinitionServiceUse extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String INTERFACE = "interface";

	@HAPAttribute
	public static String PROVIDER = "provider";

	@HAPAttribute
	public static String DATAASSOCIATION = "dataAssociation";

	private String m_provider;

	private HAPResourceId m_interfaceId;

	private HAPDefinitionGroupDataAssociationForTask m_dataAssociations;
	
	public HAPDefinitionServiceUse() {
		this.m_dataAssociations = new HAPDefinitionGroupDataAssociationForTask();
	}
	
	public HAPDefinitionGroupDataAssociationForTask getDataAssociations() {   return this.m_dataAssociations;    }
	public void setDataAssociations(HAPDefinitionGroupDataAssociationForTask dataAssociations) {  this.m_dataAssociations = dataAssociations;  }
	public void setParmDataAssociation(HAPDefinitionDataAssociation parmDataAssociation) {   this.m_dataAssociations.setInDataAssociation(parmDataAssociation);    }
	public void addResultDataAssociation(String name, HAPDefinitionDataAssociation resultDataAssociation) {  this.m_dataAssociations.addOutDataAssociation(name, resultDataAssociation);   }
	
	public String getProvider() {   return this.m_provider;   }
	public void setProvider(String provider) {   this.m_provider = provider;   }
	
	public HAPResourceId getInterfaceId() {    return this.m_interfaceId;    }
	public void setInterfaceId(HAPResourceId resourceId) {   this.m_interfaceId = resourceId;   }
	
	public void cloneBasicTo(HAPDefinitionServiceUse command) {
		this.cloneToEntityInfo(command);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROVIDER, this.m_provider);
		jsonMap.put(DATAASSOCIATION, HAPUtilityJson.buildJson(this.m_dataAssociations, HAPSerializationFormat.JSON));
		jsonMap.put(INTERFACE, HAPSerializeManager.getInstance().toStringValue(this.m_interfaceId, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_interfaceId = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, jsonObj.opt(INTERFACE));
		this.m_dataAssociations = new HAPDefinitionGroupDataAssociationForTask();
		this.m_dataAssociations.buildObject(jsonObj.optJSONObject(DATAASSOCIATION), HAPSerializationFormat.JSON);
		this.m_provider = (String)jsonObj.opt(PROVIDER);
		return true;  
	}
}
