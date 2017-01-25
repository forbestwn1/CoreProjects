package com.nosliw.entity.definition.xmlimp;

import org.w3c.dom.Element;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.common.utils.HAPXMLUtility;
import com.nosliw.data1.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPEntityDefinitionSegment;
import com.nosliw.entity.expression.HAPAttributeExpression;
import com.nosliw.entity.utils.HAPEntityNamingConversion;
import com.nosliw.entity.validation.HAPValidationInfoExpression;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;

public class HAPEntityDefinitionLoaderXmlUtility {

	public static final String TAG_METADATA = "metadata";
	public static final String TAG_ATTRIBUTE_META_CLASSPREFIX = "classPrefix";
	public static final String TAG_ATTRIBUTE_META_NAMEPREFIX = "namePrefix";
	
	public static final String TAG_ENTITY = "entity";
	public static final String TAG_DATA = "data";
	public static final String TAG_ATTRIBUTE_GROUP = "group";
	public static final String TAG_ATTRIBUTE_CLASS = "class"; 

	public static final String TAG_ELEMENT = "element";

	public static final String TAG_ATTRIBUTE = "attribute"; 
	public static final String TAG_ATTRIBUTE_ATTR_DATACATEGARY = "dataCategary";
	public static final String TAG_ATTRIBUTE_ATTR_DATATYPE = "dataType";
	public static final String TAG_ATTRIBUTE_ATTR_IDTYPE = "id"; 
	public static final String TAG_ATTRIBUTE_ATTR_OPTIONS = "options"; 
	public static final String TAG_ATTRIBUTE_ATTR_EVENTS = "events";

	public static final String TAG_ATTRIBUTE_ATTR_DEFAULT = "default";
	public static final String TAG_ATTRIBUTE_ATTR_CRITICAL = "isCritical";
	
	
	public static final String TAG_ATTRIBUTE_NAME = "name";

	//options
	public static final String TAG_OPTIONS = "options";
//	public static final String TAG_ATTRIBUTE_OPTIONS_FILTER = "filter";
	public static final String TAG_ATTRIBUTE_OPTIONS_BASE = "base";
	public static final String TAG_ATTRIBUTE_OPTIONS_QUERY = "query";

	public static final String TAG_OPTION = "option";
	public static final String TAG_ATTRIBUTE_OPTION_VALUE = "value";
	
	public static final String TAG_OPTIONS_PARM = "parm";
	public static final String TAG_ATTRIBUTE_OPTIONS_PARM_NAME = "name";
	public static final String TAG_ATTRIBUTE_OPTIONS_PARM_VALUE = "value";
	
	public final static String TAG_ATTRIBUTE_EMPTYONINIT = "isEmptyOnInit";
	
	public static String readAttributeName(Element ele){
		return ele.getAttribute(TAG_ATTRIBUTE_NAME);
	}
	
