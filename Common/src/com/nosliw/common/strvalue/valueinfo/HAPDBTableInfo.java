package com.nosliw.common.strvalue.valueinfo;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPConstantManager;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.literate.HAPLiterateType;
import com.nosliw.common.path.HAPComplexName;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPDBTableInfo {

	private String m_tableName;
	
	private HAPValueInfoEntity m_valueInfoEntity;
	
	private List<HAPDBColumnInfo> m_columns;
	
	public HAPDBTableInfo(String table, HAPValueInfoEntity valueInfoEntity){
		this.m_tableName = table;
		this.m_columns = new ArrayList<HAPDBColumnInfo>();
		this.m_valueInfoEntity = valueInfoEntity;
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
			columnInfo.updateAtomicChild(HAPDBColumnInfo.COLUMN, property);
		}
	
		String methodProperty = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.COLUMN);

		String getter = this.buildGetSetMethod(columnInfo, HAPDBColumnInfo.GETTER, attrPath, property, methodProperty, relativePath);
		String setter = this.buildGetSetMethod(columnInfo, HAPDBColumnInfo.SETTER, attrPath, property, methodProperty, relativePath);
		
		String type = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.DATATYPE);
		if(HAPBasicUtility.isStringEmpty(type)){
			//if data type is not specify, try to get from return type of getter method
			try {
				HAPComplexName getterPath = new HAPComplexName(getter);
				HAPValueInfo childValueInfo = this.m_valueInfoEntity.getChildByPath(getterPath.getPath());
				String className = HAPValueInfoUtility.getEntityClassNameFromValueInfo(childValueInfo);
				Class returnType = Class.forName(className).getMethod(getterPath.getSimpleName()).getReturnType();
				HAPLiterateType litType = HAPLiterateManager.getInstance().getLiterateTypeByClass(returnType);
				columnInfo.updateAtomicChild(HAPDBColumnInfo.DATATYPE, litType.getType());
				columnInfo.updateAtomicChild(HAPDBColumnInfo.SUBDATATYPE, litType.getSubType());
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		this.m_columns.add(columnInfo);
	}
	
	private String buildGetSetMethod(HAPDBColumnInfo columnInfo, String type, String attrPath, String attr, String opProperty, String pathType){
		String methodName = columnInfo.getAtomicAncestorValueString(type);
		
		if(HAPDBColumnInfo.SETTER.equals(type) && "no".equals(methodName)){
			columnInfo.updateAtomicChild(HAPDBColumnInfo.SETTER, null);
			columnInfo.updateAtomicChild(HAPDBColumnInfo.SETTER_PATH, null);
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
				columnInfo.updateAtomicChild(HAPDBColumnInfo.GETTER, methodMethod);
				columnInfo.updateAtomicChild(HAPDBColumnInfo.GETTER_PATH, methodPath);
				break;
			case HAPDBColumnInfo.SETTER:
				columnInfo.updateAtomicChild(HAPDBColumnInfo.SETTER, methodMethod);
				columnInfo.updateAtomicChild(HAPDBColumnInfo.SETTER_PATH, methodPath);
				break;
			}
			
			return HAPNamingConversionUtility.cascadePath(methodPath, methodMethod);
		}
	}
}
