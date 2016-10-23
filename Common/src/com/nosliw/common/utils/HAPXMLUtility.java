package com.nosliw.common.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HAPXMLUtility {

	public static Element getFirstChildElementByName(Element parent, String name){
		NodeList children = parent.getChildNodes();
		for(int i=0; i<children.getLength(); i++)
		{
			Node node = children.item(i);
			if(node instanceof Element && node.getParentNode().equals(parent))
			{
				Element ele = (Element)node;
				if(ele.getTagName().equals(name)) 	return (Element)node;
			}
		}		
		return null;
	}
	
	public static String getFirstChildElementContent(Element parent, String name){
		String out = null;
		Element childEle = getFirstChildElementByName(parent, name);
		if(childEle!=null){
			out = childEle.getTextContent();
		}
		return out;
	}
	
	public static Element[] getMultiChildElementByName(Element parent, String name){
		List<Element> out = new ArrayList<Element>(); 
		NodeList children = parent.getChildNodes();
		for(int i=0; i<children.getLength(); i++)
		{
			Node node = children.item(i);
			if(node instanceof Element && node.getParentNode().equals(parent))
			{
				Element ele = (Element)node;
				if(ele.getTagName().equals(name)) 	out.add((Element)node);
			}
		}		
		return out.toArray(new Element[0]);
	}

	
	public static Element[] getChildElements(Element parent){
		List<Element> out = new ArrayList<Element>(); 
		NodeList children = parent.getChildNodes();
		for(int i=0; i<children.getLength(); i++)
		{
			Node node = children.item(i);
			if(node instanceof Element && node.getParentNode().equals(parent))
			{
				out.add((Element)node);
			}
		}		
		return out.toArray(new Element[0]);
	}
	
	public static Map<String, String> getAllAttributes(Element element)
	{
		Map<String, String> out = new LinkedHashMap<String, String>();
		NamedNodeMap attrs = element.getAttributes();
		for(int i=0; i<attrs.getLength(); i++)
		{
			Attr n = (Attr)attrs.item(i);
			String attr = n.getNodeName();
			String value = n.getNodeValue();
			out.put(attr, value);
		}
		return out;
	}
	
	public static String getAttributeValue(Element element, String attr){
		if(element.hasAttribute(attr)){
			return element.getAttribute(attr);
		}
		else{
			return null;
		}
	}
	
	public static String getChildContent(Element parent, String attr){
		String out = getAttributeValue(parent, attr);
		if(out==null){
			getFirstChildElementContent(parent, attr);
		}
		return out;
	}
}
