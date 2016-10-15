package com.nosliw.entity.persistent.xmlfile;

import java.io.File;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.entity.data.HAPEntityContainerAttributeWraper;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPEntityData;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.data.HAPReferenceWraper;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPAttributeDefinitionContainer;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;
import com.nosliw.entity.utils.HAPEntityUtility;

public class HAPXMLEntityExporter {

	public static void writeData(HAPEntityWraper entityWraper, String fileName){
		Document doc = createEntityDocument();
		if(doc!=null){
			writeEntityToDocument(entityWraper, doc);
		}

		if(fileName==null){
			String path= System.getProperty("user.home", File.separatorChar + "home" + File.separatorChar + "zelda") + File.separatorChar;
			fileName = path + "Forbestwn/data/temp.xml";
		}

		writeDocumentToFile(doc, new File(fileName));
	}
	
	public static void writeEntityToNode(HAPEntityWraper entityWraper, Document doc, Element node)
	{
		node.setAttribute("id", entityWraper.getId());
		
		HAPEntityData entityData = entityWraper.getEntityData();
		String[] attrs = entityData.getAttributes();
		if(attrs == null)  return;
		
		for(String attr : attrs)
		{
			if(entityData.getAttributeValueWraper(attr).isEmpty())  continue;
			
			HAPAttributeDefinition attrDef = entityData.getAttributeDefinition(attr);
			HAPDataTypeDefInfo attrType = attrDef.getDataTypeDefinitionInfo();
			String attrName = attrDef.getName();
			if(HAPEntityDataTypeUtility.isAtomType(attrType))
			{
				HAPData atomData = entityData.getAttributeValue(attrName);
				String value = atomData.toStringValue(HAPSerializationFormat.XML);
				if(value != null){
					node.setAttribute(attrName, value);
				}
			}
			else if(HAPEntityDataTypeUtility.isContainerType(attrType)){
				HAPAttributeDefinitionContainer containerAttrDef = (HAPAttributeDefinitionContainer)attrDef;
				HAPDataTypeDefInfo childType = containerAttrDef.getChildDataTypeDefinitionInfo();
				
				Element attrEle = doc.createElement(attrName);
				node.appendChild(attrEle);

				HAPEntityContainerAttributeWraper containerValueWraper = (HAPEntityContainerAttributeWraper)entityData.getAttributeValueWraper(attrName);
				
				Iterator<HAPDataWraper> eleIterator = containerValueWraper.iterate();
				while(eleIterator.hasNext()){
					HAPDataWraper value = eleIterator.next();
					
					Element eleEle = doc.createElement("element");
					eleEle.setAttribute("id", value.getId());
					if(HAPEntityDataTypeUtility.isAtomType(childType)){
						//atom
						eleEle.setAttribute("value", value.toString());
					}
					else if(HAPEntityDataTypeUtility.isEntityType(childType)){
						writeEntityToNode((HAPEntityWraper)value, doc, eleEle);
					}
					else if(HAPEntityDataTypeUtility.isReferenceType(childType)){
						if(value.isEmpty())  	eleEle.setAttribute("refid", "");
						else eleEle.setAttribute("refid", ((HAPReferenceWraper)value).getIDData().toString());
					}
					attrEle.appendChild(eleEle);
				}
			}	
			else if(HAPEntityDataTypeUtility.isEntityType(attrType))
			{
				Element attrEle = doc.createElement(attrName);
				node.appendChild(attrEle);
				HAPDataWraper entityAttrWraper = entityData.getAttributeValueWraper(attrName);
				if(!entityAttrWraper.isEmpty())
				{
					writeEntityToNode((HAPEntityWraper)entityAttrWraper, doc, attrEle);
				}
			}
			else if(HAPEntityDataTypeUtility.isReferenceType(attrType)){
				Element attrEle = doc.createElement(attrName);
				node.appendChild(attrEle);
				HAPDataWraper referenceAttrWraper = entityData.getAttributeValueWraper(attrName);
				if(!referenceAttrWraper.isEmpty())
				{
					attrEle.setAttribute("refid", ((HAPReferenceWraper)referenceAttrWraper).getIDData().toString());
				}
			}
		}
	}	
	
	public static Document createEntityDocument(){
		Document doc = null;
		try{
			DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder icBuilder;
	        
	        icBuilder = icFactory.newDocumentBuilder();
	        doc = icBuilder.newDocument();
			
	        Element mainRootElement = doc.createElementNS("", "data");
	        doc.appendChild(mainRootElement);
		}
		catch(Exception e){
			e.printStackTrace();
		}

        return doc;
	}
	
	public static void writeDocumentToFile(Document doc, File file){
		try{
	        // output DOM XML to console 
	        Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	        DOMSource source = new DOMSource(doc);

	        StreamResult outStream = null;
	        if(file==null)    	outStream = new StreamResult(System.out);
	        else   	outStream = new StreamResult(file);            	
	        
	        transformer.transform(source, outStream);

	        System.out.println("\nXML DOM Created Successfully..");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeEntityToDocument(HAPEntityWraper entityWraper, Document doc){
		Element dataRootEle = doc.getDocumentElement();
		
        // append child elements to root element
		Element entityEle = doc.createElement(entityWraper.getEntityType());
		dataRootEle.appendChild(entityEle);

		writeEntityToNode(entityWraper, doc, entityEle);
	}
	
	
}