	public static void readCommonAttributeDefinition(Element ele, HAPAttributeDefinition attrDef, HAPEntityDefinitionMeta metadata, HAPDataTypeManager dataTypeMan){
		
		//type
		String type = ele.getAttribute(TAG_ATTRIBUTE_ATTR_DATATYPE);
		String categary = ele.getAttribute(TAG_ATTRIBUTE_ATTR_DATACATEGARY);
		attrDef.setDataTypeDefInfo(getDataType(categary, type, metadata, dataTypeMan));

		//empty on init
		String isEmptyOnInit = ele.getAttribute(TAG_ATTRIBUTE_EMPTYONINIT);
		if(HAPBasicUtility.isStringNotEmpty(isEmptyOnInit)){
			attrDef.setIsEmptyOnInit(HAPBasicUtility.toBoolean(isEmptyOnInit));
		}
	
		//event
		String events = ele.getAttribute(TAG_ATTRIBUTE_ATTR_EVENTS);
		if(HAPBasicUtility.isStringNotEmpty(events)){
			HAPSegmentParser eventSegs = new HAPSegmentParser(events, HAPConstant.SEPERATOR_ELEMENT);
			while(eventSegs.hasNext()){
				attrDef.addEvent(eventSegs.next());
			}
		}
		
		//rule
		readRules(ele, attrDef, dataTypeMan);
	}
	
	
	private final static String TAG_ATTRIBUTE_RULE_DESCRIPTION = "description";
	private final static String TAG_ATTRIBUTE_RULE_ERRORMESSAGE = "errorMsg";
	private final static String TAG_ATTRIBUTE_RULE_EXPRESSION = "expression";
	private final static String TAG_ATTRIBUTE_RULE_NAME = "name";
	private final static String TAG_ATTRIBUTE_RULES = "rules";
	private final static String TAG_ATTRIBUTE_RULE = "rule";
	private static void readRules(Element ele, HAPAttributeDefinition attrDef, HAPDataTypeManager dataTypeMan){
		
		//in-line rule
		HAPValidationInfoExpression validationExpressionInfo = readValidationExpressionInfo(ele, attrDef, dataTypeMan);
		if(validationExpressionInfo!=null)  attrDef.addValidationInfo(validationExpressionInfo);
		
		//multiple validation under "rules" tag
		int index = 1;
		Element rulesEle = HAPXMLUtility.getFirstChildElementByName(ele, TAG_ATTRIBUTE_RULES);
		if(rulesEle!=null){
			Element[] parmEles = HAPXMLUtility.getMultiChildElementByName(rulesEle, TAG_ATTRIBUTE_RULE);
			for(Element parmEle : parmEles){
				//valdation item name, if not set, then use index as name
				String name = parmEle.getAttribute(TAG_ATTRIBUTE_RULE_NAME);
				if(HAPBasicUtility.isStringEmpty(name)) name = String.valueOf(index);
				
				String desc = parmEle.getAttribute(TAG_ATTRIBUTE_RULE_DESCRIPTION);
				String erroMsg = parmEle.getAttribute(TAG_ATTRIBUTE_RULE_ERRORMESSAGE);
				String expression = parmEle.getAttribute(TAG_ATTRIBUTE_RULE_EXPRESSION);

				HAPExpression ruleExpression = new HAPAttributeExpression(new HAPExpressionInfo(expression, null, null), attrDef, dataTypeMan);
				HAPValidationInfoExpression info = new HAPValidationInfoExpression(name, ruleExpression);
				info.setDescription(desc);
				info.setErrorMessage(erroMsg);
				attrDef.addValidationInfo(info);
				index++;
			}
		}
		
		//check if this rule is runnable on client side
		attrDef.setServerValidationOnly(false);
		for(HAPValidationInfoExpression info : attrDef.getValidationInfos()){
			if(!info.getExpression().isScriptRunnable(HAPConstant.OPERATIONDEF_SCRIPT_JAVASCRIPT)){
				attrDef.setServerValidationOnly(true);
				break;
			}
		}
	}
	
	/*
	 * read in-line rule definition
	 */
	private static HAPValidationInfoExpression readValidationExpressionInfo(Element ele, HAPAttributeDefinition attrDef, HAPDataTypeManager dataTypeMan){
		HAPValidationInfoExpression out = null;
		String ruleString = ele.getAttribute(TAG_ATTRIBUTE_RULE);
		if(HAPBasicUtility.isStringNotEmpty(ruleString)){
			HAPExpression ruleExpression = new HAPAttributeExpression(new HAPExpressionInfo(ruleString, null, null), attrDef, dataTypeMan);
			out = new HAPValidationInfoExpression(ruleExpression);
		}
		return out;
	}
	
	private static final String ATTRIBUTE_REFENTITYTYPE = "refEntityType";
	public static void readEntityReferenceCommonAttributeDefinition(Element ele, HAPDataTypeDefInfo dataTypeDef, HAPEntityDefinitionMeta metadata, HAPDataTypeManager dataTypeMan){
		String entityRefType = ele.getAttribute(ATTRIBUTE_REFENTITYTYPE);
		
		setDataTypeDefInfoEntityType(dataTypeDef, entityRefType, metadata, dataTypeMan);
	}
	
	static public void setDataTypeDefInfoEntityType(HAPDataTypeDefInfo info, String entityType, HAPEntityDefinitionMeta metadata, HAPDataTypeManager dataTypeMan){
		String group = HAPEntityNamingConversion.getGroupName(entityType);
		if(group!=null){
			info.addEntityGroup(group);
			info.setChildDataCategary(HAPConstant.DATATYPE_CATEGARY_ENTITY);
		}
		else{
			HAPDataTypeDefInfo type = getDataType(HAPConstant.DATATYPE_CATEGARY_ENTITY, entityType, metadata, dataTypeMan);
			info.setChildDataCategary(type.getCategary());
			info.setChildDataType(type.getType());
		}
	}
	
