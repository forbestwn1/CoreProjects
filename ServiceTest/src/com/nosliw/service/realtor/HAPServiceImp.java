package com.nosliw.service.realtor;

import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.service.definition.HAPExecutableService;
import com.nosliw.data.core.service.definition.HAPResultService;
import com.nosliw.data.core.service.definition.HAPUtilityService;

public class HAPServiceImp implements HAPExecutableService{

	public static void main(String[] argus){
		HAPServiceImp dataSource = new HAPServiceImp();
		HAPResultService result = dataSource.execute(null);
		System.out.println(result);
	}
	
	private JSONArray m_data;
	
	@Override
	public HAPResultService execute(Map<String, HAPData> parms){
		HAPData outData = null;
		try{
			if(true) {
				InputStream inputStream = HAPUtilityFile.getInputStreamOnClassPath(getClass(), "homesArray.js");
				String content =  HAPUtilityFile.readFile(inputStream);
				this.m_data = new JSONArray(content);
			}
			outData = querydData(this.m_data, parms);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		output.put("output", outData);
		return HAPUtilityService.generateSuccessResult(output);

	}
	
	private HAPData querydData(JSONArray jsonHomesData, Map<String, HAPData> parms) throws JSONException{
		Set<String> buildTypeParm = new HashSet<String>();
		JSONArray buildTypeParJson = (JSONArray)parms.get("buildingType").getValue();
		for(int i=0; i<buildTypeParJson.length(); i++) {
			buildTypeParm.add(buildTypeParJson.getJSONObject(i).getJSONObject("value").getString("value"));
		}
		
		int bedroomsParm = ((Integer)parms.get("bedrooms").getValue()).intValue();
		double fromPriceParm = ((JSONObject)parms.get("fromPrice").getValue()).getDouble("price"); 
				
//				((Double)parms.get("fromPrice").getValue()).doubleValue();
		double toPriceParm = ((JSONObject)parms.get("toPrice").getValue()).getDouble("price");
		
		JSONArray homeArrayData = new JSONArray();
		
		for(int i=0; i<jsonHomesData.length(); i++){
			try{
				JSONObject outHome = new JSONObject();
				
				JSONObject jsonHomeData = jsonHomesData.optJSONObject(i);
				
				JSONObject jsonProperty = jsonHomeData.optJSONObject("Property");
				JSONObject jsonAddress = jsonProperty.optJSONObject("Address");
				JSONObject jsonBuilding = jsonHomeData.optJSONObject("Building");


				String priceStr = jsonProperty.optString("Price").trim();
				priceStr = priceStr.substring(1);
				int priceIndex = priceStr.indexOf(",");
				while(priceIndex!=-1){
					priceStr = priceStr.substring(0, priceIndex) + priceStr.substring(priceIndex+1);
					priceIndex = priceStr.indexOf(",");
				}
				double price = Double.valueOf(priceStr).doubleValue();
				JSONObject priceData = new JSONObject();
				priceData.put("price", price);
				priceData.put("currency", "$");
				outHome.put("price", createJSONData("test.price;1.0.0", priceData));
				
				//check price
				if(price<fromPriceParm || price>toPriceParm)  continue;
				
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
				outHome.put("bedroom1", createJSONData("test.string;1.0.0", bedroom1));
				outHome.put("bedroom2", createJSONData("test.string;1.0.0", bedroom2));

				//check bedroom
				if(Integer.valueOf(bedroom1.trim()) < bedroomsParm)  continue;
				
				String buildingType = jsonBuilding.optString("Type");
				JSONObject buildingTypeOptionJson = new JSONObject();
				buildingTypeOptionJson.put("value", buildingType);
				buildingTypeOptionJson.put("optionsId", "buildingType");
				
				outHome.put("buildingType", createJSONData("test.optiosn;1.0.0", buildingType));
				if(!buildTypeParm.contains(buildingType))  continue;
				
				JSONObject outGeoValue = new JSONObject();
				outGeoValue.put("latitude", jsonAddress.optDouble("Latitude"));
				outGeoValue.put("longitude", jsonAddress.optDouble("Longitude"));
				outHome.put("geo", createJSONData("test.geo;1.0.0", outGeoValue));
				
				String address = jsonBuilding.optString("AddressText");
				outHome.put("address", createJSONData("test.string;1.0.0", address));

				String bathrooms = jsonBuilding.optString("BathroomTotal");
				outHome.put("bathrooms", createJSONData("test.string;1.0.0", bathrooms));

				outHome.put("MlsNumber", createJSONData("test.string;1.0.0", jsonHomeData.optString("MlsNumber")));
				outHome.put("name", createJSONData("test.string;1.0.0", jsonHomeData.optString("MlsNumber")));
				
				outHome.put("url", createJSONData("test.url;1.0.0", "https://www.realtor.ca/"+jsonHomeData.optString("RelativeDetailsURL")));
				
				homeArrayData.put(createJSONData("test.map;1.0.0", outHome));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}			
		HAPDataWrapper out = new HAPDataWrapper(new HAPDataTypeId("test.array;1.0.0"), homeArrayData);
		return out;
	}

	private static JSONObject createJSONData(String dataTypeId, Object value) throws JSONException{
		JSONObject out = new JSONObject();
		out.put(HAPData.VALUE, value);
		out.put(HAPData.DATATYPEID, dataTypeId);
		return out;
	}
}
