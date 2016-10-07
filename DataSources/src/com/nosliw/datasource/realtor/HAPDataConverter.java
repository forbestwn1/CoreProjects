package com.nosliw.datasource.realtor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.basic.list.HAPListData;
import com.nosliw.data.info.HAPDataTypeInfo;

public class HAPDataConverter {

	public static HAPListData buildData(JSONArray jsonHomesData, HAPDataTypeManager dataTypeMan){
		HAPDataTypeInfo listDataTypeInfo = new HAPDataTypeInfo("simple", "list");
		HAPDataType listDataType = dataTypeMan.getDataType(listDataTypeInfo);
		HAPListData out = (HAPListData)listDataType.newData(null, null).getData();

		for(int i=0; i<jsonHomesData.length(); i++){
			JSONObject jsonHomeData = jsonHomesData.optJSONObject(i);
			
			JSONObject jsonProperty = jsonHomeData.optJSONObject("Property");
			JSONObject jsonAddress = jsonProperty.optJSONObject("Address");
			
			HAPData[] parms1 = {
					HAPDataTypeManager.DOUBLE.createDataByValue(jsonAddress.optDouble("Latitude")),
					HAPDataTypeManager.DOUBLE.createDataByValue(jsonAddress.optDouble("Longitude")),
			};
			HAPData geoLocationData = dataTypeMan.newData(new HAPDataTypeInfo("simple", "geoLocation"), parms1, null);

			String jsonMlsNo = jsonHomeData.optString("MlsNumber"); 
			HAPData mlsData = HAPDataTypeManager.STRING.createDataByValue(jsonMlsNo);
			
			HAPDataTypeInfo entityDataTypeInfo = new HAPDataTypeInfo("simple", "entity");
			HAPDataType entityDataType = dataTypeMan.getDataType(entityDataTypeInfo);
			HAPData entityData = (HAPData)entityDataType.newData(null, null).getData();
			
			HAPData[] parms2 = {
					entityData,
					HAPDataTypeManager.STRING.createDataByValue("geoLocation"),
					geoLocationData
			};
			entityData = (HAPData)entityDataType.operate("setAttribute", parms2, null).getData();

			HAPData[] parms4 = {
					entityData,
					HAPDataTypeManager.STRING.createDataByValue("MlsNumber"),
					mlsData
			};
			entityData = (HAPData)entityDataType.operate("setAttribute", parms4, null).getData();
			
			List<HAPData> parms3 = new ArrayList<HAPData>();
			parms3.add(entityData);
			out.operate("add", parms3, null);
		}			
		return out;
	}
	
}
