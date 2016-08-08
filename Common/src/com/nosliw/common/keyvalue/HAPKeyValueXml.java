package com.nosliw.common.keyvalue;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * providing util method to read attribute-value pair from a node in XML
 */

public class HAPKeyValueXml extends HAPKeyValue{

	//read and save element's all attribute and value
	//<aa bb="cc" dd="kk"/>
	protected void readAllAttributes(Element element)
	{
		NamedNodeMap attrs = element.getAttributes();
		for(int i=0; i<attrs.getLength(); i++)
		{
			Attr n = (Attr)attrs.item(i);
			String attr = n.getNodeName();
			String value = n.getNodeValue();
			this.setValue(attr, value);
		}
	}

	//read and save element's child's text content, 
	//<aa XXX>
	//    <bb> cccccc </bb>
	//    <cc> dddddd </cc>
	//</aa>
	//
	protected void readChildAttributes(Element element)
	{
		NodeList nodes = element.getChildNodes();
		for(int i=0; i<nodes.getLength(); i++)
		{
			Node n = nodes.item(i);
			if(n instanceof Element)
			{
				Element ele = (Element)n;
				this.setValue(ele.getTagName(), ele.getTextContent());
			}
		}
	}
	
}
