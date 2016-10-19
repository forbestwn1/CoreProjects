package com.nosliw.datasource.realtor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.basic.list.HAPListData;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.library.entity.v100.HAPEntityData;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.datasource.HAPDataSource;
import com.nosliw.datasource.school.HAPDataSourceSchool;

public class HAPDataSourceRealtorMock implements HAPDataSource{

	private HAPDataTypeManager m_dataTypeMan;
	
	private JSONArray m_originalData = null;
	
	public HAPDataSourceRealtorMock(HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
	}
	
	@Override
	public HAPData getData() {
		HAPData out = null;
		
		try{
			InputStream elementDataInputStream = new FileInputStream(new File("homesArray.js"));
//			InputStream elementDataInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPDataSourceRealtorMock.class, "homes.json");
			
			String content = HAPFileUtility.readFile(elementDataInputStream);
			this.m_originalData = new JSONArray(content);

			out = HAPDataConverter.buildData(this.m_originalData, m_dataTypeMan);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	public String updatedData(HAPListData listData){
		Set<String> mlsNos = new HashSet<String>();
		for(int i=0; i<listData.getSize(); i++){
			HAPStringData mlsNumberData = (HAPStringData)((HAPEntityData)listData.getData(i)).getAttribute("MlsNumber");
			mlsNos.add(mlsNumberData.getValue());
		}
		
		JSONArray outJsonArray = new JSONArray();
		for(int i=0; i<this.m_originalData.length(); i++){
			JSONObject jsonHome = this.m_originalData.optJSONObject(i);
			String jsonMlsNo = jsonHome.optString("MlsNumber"); 
			if(mlsNos.contains(jsonMlsNo)){
				outJsonArray.put(jsonHome);
			}
		}
		
		String out = outJsonArray.toString();
		return HAPJsonUtility.formatJson(out);
	}
	
}
