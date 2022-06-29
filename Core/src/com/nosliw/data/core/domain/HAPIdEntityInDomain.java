package com.nosliw.data.core.domain;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

//id for entity in domain
@HAPEntityWithAttribute
public class HAPIdEntityInDomain extends HAPSerializableImp{

	@HAPAttribute
	public static final String DOMAINID = "domainId";

	@HAPAttribute
	public static final String ENTITYTYPE = "entityType";

	@HAPAttribute
	public static final String ENTITYID = "entityId";

	private String m_domainId;
	
	//entity type
	private String m_entityType;

	//entity id
	private String m_entityId;

	public HAPIdEntityInDomain(String entityId, String entityType, String domainId) {
		this.m_entityId = entityId;
		this.m_entityType = entityType;
		this.m_domainId = domainId;
	}

	public HAPIdEntityInDomain(String entityId, String entityType) {
		this.m_entityId = entityId;
		this.m_entityType = entityType;
	}

	public HAPIdEntityInDomain() {}
	
	public static HAPIdEntityInDomain newInstance(Object obj) {
		HAPIdEntityInDomain out = null;
		if(obj!=null) out = (HAPIdEntityInDomain)HAPSerializeUtility.buildObject(HAPIdEntityInDomain.class, obj);
		return out;
	}
	
	public String getEntityId() {	return this.m_entityId;	}
	
	public String getEntityType() {     return this.m_entityType;      }

	public String getDomainId() {    return this.m_domainId;   }
	
	public HAPIdEntityInDomain cloneIdEntityInDomain() {
		return new HAPIdEntityInDomain(this.m_domainId, this.m_entityId, this.m_entityType);
	}
	
	@Override
	public int hashCode() {
		return this.toStringValue(HAPSerializationFormat.LITERATE).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPIdEntityInDomain) {
			HAPIdEntityInDomain entityId = (HAPIdEntityInDomain)obj;
			out = HAPBasicUtility.isEquals(entityId.getEntityId(), this.getEntityId()) && HAPBasicUtility.isEquals(entityId.getEntityType(), this.getEntityType());
		}
		return out;
	}
	
	@Override
	protected String buildLiterate(){  
		return HAPUtilityNamingConversion.cascadeElements(new String[] {this.m_entityId, this.m_entityType, this.m_domainId}, HAPConstantShared.SEPERATOR_LEVEL1); }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DOMAINID, this.m_domainId);
		jsonMap.put(ENTITYTYPE, this.m_entityType);
		jsonMap.put(ENTITYID, this.m_entityId);
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] segs = HAPUtilityNamingConversion.parseLevel1(literateValue);
		this.m_entityId = segs[0];
		if(segs.length>1)  this.m_entityType = segs[1];
		if(segs.length>2)  this.m_domainId = segs[2];
		return true;  
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		Object typeObj = jsonObj.opt(ENTITYTYPE);
		if(typeObj!=null)		this.m_entityType = (String)typeObj;
		this.m_entityId = jsonObj.getString(ENTITYID);
		this.m_domainId = (String)jsonObj.opt(DOMAINID);
		return true;  
	}

}
