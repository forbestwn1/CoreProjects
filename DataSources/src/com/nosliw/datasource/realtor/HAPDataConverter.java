package com.nosliw.datasource.realtor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.basic.list.HAPListData;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.datasource.HAPDataSourceUtility;

public class HAPDataConverter {

/*
	BuildingTypeId  -   ConstructionStyleId:
	  	1  : house
	  		3: detached
	  		5: semi-detached
	  		1: attached
	  		9: link
	    17 : apartment
	    16 : townhouse
 
 
 Row \/ Townhouse
 House
 */
	
	public static HAPListData buildData(JSONArray jsonHomesData, HAPDataTypeManager dataTypeMan){
		HAPDataTypeInfo listDataTypeInfo = new HAPDataTypeInfo("simple", "list");
		HAPDataType listDataType = dataTypeMan.getDataType(listDataTypeInfo);
		HAPListData out = (HAPListData)listDataType.newData(null, null).getData();

		for(int i=0; i<jsonHomesData.length(); i++){
			JSONObject jsonHomeData = jsonHomesData.optJSONObject(i);
			
			JSONObject jsonProperty = jsonHomeData.optJSONObject("Property");
			JSONObject jsonAddress = jsonProperty.optJSONObject("Address");
			JSONObject jsonBuilding = jsonHomeData.optJSONObject("Building");
			
			HAPData[] parms1 = {
					HAPDataTypeManager.DOUBLE.createDataByValue(jsonAddress.optDouble("Latitude")),
					HAPDataTypeManager.DOUBLE.createDataByValue(jsonAddress.optDouble("Longitude")),
			};
			HAPData geoLocationData = dataTypeMan.newData(new HAPDataTypeInfo("simple", "geoLocation"), parms1, null);

			String bedroomStr = jsonBuilding.optString("Bedrooms").trim();
			int index = bedroomStr.indexOf("+");
			String bedroom1 = "0";
			String bedroom2 = "0";
			if(index==-1){
				bedroom1 = bedroomStr;
			}
			else{
				bedroom1 = bedroomStr.substring(0, index);
				bedroom2 = bedroomStr.substring(index+1);
			}

			String bathrooms = jsonBuilding.optString("BathroomTotal");

			String priceStr = jsonProperty.optString("Price").trim();
			priceStr = priceStr.substring(1);
			int priceIndex = priceStr.indexOf(",");
			while(priceIndex!=-1){
				priceStr = priceStr.substring(0, priceIndex) + priceStr.substring(priceIndex+1);
				priceIndex = priceStr.indexOf(",");
			}
			int price = Integer.valueOf(priceStr).intValue();

			HAPDataTypeInfo entityDataTypeInfo = new HAPDataTypeInfo("simple", "entity");
			HAPDataType entityDataType = dataTypeMan.getDataType(entityDataTypeInfo);
			HAPData entityData = (HAPData)entityDataType.newData(null, null).getData();
			
			HAPDataSourceUtility.setEntityAttribute(entityData, "geoLocation", geoLocationData, dataTypeMan);
			HAPDataSourceUtility.setEntityAttribute(entityData, "MlsNumber", HAPDataTypeManager.STRING.createDataByValue(jsonHomeData.optString("MlsNumber")), dataTypeMan);
			HAPDataSourceUtility.setEntityAttribute(entityData, "buildingType", HAPDataTypeManager.STRING.createDataByValue(jsonBuilding.optString("Type")), dataTypeMan);
			HAPDataSourceUtility.setEntityAttribute(entityData, "bedroom1", HAPDataTypeManager.STRING.createDataByValue(bedroom1), dataTypeMan);
			HAPDataSourceUtility.setEntityAttribute(entityData, "bedroom2", HAPDataTypeManager.STRING.createDataByValue(bedroom2), dataTypeMan);
			HAPDataSourceUtility.setEntityAttribute(entityData, "bathrooms", HAPDataTypeManager.STRING.createDataByValue(bathrooms), dataTypeMan);
			HAPDataSourceUtility.setEntityAttribute(entityData, "price", HAPDataTypeManager.INTEGER.createDataByValue(price), dataTypeMan);
			
			List<HAPData> parms10 = new ArrayList<HAPData>();
			parms10.add(entityData);
			out.operate("add", parms10, null);
		}			
		return out;
	}
	
}
