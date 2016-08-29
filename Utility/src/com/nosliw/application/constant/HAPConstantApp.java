package com.nosliw.application.constant;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.constant.HAPConstantGroup;
import com.nosliw.common.constant.HAPConstantManager;
import com.nosliw.common.constant.HAPEntityWithAttributeConstant;
import com.nosliw.common.interpolate.HAPInterpolateUtility;
import com.nosliw.common.pattern.HAPPatternProcessor;
import com.nosliw.common.pattern.HAPPatternProcessorInfo;
import com.nosliw.common.strvalue.basic.HAPStringableValueUtility;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporterXML;
import com.nosliw.common.utils.HAPClassFilter;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPXMLUtility;

public class HAPConstantApp extends HAPConfigurableImp{

	protected HAPConstantApp(){
		super("constantprocess.properties", null);
	}

	public static void main(String[] args){
		HAPConstantProcessingApp app = new HAPConstantProcessingApp();
		app.process();
	}

	public void process(){
		HAPConstantManager constantMan = new HAPConstantManager();
		
		try{
			InputStream configureStream = HAPFileUtility.getInputStreamOnClassPath(HAPConstantProcessingApp.class, "constant.xml");
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(configureStream);

			Element rootEle = doc.getDocumentElement();
			
			String jsPath = HAPInterpolateUtility.interpolateByConfigure(rootEle.getAttribute("jsPath"), this.getConfiguration()); 
			String jsAttributeFile = rootEle.getAttribute("jsAttributeFile");
			String jsConstantFile = rootEle.getAttribute("jsConstantFile");

			Element[] attrsEles = HAPXMLUtility.getMultiChildElementByName(rootEle, "attributes");
			for(Element attrsEle : attrsEles){
				HAPConstantGroup group = (HAPConstantGroup)HAPStringableEntityImporterXML.readRootEntity(attrsEle, "group_attribute", constantMan.getValueInfoManager());
				constantMan.addConstantGroup(group);
			}
			
			Element[] constantsEles = HAPXMLUtility.getMultiChildElementByName(rootEle, "constants");
			for(Element constantsEle : constantsEles){
				HAPConstantGroup group = (HAPConstantGroup)HAPStringableEntityImporterXML.readRootEntity(constantsEle, "group_constant", constantMan.getValueInfoManager());
				constantMan.addConstantGroup(group);
			}
			
			new HAPClassFilter(){
				@Override
				protected void process(Class checkClass, Object data) {
					Set<String> attribute = HAPStringableValueUtility.getExpectedAttributesInEntity(checkClass);
					
				}

				@Override
				protected boolean isValid(Class cls) {		return HAPEntityWithAttributeConstant.class.isAssignableFrom(cls);	}
			}.process(processorInfos);

			
			
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
	
}
