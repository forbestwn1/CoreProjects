package com.nosliw.common.strvalue.valueinfo;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPValueInfoReference extends HAPValueInfo{

	//a list of AttributeValues. It is used when we want to change some attribute value for property inherited from parent
	//for each element in it, it has "path" which is path to sub value info and "attributes" which is all the attributes value want to override  
	public static final String OVERRIDE = "override";
	
	public static final String REFERENCE = "reference";
	public static final String DEFAULT = "default";
	
	private HAPValueInfo m_solidValueInfo;
	
	private Map<String, Object> m_defaultValues;

	public static HAPValueInfoReference build(){
		HAPValueInfoReference out = new HAPValueInfoReference();
		out.init();
		return out;
	}

	@Override
	public void init(){
		super.init();
		this.m_defaultValues = new LinkedHashMap<String, Object>();
		this.updateAtomicChildStrValue(HAPValueInfo.TYPE, HAPConstantShared.STRINGALBE_VALUEINFO_REFERENCE);
	}

	@Override
	public void afterBuild(){
	}

	private HAPStringableValueList<HAPAttributeValues> getOverrideList(){  return (HAPStringableValueList)this.getChild(OVERRIDE); }
	
	@Override
	public String getValueInfoType(){	
		String out = super.getValueInfoType();
		if(out==null)  out = HAPConstantShared.STRINGALBE_VALUEINFO_REFERENCE;
		return out;
	}
	
	@Override
	public String getSolidValueInfoType(){
		return this.getSolidValueInfo().getValueInfoType();
	}
	
	@Override
	public HAPValueInfo getSolidValueInfo(){
		if(this.m_solidValueInfo==null){
			this.m_solidValueInfo = this.getValueInfoManager().getValueInfo(this.getReferencedName()).getSolidValueInfo().clone();

			//columns
			if(this.m_solidValueInfo.getValueInfoType().equals(HAPConstantShared.STRINGABLE_VALUESTRUCTURE_ENTITY)){
				HAPValueInfoEntity solidValueInfoEntity = (HAPValueInfoEntity)m_solidValueInfo;
				if(this.getDBColumnsInfo()!=null){
					solidValueInfoEntity.setDBColumnsInfo(this.getDBColumnsInfo().clone(HAPDBColumnsInfo.class));
					if(solidValueInfoEntity.getDBColumnsInfo().isIncludeColumn())				processSolidEntityValueInfo(solidValueInfoEntity);
					else			removeColumnInfo(solidValueInfoEntity);
				}
				else{
					removeColumnInfo(solidValueInfoEntity);
				}
			}
			
			//override attirubtes
			HAPStringableValueList overrideProperties = this.getOverrideList();
			Iterator overridePropertiesIt = overrideProperties.iterate();
			while(overridePropertiesIt.hasNext()){
				HAPAttributeValues overrideAttributeValues = (HAPAttributeValues)overridePropertiesIt.next();
				HAPValueInfoUtility.updateValueInfoAttributeValue(m_solidValueInfo, overrideAttributeValues);
			}
		}
		return this.m_solidValueInfo;
	}
	
	private void processSolidEntityValueInfo(HAPValueInfoEntity valueInfoEntity){
		Set<String> properties = valueInfoEntity.getEntityProperties();
		for(String property : properties){
			HAPValueInfo propertyValueInfo = valueInfoEntity.getPropertyInfo(property);
			HAPDBColumnsInfo columnsInfo = propertyValueInfo.getDBColumnsInfo();

			if(columnsInfo!=null){
				//get prefix for column
				String prefix = columnsInfo.getPrefix();
				if(prefix==null){
					prefix = this.getName();
				}
				else if(prefix.equals("")){
					prefix = null;
				}

				HAPStringableValueList<HAPDBColumnInfo> columns = columnsInfo.getColumns();
				for(int i=0; i<columns.size(); i++){
					HAPDBColumnInfo column = columns.getValue(i);
					
					switch(columnsInfo.getPathType()){
					case HAPConstantShared.STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_ABSOLUTE:
						columnsInfo.setPathType(HAPConstantShared.STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_PROPERTY);
						break;
					case HAPConstantShared.STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_PROPERTY:
						break;
					case HAPConstantShared.STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_PROPERTYASPATH:
						break;
					}
					
//					column.setGetterPath(HAPNamingConversionUtility.cascadePath(this.getName(), column.getGetterPath()));
//					column.setSetterPath(HAPNamingConversionUtility.cascadePath(this.getName(), column.getSetterPath()));
					
					if(prefix!=null)  column.setColumnNamePrefix(prefix);  
				}
				
				String propertyValueInfoType = propertyValueInfo.getValueInfoType();
				if(HAPConstantShared.STRINGALBE_VALUEINFO_ENTITY.equals(propertyValueInfoType)){
					processSolidEntityValueInfo((HAPValueInfoEntity)propertyValueInfo);
				}
			}
		}
	}
	
	private void removeColumnInfo(HAPValueInfoEntity valueInfoEntity){
		Set<String> properties = valueInfoEntity.getEntityProperties();
		for(String property : properties){
			HAPValueInfo propertyValueInfo = valueInfoEntity.getPropertyInfo(property);
			HAPDBColumnsInfo columnsInfo = propertyValueInfo.getDBColumnsInfo();
			if(columnsInfo!=null){
				columnsInfo.getColumns().clear();
			}

			String propertyValueInfoType = propertyValueInfo.getValueInfoType();
			if(HAPConstantShared.STRINGALBE_VALUEINFO_ENTITY.equals(propertyValueInfoType)){
				removeColumnInfo((HAPValueInfoEntity)propertyValueInfo);
			}
		}
	}
	
	
	private void buildDefaultValue(HAPValueInfo valueInfo, Object defaultValue){
		if(defaultValue==null)  return;
		
		String categary = valueInfo.getValueInfoType();
		if(HAPConstantShared.STRINGALBE_VALUEINFO_ATOMIC.equals(categary)){
			valueInfo.updateAtomicChildStrValue(HAPValueInfoAtomic.DEFAULTVALUE, (String)defaultValue);
		}
		else if(HAPConstantShared.STRINGALBE_VALUEINFO_ENTITY.equals(categary)){
			HAPValueInfoEntity valueInfoEntity = (HAPValueInfoEntity)valueInfo;
			for(String property : valueInfoEntity.getProperties()){
				Object propDefaultValue = ((Map<String, Object>)defaultValue).get(property);
				if(propDefaultValue!=null){
					buildDefaultValue(valueInfoEntity.getPropertyInfo(property), propDefaultValue);
				}
			}
		}
		else if(HAPConstantShared.STRINGALBE_VALUEINFO_LIST.equals(categary)){
			HAPValueInfoList valueInfoList = (HAPValueInfoList)valueInfo;
			this.buildDefaultValue(valueInfoList.getChildValueInfo(), defaultValue);
		}
		else if(HAPConstantShared.STRINGALBE_VALUEINFO_MAP.equals(categary)){
			HAPValueInfoMap valueInfoMap = (HAPValueInfoMap)valueInfo;
			this.buildDefaultValue(valueInfoMap.getChildValueInfo(), defaultValue);
		}
		else if(HAPConstantShared.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(categary)){
			HAPValueInfoEntityOptions valueInfoEntityOptions = (HAPValueInfoEntityOptions)valueInfo;
			for(String key : valueInfoEntityOptions.getOptionsKey()){
				Object keyDefaultValue = ((Map<String, Object>)defaultValue).get(key);
				if(keyDefaultValue!=null){
					this.buildDefaultValue(valueInfoEntityOptions.getOptionsValueInfo(key), keyDefaultValue);
				}
			}
		}
	}
	
	private String getReferencedName(){
		return this.getAtomicAncestorValueString(REFERENCE);
	}

	@Override
	public HAPValueInfoReference clone(){
		HAPValueInfoReference out = new HAPValueInfoReference();
		out.cloneFrom(this);
		return out;
	}

	@Override
	public HAPStringableValue newValue() {		return null;	}
}
