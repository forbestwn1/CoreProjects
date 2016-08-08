package com.nosliw.entity.definition.xmlimp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPAttributeDefinitionLoader;

public class HAPEntityAttributeDefinitionLoaderXML extends HAPAttributeDefinitionLoader{
	private static final String TAG_ATTRIBUTE = "attribute"; 

	private Document[] m_docs = null;
	
	public HAPEntityAttributeDefinitionLoaderXML(String name, Document[] doc)
	{
		super(name);
		this.m_docs = doc;
	}

	public HAPEntityAttributeDefinitionLoaderXML(String name,InputStream[] inputs)
	{
		super(name);
		try{
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			List<Document> docs = new ArrayList<Document>();
			for(InputStream input : inputs)
			{
				if(input != null)
				{
					Document entityDoc = DOMbuilder.parse(input);
					docs.add(entityDoc);
				}
			}
			this.m_docs = docs.toArray(new Document[0]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Set<HAPAttributeDefinition> loadEntityAttributeDefinition() {
		Set<HAPAttributeDefinition> out = new HashSet<HAPAttributeDefinition>();
		
//		for(Document doc : this.m_docs)
//		{
//			Element[] elements = HAPXMLUtility.getMultiChildElementByName(doc.getDocumentElement(), TAG_ATTRIBUTE);
//			for(Element element : elements){
//				HAPAttributeDefinition attrDef = HAPEntityDefinitionLoaderXML.readAttribute(element, null, null, this.getApplicationContext());
//				out.add(attrDef);
//			}
//		}
		return out;
	}
}
