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
import com.nosliw.common.path.HAPComplexName;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
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
	public void addColumnInfo(HAPDBColumnInfo columnInfo, String propertyPath){
		//get path and property from property path
		HAPComplexName complexName = new HAPComplexName(propertyPath);
		String property = complexName.getSimpleName();
		String path = complexName.getPath();
		
		//if no column name specified, use property name
		if(HAPBasicUtility.isStringEmpty(columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.COLUMN))){
			columnInfo.updateAtomicChild(HAPDBColumnInfo.COLUMN, property);
		}
	
		if("info".equals(propertyPath)){
			int kkkk = 5555;
			kkkk++;
		}
		
		String getter = this.buildGetSetMethod(columnInfo, HAPDBColumnInfo.GETTER, path, property);
		
		String setter = this.buildGetSetMethod(columnInfo, HAPDBColumnInfo.SETTER, path, property);

		String type = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.DATATYPE);
		if(HAPBasicUtility.isStringEmpty(type)){
			//if data type is not specify, try to get from return type of getter method
			try {
				HAPComplexName getterPath = new HAPComplexName(getter);
				HAPValueInfoEntity leafEntityInfo = (HAPValueInfoEntity)this.m_valueInfoEntity.getChildByPath(getterPath.getPath());
				Class returnType = Class.forName(leafEntityInfo.getClassName()).getMethod(getter).getReturnType();
				type = HAPLiterateManager.getInstance().getSubLiterateTypeByClass(returnType);
				columnInfo.updateAtomicChild(HAPDBColumnInfo.DATATYPE, type);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}		
	}
	
	private String buildGetSetMethod(HAPDBColumnInfo columnInfo, String type, String path, String property){
		String out = columnInfo.getAtomicAncestorValueString(type);

		String uperProperty = property.substring(0, 1).toUpperCase() + property.substring(1);

		if(HAPDBColumnInfo.SETTER.equals(type) && "no".equals(out)){
			out = null;
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
			
			String opPath = path;
			String opProperty = null;
			if(HAPBasicUtility.isStringEmpty(out)){
				opPath = path;
				opProperty =  op + uperProperty;
			}
			else{
				opPath = path;
				opProperty = out;
			}
			out = HAPNamingConversionUtility.cascadePath(opPath, opProperty);
		}
		columnInfo.updateAtomicChild(type, out);
		return out;
	}
}
