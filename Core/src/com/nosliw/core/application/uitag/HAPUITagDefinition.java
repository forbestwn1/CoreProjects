package com.nosliw.core.application.uitag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUITagDefinition{

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";
	@HAPAttribute
	public static final String SCRIPTRESOURCEID = "scriptResourceId";
	@HAPAttribute
	public static final String BASE = "base";
	@HAPAttribute
	public static final String PARENTRELATION = "parentRelation";
	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	
	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";
	
	
	private HAPUITagValueContextDefinition m_valueContext;
	
	private HAPResourceId m_scriptResourceId;
	
	private String m_base;
	
	private List<HAPManualDefinitionBrickRelation> m_parentRelations;
	
	private Map<String, HAPUITagAttributeDefinition> m_attributes;
	
	public HAPUITagDefinition() {
		this.m_parentRelations = new ArrayList<HAPManualDefinitionBrickRelation>();
		this.m_attributes = new LinkedHashMap<String, HAPUITagAttributeDefinition>();
	}
	
	public HAPUITagValueContextDefinition getValueContext() {    return this.m_valueContext;     }
	public void setValueContext(HAPUITagValueContextDefinition valueContext) {    this.m_valueContext = valueContext;       }
	
	public HAPResourceId getScriptResourceId() {     return this.m_scriptResourceId;     }
	public void setScriptResourceId(HAPResourceId scriptResourceId) {     this.m_scriptResourceId = scriptResourceId;         }

	public String getBase() {    return this.m_base;     }
	public void setBase(String base) {     this.m_base = base;       }

	public List<HAPManualDefinitionBrickRelation> getParentRelations(){  return this.m_parentRelations;  }
	public void addParentRelation(HAPManualDefinitionBrickRelation parentRelation) {     this.m_parentRelations.add(parentRelation);       }

	public void addAttributeDefinition(HAPUITagAttributeDefinition attribute) {   this.m_attributes.put(attribute.getName(), attribute);    }
	public Map<String, HAPUITagAttributeDefinition> getAttributeDefinition() {   return this.m_attributes;    }
}
