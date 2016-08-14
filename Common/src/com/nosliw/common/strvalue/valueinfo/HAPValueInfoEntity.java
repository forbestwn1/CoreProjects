package com.nosliw.common.strvalue.valueinfo;

import java.util.Set;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoEntity extends HAPValueInfoComplex{

	public static final String ATTR_CLASSNAME = "class";
	public static final String ATTR_MANDATORY = "mandatory";
	public static final String ATTR_PROPERTIES = "property";
	public static final String ATTR_PARENT = "parent";
	
	private HAPValueInfoEntity m_solidValueInfo;
	
	private HAPValueInfoEntity(){}
	
	public static HAPValueInfoEntity build(HAPValueInfoManager valueInfoMan){
		HAPValueInfoEntity out = new HAPValueInfoEntity();
		out.setValueInfoManager(valueInfoMan);
		out.init();
		return out;
	}

	public HAPValueInfo getSolidValueInfo(){
		if(this.m_solidValueInfo==null){
			if(HAPBasicUtility.isStringEmpty(this.getParent())){
				this.m_solidValueInfo = this;
			}
			else{
				HAPValueInfoEntity parentValueInfo = this.getParentEntityValueInfo();
				this.m_solidValueInfo = parentValueInfo.clone();
				
				for(String property : this.getProperties()){
					if(HAPValueInfoEntity.ATTR_PROPERTIES.equals(property)){
						for(String entityPro : this.getEntityProperties()){
							this.m_solidValueInfo.updateEntityProperty(entityPro, this.getPropertyInfo(entityPro).clone());
						}
					}
					else if(HAPValueInfoEntity.ATTR_PARENT.equals(property)){
						
					}
					else{
						this.m_solidValueInfo.addChild(property, this.getChild(property).clone());
					}
				}
			}
		}
		return this.m_solidValueInfo;
	}
	
	@Override
	public String getCategary() {		return HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY;	}

	@Override
	public void init(){
		super.init();
		this.updateBasicChildValue(ATTR_MANDATORY, true);
	}
	
	public HAPValueInfo getPropertyInfo(String name){
		HAPStringableValueEntity properties = this.getPropertiesEntity();
		return (HAPValueInfo)properties.getChild(name);
	}

	public Set<String> getEntityProperties(){
		HAPStringableValueEntity properties = this.getPropertiesEntity();
		return properties.getProperties();
	}
	
	private HAPStringableValueEntity getPropertiesEntity(){		return (HAPStringableValueEntity)this.getChild(ATTR_PROPERTIES);	}
	
	private void updateEntityProperty(String name, HAPValueInfo valueInfo){
		this.getPropertiesEntity().addChild(name, valueInfo);
	}
	
	private String getParent(){
		return this.getBasicAncestorValueString(ATTR_PARENT);
	}
	
	private HAPValueInfoEntity getParentEntityValueInfo(){
		HAPValueInfoEntity out = null;
		String parent = this.getParent();
		if(HAPBasicUtility.isStringNotEmpty(parent)){
			out = (HAPValueInfoEntity)this.getValueInfoManager().getValueInfo(parent);
		}
		return out;
	}

	@Override
	public HAPValueInfoEntity clone(){
		HAPValueInfoEntity out = new HAPValueInfoEntity();
		out.cloneFrom(this);
		return out;
	}
}
