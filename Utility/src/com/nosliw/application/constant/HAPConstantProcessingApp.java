package com.nosliw.application.constant;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.interpolate.HAPInterpolateUtility;
import com.nosliw.common.interpolate.HAPPatternProcessorDocVariable;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.pattern.HAPPatternManager;
import com.nosliw.common.strvalue.basic.HAPStringResolveUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPXMLUtility;

/*
 * Utiltiy application that create constant java class and javascript file according to the constant definitions in configure file
 * With this application, all the constants in other application can be defined in one place and used in multiple places : server side and client side
 */
public class HAPConstantProcessingApp extends HAPConfigurableImp{
	protected HAPConstantProcessingApp() {
		super("constantprocess.properties", null);
	}

	public void process(){
		try{
			InputStream configureStream = HAPFileUtility.getInputStreamOnClassPath(HAPConstantProcessingApp.class, "constant.xml");
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(configureStream);

			Element rootEle = doc.getDocumentElement();
			String jsPath = HAPInterpolateUtility.interpolateByConfigure(rootEle.getAttribute("jsPath"), this.getConfiguration()); 
			String jsAttributeFile = rootEle.getAttribute("jsAttributeFile");
			String jsConstantFile = rootEle.getAttribute("jsConstantFile");
			
			Map<String, String> attributeJsonMap = new LinkedHashMap<String, String>();
			Map<String, Class> attributeTypesJsonMap = new LinkedHashMap<String, Class>();
			Element[] attrsEles = HAPXMLUtility.getMultiChildElementByName(rootEle, "attributes");
			for(Element attrsEle : attrsEles){
				List<HAPConstantInfo> attrConsInfos = new ArrayList<HAPConstantInfo>();
				Element[] attrEles = HAPXMLUtility.getMultiChildElementByName(attrsEle, "definition");
				for(Element attrEle : attrEles){
					HAPConstantInfo attrInfo = new HAPConstantInfo(attrEle.getAttribute("name"), attrEle.getAttribute("value"), attrEle.getAttribute("type"), attrEle.getAttribute("skip"));
					attrConsInfos.add(attrInfo);
					processJSItem(attrInfo, attributeJsonMap, attributeTypesJsonMap);
				}
				processJavaConstant(attrConsInfos, attrsEle.getAttribute("packagename"), attrsEle.getAttribute("classname"), HAPInterpolateUtility.interpolateByConfigure(attrsEle.getAttribute("filepath"), this.getConfiguration())); 
			}
			//attribute json structure
			String attrJson = HAPJsonUtility.getMapJson(attributeJsonMap, attributeTypesJsonMap);
			processJsonFile(attrJson, jsPath, jsAttributeFile);
			
			Map<String, String> constantJsonMap = new LinkedHashMap<String, String>();
			Map<String, Class> constantTypesJsonMap = new LinkedHashMap<String, Class>();
			List<HAPConstantInfo> consConsInfos = new ArrayList<HAPConstantInfo>();
			Element[] constantsEles = HAPXMLUtility.getMultiChildElementByName(rootEle, "constants");
			for(Element constantsEle : constantsEles){
				Element[] constantEles = HAPXMLUtility.getMultiChildElementByName(constantsEle, "definition");
				for(Element constantEle : constantEles){
					HAPConstantInfo consInfo = new HAPConstantInfo(constantEle.getAttribute("name"), constantEle.getAttribute("value"), constantEle.getAttribute("type"), constantEle.getAttribute("skip"));
					consConsInfos.add(consInfo);
					processJSItem(consInfo, constantJsonMap, constantTypesJsonMap);
				}
				processJavaConstant(consConsInfos, constantsEle.getAttribute("packagename"), constantsEle.getAttribute("classname"), HAPInterpolateUtility.interpolateByConfigure(constantsEle.getAttribute("filepath"), this.getConfiguration())); 
			}
			//constant json structure
			String constantJson = HAPJsonUtility.getMapJson(constantJsonMap, constantTypesJsonMap);
			processJsonFile(constantJson, jsPath, jsConstantFile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String getFilePath(String filePath, String projectsPath){
		if(filePath.startsWith("./")){
			filePath = projectsPath + filePath.substring(1);
		}
		return filePath;
	}
	
	private void processJsonFile(String jsonContent, String filePath, String fileName){
		String content = "var " + fileName + "=\n" + HAPJsonUtility.formatJson(jsonContent) + ";";
		HAPFileUtility.writeFile(filePath+"/"+fileName+".js", content);
		
	}
	
	/*
	 * process constant definition and create jason map and type map
	 */
	private void processJSItem(HAPConstantInfo info, Map<String, String> valueMap, Map<String, Class> datatypeMap){
		if("js".equals(info.skip))  return;
		
		if(HAPBasicUtility.isStringEmpty(info.type)){
			valueMap.put(info.name, info.value);
		}
		else if(info.type.equals("string")){
			valueMap.put(info.name, info.value);
			datatypeMap.put(info.name, String.class);
		}
		else if(info.type.equals("int")){
			valueMap.put(info.name, info.value);
			datatypeMap.put(info.name, Integer.class);
		}
		else if(info.type.equals("space")){
		}
	}
	
	/*
	 * process a list of constant and save them to java class
	 */
	private String processJavaConstant(List<HAPConstantInfo> values, String packageName, String className, String filePath){

		Map<String, String> attrJavaTemplateParms = new LinkedHashMap<String, String>();
		
		attrJavaTemplateParms.put("packagename", packageName);
		attrJavaTemplateParms.put("classname", className);
		
		StringBuffer attrDefs = new StringBuffer("\n");
		for(HAPConstantInfo value : values){
			String itemString = getJavaItem(value);
			attrDefs.append(itemString);
		}
		attrJavaTemplateParms.put("attrdef", attrDefs.toString());

		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPConstantProcessingApp.class, "ConstantJava.temp");
		String attrJavaContent = HAPStringTemplateUtil.getStringValue(javaTemplateStream, attrJavaTemplateParms);
		
		HAPFileUtility.writeFile(filePath+"/"+className+".java", attrJavaContent);
		
		return attrJavaContent;
	}

	/*
	 * process individual constant def
	 */
	private String getJavaItem(HAPConstantInfo info){
		String out = "";
		if(HAPBasicUtility.isStringEmpty(info.type) || info.type.equals("string")){
			out = "		public static final String " + info.name + " = \"" + info.value + "\";\n";
		}
		else if(info.type.equals("int")){
			out = "		public static final int " + info.name + " = " + info.value + ";\n";
		}
		else if(info.type.equals("space")){
			out = "\n\n\n";
		}
		return out;
	}
	
	public static void main(String[] args){
		HAPConstantProcessingApp app = new HAPConstantProcessingApp();
		app.process();
	}
}
