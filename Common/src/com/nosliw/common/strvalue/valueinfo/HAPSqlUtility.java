package com.nosliw.common.strvalue.valueinfo;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.literate.HAPLiterateType;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPSqlUtility {

	public static String dropoffTableSql(HAPDBTableInfo tableInfo){
		if(tableInfo==null){
			int kkk = 5555;
			kkk++;
		}
		
		return "DROP TABLE IF EXISTS " + tableInfo.getTableName() + ";";
	}
	
	public static String buildEntityQuerySql(String tableName, String query){
		String out = "SELECT * FROM " + tableName;
		if(HAPBasicUtility.isStringNotEmpty(query)){
			out = "SELECT * FROM " + tableName + " WHERE " + query;
		}
		System.out.println(out);
		return out;
	}
	
	public static void createDBTable(HAPDBTableInfo tableInfo, Connection connection){
		String dropoffSql = HAPSqlUtility.dropoffTableSql(tableInfo);
		String createSql = HAPSqlUtility.createTableSql(tableInfo);
		
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(dropoffSql);
			stmt.executeUpdate(createSql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(createSql);
	}
	
	public static void saveToDB(HAPStringableValueEntity obj, Connection connection){
		HAPValueInfoEntity valueInfoEntity = HAPValueInfoManager.getInstance().getEntityValueInfoByClass(obj.getClass());
		saveToDB(obj, valueInfoEntity, connection);
	}

	public static void saveToDB(HAPStringableValueEntity obj, HAPValueInfoEntity valueInfoEntity, Connection connection){
		HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(valueInfoEntity.getName());
		saveToDB(obj, dbTableInfo, connection);
	}

	public static void saveToDB(HAPStringableValueEntity obj, HAPDBTableInfo dbTableInfo, Connection connection){
		try {
			String insertSql = buildInstertSql(dbTableInfo);
			System.out.println(insertSql);
			PreparedStatement statement = connection.prepareStatement(insertSql);
			
			saveToDB(obj, dbTableInfo, statement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Build create table sql 
	 * @param tableInfo
	 * @return
	 */
	public static String createTableSql(HAPDBTableInfo tableInfo){
		StringBuffer sqlColumns = new StringBuffer();
		List<HAPDBColumnInfo> columns = tableInfo.getColumnsInfo(); 
		for(int i=0; i<columns.size(); i++){
			HAPDBColumnInfo columnInfo = columns.get(i);
			String columnSql = createColumnSql(columnInfo);
			if(i!=0){
				sqlColumns.append(",\n");
			}
			sqlColumns.append(columnSql);
		}

		InputStream createTableTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPDBTableInfo.class, "CreateTable.temp");
		Map<String, String> createTableTemplateMap = new LinkedHashMap<String, String>();
		createTableTemplateMap.put("tableName", tableInfo.getTableName());
		createTableTemplateMap.put("columns", sqlColumns.toString());
		String out = HAPStringTemplateUtil.getStringValue(createTableTemplateStream, createTableTemplateMap);
		return out;
	}
	
	private static String createColumnSql(HAPDBColumnInfo columnInfo){
		String columnName = columnInfo.getColumnName();
		String columnDef = columnInfo.getAtomicAncestorValueString(HAPDBColumnInfo.DEFINITION);
		return columnName + " " + columnDef + (columnInfo.isPrimaryKey()?" PRIMARY KEY " : "");
	}

	/**
	 * Build insert sql according to db table info
	 * @param dbTableInfo
	 * @return
	 */
	public static String buildInstertSql(HAPDBTableInfo dbTableInfo){
		StringBuffer insertSql = new StringBuffer().append("INSERT INTO "+ dbTableInfo.getTableName() +" ( ");

		StringBuffer questions = new StringBuffer();
		List<HAPDBColumnInfo> columnInfos = dbTableInfo.getColumnsInfo();
		for(int i=0; i<columnInfos.size(); i++){
			HAPDBColumnInfo columnInfo = columnInfos.get(i);
			if(i>0)    {
				insertSql.append(", ");
				questions.append(",");
			}
			insertSql.append(columnInfo.getColumnName());
			questions.append("?");
		}
		insertSql.append(") VALUES (" + questions.toString() + ")");
		return insertSql.toString();
	}
	
	public static void saveToDB(HAPStringableValueEntity obj, HAPDBTableInfo dbTableInfo, PreparedStatement statement){
		try {
			List<HAPDBColumnInfo> columnInfos = dbTableInfo.getColumnsInfo();
			for(int i=0; i<columnInfos.size(); i++){
				HAPDBColumnInfo columnInfo = columnInfos.get(i);
				String getterMethod = columnInfo.getGetter();
				String getterPath = columnInfo.getGetterPath();
				HAPStringableValue columnStrableValue = obj.getAncestorByPath(getterPath);
				Object columnValue = null;
				if(columnStrableValue!=null){
					Object columnObj = HAPValueInfoUtility.getObjectFromStringableValue(columnStrableValue);
					columnValue = columnObj.getClass().getMethod(getterMethod).invoke(columnObj);
				}
				String dataType = columnInfo.getDataType();
				if(HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING.equals(dataType)){
					statement.setString(i+1, (String)columnValue);
				}
				else if(HAPConstant.STRINGABLE_ATOMICVALUETYPE_INTEGER.equals(dataType)){
					if(statement==null || columnValue==null){
						statement.setObject(i+1, null);
					}
					else{
						statement.setInt(i+1, (Integer)columnValue);
					}
				}
				else if(HAPConstant.STRINGABLE_ATOMICVALUETYPE_BOOLEAN.equals(dataType)){
					statement.setBoolean(i+1, (Boolean)columnValue);
				}
				else if(HAPConstant.STRINGABLE_ATOMICVALUETYPE_FLOAT.equals(dataType)){
					statement.setFloat(i+1, (Float)columnValue);
				}
				else if(HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT.equals(dataType)){
					statement.setString(i+1, HAPSerializeManager.getInstance().toStringValue(columnValue, HAPSerializationFormat.LITERATE));
				}
				else if(HAPConstant.STRINGABLE_ATOMICVALUETYPE_ARRAY.equals(dataType)){
					statement.setString(i+1, HAPSerializeManager.getInstance().toStringValue(columnValue, HAPSerializationFormat.LITERATE));
				}
			}
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static String buildQuerySql(String dataTypeName, String query){
		HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(dataTypeName);
		String tableName = dbTableInfo.getTableName();
		StringBuffer out = new StringBuffer();
		out.append("SELECT * FROM ").append(tableName).append(" WHERE ").append(query);
		return out.toString();
	}
	
	public static List<Object> queryFromDB(String dataTypeName, PreparedStatement statement){
		List<Object> out = new ArrayList<Object>();
		
		try {
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				HAPValueInfoEntity valueInfo = (HAPValueInfoEntity)HAPValueInfoManager.getInstance().getValueInfo(dataTypeName).getSolidValueInfo();
				HAPStringableValueEntity entity = (HAPStringableValueEntity)valueInfo.newValue(); 
				
				HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(dataTypeName);
				List<HAPDBColumnInfo> columns = dbTableInfo.getColumnsInfo();
				for(HAPDBColumnInfo column : columns){
					
					String setterMethodName = column.getSetter();
					if(HAPBasicUtility.isStringNotEmpty(setterMethodName)){
						String setterPath = column.getSetterPath();
						HAPStringableValue columnStrableValue = HAPStringableValueUtility.buildAncestorByPath(entity, setterPath, valueInfo);
						Object obj = HAPValueInfoUtility.getObjectFromStringableValue(columnStrableValue);
						
						HAPLiterateType literateType = new HAPLiterateType(column.getDataType(), column.getSubDataType());
						Class parmClass = HAPLiterateManager.getInstance().getClassByLiterateType(literateType);
						
						Method setterMethod = null;
						Object columnObject = null;
						try{
							Object columnValue = resultSet.getObject(column.getColumnName());
							if(columnValue!=null){
								if(columnValue instanceof String){
									columnObject = HAPLiterateManager.getInstance().stringToValue((String)columnValue, literateType);
									if(columnObject==null){
										//if literateType is not a valid one, then use the type info from property
										HAPValueInfo propertyValueInfo = valueInfo.getChildByPath(column.getProperty()).getSolidValueInfo();
										String propertyValueInfoType = propertyValueInfo.getValueInfoType();
										if(HAPConstant.STRINGABLE_VALUESTRUCTURE_ENTITY.equals(propertyValueInfoType)){
											literateType = new HAPLiterateType(HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, ((HAPValueInfoEntity)propertyValueInfo).getClassName());
										}
										else if(HAPConstant.STRINGABLE_VALUESTRUCTURE_ATOMIC.equals(propertyValueInfoType)){
											literateType = ((HAPValueInfoAtomic)propertyValueInfo).getLiterateType();
										}
									}
									
									columnObject = HAPLiterateManager.getInstance().stringToValue((String)columnValue, literateType);
								}
								setterMethod = obj.getClass().getMethod(setterMethodName, parmClass);
								setterMethod.invoke(obj, columnObject);
							}
						}
						catch(NoSuchMethodException e){
							if(obj instanceof HAPStringableValueEntity){
								HAPValueInfo propertyValueInfo = valueInfo.getChildByPath(column.getProperty()).getSolidValueInfo();
								String propertyValueInfoType = propertyValueInfo.getValueInfoType();
								if(HAPConstant.STRINGABLE_VALUESTRUCTURE_ENTITY.equals(propertyValueInfoType)){
									((HAPStringableValueEntity)obj).updateChild(column.getColumnName(), (HAPStringableValue)columnObject);
								}
								else if(HAPConstant.STRINGABLE_VALUESTRUCTURE_ATOMIC.equals(propertyValueInfoType)){
									((HAPStringableValueEntity)obj).updateAtomicChildStrValue(column.getColumnName(), HAPLiterateManager.getInstance().valueToString(columnObject), literateType.getType(), literateType.getSubType());
								}
							}
						}
					}
				}
				out.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
}
