package com.nosliw.data.core.domain.entity.test.complex.script;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;

@HAPEntityWithAttribute
public class HAPInfoAttachmentResolve extends HAPSerializableImp{

	@HAPAttribute
	public static String VALUETYPE = "valueType";
	
	@HAPAttribute
	public static String ITEMNAME = "itemName";

	@HAPAttribute
	public static String ATTACHMENT = "attachment";
	
	@HAPAttribute
	public static String ENTITYSTR = "entityStr";
	

	private String m_valueType;
	
	private String m_itemName;
	
	private HAPAttachment m_attachment;
	
	private String m_entityStr;
	
	public HAPInfoAttachmentResolve(String valueType, String itemName, HAPAttachment attachment, String entityStr) {
		this.m_valueType = valueType;
		this.m_itemName = itemName;
		this.m_attachment = attachment;
		this.m_entityStr = entityStr;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, this.m_valueType);
		jsonMap.put(ITEMNAME, this.m_itemName);
		jsonMap.put(ATTACHMENT, HAPSerializeManager.getInstance().toStringValue(this.m_attachment, HAPSerializationFormat.JSON));
		jsonMap.put(ENTITYSTR, this.m_entityStr);
	}

}
