package com.nosliw.application;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.basic.list.HAPList;
import com.nosliw.data.basic.list.HAPListData;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;
import com.nosliw.data.library.distance.v100.HAPDistance;
import com.nosliw.data.library.entity.v100.HAPEntity;
import com.nosliw.data.library.entity.v100.HAPEntityData;
import com.nosliw.data.library.expression.v100.HAPExpressionData;
import com.nosliw.data.library.expression.v100.HAPExpressionType;
import com.nosliw.data.library.geolocationi.v100.HAPGeoLocation;
import com.nosliw.data.library.lengthunit.v100.HAPLengthUnit;
import com.nosliw.datasource.realtor.HAPDataSourceRealtorMock;
import com.nosliw.datasource.school.HAPDataSourceSchool;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;

public class Main {

	public static HAPDataTypeManager m_dataTypeMan;
	
	public static HAPListData m_validSchools;
	public static HAPListData m_validHomes;
	
	public static void main(String[] argus){
		m_dataTypeMan = new HAPDataTypeManager(null);

		HAPDataTypeInfoWithVersion lengthUnitDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, "lengthUnit");
		m_dataTypeMan.registerDataType(new HAPLengthUnit(lengthUnitDataTypeInfo, null, null, null, "", m_dataTypeMan));
		
		HAPDataTypeInfoWithVersion expressionDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, "expression");
		m_dataTypeMan.registerDataType(new HAPExpressionType(expressionDataTypeInfo, null, null, null, "", m_dataTypeMan));

		HAPDataTypeInfoWithVersion distanceDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, "distance");
		m_dataTypeMan.registerDataType(new HAPDistance(distanceDataTypeInfo, null, null, null, "", m_dataTypeMan));
		
		HAPDataTypeInfoWithVersion geoLocationDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, "geoLocation");
		m_dataTypeMan.registerDataType(new HAPGeoLocation(geoLocationDataTypeInfo, null, null, null, "", m_dataTypeMan));
		
		HAPDataTypeInfoWithVersion entityDataTypeInfo = new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_SIMPLE, "entity");
		m_dataTypeMan.registerDataType(new HAPEntity(entityDataTypeInfo, null, null, null, "", m_dataTypeMan));

		m_dataTypeMan.init();
		
		String schoolsContent = getValidSchools();
		HAPFileUtility.writeFile("myschool.js", "var slMapData="+schoolsContent+";");

		String homesContent = getValidHomes(m_validSchools);
		HAPFileUtility.writeFile("C:\\Users\\ewaniwa\\Desktop\\MyWork\\CoreProjects\\DataSources\\myhomes.js", "var homesData="+homesContent+";");
	}
	
	private static String getValidSchools(){
		HAPDataSourceSchool schoolDataSource = new HAPDataSourceSchool(m_dataTypeMan);
		HAPData schoolsData = schoolDataSource.getData();

		double score = 8.00;
		
		String[] schoolColors = {"green"};
		String[] schoolTypes = {"Public", "Catholic"};
		
		String mainExprssion = "?(schoolsData)?.filter(?(schoolFilterExpression)?,&(schoolData)&)";
		
		String scoreExpression = "?(schoolData)?.getAttribute(&(score)&).largerThan(?(score)?)";
		
		String colorExpression = "?(schoolColors)?.contains(?(schoolData)?.getAttribute(&(color)&)))";

		String schoolTypeExpression = "?(schoolTypes)?.contains(?(schoolData)?.getAttribute(&(type)&)))";
		
		
		String[] filters = {scoreExpression, colorExpression, schoolTypeExpression};
		StringBuffer filterExpression = new StringBuffer();
		buildAndExpression(filterExpression, filters, 0);
		
		HAPExpressionData schoolFilterExpression = createExpressionData(filterExpression.toString());
		
		Map<String, HAPData> parmDatas = new LinkedHashMap<String, HAPData>();
		parmDatas.put("schoolsData", schoolsData);
		parmDatas.put("schoolFilterExpression", schoolFilterExpression);

		parmDatas.put("score", HAPDataTypeManager.DOUBLE.createDataByValue(score));
		parmDatas.put("schoolColors", createListDataByStringArray(schoolColors));
		parmDatas.put("schoolTypes", createListDataByStringArray(schoolTypes));
		
		HAPExpressionInfo expressionInfo = new HAPExpressionInfo(mainExprssion, null, null);
		HAPExpression expression = new HAPExpression(expressionInfo, m_dataTypeMan);
		HAPOperationContext opContext = new HAPOperationContext();
		opContext.setVariables(parmDatas);
		m_validSchools = (HAPListData)expression.execute(parmDatas, null, opContext);
		
		String out = schoolDataSource.updatedData(m_validSchools);
		return out;
	}
	
	private static String getValidHomes(HAPListData schoolsData){
		HAPDataSourceRealtorMock realtorDataSource = new HAPDataSourceRealtorMock(m_dataTypeMan);
		HAPData homesData = realtorDataSource.getData();

		String mainExprssion = "?(homesData)?.filter(?(basicHomeFilterExpression)?,&(homeData)&).filter(?(homeFilterExpression)?,&(homeData)&)";
		HAPExpressionData homeFilterExpression = createExpressionData("?(schoolsData)?.each(?(validHomeExpression)?,&(schoolData)&,&(valid)&)");
		HAPExpressionData validHomeExpression = createExpressionData("?(schoolData)?.getAttribute(&(geoLocation)&).distance(?(homeData)?.getAttribute(&(geoLocation)&)).longer(?(distanceData)?).not().or(?(valid)?)");
		
		String[] buildingTypes = {"House", "Row / Townhouse"};
		String[] bedroomsNos = {"4", "5"};

		String bedroomFilterExpression = "?(bedroomsNos)?.contains(?(homeData)?.getAttribute(&(bedroom1)&)))";
		String buildingTypeFilterExpression = "?(buildingTypes)?.contains(?(homeData)?.getAttribute(&(buildingType)&)))";
		HAPExpressionData basicHomeFilterExpression = createExpressionData(bedroomFilterExpression);
		
		Map<String, HAPData> parmDatas = new LinkedHashMap<String, HAPData>();
		parmDatas.put("homesData", homesData);
		parmDatas.put("schoolsData", schoolsData);
		parmDatas.put("homeFilterExpression", homeFilterExpression);
		parmDatas.put("validHomeExpression", validHomeExpression);
		parmDatas.put("basicHomeFilterExpression", basicHomeFilterExpression);

		parmDatas.put("distanceData", m_dataTypeMan.newData(new HAPDataTypeInfo("simple", "distance"), "newKm", new HAPData[]{HAPDataTypeManager.DOUBLE.createDataByValue(1.5)}, null));
		
		parmDatas.put("allTrue", HAPDataTypeManager.BOOLEAN.createDataByValue(true));

		parmDatas.put("buildingTypes", createListDataByStringArray(buildingTypes));
		parmDatas.put("bedroomsNos", createListDataByStringArray(bedroomsNos));
		
		
		HAPExpressionInfo expressionInfo = new HAPExpressionInfo(mainExprssion, null, null);
		HAPExpression expression = new HAPExpression(expressionInfo, m_dataTypeMan);
		HAPOperationContext opContext = new HAPOperationContext();
		opContext.setVariables(parmDatas);
		HAPListData outData = (HAPListData)expression.execute(parmDatas, null, opContext);
		output(outData);

		String homesOut = realtorDataSource.updatedData(outData);
		return homesOut;
	}

	
	private static void buildAndExpression(StringBuffer out, String[] expressions, int i){
		if(i<expressions.length-1){
			out.append(expressions[i]+".and(");
			i++;
			buildAndExpression(out, expressions, i);
		}
		else{
			out.append(expressions[i]);
		}
		out.append(")");
	}
	
	
	private static void output(HAPListData outData){
		System.out.println(outData.getSize());
		
		for(int i=0; i<outData.getSize(); i++){
			HAPEntityData entityData = (HAPEntityData)outData.getData(i);
			HAPStringData mlsData = (HAPStringData)entityData.getAttribute("MlsNumber");
			System.out.println(mlsData.getValue());
		}
	}
	
	
	private static HAPExpressionData createExpressionData(String expression){
		HAPData[] parm1 = {HAPDataTypeManager.STRING.createDataByValue(expression), HAPDataTypeManager.MAP.newMap()};
		return (HAPExpressionData)m_dataTypeMan.newData(new HAPDataTypeInfo("simple", "expression"),  parm1, null);
	}
	
	private static HAPListData createListDataByStringArray(String[] strs){
		HAPListData out = HAPDataTypeManager.LIST.newList();
		
		for(String str : strs){
			out.addData(HAPDataTypeManager.STRING.createDataByValue(str));
		}
		
		return out;
	}
	
}
