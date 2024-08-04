package com.nosliw.core.application.uitag;

import java.util.ArrayList;
import java.util.List;

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
	public static final String ATTRIBUTES = "attributes";
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
	
	public HAPUITagDefinition() {
		this.m_parentRelations = new ArrayList<HAPManualDefinitionBrickRelation>();
	}
	
	public HAPUITagValueContextDefinition getValueContext() {    return this.m_valueContext;     }
	public void setValueContext(HAPUITagValueContextDefinition valueContext) {    this.m_valueContext = valueContext;       }
	
	public HAPResourceId getScriptResourceId() {     return this.m_scriptResourceId;     }
	public void setScriptResourceId(HAPResourceId scriptResourceId) {     this.m_scriptResourceId = scriptResourceId;         }

	public String getBase() {    return this.m_base;     }
	public void setBase(String base) {     this.m_base = base;       }

	public List<HAPManualDefinitionBrickRelation> getParentRelations(){  return this.m_parentRelations;  }
	public void addParentRelation(HAPManualDefinitionBrickRelation parentRelation) {     this.m_parentRelations.add(parentRelation);       }

}
