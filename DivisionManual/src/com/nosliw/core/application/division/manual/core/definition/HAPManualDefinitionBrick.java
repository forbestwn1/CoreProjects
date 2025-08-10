package com.nosliw.core.application.division.manual.core.definition;

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
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.constant.HAPWithConstantDefinition;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelationValueContext;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPWithScriptExpressionConstantMaster;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructureDefinition;
import com.nosliw.core.application.division.manual.brick.data.HAPManualDefinitionBlockData;
import com.nosliw.core.application.division.manual.brick.value.HAPManualDefinitionBlockValue;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.wrappertask.HAPManualDefinitionBlockTaskWrapper;
import com.nosliw.core.application.division.manual.common.task.HAPManualUtilityTask;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.xxx.application.division.manual.core.definition1.HAPManualDefinitionUtilityValueContext;
import com.nosliw.core.xxx.application.division.manual.core.definition1.HAPManualDefinitionWrapperValueDynamic;

public abstract class HAPManualDefinitionBrick extends HAPSerializableImp implements HAPEntityOrReference, HAPWithConstantDefinition, HAPWithScriptExpressionConstantMaster{

	public final static String ATTRIBUTE = "attribute"; 
	
	final private static String ATTR_IDINDEX = HAPUtilityNosliw.buildNosliwFullName("idIndex"); 

	static private String VALUECONTEXT = "valueContext"; 

	//all attributes
	private List<HAPManualDefinitionAttributeInBrick> m_attributes;

	//attachment
	private HAPManualDefinitionAttachment m_attachment;
	
	//constant script expression
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
		List<HAPWrapperValueStructureDefinition> valueStructures = this.getValueContextBrick().getValueStructures();
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

}
