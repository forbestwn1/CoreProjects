package com.nosliw.app.instance;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPXMLUtility;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data1.HAPDataTypeManager;

public class HAPApplicationDataTypeManager extends HAPDataTypeManager{

	public HAPApplicationDataTypeManager(HAPConfigure configure){
		super(configure);
		String[] libs = configure.getArrayValue("libs");
		for(String lib : libs){
			this.readLib(lib);
		}
	}

	private void readLib(String libPath){
		DocumentBuilder DOMbuilder = null;
		DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
		try {
			DOMbuilder = DOMfactory.newDocumentBuilder();
		} catch (Exception e) {    
			e.printStackTrace();
		}

		try {
			InputStream stream = new FileInputStream(new File(libPath));
			Document doc = DOMbuilder.parse(stream);
			Element[] eles = HAPXMLUtility.getMultiChildElementByName(doc.getDocumentElement(), "datatype");
			for(Element ele : eles){
				String description = ele.getAttribute("description");

				String parent = ele.getAttribute("parent");
				HAPDataTypeInfoWithVersion parentDataTypeInfo = null;
				if(HAPBasicUtility.isStringNotEmpty(parent)){
					parentDataTypeInfo = HAPDataTypeInfoWithVersion.parseDataTypeInfo(parent);
				}
				
				String categary = ele.getAttribute("categary");
				String type = ele.getAttribute("type");
				
				HAPDataType oldDataType = null;
				Element[] versionEles = HAPXMLUtility.getMultiChildElementByName(ele, "version");
				for(Element versionEle : versionEles){
					String factoryClassName = versionEle.getAttribute("factory");
					int versionNum = Integer.valueOf(versionEle.getAttribute("number"));
					
					Class factoryClass = Class.forName(factoryClassName);
					Method method = factoryClass.getMethod("createDataType", 
															HAPDataTypeInfoWithVersion.class,
															HAPDataType.class,
															HAPDataTypeInfoWithVersion.class,
															HAPConfigure.class,
															String.class,
															HAPDataTypeManager.class);
					HAPDataType dataType = (HAPDataType)method.invoke(null,
											new HAPDataTypeInfoWithVersion(categary, type, versionNum), 
											oldDataType, 
											parentDataTypeInfo, 
											this.readConfigure(versionEle),
											description,
											this);
					oldDataType = dataType;
				}
				
				this.registerDataType(oldDataType);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private HAPConfigure readConfigure(Element versionEle){
		HAPConfigure out = null;
		Element parmsEle = HAPXMLUtility.getFirstChildElementByName(versionEle, "parms");
		if(parmsEle!=null){
			Map<String, Object> parmMap = new LinkedHashMap<String, Object>();
			Element[] parmEles = HAPXMLUtility.getMultiChildElementByName(versionEle, "parm");
			for(Element parmEle : parmEles){
				parmMap.put(parmEle.getAttribute("name"), parmEle.getAttribute("value"));
			}
			out = new HAPConfigureImp(parmMap);
		}
		return out;
	}
}
