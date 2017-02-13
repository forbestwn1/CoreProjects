package com.nosliw.common.strvalue.valueinfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoEntity extends HAPValueInfoComplex{
	//class name for entity
	public static final String CLASSNAME = "class";
	//when it is used to define property of entity, whether this property is mandatory 
	public static final String MANDATORY = "mandatory";
	//all properties
	public static final String PROPERTIES = "property";
	//parent entity, the entity that this entity inherit property from
	public static final String PARENT = "parent";
	//a list of override property. It is used when we want to change some attribute value for property inherited from parent
	//for each element in it, it has "property" which is property name and "value" which is all the attributes value want to override  
	public static final String OVERRIDE = "override";
	public static final String OVERRIDE_PROPERTY = "property";
	public static final String OVERRIDE_VALUE = "value";
	//table name this entity represent
	public static final String TABLE = "table";
	
	private HAPValueInfoEntity m_solidValueInfo;
	
	private HAPValueInfoEntity(){}
	
	public static HAPValueInfoEntity build(){
		HAPValueInfoEntity out = new HAPValueInfoEntity();
		out.init();
		return out;
	}

	@Override
	public String getValueInfoType(){	
		String out = super.getValueInfoType();
		if(out==null)  out = HAPConstant.STRINGALBE_VALUEINFO_ENTITY;
		return out;
	}

	@Override
	public HAPValueInfo getSolidValueInfo(){
		if(this.m_solidValueInfo==null){
			if(HAPBasicUtility.isStringEmpty(this.getParent())){
				this.m_solidValueInfo = this;
			}
			else{
				HAPValueInfoEntity parentValueInfo = this.getParentEntityValueInfo();
				this.m_solidValueInfo = parentValueInfo.clone();
				
				for(String property : this.getProperties()){
					if(HAPValueInfoEntity.PROPERTIES.equals(property)){
						for(String entityPro : this.getEntityProperties()){
							this.m_solidValueInfo.updateEntityProperty(entityPro, this.getPropertyInfo(entityPro).clone());
						}
					}
					else if(HAPValueInfoEntity.PARENT.equals(property)){
						//ignore parent
					}
					else if(HAPValueInfoEntity.OVERRIDE.equals(property)){
						//apply override property
						HAPStringableValueList overrideProperties = this.getOverrideList();
						Iterator overridePropertiesIt = overrideProperties.iterate();
						while(overridePropertiesIt.hasNext()){
							HAPStringableValueEntity overridePropertyEntity = (HAPStringableValueEntity)overridePropertiesIt.next();
							String overridePropertyName = overridePropertyEntity.getAtomicAncestorValueString(OVERRIDE_PROPERTY);
							String overridePropertyValues = overridePropertyEntity.getAtomicAncestorValueString(OVERRIDE_VALUE);
							Map<String, String> propertyValues = HAPNamingConversionUtility.parsePropertyValuePairs(overridePropertyValues);
							HAPValueInfo propertyValueInfo = this.m_solidValueInfo.getPropertyInfo(overridePropertyName);
							for(String pro : propertyValues.keySet()){
								propertyValueInfo.updateAtomicChild(pro, propertyValues.get(pro));
							}
						}
					}
					else{
						this.m_solidValueInfo.updateChild(property, this.getChild(property).clone());
					}
				}
			}
		}
		return this.m_solidValueInfo;
	}
	
	private HAPStringableValueEntity newEntity(){
		HAPStringableValueEntity out = null;
		try{
			String className = this.getAtomicAncestorValueString(HAPValueInfoEntity.CLASSNAME);
			if(className==null)    	className = HAPStringableValueEntity.class.getName();
			
			out = (HAPStringableValueEntity)Class.forName(className).newInstance();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public void init(){
		super.init();
		this.updateAtomicChildValue(MANDATORY, true);
		this.updateAtomicChild(HAPValueInfo.TYPE, HAPConstant.STRINGALBE_VALUEINFO_ENTITY);
	}
	
	public HAPValueInfo getPropertyInfo(String name){
		HAPStringableValueEntity properties = this.getPropertiesEntity();
		return (HAPValueInfo)properties.getChild(name);
	}

	public Set<String> getEntityProperties(){
		HAPStringableValueEntity properties = this.getPropertiesEntity();
		return properties.getProperties();
	}
	
	private HAPStringableValueEntity getPropertiesEntity(){		return (HAPStringableValueEntity)this.getChild(PROPERTIES);	}
	private HAPStringableValueList getOverrideList(){  return (HAPStringableValueList)this.getChild(OVERRIDE); }
	
	public void updateEntityProperty(String name, HAPValueInfo valueInfo){
		this.getPropertiesEntity().updateChild(name, valueInfo);
	}
	
	public String getParent(){		return this.getAtomicAncestorValueString(PARENT);	}
	public String getClassName(){  return this.getAtomicAncestorValueString(CLASSNAME); }
	public void setClassName(String name){  this.updateAtomicChild(CLASSNAME, name); }
	public String getTable(){  return this.getAtomicAncestorValueString(TABLE);  }
	
	private HAPValueInfoEntity getParentEntityValueInfo(){
		HAPValueInfoEntity out = null;
		String parent = this.getParent();
		if(HAPBasicUtility.isStringNotEmpty(parent)){
			out = (HAPValueInfoEntity)this.getValueInfoManager().getValueInfo(parent);
		}
		return out;
	}

	@Override
	protected HAPValueInfo getElement(String name){		return this.getPropertyInfo(name);	}
	
	@Override
	public HAPValueInfoEntity clone(){
		HAPValueInfoEntity out = new HAPValueInfoEntity();
		out.cloneFrom(this);
		return out;
	}

	@Override
	public HAPStringableValue newValue() {
		HAPStringableValueEntity out = this.newEntity();
		if(out!=null){
			Set<String> optionsAttr = new HashSet<String>();
			
			for(String property : this.getEntityProperties()){
				HAPValueInfo propertyValueInfo = this.getPropertyInfo(property);
				if(HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyValueInfo.getValueInfoType()))  optionsAttr.add(property);
				else{
					if(!propertyValueInfo.getValueInfoType().equals(HAPConstant.STRINGABLE_VALUESTRUCTURE_ATOMIC)){
						HAPStringableValue entityProperty = propertyValueInfo.newValue();
						if(entityProperty!=null)			out.updateChild(property, entityProperty);
					}
				}
			}
			
			for(String property : optionsAttr){
				HAPValueInfoEntityOptions propertyValueInfo = (HAPValueInfoEntityOptions)this.getPropertyInfo(property);
				String optionsValue = out.getChild(propertyValueInfo.getKeyName()).toString();
				HAPStringableValue entityProperty = propertyValueInfo.buildDefault(optionsValue);
				if(entityProperty!=null)			out.updateChild(property, entityProperty);
			}
		}
		return out;
	}
	
	@Override
	public boolean equals(Object data){
		boolean out = false;
		if(data instanceof HAPValueInfoEntity){
			out = HAPBasicUtility.isEquals(this.getName(), ((HAPValueInfoEntity)data).getName());
		}
		return out;
	}
}
