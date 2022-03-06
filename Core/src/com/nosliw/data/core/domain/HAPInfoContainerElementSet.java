package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPInfoContainerElementSet extends HAPInfoContainerElement{

	public HAPInfoContainerElementSet(HAPIdEntityInDomain entityId) {
		super(entityId);
	}

	public HAPInfoContainerElementSet() {	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_SET;    }

	@Override
	public HAPInfoContainerElement cloneContainerElementInfo() {
		HAPInfoContainerElementSet out = new HAPInfoContainerElementSet();
		this.cloneToInfoContainerElement(out);
		return out;
	}

	public HAPInfoContainerElementSet cloneContainerElementInfoSet() {
		return (HAPInfoContainerElementSet)this.cloneContainerElementInfo();
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		return super.buildObjectByJson(json);
	}

	@Override
	public String toExpandedJsonString(HAPDomainDefinitionEntity entityDefDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(ELEMENTNAME, this.getElementName());
		jsonMap.put(ENTITY, HAPUtilityDomain.getEntityExpandedJsonString(this.getElementEntityId(), entityDefDomain));
		jsonMap.put(ENTITYID, this.getElementEntityId().toStringValue(HAPSerializationFormat.JSON));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

}
