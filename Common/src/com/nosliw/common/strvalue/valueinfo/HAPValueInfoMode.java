package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.mode.HAPStringableValueModeEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPValueInfoMode extends HAPValueInfo{

	public static final String TEMPLATE = "template";
	public static final String CLASSNAME = "class";
	public static final String ELEMENT_TYPE = "eleType";

	public static final String SOLID_ATTRIBUTE_MODE = "mode";
	public static final String SOLID_ATTRIBUTE_CATEGARY = "categary";
	public static final String SOLID_ATTRIBUTE_CHILD = "child";
	public static final String SOLID_ATTRIBUTE_ATTRIBUTES = "attributes";
	public static final String SOLID_ATTRIBUTE_OPTIONS = "options";
	
	private HAPValueInfoEntity m_solidValueInfo;
	private HAPValueInfoEntity m_templateValueInfo;
	
	private HAPValueInfoManager m_valueInfoMan;
	
	public HAPValueInfoMode(){
	}
	
	public static HAPValueInfoMode build(){
		HAPValueInfoMode out = new HAPValueInfoMode();
		out.init();
		return out;
	}

	@Override
	public void init(){
		super.init();
		this.updateAtomicChildStrValue(HAPValueInfo.TYPE, HAPConstantShared.STRINGALBE_VALUEINFO_MODE);
	}
	
	@Override
	public HAPValueInfo getSolidValueInfo(){
		if(this.m_solidValueInfo==null){
			HAPValueInfoEntity templateValueInfo = this.getTemplateValueInfo();
			this.m_solidValueInfo = buildModeValueInfo(templateValueInfo);
			if(HAPBasicUtility.isStringEmpty(this.m_solidValueInfo.getClassName())){
				this.m_solidValueInfo.setClassName(HAPStringableValueModeEntity.class.getName());
			}
		}
		return this.m_solidValueInfo;
	}
	
	private HAPValueInfoEntity getModeValueInfo(String categary){
		return (HAPValueInfoEntity)this.m_valueInfoMan.getValueInfo("mode_"+categary);
	}
	
	private HAPValueInfoEntity buildModeValueInfo(HAPValueInfo templateValueInfo){
		HAPValueInfoEntity modeValueInfo = null;
		String templateCategary = templateValueInfo.getValueInfoType();
		HAPValueInfoEntity categaryValueInfoEntity = this.getModeDefValueInfoByCategary(templateCategary); 
		if(categaryValueInfoEntity!=null){
			modeValueInfo = getModeValueInfo(templateCategary); 
			modeValueInfo.updateEntityProperty(SOLID_ATTRIBUTE_MODE, categaryValueInfoEntity.clone());
			if(HAPConstantShared.STRINGALBE_VALUEINFO_ATOMIC.equals(templateCategary)){
			}
			else if(HAPConstantShared.STRINGALBE_VALUEINFO_LIST.equals(templateCategary)){
				HAPValueInfoList modeListValueInfo = (HAPValueInfoList)modeValueInfo.getPropertyInfo(SOLID_ATTRIBUTE_CHILD);
				HAPValueInfo templateChildValueInfo = ((HAPValueInfoList)templateValueInfo).getChildValueInfo();
				HAPValueInfoEntity modeChildValueInfo = this.buildModeValueInfo(templateChildValueInfo);
				modeListValueInfo.setChildValueInfo(modeChildValueInfo);
			}
			else if(HAPConstantShared.STRINGALBE_VALUEINFO_MAP.equals(templateCategary)){
				HAPValueInfoMap modeMapValueInfo = (HAPValueInfoMap)modeValueInfo.getPropertyInfo(SOLID_ATTRIBUTE_CHILD);
				HAPValueInfo templateChildValueInfo = ((HAPValueInfoMap)templateValueInfo).getChildValueInfo();
				String key = ((HAPValueInfoMap)templateValueInfo).getKey();
				modeMapValueInfo.setKey(key);
				HAPValueInfoEntity modeChildValueInfo = this.buildModeValueInfo(templateChildValueInfo);
				modeMapValueInfo.setChildValueInfo(modeChildValueInfo);
			}
			else if(HAPConstantShared.STRINGALBE_VALUEINFO_ENTITY.equals(templateCategary)){
				HAPValueInfoEntity modeAttrsValueInfo = (HAPValueInfoEntity)modeValueInfo.getPropertyInfo(SOLID_ATTRIBUTE_CHILD);
				HAPValueInfoEntity templateValueInfoEntity = (HAPValueInfoEntity)templateValueInfo;
				for(String property : templateValueInfoEntity.getEntityProperties()){
					HAPValueInfo propertyValueInfo = templateValueInfoEntity.getPropertyInfo(property);
					HAPValueInfoEntity propertyModeValueInfo = this.buildModeValueInfo(propertyValueInfo);
					if(propertyModeValueInfo!=null){
						modeAttrsValueInfo.updateEntityProperty(property, propertyModeValueInfo);
					}
				}
			}
			else if(HAPConstantShared.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(templateCategary)){
				HAPValueInfoEntityOptions modeOptionsValueInfo = (HAPValueInfoEntityOptions)modeValueInfo.getPropertyInfo(SOLID_ATTRIBUTE_OPTIONS);
				HAPValueInfoEntityOptions templateValueInfoEntityOptions = (HAPValueInfoEntityOptions)templateValueInfo;
				modeOptionsValueInfo.setKeyName(templateValueInfoEntityOptions.getKeyName());
				for(String key : templateValueInfoEntityOptions.getOptionsKey()){
					HAPValueInfo optionsEleValueInfo = templateValueInfoEntityOptions.getOptionsValueInfo(key);
					HAPValueInfoEntity optionsEleModeValueInfo = this.buildModeValueInfo(optionsEleValueInfo);
					modeOptionsValueInfo.setOptionsValueInfo(key, optionsEleModeValueInfo);
				}
			}
		}
		
		return modeValueInfo;
	}
	
	private HAPValueInfoEntity getModeDefValueInfoByCategary(String categary){
		return (HAPValueInfoEntity)this.getModeDefsValueInfoEntity().getChild(categary);
	}
	
	private HAPStringableValueEntity getModeDefsValueInfoEntity(){		return (HAPStringableValueEntity)this.getChild(ELEMENT_TYPE);	}
	
	private HAPValueInfoEntity getTemplateValueInfo(){
		if(this.m_templateValueInfo==null){
			String templateName = this.getAtomicAncestorValueString(TEMPLATE);
			if(templateName!=null){
				this.m_templateValueInfo = (HAPValueInfoEntity)this.getValueInfoManager().getValueInfo(templateName);
			}
		}
		return this.m_templateValueInfo;
	}
	
	@Override
	public HAPValueInfoMode clone(){
		HAPValueInfoMode out = new HAPValueInfoMode();
		out.cloneFrom(this);
		return out;
	}

	@Override
	public HAPStringableValue newValue() {
		return null;
	}
}
