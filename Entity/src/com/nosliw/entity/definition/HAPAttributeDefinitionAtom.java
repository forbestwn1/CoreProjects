package com.nosliw.entity.definition;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.utils.HAPAttributeConstant;


public class HAPAttributeDefinitionAtom extends HAPAttributeDefinition{

	//default value for this attribute
	private HAPData m_defaultValue;

	//the mapping between critical attribute value and Entity Definition
	private boolean m_isCritical = false;
	
	public HAPAttributeDefinitionAtom(String name, HAPEntityDefinitionSegment entityDef, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan, HAPOptionsDefinitionManager optionsMan) {
		super(name, entityDef, dataTypeMan, entityDefMan, optionsMan);
	}

	@Override
	public HAPAttributeDefinition getChildAttrByName(String name){
		return this;
	}
	
	//*****************valid for atom type only
	/*
	 * get default value which are normally used when no value is set
	 * default:  default value for data type of attribute
	 */
	public HAPData getDefaultValue(){return this.m_defaultValue;}
	protected void setDefaultValue(HAPData value){this.m_defaultValue=value;}

	//*****************valid for critical attribute
	
	/*
	 * if the attribute is complex entity's Critical Attribute:
	 * the class name of the complex entity containing the attribute is determined by the value of Critical Attribute attribute
	 * default : false 
	 */
	public boolean getIsCritical(){return this.m_isCritical;}
	protected void setIsCritical(boolean critical){this.m_isCritical=critical;}
	
	
	@Override
	public HAPAttributeDefinition cloneDefinition(HAPEntityDefinitionSegment entityDef)
	{
		HAPAttributeDefinitionAtom out = new HAPAttributeDefinitionAtom(this.getName(), entityDef, this.getDataTypeManager(), this.getEntityDefinitionManager(), this.getOptionsManager());
		cloneTo(out);
		out.m_defaultValue = this.m_defaultValue;
		out.m_isCritical = this.m_isCritical;
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> map, Map<String, Class> dataTypeMap){
		dataTypeMap.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_ISCRITICAL, Boolean.class);
		map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_ISCRITICAL, String.valueOf(m_isCritical));
		if(this.getDefaultValue()!=null){
			map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_DEFAULTVALUE, this.getDefaultValue().toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
		}
	}
	
}
