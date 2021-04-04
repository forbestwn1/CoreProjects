package com.nosliw.data.core.expression;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;
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

	private HAPResourceId m_groupExprssionResourceId;
	
	private String m_groupElementName;
	
	private HAPDefinitionDataAssociation m_inputMapping;
	
	public HAPDefinitionReference() {}
	
	public HAPResourceId getResourceId() {   return this.m_groupExprssionResourceId;   }
	public void setResourceId(HAPResourceId resourceId) {    this.m_groupExprssionResourceId = resourceId;     }
	
	public String getElementName() {   return this.m_groupElementName;    }
	public void setElementName(String name) {    
		this.m_groupElementName = name;    
		if(HAPBasicUtility.isStringEmpty(this.m_groupElementName))   this.m_groupElementName = HAPConstantShared.NAME_DEFAULT;
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
		out.m_groupExprssionResourceId = this.m_groupExprssionResourceId.clone();
		out.m_groupElementName = this.m_groupElementName; 
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);

		setInputMapping(HAPParserDataAssociation.buildDefinitionByJson(jsonObj.optJSONObject(INPUTMAPPING)));
		
		this.m_groupExprssionResourceId = HAPFactoryResourceId.newInstance(jsonObj.opt(RESOURCEID));
		
		setElementName(jsonObj.optString(ELEMENTNAME));
		
		return true;  
	}
}
