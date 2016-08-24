package com.nosliw.entity.definition;

import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeDefInfo;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.utils.HAPAttributeConstant;

public class HAPAttributeDefinitionContainer extends HAPAttributeDefinition{

	//element definition
	private HAPAttributeDefinition m_childAttrDef;
	
	public HAPAttributeDefinitionContainer(String name, HAPEntityDefinitionSegment entityDef, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan, HAPOptionsDefinitionManager optionsMan) {
		super(name, entityDef, dataTypeMan, entityDefMan, optionsMan);
	}

	/*
	 * path may be simple name or full name
	 */
	@Override
	public HAPAttributeDefinition getChildAttrByName(String path){
		if(this.getChildAttributeDefinition()==null)  return this;
		if(HAPBasicUtility.isStringEmpty(path))  return this;
		
		HAPSegmentParser pathParser = new HAPSegmentParser(path, HAPConstant.CONS_SEPERATOR_PATH);
		String attrName = pathParser.next();
		HAPSegmentParser segments = HAPNamingConversionUtility.isKeywordPhrase(attrName);
		if(segments!=null){
			String keyWord = segments.next();
			if(keyWord.equals(HAPConstant.CONS_ATTRIBUTE_PATH_ELEMENT)){
				String attrRest = pathParser.getRestPath();
				return this.getChildAttributeDefinition().getChildAttrByName(attrRest);
			}
		}
		return this.getChildAttributeDefinition().getChildAttrByName(path);
	}

	/*
	 * element data type in container
	 * only valid for container type(list, set, map)
	 */
	public HAPDataTypeDefInfo getChildDataTypeDefinitionInfo(){	return this.m_childAttrDef.getDataTypeDefinitionInfo();	}

	public HAPAttributeDefinition getChildAttributeDefinition(){return this.m_childAttrDef;}
	public void setChildAttributeDefinition(HAPAttributeDefinition attrDef){this.m_childAttrDef = attrDef;}
	
	@Override
	public HAPAttributeDefinition cloneDefinition(HAPEntityDefinitionSegment entityDef)
	{
		HAPAttributeDefinitionContainer out = new HAPAttributeDefinitionContainer(this.getName(), entityDef, this.getDataTypeManager(), this.getEntityDefinitionManager(), this.getOptionsManager());
		cloneTo(out);

		HAPAttributeDefinition childAttrDef = this.getChildAttributeDefinition().cloneDefinition(entityDef);
		out.setChildAttributeDefinition(childAttrDef);
		return out;
	}
	
	
	@Override
	protected void buildFullJsonMap(Map<String, String> map, Map<String, Class> dataTypeMap){
		if(this.getChildAttributeDefinition()!=null){
			map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_ELEMENT, this.getChildAttributeDefinition().toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
		}
	}
	
}
