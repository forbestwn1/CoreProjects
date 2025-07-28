package com.nosliw.core.application.division.manual.definition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNosliw;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.constant.HAPWithConstantDefinition;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelationValueContext;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPWithScriptExpressionConstantMaster;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.brick.data.HAPManualDefinitionBlockData;
import com.nosliw.core.application.division.manual.brick.value.HAPManualDefinitionBlockValue;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.wrappertask.HAPManualDefinitionBlockTaskWrapper;
import com.nosliw.core.application.division.manual.common.attachment.HAPManualDefinitionAttachment;
import com.nosliw.core.application.division.manual.common.task.HAPManualUtilityTask;
import com.nosliw.core.resource.HAPResourceId;

public abstract class HAPManualDefinitionBrick extends HAPSerializableImp implements HAPEntityOrReference, HAPWithConstantDefinition, HAPWithScriptExpressionConstantMaster{

	public final static String ATTRIBUTE = "attribute"; 
	
	final private static String ATTR_IDINDEX = HAPUtilityNosliw.buildNosliwFullName("idIndex"); 

	static private String VALUECONTEXT = "valueContext"; 

	//all attributes
	private List<HAPManualDefinitionAttributeInBrick> m_attributes;

	private HAPManualDefinitionAttachment m_attachment;
	
	private HAPDefinitionContainerScriptExpression m_constantScriptExpressions;
	
	private HAPIdBrickType m_brickTypeId;

	private HAPManualManagerBrick m_manualBrickMan;
	
	public HAPManualDefinitionBrick (HAPIdBrickType brickTypeId) {
		this.m_attributes = new ArrayList<HAPManualDefinitionAttributeInBrick>();
		this.m_attachment = new HAPManualDefinitionAttachment();
		this.m_brickTypeId = brickTypeId;
		this.m_constantScriptExpressions = new HAPDefinitionContainerScriptExpression();
	}

