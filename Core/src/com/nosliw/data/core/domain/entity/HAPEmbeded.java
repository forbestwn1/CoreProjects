package com.nosliw.data.core.domain.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.value.HAPUtilityClone;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

@HAPEntityWithAttribute
public abstract class HAPEmbeded extends HAPSerializableImp implements HAPExpandable{

	public static String EMBEDED = "embeded";

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String VALUE = "value";

	@HAPAttribute
	public static String VALUEEXPANDED = "valueExpanded";

	@HAPAttribute
	public static String ADAPTER = "adapter";

	@HAPAttribute
	public static String VALUETYPE = "valueType";

	@HAPAttribute
	public static String ISCOMPLEX = "isComplex";

	private String m_entityType;
	
	private boolean m_isComplex;
	
	private Object m_value;
	
	private Object m_adapter;
	
	public HAPEmbeded() {}
	
	public HAPEmbeded(Object value, String entityType, Object adapter, boolean isComplex) {
		this.m_value = value;
		this.m_entityType = entityType;
		this.m_adapter = adapter;
		this.m_isComplex = isComplex;
	}
	
	public String getEntityType() {   return this.m_entityType;    }
	public boolean getIsComplex() {    return this.m_isComplex;   }
	
	public Object getValue() {   return this.m_value;   }
	public void setValue(Object value) {    this.m_value = value;    }
	
	public Object getAdapter() {	return m_adapter;	}
	public void setAdapter(Object adapter) {	this.m_adapter = adapter;	}

	abstract public String getType();
	
	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		buildExpandedJsonMap(jsonMap, typeJsonMap, entityDomain);
		return HAPUtilityJson.buildMapJson(jsonMap);
	}

	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		if(this.getValue() instanceof HAPExpandable)   jsonMap.put(VALUEEXPANDED, ((HAPExpandable) this.getValue()).toExpandedJsonString(entityDomain));
		else if(this.getValue() instanceof HAPIdEntityInDomain) jsonMap.put(VALUEEXPANDED, entityDomain.getEntityInfo((HAPIdEntityInDomain)this.getValue()).toExpandedJsonString(entityDomain));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(VALUETYPE, this.m_entityType);
		jsonMap.put(ISCOMPLEX, this.m_isComplex+"");
		typeJsonMap.put(ISCOMPLEX, Boolean.class);
		if(this.getAdapter()!=null) {
			jsonMap.put(ADAPTER, HAPSerializeManager.getInstance().toStringValue(this.getAdapter(), HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(this.getValue(), HAPSerializationFormat.JSON));
	}
	
	public abstract HAPEmbeded cloneEmbeded();

	protected void cloneToEmbeded(HAPEmbeded embeded) {
		embeded.m_entityType = this.m_entityType;
		embeded.m_isComplex = this.m_isComplex;
		embeded.setAdapter(HAPUtilityClone.cloneValue(this.getAdapter()));
		embeded.setValue(HAPUtilityClone.cloneValue(this.getValue()));	
	}
}