	/*
	 * get data type def info based on value in entity definition xml file
	 */
	public static HAPDataTypeDefInfo getDataType(String categary, String type, HAPEntityDefinitionMeta metadata, HAPDataTypeManager dataTypeMan){
		HAPDataTypeDefInfo out = null;
		if(HAPBasicUtility.isStringNotEmpty(categary)){
			if(categary.equals(HAPConstant.DATATYPE_CATEGARY_ENTITY)){
				String fType = getEntityFullName(type, metadata);
				out = new HAPDataTypeDefInfo(categary, fType);
			}
			else if(categary.equals(HAPConstant.DATATYPE_CATEGARY_CONTAINER) && HAPBasicUtility.isStringEmpty(type)){
				out = new HAPDataTypeDefInfo(categary, HAPConstant.DATATYPE_TYPE_CONTAINER_ENTITYATTRIBUTE);
			}
			else if(categary.equals(HAPConstant.DATATYPE_CATEGARY_REFERENCE) && HAPBasicUtility.isStringEmpty(type)){
				out = new HAPDataTypeDefInfo(categary, HAPConstant.DATATYPE_TYPE_REFERENCE_NORMAL);
			}
			else{
				out = new HAPDataTypeDefInfo(categary, type);
			}
		}
		else{
			//both categary and type is not set, use default datat ype
			if(HAPBasicUtility.isStringEmpty(type))  return new HAPDataTypeDefInfo(dataTypeMan.getDefaultDataTypeInfo());

			String entityName = isEntityName(type, metadata);
			if(entityName!=null)	out = new HAPDataTypeDefInfo(HAPConstant.DATATYPE_CATEGARY_ENTITY, entityName);
			else	out = new HAPDataTypeDefInfo(dataTypeMan.getDataTypeInfoByTypeName(type));
		}
		return out;
	}
	
	/*
	 * check if name is an entity name:
	 * 	".XXX"
	 * 	"..XXXX"
	 * 	"XXX.XXX"
	 * if yes, return entity full name
	 * if not, return null
	 */
	static private String isEntityName(String name, HAPEntityDefinitionMeta metadata){
		if(name.startsWith(HAPConstant.SYMBOL_ENTITYNAME_COMMON))   return getEntityFullName(name, metadata); 
		if(name.startsWith(HAPConstant.SYMBOL_ENTITYNAME_CURRENT)) return  getEntityFullName(name, metadata);
		HAPSegmentParser segs = new HAPSegmentParser(name, HAPConstant.SEPERATOR_FULLNAME);
		if(segs.getSegmentSize()>1)  return  getEntityFullName(name, metadata);
		return null;
	}
	
	/*
	 * get entity full name based on name in configure and metadata
	 */
	static String getEntityFullName(String name, HAPEntityDefinitionMeta metadata){
		if(metadata==null)  return name;
			
		String out = "";
		if(name.startsWith(HAPConstant.SYMBOL_ENTITYNAME_COMMON)){
			//".."   common prefix
			out = HAPNamingConversionUtility.cascadeComponents(HAPEntityDefinitionLoaderXML.PACKAGE_COMMON, 
							name.substring(HAPConstant.SYMBOL_ENTITYNAME_COMMON.length()), 
							HAPConstant.SEPERATOR_FULLNAME);
		}
		else{
			if(name.startsWith(HAPConstant.SYMBOL_ENTITYNAME_CURRENT)){
				//"."   current metadata prefix
				out = HAPNamingConversionUtility.cascadeComponents(metadata.getNamePrefix(), 
						name.substring(HAPConstant.SYMBOL_ENTITYNAME_CURRENT.length()), 
						HAPConstant.SEPERATOR_FULLNAME);
			}
			else{
				//full name
				out = name;
			}
		}
		return out;
	}
	
	/*
	 * get full class based on class name configured and metadata
	 * if className is not provided, then use backupClass instead
	 */
	public static String getFullClassName(String className, HAPEntityDefinitionMeta metadata, String backupClass){
		String name = backupClass;
		if(HAPBasicUtility.isStringNotEmpty(className)){
			name = className;
			HAPSegmentParser segs = new HAPSegmentParser(className, HAPConstant.SEPERATOR_FULLNAME);
			if(segs.getSegmentSize()<=1){
				name = HAPNamingConversionUtility.cascadeComponents(metadata.getClassPrefix(), name, HAPConstant.SEPERATOR_FULLNAME);
			}
		}
		return name;
	}
	
	/*
	 * create name for attribute options
	 */
	public static String createOptionsName(HAPEntityDefinitionSegment entityDef, HAPAttributeDefinition attrDef){
		return createOptionsName(entityDef.getEntityName(), attrDef.getFullName());
	}

	/*
	 * create name for attribute options
	 */
	public static String createOptionsName(String entityName, String attrPath){
		return HAPNamingConversionUtility.cascadeComponents(attrPath, entityName, HAPConstant.SEPERATOR_SURFIX);
	}
}