	protected void init() {}
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.BRICK; }

	public HAPIdBrickType getBrickTypeId() {  return this.m_brickTypeId;	}

	public HAPManualDefinitionBrickValueContext getValueContextBrick() {    return (HAPManualDefinitionBrickValueContext)this.getAttributeValueOfBrick(VALUECONTEXT);       }
	public void setValueContextBrick(HAPManualDefinitionBrickValueContext valueContext) {    this.setAttributeValueWithBrick(VALUECONTEXT, valueContext);      }

	public boolean isValueContextEmpty() {
		if(this.getValueContextBrick()==null) {
			return true;
		}
		List<HAPWrapperValueStructure> valueStructures = this.getValueContextBrick().getValueStructures();
		return valueStructures==null||valueStructures.isEmpty();
	}
	
	public HAPManualDefinitionBrickRelationValueContext getValueContextRelationWithParent() {    return null;    }
	
	public void setAttachment(HAPManualDefinitionAttachment attachment) {   this.m_attachment = attachment;	}
	public HAPManualDefinitionAttachment getAttachment() {    return this.m_attachment;      }
	
	@Override
	public HAPDefinitionContainerScriptExpression getScriptExpressionConstantContainer() {   return this.m_constantScriptExpressions;  }

	@Override
	public void discoverConstantScript() {
		HAPManualDefinitionUtilityValueContext.discoverConstantScript(getValueContextBrick(), this.getScriptExpressionConstantContainer());
	}

	@Override
	public void solidateConstantScript(Map<String, Object> values) {
		HAPManualDefinitionUtilityValueContext.solidateConstantScript(this.getValueContextBrick(), values);
	}

	public void setManualBrickManager(HAPManualManagerBrick manualBrickMan) {     this.m_manualBrickMan = manualBrickMan;       }
	protected HAPManualManagerBrick getManualBrickManager() {    return this.m_manualBrickMan;     }
	
	public void setAttribute(HAPManualDefinitionAttributeInBrick attribute) {
		for(int i=0; i<this.m_attributes.size(); i++) {
			if(this.m_attributes.get(i).getName().equals(attribute.getName())) {
				this.m_attributes.remove(i);
				break;
			}
		}
		
		boolean isTaskAttr = false;
		HAPIdBrickType attrBrickTypeId = HAPManualDefinitionUtilityBrick.getBrickType(attribute.getValueWrapper());
		if(attrBrickTypeId!=null) {
			isTaskAttr = HAPManualUtilityTask.getBrickTaskType(attrBrickTypeId, this.getManualBrickManager())!=null;
		}
		
		if(isTaskAttr&&this.getBrickTypeId()!=HAPEnumBrickType.TASKWRAPPER_100) {
			//insert task wrapper if attribute value is task, the reason is wrapper can create instance for each task execute
			HAPManualDefinitionBlockTaskWrapper taskWrapperBrick = (HAPManualDefinitionBlockTaskWrapper)this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.TASKWRAPPER_100); 
			String taskWrapperAttrName = attribute.getName();
			attribute.setName(HAPBlockTaskWrapper.TASK);
			taskWrapperBrick.setAttribute(attribute);
			this.setAttributeValueWithBrick(taskWrapperAttrName, taskWrapperBrick);
		}
		else {
			this.m_attributes.add(attribute);    
		}
	}

	public List<HAPManualDefinitionAttributeInBrick> getAllAttributes(){    return this.m_attributes;    }
	public List<HAPManualDefinitionAttributeInBrick> getPublicAttributes(){
		List<HAPManualDefinitionAttributeInBrick> out = new ArrayList<HAPManualDefinitionAttributeInBrick>();
		for(HAPManualDefinitionAttributeInBrick attr : this.getAllAttributes()) {
			if(HAPUtilityNosliw.getNosliwCoreName(attr.getName())==null) {
				out.add(attr);
			}
		}
		return out;    
	}
	
	public HAPManualDefinitionAttributeInBrick getAttribute(String attrName) {
		HAPManualDefinitionAttributeInBrick out = null;
		for(HAPManualDefinitionAttributeInBrick attr : this.m_attributes) {
			if(attr.getName().equals(attrName)) {
				return attr;
			}
		}
		return out;
	}
	
	public void setAttributeWithValueWrapper(String attributeName, HAPManualDefinitionWrapperValue valueWrapper) {	this.setAttribute(new HAPManualDefinitionAttributeInBrick(attributeName, valueWrapper));	}
	
	public void setAttributeValueWithValue(String attributeName, Object attrValue) {  this.setAttributeWithValueWrapper(attributeName, new HAPManualDefinitionWrapperValueValue(attrValue));  }	
	public void setAttributeValueWithBrick(String attributeName, HAPEntityOrReference brickOrRef) {
		if(brickOrRef.getEntityOrReferenceType().equals(HAPConstantShared.BRICK)) {
			this.setAttributeWithValueWrapper(attributeName, new HAPManualDefinitionWrapperValueBrick((HAPManualDefinitionBrick)brickOrRef));
		}
		else if(brickOrRef.getEntityOrReferenceType().equals(HAPConstantShared.RESOURCEID)) {
			this.setAttributeWithValueWrapper(attributeName, new HAPManualDefinitionWrapperValueReferenceResource((HAPResourceId)brickOrRef));
		}
	}
	public void setAttributeValueWithDynamic(String attributeName, HAPValueOfDynamic attrValue) {  this.setAttributeWithValueWrapper(attributeName, new HAPManualDefinitionWrapperValueDynamic(attrValue));  }	
	
	public Object getAttributeValueOfValue(String attributeName){
		HAPManualDefinitionAttributeInBrick attr = this.getAttribute(attributeName);
		if(attr!=null) {
			return ((HAPManualDefinitionWrapperValueValue)attr.getValueWrapper()).getValue();
		}
		return null;
	}
	public Object getAttributeValueOfValue(String attrName, Object defaultValue) {
		HAPManualDefinitionAttributeInBrick att = this.getAttribute(attrName);
		if(att==null) {
			this.setAttributeValueWithValue(attrName, defaultValue);
			att = this.getAttribute(attrName);
		}
		return ((HAPManualDefinitionWrapperValueValue)att.getValueWrapper()).getValue();
	}
	
	public HAPEntityOrReference getAttributeValueOfBrick(String attributeName) {
		HAPEntityOrReference out = null;
		HAPManualDefinitionAttributeInBrick attr = this.getAttribute(attributeName);
		if(attr!=null) {
			HAPManualDefinitionWrapperValue attrValueWrapper = attr.getValueWrapper();
			String wrapperType = attrValueWrapper.getValueType();
			if(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK.equals(wrapperType)) {
				out = ((HAPManualDefinitionWrapperValueBrick)attr.getValueWrapper()).getBrick();
			}
			else if(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE.equals(wrapperType)) {
				out = ((HAPManualDefinitionWrapperValueReferenceResource)attr.getValueWrapper()).getResourceId();
			}
		}
		return out;
	}

	public HAPValueOfDynamic getAttributeValueOfDynamic(String attributeName){
		HAPManualDefinitionAttributeInBrick attr = this.getAttribute(attributeName);
		if(attr!=null) {
			return ((HAPManualDefinitionWrapperValueDynamic)attr.getValueWrapper()).getDynamicValue();
		}
		return null;
	}

	@Override
	public Map<String, HAPDefinitionConstant> getConstantDefinitions(){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();
		
		Map<String, HAPManualDefinitionWrapperBrickRoot> valueItems =  this.getAttachment().getItemsByBrickType(HAPEnumBrickType.VALUE_100.getBrickType(), HAPEnumBrickType.VALUE_100.getVersion());
		if(valueItems!=null) {
			for(String name : valueItems.keySet()) {
				HAPManualDefinitionWrapperBrickRoot itemWrapper = valueItems.get(name);
				HAPManualDefinitionBlockValue valueItem = (HAPManualDefinitionBlockValue)itemWrapper.getBrick();
				HAPDefinitionConstant constantDef = new HAPDefinitionConstant();
				itemWrapper.cloneToEntityInfo(constantDef);
				constantDef.setValue(valueItem.getValue());
				out.put(name, constantDef);
			}
		}

		Map<String, HAPManualDefinitionWrapperBrickRoot> dataItems =  this.getAttachment().getItemsByBrickType(HAPEnumBrickType.DATA_100.getBrickType(), HAPEnumBrickType.DATA_100.getVersion());
		if(dataItems!=null) {
			for(String name : dataItems.keySet()) {
				HAPManualDefinitionWrapperBrickRoot itemWrapper = dataItems.get(name);
				HAPManualDefinitionBlockData dataItem = (HAPManualDefinitionBlockData)itemWrapper.getBrick();
				HAPDefinitionConstant constantDef = new HAPDefinitionConstant();
				itemWrapper.cloneToEntityInfo(constantDef);
				constantDef.setValue(dataItem.getData());
				out.put(name, constantDef);
			}
		}
		
		return out;
	}

	
	protected String generateId() {
		int idIndex = (Integer)this.getAttributeValueOfValue(ATTR_IDINDEX, Integer.valueOf(0));
		idIndex++;
		this.setAttributeValueWithValue(ATTR_IDINDEX, idIndex);
		return "generatedId_"+ idIndex;
	}
	
	//normal json
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> attrMap = new LinkedHashMap<String, String>();
		for(HAPManualDefinitionAttributeInBrick attribute : this.getAllAttributes()) {
			attrMap.put(attribute.getName(),  attribute.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildMapJson(attrMap));
	}


	
	
	
	
