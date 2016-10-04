package com.nosliw.application;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
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
import com.nosliw.datasource.realtor.HAPDataSourceRealtor;
import com.nosliw.datasource.school.HAPDataSourceSchool;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;

public class Main {

	public static HAPDataTypeManager m_dataTypeMan;
	
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
		
		HAPDataSourceRealtor realtorDataSource = new HAPDataSourceRealtor(m_dataTypeMan);
		
		HAPDataSourceSchool schoolDataSource = new HAPDataSourceSchool(m_dataTypeMan);
		
		HAPData homesData = realtorDataSource.getData();
		HAPData schoolsData = schoolDataSource.getData();

//		String mainExprssion = "?(homesData)?.filter(?(homeFilterExpression)?,?(homeDataName)?)";
//		HAPExpressionData homeFilterExpression = createExpressionData("?(schoolsData)?.each(?(validHomeExpression)?,?(schoolDataName)?,?(out)?)");
//		HAPExpressionData validHomeExpression = createExpressionData("?(schoolData)?.getAttribute(?(geoLocation)?).distance(?(homeData)?.getAttribute(?(geoLocation)?)).longer(?(distanceData)?).not().or(?(valid)?)");

		String mainExprssion = "?(homesData)?.filter(?(homeFilterExpression)?,?(homeDataName)?)";
		HAPExpressionData homeFilterExpression = createExpressionData("?(schoolsData)?.each(?(validHomeExpression)?,?(schoolDataName)?,?(out)?)");
		HAPExpressionData validHomeExpression = createExpressionData("?(schoolData)?.getAttribute(?(geoLocation)?).distance(?(homeData)?.getAttribute(?(geoLocation)?)).longer(?(distanceData)?).not().or(?(valid)?)");
		
		Map<String, HAPData> parmDatas = new LinkedHashMap<String, HAPData>();
		parmDatas.put("homesData", homesData);
		parmDatas.put("schoolsData", schoolsData);
		parmDatas.put("homeFilterExpression", homeFilterExpression);
		parmDatas.put("validHomeExpression", validHomeExpression);

		parmDatas.put("out", HAPDataTypeManager.STRING.createDataByValue("valid"));
		parmDatas.put("homeDataName", HAPDataTypeManager.STRING.createDataByValue("homeData"));
		parmDatas.put("schoolDataName", HAPDataTypeManager.STRING.createDataByValue("schoolData"));
		parmDatas.put("geoLocation", HAPDataTypeManager.STRING.createDataByValue("geoLocation"));
		parmDatas.put("distanceData", m_dataTypeMan.newData(new HAPDataTypeInfo("simple", "distance"), "newKm", new HAPData[]{HAPDataTypeManager.DOUBLE.createDataByValue(20)}, null));
		
		parmDatas.put("allTrue", HAPDataTypeManager.BOOLEAN.createDataByValue(true));
		
		
		HAPExpressionInfo expressionInfo = new HAPExpressionInfo(mainExprssion, null, null);
		HAPExpression expression = new HAPExpression(expressionInfo, m_dataTypeMan);
		HAPOperationContext opContext = new HAPOperationContext();
		opContext.setVariables(parmDatas);
		HAPListData outData = (HAPListData)expression.execute(parmDatas, null, opContext);
		output(outData);
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
	
}
