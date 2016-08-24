package com.nosliw.entity.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.configure.HAPConfigureValue;
import com.nosliw.common.configure.HAPConfigureValueString;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPStringableJson;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntityBasic;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeDefInfo;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.entity.options.HAPOptionsDefinition;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.utils.HAPAttributeConstant;
import com.nosliw.entity.utils.HAPEntityNamingConversion;
import com.nosliw.entity.validation.HAPValidationInfoExpression;

/*
 * this abstract class define all the methods to provide information for an complex entity's attribute definition
 * however, child element of container is also defined as attribute, but it has name attirbute with null value
 */
public abstract class HAPAttributeDefinition extends HAPStringableValueEntityBasic{

	public static String ENTITY_PROPERTY_ISEMPTYONINIT = "isEmptyOnInit";
	public static String ENTITY_PROPERTY_EVENTS = "events";
	public static String ENTITY_PROPERTY_OPTIONS = "options";
	public static String ENTITY_PROPERTY_VALIDATIONS = "validations";
	
	
	//store all the document related information, for instance, name, description
	private HAPStringableValueEntityBasic m_properties;

	//entity definition this attribute belong to
	private HAPEntityDefinitionSegment m_entityDefinition;

	//which critical value that this attribute is defined under
	private String m_criticalValue;
	
	//data type information
	//note: it is not HAPDataTypeInfo which is more strict than HAPDataTypeDefInfo
	private HAPDataTypeDefInfo m_dataTypeDefInfo;
	
	//calculated attribute which show whether the value validation is able to be done on client side
	private boolean m_serverValidationOnly = true;
	
	private HAPDataTypeManager m_dataTypeMan;
	private HAPOptionsDefinitionManager m_optionsMan;
	private HAPEntityDefinitionManager m_entityDefinitionMan;
	
	public HAPAttributeDefinition(HAPStringableValueEntityBasic properties, HAPEntityDefinitionSegment entityDef, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan, HAPOptionsDefinitionManager optionsMan){
		this.m_properties = properties;
		this.m_dataTypeMan = dataTypeMan;
		this.m_entityDefinitionMan = entityDefMan;
		this.m_optionsMan = optionsMan;
		this.m_entityDefinition = entityDef;
	}

	protected void initProperty(String name, Object defaultValue){
		this.m_properties.initProperty(name, this.getEntityDefinitionManager().getConfiguration(), defaultValue);
	}
	
	protected void initProperty(String name){
		this.m_properties.initProperty(name, this.getEntityDefinitionManager().getConfiguration(), null);
	}

	private void init(){
		this.initProperty(HAPAttributeConstant.ATTR_ENTITYATTRDEF_ISEMPTYONINIT);
		
		Boolean isEmptyOnInit = this.getProperties().getBasicAncestorValueBoolean(HAPAttributeConstant.ATTR_ENTITYATTRDEF_ISEMPTYONINIT);
		if(isEmptyOnInit==null){
			isEmptyOnInit = this.getEntityDefinitionManager().getConfiguration().getConfigureValue(HAPConstant.CONS_CONFIGUREITEM_ENTITY_ISEMPTYONINIT).getBooleanValue();
			if(isEmptyOnInit!=null)		this.m_isEmptyOnInit = isEmptyOnInit; 
		}
	}
	
	/******************************************   Basic Information  *********************************************/
	public HAPStringableValueEntityBasic getProperties(){ return this.m_properties; }
	
	public HAPEntityDefinitionSegment getEntityDefinition(){return this.m_entityDefinition;}

	
	public void setServerValidationOnly(boolean only){this.m_serverValidationOnly=only;}
	public boolean getServerValidationOnly(){return this.m_serverValidationOnly;}
	

	/*
	 * get the attribute name in complex entity
	 * default : mandatory
	 */
	public String getName(){return this.getProperties().getName();}
	public void setName(String name){this.getProperties().setName(name);}

	public String getDescription(){return this.getProperties().getDescription();}
	public void setDescription(String desc){this.getProperties().setDescription(desc);}

	/*
	 * which critical value that this attribute is defined under
	 */
	public String getCriticalValue(){return this.m_criticalValue;}
	public void setCriticalValue(String criticalValue){this.m_criticalValue=criticalValue;}
	
	public HAPValidationInfoExpression[] getValidationInfos(){return this.m_validationInfos.toArray(new HAPValidationInfoExpression[0]);}
	public void addValidationInfo(HAPValidationInfoExpression validationInfo){this.m_validationInfos.add(validationInfo);}

	/*
	 * get the attribute type
	 * default:   data type manager default data type :  simple / text
	 */
	public HAPDataTypeDefInfo getDataTypeDefinitionInfo(){
		if(this.m_dataTypeDefInfo==null)  this.m_dataTypeDefInfo = new HAPDataTypeDefInfo(HAPDataTypeManager.getDefaultDataTypeInfo());
		return this.m_dataTypeDefInfo;
	}
	public void setDataTypeDefInfo(HAPDataTypeDefInfo defInfo){this.m_dataTypeDefInfo=defInfo;}

	/*
	 * get a list of event names that this attribute may created
	 * 
	 */
	public String[] getEvents(){return this.m_events.toArray(new String[0]);}
	public void addEvent(String event){this.m_events.add(event);}
	
	/*
	 * options definition configure (name,...)
	 * can be either attribute options or common options
	 * default : null --- no options
	 */
	public HAPOptionsDefinition getOptionsDefinition(){return this.m_options;}
	public void setOptionsDefinition(HAPOptionsDefinition def){this.m_options=def;}

