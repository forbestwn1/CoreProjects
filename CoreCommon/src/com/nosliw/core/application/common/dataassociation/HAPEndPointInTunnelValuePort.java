package com.nosliw.core.application.common.dataassociation;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.valueport.HAPIdValuePortInBundle;

@HAPEntityWithAttribute
public class HAPEndPointInTunnelValuePort extends HAPEndpointInTunnel{

	@HAPAttribute
	public static String VALUEPORTREF = "valuePortRef";
	@HAPAttribute
	public static String VALUESTRUCTUREID = "valueStructureId";
	@HAPAttribute
	public static String ITEMPATH = "itemPath";

	private HAPIdValuePortInBundle m_valuePortRef;
	private String m_valueStructureId;
	private String m_itemPath;

	public HAPEndPointInTunnelValuePort(HAPIdValuePortInBundle valuePortRef, String valueStructureId, String itemPath) {
		super(HAPConstantShared.TUNNELENDPOINT_TYPE_VALUEPORT);
		this.m_valuePortRef = valuePortRef;
		this.m_valueStructureId = valueStructureId;
		this.m_itemPath = itemPath;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEPORTREF, this.m_valuePortRef.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(VALUESTRUCTUREID, this.m_valueStructureId);
		jsonMap.put(ITEMPATH, this.m_itemPath);
	}
}
