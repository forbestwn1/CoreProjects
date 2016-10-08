package com.nosliw.datasource.school;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.basic.list.HAPListData;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.data.library.entity.v100.HAPEntityData;
import com.nosliw.datasource.HAPDataSource;

public class HAPDataSourceSchool implements HAPDataSource{

	public static int INDEX_ID = 0;
	public static int INDEX_LAT = 1;
	public static int INDEX_LON = 2;
	public static int INDEX_URL = 3;
	public static int INDEX_NAME = 4;
	public static int INDEX_TYPE = 5;
	public static int INDEX_SCORE = 6;
	public static int INDEX_COLOR = 8;

	private HAPDataTypeManager m_dataTypeMan;
	
	private JSONArray m_originalData;
	
	public HAPDataSourceSchool(HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
	}
	
	@Override
	public HAPData getData(){

		HAPData out = null;
		
		try{
			InputStream elementDataInputStream = new FileInputStream(new File("elementSchoolArray.js"));
//			InputStream elementDataInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPDataSourceSchool.class, "element.json");
			
			String content = HAPFileUtility.readFile(elementDataInputStream);

			HAPDataTypeInfo listDataTypeInfo = new HAPDataTypeInfo("simple", "list");
			HAPDataType listDataType = this.m_dataTypeMan.getDataType(listDataTypeInfo);
			out = (HAPData)listDataType.newData(null, null).getData();

			m_originalData = new JSONArray(content);
			for(int i=0; i<m_originalData.length(); i++){
				JSONArray jsonSchoolData = m_originalData.optJSONArray(i);
				
				HAPData[] parms1 = {
						HAPDataTypeManager.DOUBLE.createDataByValue(jsonSchoolData.getDouble(INDEX_LAT)),
						HAPDataTypeManager.DOUBLE.createDataByValue(jsonSchoolData.getDouble(INDEX_LON)),
				};
				HAPData geoLocationData = this.m_dataTypeMan.newData(new HAPDataTypeInfo("simple", "geoLocation"), parms1, null);

				HAPData idData = HAPDataTypeManager.STRING.createDataByValue(jsonSchoolData.getString(INDEX_ID));
				
				HAPData typeData = HAPDataTypeManager.STRING.createDataByValue(jsonSchoolData.getString(INDEX_TYPE));
				
				HAPData nameData = HAPDataTypeManager.STRING.createDataByValue(jsonSchoolData.getString(INDEX_NAME));

				HAPData scoreData = HAPDataTypeManager.DOUBLE.createDataByValue(jsonSchoolData.getDouble(INDEX_SCORE));

				HAPData colorData = HAPDataTypeManager.STRING.createDataByValue(jsonSchoolData.getString(INDEX_COLOR));

				
				HAPDataTypeInfo entityDataTypeInfo = new HAPDataTypeInfo("simple", "entity");
				HAPDataType entityDataType = this.m_dataTypeMan.getDataType(entityDataTypeInfo);
				HAPData entityData = (HAPData)entityDataType.newData(null, null).getData();
				
				HAPData[] parms2 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("geoLocation"),
						geoLocationData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms2, null).getData();

				HAPData[] parms3 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("name"),
						nameData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms3, null).getData();
				
				HAPData[] parms4 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("type"),
						typeData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms4, null).getData();
				
				HAPData[] parms5 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("score"),
						scoreData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms5, null).getData();
				

				HAPData[] parms6 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("color"),
						colorData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms6, null).getData();

				HAPData[] parms7 = {
						entityData,
						HAPDataTypeManager.STRING.createDataByValue("id"),
						idData
				};
				entityData = (HAPData)entityDataType.operate("setAttribute", parms7, null).getData();
				
				HAPData[] parms10 = {out, entityData	};
				out = (HAPData)listDataType.operate("add", parms10, null).getData();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return out;
	}

	public String updatedData(HAPListData listData){
		Set<String> idNos = new HashSet<String>();
		for(int i=0; i<listData.getSize(); i++){
			HAPStringData idData = (HAPStringData)((HAPEntityData)listData.getData(i)).getAttribute("id");
			idNos.add(idData.getValue());
		}
		
		JSONArray outJsonArray = new JSONArray();
		for(int i=0; i<this.m_originalData.length(); i++){
			JSONArray jsonSchool = this.m_originalData.optJSONArray(i);
			String jsonId = jsonSchool.optString(0); 
			if(idNos.contains(jsonId)){
				outJsonArray.put(jsonSchool);
			}
		}
		
		String out = outJsonArray.toString();
		return HAPJsonUtility.formatJson(out);
	}
}
