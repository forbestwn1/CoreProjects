package com.nosliw.application.constant;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.constant.HAPConstantGroup;
import com.nosliw.common.constant.HAPConstantManager;
import com.nosliw.common.interpolate.HAPInterpolateUtility;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporterXML;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPXMLUtility;

public class HAPConstantApp extends HAPConfigurableImp{

	protected HAPConstantApp(){
		super("constantprocess.properties", null);
	}

	public static void main(String[] args){
		HAPConstantApp app = new HAPConstantApp();
		app.process();
	}

	public void process(){
		HAPConstantManager constantMan = new HAPConstantManager(null);
		
		try{
			InputStream configureStream = HAPFileUtility.getInputStreamOnClassPath(HAPConstantApp.class, "constant.xml");
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(configureStream);

			Element rootEle = doc.getDocumentElement();
			
			String jsPath = HAPInterpolateUtility.interpolateByConfigure(rootEle.getAttribute("jsPath"), this.getConfiguration()); 
			constantMan.setJsPath(jsPath);
			String jsAttributeFile = rootEle.getAttribute("jsAttributeFile");
			constantMan.setJsAttributeFile(jsAttributeFile);
			String jsConstantFile = rootEle.getAttribute("jsConstantFile");
			constantMan.setJsConstantFile(jsConstantFile);

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
			
			constantMan.buildConstantGroupFromClassAttr();
			
			constantMan.resolve();
			
			constantMan.exportJS();
			constantMan.exportJava();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
