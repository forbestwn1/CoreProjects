package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.strvalue.basic.HAPStringableValue;
import com.nosliw.common.strvalue.basic.HAPStringableValueBasic;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.strvalue.entity.test.HAPStringableEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoMode extends HAPValueInfo{

	public static final String TEMPLATE = "template";
	public static final String ELEMENT_TYPE = "eleType";

	private HAPValueInfoEntity m_solidValueInfo;
	private HAPValueInfoEntity m_templateValueInfo;
	
	public static HAPValueInfoMode build(){
		HAPValueInfoMode out = new HAPValueInfoMode();
		out.init();
		return out;
	}

	@Override
	public HAPValueInfo getSolidValueInfo(){
		if(this.m_solidValueInfo==null){
			HAPValueInfoEntity templateValueInfo = this.getTemplateValueInfo();
			this.m_solidValueInfo = buildModeValueInfo(templateValueInfo);
		}
		return this.m_solidValueInfo;
	}
	
	private HAPValueInfoEntity buildModeValueInfo(HAPValueInfo templateValueInfo){
		HAPValueInfoEntity out = null;
		String modeValueAttrName = "value";
		String categary = templateValueInfo.getCategary();
		HAPValueInfoEntity eleValueInfoEntity = this.getEleTypeValueInfo(categary); 
		if(eleValueInfoEntity!=null){
			out = HAPValueInfoEntity.build(this.getValueInfoManager());
			out.updateEntityProperty(modeValueAttrName, eleValueInfoEntity);
			out.updateChild("type", new HAPStringableValueBasic(categary));
			if(HAPConstant.STRINGALBE_VALUEINFO_BASIC.equals(categary)){
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_LIST.equals(categary)){
				HAPValueInfoList valueInfoListMode = HAPValueInfoList.build();
				out.updateEntityProperty("child", valueInfoListMode);
				HAPValueInfo childValueInfo = ((HAPValueInfoList)templateValueInfo).getChildValueInfo();
				HAPValueInfoEntity childValueInfoMode = this.buildModeValueInfo(childValueInfo);
				valueInfoListMode.setChildValueInfo(childValueInfoMode);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_MAP.equals(categary)){
				HAPValueInfoMap valueInfoMapMode = HAPValueInfoMap.build();
				out.updateEntityProperty("child", valueInfoMapMode);
				HAPValueInfo childValueInfo = ((HAPValueInfoMap)templateValueInfo).getChildValueInfo();
				HAPValueInfoEntity childValueInfoMode = this.buildModeValueInfo(childValueInfo);
				valueInfoMapMode.setChildValueInfo(childValueInfoMode);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(categary)){
				HAPValueInfoEntity childValueInfoMode = HAPValueInfoEntity.build(this.getValueInfoManager());
				out.updateEntityProperty("child", childValueInfoMode);
				HAPValueInfoEntity templateValueInfoEntity = (HAPValueInfoEntity)templateValueInfo;
				for(String property : templateValueInfoEntity.getEntityProperties()){
					HAPValueInfo propertyValueInfo = templateValueInfoEntity.getPropertyInfo(property);
					HAPValueInfoEntity propertyModeValueInfo = this.buildModeValueInfo(propertyValueInfo);
					if(propertyModeValueInfo!=null){
						childValueInfoMode.updateEntityProperty(property, propertyModeValueInfo);
					}
				}
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(categary)){
				HAPValueInfoEntityOptions valueInfoEntityOptionsMode = HAPValueInfoEntityOptions.build();
				out.updateEntityProperty("child", valueInfoEntityOptionsMode);
				HAPValueInfoEntityOptions templateValueInfoEntityOptions = (HAPValueInfoEntityOptions)templateValueInfo;
				for(String key : templateValueInfoEntityOptions.getOptionsKey()){
					HAPValueInfo optionsEleValueInfo = templateValueInfoEntityOptions.getOptionsValueInfo(key);
					HAPValueInfoEntity optionsEleModeValueInfo = this.buildModeValueInfo(optionsEleValueInfo);
					valueInfoEntityOptionsMode.setOptionsValueInfo(key, optionsEleValueInfo);
				}
			}
		}
		
		return out;
	}
	
	private HAPValueInfoEntity getEleTypeValueInfo(String categary){
		return (HAPValueInfoEntity)this.getEleTypesEntity().getChild(categary);
	}
	
	private HAPStringableValueEntity getEleTypesEntity(){		return (HAPStringableValueEntity)this.getChild(ELEMENT_TYPE);	}
	
	private HAPValueInfoEntity getTemplateValueInfo(){
		if(this.m_templateValueInfo==null){
			String templateName = this.getBasicAncestorValueString(TEMPLATE);
			if(templateName!=null){
				this.m_templateValueInfo = (HAPValueInfoEntity)this.getValueInfoManager().getValueInfo(templateName);
			}
		}
		return this.m_templateValueInfo;
	}
	
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
