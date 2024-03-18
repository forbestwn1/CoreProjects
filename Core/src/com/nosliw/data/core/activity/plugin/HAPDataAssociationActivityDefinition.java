package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.component.HAPPathToElement;

public class HAPDataAssociationActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String PATH = "path";

	private HAPPathToElement m_path;
	
	public HAPDataAssociationActivityDefinition(String type) {
		super(type);
	}

	public HAPPathToElement getPathToElement() {    return this.m_path;     }
	
	@Override
	protected void buildConfigureByJson(JSONObject configurJsonObj) {
		Object pathObj = configurJsonObj.opt(PATH);
		this.m_path = new HAPPathToElement();
		this.m_path.buildObject(pathObj, HAPSerializationFormat.JSON);
		this.m_path.getLastSegment().setElementType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATION);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH, this.m_path.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPDataAssociationActivityDefinition out = new HAPDataAssociationActivityDefinition(this.getActivityType());
		this.cloneToNormalActivityDefinition(out);
		out.m_path = this.m_path.clonePathToElement();
		return out;
	}

	@Override
	public void parseActivityDefinition(Object obj, HAPManualBrickComplex complexEntity, HAPSerializationFormat format) {
		this.buildObject(obj, format);
	}
}