/*	
	
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	public void setEntityType(String entityType) {    this.m_entityTypeId = entityType;     }

	public HAPEmbededDefinition getAttributeEmbeded(String attrName) {
		HAPManualDefinitionAttributeInBrick att = this.getAttribute(attrName);
		if(att!=null) {
			return att.getValue();
		} else {
			return null;
		}
	}
	
	public Object getAttributeValue1(String attrName) {
		Object out = null;
		HAPEmbededDefinition embeded = this.getAttributeEmbeded(attrName);
		if(embeded!=null) {
			out = embeded.getValue();
		}
		return out;
	}

	public HAPIdEntityInDomain getAttributeValueEntityId(String attrName) {    return (HAPIdEntityInDomain)this.getAttributeValue(attrName);      }
	public HAPManualDefinitionBrick getAttributeValueEntity(String attrName, HAPContextParser parserContext) {
		HAPManualDefinitionBrick out = null;
		HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)this.getAttributeValue(attrName);
		if(attrEntityId!=null) {
			out = parserContext.getGlobalDomain().getEntityDefinition(attrEntityId);
		}
		return out;
	}
	
	public void setAttribute(String attributeName, HAPEmbededDefinition embededEntity, HAPInfoBrickType valueTypeInfo, Boolean autoProcess) {
		if(embededEntity!=null) {
			Object value = embededEntity.getValue();
			HAPManualDefinitionAttributeInBrick attribute = new HAPManualDefinitionAttributeInBrick(attributeName, embededEntity, valueTypeInfo);	
			if(!(value instanceof HAPIdEntityInDomain)) {
				//for not entity attribute, then not autoprocess anyway
				attribute.setAttributeAutoProcess(false);
			}
			else if(autoProcess!=null) {
				attribute.setAttributeAutoProcess(autoProcess);
			}
			this.setAttribute(attribute);
		}
	}

	public void setAttribute(String attributeName, HAPEmbededDefinition embededEntity, HAPInfoBrickType valueTypeInfo) {
		this.setAttribute(attributeName, embededEntity, valueTypeInfo, null);
	}

	public void setAttributeValue(String attributeName, Object attrValue, HAPInfoBrickType valueTypeInfo) {	this.setAttribute(attributeName, new HAPEmbededDefinition(attrValue), valueTypeInfo);	}
	public void setAttributeValueObject1(String attributeName, Object attrValue) {	this.setAttribute(attributeName, new HAPEmbededDefinition(attrValue), new HAPInfoBrickType());	}
	public void setAttributeValueSimple(String attributeName, HAPIdEntityInDomain attrEntityIdInDomain) {setAttributeValue(attributeName, attrEntityIdInDomain, new HAPInfoBrickType(attrEntityIdInDomain.getEntityType(), false));}
	public void setAttributeValueComplex(String attributeName, HAPIdEntityInDomain attrEntityIdInDomain) {setAttributeValue(attributeName, attrEntityIdInDomain, new HAPInfoBrickType(attrEntityIdInDomain.getEntityType(), true));}
	
	
	public void setAttributeObject(String attributeName, HAPEmbededDefinition embededEntity) {setAttribute(attributeName, embededEntity, new HAPInfoBrickType());}
	public void setAttributeSimple(String attributeName, HAPEmbededDefinition embededEntity) {setAttribute(attributeName, embededEntity, new HAPInfoBrickType(((HAPIdEntityInDomain)embededEntity.getValue()).getEntityType(), false));}
	public void setAttributeComplex(String attributeName, HAPEmbededDefinition embededEntity) {setAttribute(attributeName, embededEntity, new HAPInfoBrickType(((HAPIdEntityInDomain)embededEntity.getValue()).getEntityType(), true));}

	public HAPIdEntityInDomain getChild(String childName) {	
		return (HAPIdEntityInDomain)this.getAttribute(childName).getValue().getValue();
	}
	
	public String toExpandedJsonString(HAPDomainEntityDefinitionGlobal entityDefDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>(); 

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntity attribute : this.getAttributes()) {
			attrArray.add(attribute.toExpandedJsonString(entityDefDomain));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
		return HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap);
	}
	
	public abstract HAPManualDefinitionBrick cloneEntityDefinitionInDomain();

	protected void cloneToDefinitionEntityInDomain(HAPManualDefinitionBrick entityDefinitionInDomain) {
		entityDefinitionInDomain.m_entityTypeId = this.m_entityTypeId;
		entityDefinitionInDomain.m_attributes = new ArrayList<HAPManualDefinitionAttributeInBrick>();
		for(HAPManualDefinitionAttributeInBrick attribute : this.getAttributes()) {
			entityDefinitionInDomain.setAttribute((HAPManualDefinitionAttributeInBrick)attribute.cloneEntityAttribute());
		}
	}
*/	
}