	/*
	 * if the attribute empty when created
	 * if it returns true,
	 * 		this attribute is created usring default value when creating new entity
	 * note : for the case, when user's name cannot be blank, it should be enforced through constrain appled to text
	 * default : false
	 */
	public boolean getIsEmptyOnInit(){return this.m_isEmptyOnInit;}
	public void setIsEmptyOnInit(boolean mandatory){this.m_isEmptyOnInit=mandatory;}
	/*
	 * whether have a valid name is used to judge if this attribute definition is a real entity attribute or a container element definition
	 */
	public boolean isContainerElementAttr(){return this.getName()==null;}
	public void setContainerElementAttr(){this.setName(null);}
	
	
	
	/******************************************   Path  *********************************************/
	
	public abstract HAPAttributeDefinition getChildAttrByName(String name);

	public HAPAttributeDefinition getChildAttrByPath(String path){
		HAPSegmentParser patSegs = new HAPSegmentParser(path);
		if(patSegs.isEmpty())  return this;
		
		String name = patSegs.next();
		String keyword = HAPNamingConversionUtility.getKeyword(name);
		if(HAPConstant.CONS_ATTRIBUTE_PATH_ENTITY.equals(keyword)){
			//"entity" key word
			HAPEntityDefinitionSegment entityDef = this.getEntityDefinition();
			return entityDef.getAttributeDefinitionByPath(patSegs.getRestPath());
		}
		else{
			HAPAttributeDefinition attrDef = this.getChildAttrByName(name);
			if(attrDef!=null) 	return attrDef.getChildAttrByPath(patSegs.getRestPath());
			else return attrDef;
		}
	}

	public HAPDataTypeInfo getChildDataTypeByPath(String path){
		HAPAttributeDefinition childAttrDef = this.getChildAttrByPath(path);
		if(childAttrDef!=null)   return childAttrDef.getDataTypeDefinitionInfo();
		else return null;
	}

	/*
	 * get full name for this attribute. 
	 * when attribute is under critical value, then full name is a combination of getName and getCriticalValue
	 */
	public String getFullName(){
		if(this.isContainerElementAttr()){
			return HAPEntityNamingConversion.createContainerElementName();
		}
		
		if(!HAPBasicUtility.isStringEmpty(this.getCriticalValue())){
			return HAPEntityNamingConversion.createAttributeWithCriticalFullName(this.getCriticalValue(), this.getName());
		}
		else{
			return this.getName();
		}
	}
	
	
	/******************************************   Clone  *********************************************/
	public HAPAttributeDefinition cloneDefinition(HAPEntityDefinitionSegment entityDef){
		return null;
	}

	protected void cloneFrom(HAPAttributeDefinition attrDef)
	{
		this.m_properties.cloneFrom(attrDef.getProperties());
		this.m_criticalValue = attrDef.m_criticalValue;
		this.m_dataTypeDefInfo = attrDef.getDataTypeDefinitionInfo();
		this.m_isEmptyOnInit = attrDef.m_isEmptyOnInit;
		
		this.m_validationInfos.addAll(attrDef.m_validationInfos);
		this.m_serverValidationOnly = attrDef.m_serverValidationOnly;
		
		if(attrDef.m_options!=null)		this.m_options = attrDef.m_options;
		
		for(String event : attrDef.getEvents()){
			this.addEvent(event);
		}
	}
	
	/******************************************   Serialization  *********************************************/
	@Override
	protected void buildFullJsonMap(Map<String, String> map, Map<String, Class> dataTypeMap){
		map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_DESCRIPTION, this.getProperties().toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
		map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_FULLNAME, this.getFullName());
		map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_CRITICALVALUE, this.m_criticalValue);
		map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_ISEMPTYONINIT, String.valueOf(this.m_isEmptyOnInit));

		map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_DATATYPEDEFINFO, this.m_dataTypeDefInfo.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));

		dataTypeMap.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_VALIDATION, Boolean.class);
		if(this.m_validationInfos.size()==0){
			//dont't need validation
			map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_VALIDATION, "false");
		}
		else{
			//need validation
			map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_VALIDATION, "true");
			//if need server side validata, then do not add any rules
			if(!this.getServerValidationOnly()){
				List<String> validationJsons = new ArrayList<String>();
				for(HAPValidationInfoExpression validationInfo : this.m_validationInfos){
					validationJsons.add(validationInfo.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
				}
				map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_RULES, HAPJsonUtility.getArrayJson(validationJsons.toArray(new String[0])));
			}
		}

		if(this.m_options!=null){
			map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_OPTIONS, this.m_options.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
		}
		
		map.put(HAPAttributeConstant.ATTR_ENTITYATTRDEF_EVENTS, HAPJsonUtility.getArrayJson(this.m_events.toArray(new String[0])));
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof HAPAttributeDefinition)
		{
			HAPAttributeDefinition oo = (HAPAttributeDefinition)o;
			if(this.getName().equals(oo.getName()) && this.getDataTypeDefinitionInfo().equals(oo.getDataTypeDefinitionInfo()))
			{
				return HAPBasicUtility.isEquals(this.getCriticalValue(), oo.getCriticalValue());
			}
			else return false;
		}
		else
		{
			return false;
		}
	}
	
	protected HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}
	protected HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefinitionMan;}
	protected HAPOptionsDefinitionManager getOptionsManager(){return this.m_optionsMan;}
}
