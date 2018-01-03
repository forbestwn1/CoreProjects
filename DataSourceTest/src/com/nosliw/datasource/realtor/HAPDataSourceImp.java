package com.nosliw.datasource.realtor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.datasource.HAPDataSource;

public class HAPDataSourceImp implements HAPDataSource{

	public static void main(String[] argus){
		HAPDataSourceImp dataSource = new HAPDataSourceImp();
		HAPData data = dataSource.getData(null);
		System.out.println(data);
	}
	
	@Override
	public HAPData getData(Map<String, HAPData> parms){
		HAPData out = null;
		try{
			InputStream elementDataInputStream = new FileInputStream(new File("homesArray.js"));
			String content = HAPFileUtility.readFile(elementDataInputStream);
			out = buildData(new JSONArray(content));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	private HAPData buildData(JSONArray jsonHomesData) throws JSONException{
		JSONArray homeArrayData = new JSONArray();
		
		for(int i=0; i<jsonHomesData.length(); i++){
			try{
				JSONObject outHome = new JSONObject();
				
				JSONObject jsonHomeData = jsonHomesData.optJSONObject(i);
				
				JSONObject jsonProperty = jsonHomeData.optJSONObject("Property");
				JSONObject jsonAddress = jsonProperty.optJSONObject("Address");
				JSONObject jsonBuilding = jsonHomeData.optJSONObject("Building");

				
				JSONObject outGeoValue = new JSONObject();
				outGeoValue.put("latitude", jsonAddress.optDouble("Latitude"));
				outGeoValue.put("longitude", jsonAddress.optDouble("Longitude"));
				outHome.put("geo", createJSONData("test.geo;1.0.0", outGeoValue));
				
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

				String bathrooms = jsonBuilding.optString("BathroomTotal");
				outHome.put("bathrooms", createJSONData("test.string;1.0.0", bathrooms));

				String priceStr = jsonProperty.optString("Price").trim();
				priceStr = priceStr.substring(1);
				int priceIndex = priceStr.indexOf(",");
				while(priceIndex!=-1){
					priceStr = priceStr.substring(0, priceIndex) + priceStr.substring(priceIndex+1);
					priceIndex = priceStr.indexOf(",");
				}
				double price = Double.valueOf(priceStr).doubleValue();
				outHome.put("bathrooms", createJSONData("test.price;1.0.0", price));

				outHome.put("MlsNumber", createJSONData("test.string;1.0.0", jsonHomeData.optString("MlsNumber")));
				outHome.put("buildingType", createJSONData("test.string;1.0.0", jsonBuilding.optString("Type")));
				
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
