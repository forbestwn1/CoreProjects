package com.nosliw.common.pattern;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityXML;

public class HAPPatternXmlResourceUtility {

	public static Set<HAPPatternProcessorInfo> importProcessorInfos(InputStream inputStream){
		Set<HAPPatternProcessorInfo> out = new HashSet<HAPPatternProcessorInfo>();
		try {
			DocumentBuilder DOMbuilder = null;
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DOMbuilder = DOMfactory.newDocumentBuilder();

			Document doc = DOMbuilder.parse(inputStream);
			Element[] eles = HAPUtilityXML.getChildElements(doc.getDocumentElement());
			for(Element ele : eles){
				String type = ele.getTagName();
				HAPPatternProcessorInfo processorInfo = importProcessorInfo(ele);
				out.add(processorInfo);
			}
		} catch (Exception e) {    
			e.printStackTrace();
		}
		return out;
	}
	
	public static void exportProcessorInfos(Set<HAPPatternProcessorInfo> processors, OutputStream outputSteam){
		Document doc = null;
		try{
			DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder icBuilder;
	        
	        icBuilder = icFactory.newDocumentBuilder();
	        doc = icBuilder.newDocument();
			
	        Element mainRootElement = doc.createElementNS("", "processors");
	        doc.appendChild(mainRootElement);

	        //build document elements
	        for(HAPPatternProcessorInfo processor : processors){
	        	Element processorEle = outportProcessorInfo(processor, doc);
	        	if(processorEle!=null)        	mainRootElement.appendChild(processorEle);
	        }

	        StreamResult outStream = null;
	        if(outputSteam==null)    	outStream = new StreamResult(System.out);
	        else   	outStream = new StreamResult(outputSteam);            	
	        
	        Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	        DOMSource source = new DOMSource(doc);
	        transformer.transform(source, outStream);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static Element outportProcessorInfo(HAPPatternProcessorInfo processorInfo, Document doc){
		Element element = null;
		String className = processorInfo.getClassName();
		try {
			element = doc.createElement("processor");
			element.setAttribute("classname", className);

			String name = processorInfo.getName();
			if(HAPUtilityBasic.isStringEmpty(name)){
				HAPPatternProcessor processor = (HAPPatternProcessor) Class.forName(className).newInstance();
				name = processor.getName();
			}
			element.setAttribute("name", name);
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
		return element;
	}

	private static HAPPatternProcessorInfo importProcessorInfo(Element ele){
		HAPPatternProcessorInfo processorInfo = new HAPPatternProcessorInfo();
		String name = ele.getAttribute("name");
		processorInfo.setName(name);
		String className = ele.getAttribute("classname");
		processorInfo.setClassName(className);
		return processorInfo;
	}
	
}
