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
	public void setResourceId(HAPResourceId resourceId) {    this.m_resourceId = resourceId;     }
	
	public String getElementName() {   return this.m_elementName;    }
	public void setElementName(String name) {    
		this.m_elementName = name;    
		if(HAPBasicUtility.isStringEmpty(this.m_elementName))   this.m_elementName = HAPConstant.NAME_DEFAULT;
	}
	
	public HAPDefinitionDataAssociation getInputMapping() {   return this.m_inputMapping;    }
	public void setInputMapping(HAPDefinitionDataAssociation inputMapping) {   
		this.m_inputMapping = inputMapping;    
		if(this.m_inputMapping==null) {
			this.m_inputMapping = new HAPDefinitionDataAssociationMirror();
		}
	}
	
	public HAPDefinitionReference cloneReferenceDefinition() {
		HAPDefinitionReference out = new HAPDefinitionReference();
		if(this.m_inputMapping!=null)  out.m_inputMapping = this.m_inputMapping.cloneDataAssocation();
		out.m_resourceId = this.m_resourceId.clone();
		out.m_elementName = this.m_elementName; 
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);

		setInputMapping(HAPParserDataAssociation.buildDefinitionByJson(jsonObj.optJSONObject(INPUTMAPPING)));
		
		this.m_resourceId = HAPResourceIdFactory.newInstance(jsonObj.opt(RESOURCEID));
		
		setElementName(jsonObj.optString(ELEMENTNAME));
		
		return true;  
	}
}
