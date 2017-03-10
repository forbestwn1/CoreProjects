package com.nosliw.common.strvalue.valueinfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.literate.HAPLiterateType;
import com.nosliw.common.path.HAPComplexName;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPDBTableInfo {

	private String m_tableName;
	
	private Set<String> m_primaryKeys;
	
	private HAPValueInfoEntity m_valueInfoEntity;
	
	private List<HAPDBColumnInfo> m_columns;
	
	public HAPDBTableInfo(String table, Set<String> primaryKeys, HAPValueInfoEntity valueInfoEntity){
		this.m_tableName = table;
		this.m_columns = new ArrayList<HAPDBColumnInfo>();
		this.m_valueInfoEntity = valueInfoEntity;
		
		if(primaryKeys==null)  primaryKeys = new HashSet<String>();
		else		this.m_primaryKeys = primaryKeys;
	}
	
	public String getTableName(){ return this.m_tableName; }
	public List<HAPDBColumnInfo> getColumnsInfo(){  return this.m_columns;  }
	
	/**
	 * 
	 * @param columnInfo column definition
	 * @param entityProperty  
	 */
	public void addColumnInfo(HAPDBColumnInfo columnInfo, String attrPath, String property, String relativePath){
		//if no column name specified, use property name
		if(HAPBasicUtility.isStringEmpty(columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.COLUMN))){
			columnInfo.updateAtomicChildStrValue(HAPDBColumnInfo.COLUMN, property);
		}
	
		//update property
		columnInfo.setProperty(HAPNamingConversionUtility.cascadePath(attrPath, property));
		
		String methodProperty = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.COLUMN);

		if(this.m_primaryKeys.contains(methodProperty)){
			//primary key column
			columnInfo.updateAtomicChildStrValue(HAPDBColumnInfo.PRIMARYKEY, String.valueOf(true));
		}
		
		if(columnInfo.getColumnName().equals("source_info")){
			int kkkk = 5555;
			kkkk++;
		}
		
		String getter = this.buildGetSetMethod(columnInfo, HAPDBColumnInfo.GETTER, attrPath, property, methodProperty, relativePath);
		String setter = this.buildGetSetMethod(columnInfo, HAPDBColumnInfo.SETTER, attrPath, property, methodProperty, relativePath);
		
		String type = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.DATATYPE);
		if(HAPBasicUtility.isStringEmpty(type)){
			//if data type is not specify, try to get from return type of getter method
			try {
				HAPComplexName getterPath = new HAPComplexName(getter);

				if(getterPath.getSimpleName().equals("getFullName")){
					int kkkk = 555;
					kkkk++;
				}
				
				HAPValueInfo childValueInfo = this.m_valueInfoEntity.getChildByPath(getterPath.getPath());
				String className = HAPValueInfoUtility.getEntityClassNameFromValueInfo(childValueInfo);
				
				Class returnType = Class.forName(className).getMethod(getterPath.getSimpleName()).getReturnType();
				HAPLiterateType litType = HAPLiterateManager.getInstance().getLiterateTypeByClass(returnType);
				columnInfo.updateAtomicChildStrValue(HAPDBColumnInfo.DATATYPE, litType.getType());
				columnInfo.updateAtomicChildStrValue(HAPDBColumnInfo.SUBDATATYPE, litType.getSubType());
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		this.m_columns.add(columnInfo);
	}
	
	private String buildGetSetMethod(HAPDBColumnInfo columnInfo, String type, String attrPath, String attr, String opProperty, String pathType){
		String methodName = columnInfo.getAtomicAncestorValueString(type);
		
		if(HAPDBColumnInfo.SETTER.equals(type) && "no".equals(methodName)){
			columnInfo.updateAtomicChildStrValue(HAPDBColumnInfo.SETTER, null);
			columnInfo.updateAtomicChildStrValue(HAPDBColumnInfo.SETTER_PATH, null);
			return null;
		}
		else{
			String methodPath = null;
			String methodMethod = null;
			if(HAPBasicUtility.isStringNotEmpty(methodName)){
				HAPComplexName complexName = new HAPComplexName(methodName);
				methodMethod= complexName.getSimpleName();
				methodPath = complexName.getPath();
			}
			else{
				String op = null;
				switch(type){
				case HAPDBColumnInfo.GETTER:
					op = "get";
					break;
				case HAPDBColumnInfo.SETTER:
					op = "set";
					break;
				}
				methodMethod = op + HAPBasicUtility.upperCaseFirstLetter(opProperty);
			}

			switch(pathType){
			case HAPConstant.STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_ABSOLUTE:
				break;
			case HAPConstant.STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_PROPERTY:
				methodPath = attrPath;
				break;
			case HAPConstant.STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_PROPERTYASPATH:
				methodPath = HAPNamingConversionUtility.cascadePath(new String[]{attrPath, attr, methodPath});
				break;
			}
			
			switch(type){
			case HAPDBColumnInfo.GETTER:
				columnInfo.updateAtomicChildStrValue(HAPDBColumnInfo.GETTER, methodMethod);
				columnInfo.updateAtomicChildStrValue(HAPDBColumnInfo.GETTER_PATH,  methodPath);
				break;
			case HAPDBColumnInfo.SETTER:
				columnInfo.updateAtomicChildStrValue(HAPDBColumnInfo.SETTER, methodMethod);
				columnInfo.updateAtomicChildStrValue(HAPDBColumnInfo.SETTER_PATH, methodPath);
				break;
			}
			
			return HAPNamingConversionUtility.cascadePath(methodPath, methodMethod);
		}
	}
}
