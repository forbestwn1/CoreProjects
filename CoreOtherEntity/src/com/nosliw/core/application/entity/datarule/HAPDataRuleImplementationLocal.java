package com.nosliw.core.application.entity.datarule;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPDataRuleImplementationLocal extends HAPSerializableImp implements HAPDataRuleImplementation{

	@HAPAttribute
    public static final String PATHID = "pathId";
	
	private String m_pathId;
	
	public HAPDataRuleImplementationLocal(String pathId) {
		this.m_pathId = pathId;
	}
	
	@Override
	public String getImmplementationType() {
		return HAPConstantShared.DATARULE_IMPLEMENTATION_LOCAL;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
        jsonMap.put(PATHID, this.m_pathId);
    }
}
