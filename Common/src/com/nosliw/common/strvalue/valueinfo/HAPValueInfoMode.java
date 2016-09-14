package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.strvalue.basic.HAPStringableValue;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoMode extends HAPValueInfo{

	public static final String TEMPLATE = "template";
	public static final String ELEMENT_TYPE = "eleType";

	private HAPValueInfoEntity m_solidValueInfo;
	
	public static HAPValueInfoMode build(){
		HAPValueInfoMode out = new HAPValueInfoMode();
		out.init();
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
					if(HAPValueInfoEntity.ENTITY_PROPERTY_PROPERTIES.equals(property)){
						for(String entityPro : this.getEntityProperties()){
							this.m_solidValueInfo.updateEntityProperty(entityPro, this.getPropertyInfo(entityPro).clone());
						}
					}
					else if(HAPValueInfoEntity.ENTITY_PROPERTY_PARENT.equals(property)){
						
					}
					else{
						this.m_solidValueInfo.updateChild(property, this.getChild(property).clone());
					}
				}
			}
		}
		return this.m_solidValueInfo;
	}
	
	private HAPValueInfoEntity buildModeValueInfo(HAPValueInfo templateValueInfo){
		HAPValueInfoEntity out = null;
		String categary = templateValueInfo.getCategary();
		if(HAPConstant.STRINGALBE_VALUEINFO_BASIC.equals(categary)){
			out = this.getEleTypeValueInfo(categary);
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_LIST.equals(categary)){
			out = HAPValueInfoEntity.build(this.getValueInfoManager());
			out.updateEntityProperty("value", this.getEleTypeValueInfo(categary));
			HAPValueInfo childValueInfo = ((HAPValueInfoList)templateValueInfo).getChildValueInfo();
			HAPValueInfoEntity childValueInfoMode = this.buildModeValueInfo(childValueInfo);
			out.updateEntityProperty("child", childValueInfoMode);
		}
		
		return out;
	}
	
	private HAPValueInfoEntity getEleTypeValueInfo(String categary){
		return (HAPValueInfoEntity)this.getEleTypesEntity().getChild(categary);
	}
	
	private HAPStringableValueEntity getEleTypesEntity(){		return (HAPStringableValueEntity)this.getChild(ELEMENT_TYPE);	}
	
	@Override
	public String getCategary() {
		return null;
	}

	@Override
	public HAPValueInfo clone() {
		return null;
	}

	@Override
	public HAPStringableValue buildDefault() {
		return null;
	}

	@Override
	public HAPInterpolateOutput resolveByConfigure(HAPConfigureImp configure) {
		return null;
	}

}
