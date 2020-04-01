package com.nosliw.data.core.expression;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdFactory;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPDefinitionDataAssociationMirror;

public class HAPDefinitionReference extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String RESOURCEID = "resourceId";

	@HAPAttribute
	public static String ELEMENTNAME = "elementName";

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";

	private HAPResourceId m_resourceId;
	
	private String m_elementName;
	
	private HAPDefinitionDataAssociation m_inputMapping;
	
	public HAPResourceId getResourceId() {   return this.m_resourceId;   }

	public String getElementName() {   return this.m_elementName;    }
	
	public HAPDefinitionDataAssociation getInputMapping() {   return this.m_inputMapping;    }
	
	public HAPDefinitionReference cloneReferenceDefinition() {
		HAPDefinitionReference out = new HAPDefinitionReference();
		out.m_inputMapping = this.m_inputMapping.cloneDataAssocation();
		out.m_resourceId = this.m_resourceId.clone();
		out.m_elementName = this.m_elementName; 
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		
		this.m_inputMapping = HAPParserDataAssociation.buildDefinitionByJson(jsonObj.optJSONObject(INPUTMAPPING));
		if(this.m_inputMapping==null) {
			this.m_inputMapping = new HAPDefinitionDataAssociationMirror();
		}
		
		this.m_resourceId = HAPResourceIdFactory.newInstance(jsonObj.opt(RESOURCEID));
		
		this.m_elementName = jsonObj.optString(ELEMENTNAME);
		if(HAPBasicUtility.isStringEmpty(this.m_elementName))   this.m_elementName = HAPConstant.NAME_DEFAULT;
		
		return true;  
	}
}
