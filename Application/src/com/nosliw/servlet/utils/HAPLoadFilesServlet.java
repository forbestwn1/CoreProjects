package com.nosliw.servlet.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPSystemUtility;
import com.nosliw.servlet.HAPBaseServlet;
import com.nosliw.servlet.HAPRequestInfo;

public class HAPLoadFilesServlet extends HAPBaseServlet{

	private static final long serialVersionUID = -1256294161988262894L;

	@Override
	public void doGet (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		
		HAPRequestInfo requestInfo = new HAPRequestInfo(request);
		HAPServiceData serviceData = null;
		try {
			List<String> tempNames = new ArrayList<String>();
			JSONObject parmsObj = new JSONObject(requestInfo.getParms());
			JSONArray fileFoldersArray = parmsObj.optJSONArray("fileFolders");
			for(int i=0; i<fileFoldersArray.length(); i++) {
				String folder = fileFoldersArray.optString(i);
				List<File> files = new ArrayList(HAPFileUtility.getAllFiles(folder));
				Collections.sort(files, new Comparator() {
					@Override
					public int compare(Object arg0, Object arg1) {
						File f0 = (File)arg0;
						File f1 = (File)arg1;
						return f0.getName().compareTo(f1.getName());
					}
				});
				for(File file : files) {
					String fileName = file.getName();
					fileName = fileName.replace(";", "_");
//					fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
					HAPFileUtility.writeFile(HAPSystemUtility.getJSTempFolder()+"files/"+fileName, HAPFileUtility.readFile(file));
					tempNames.add("temp/files/"+fileName);
				}
			}
			serviceData = HAPServiceData.createSuccessData(tempNames);
		}
		catch(Exception e) {
			serviceData = HAPServiceData.createFailureData(e, "Exceptione during load library service request!!!!");
		}
		this.printContent(serviceData, response);
	}
}
