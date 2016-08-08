package com.nosliw.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.dataaccess.HAPOperationAllResult;
import com.nosliw.entity.operation.HAPEntityOperation;
import com.nosliw.entity.operation.HAPEntityOperationFactory;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.utils.HAPEntityUtility;

public class HAPWebServiceTester{// extends HAPUserContextManager{
/*
	private List<HAPEntityOperationInfo> m_operations;
	
	public HAPWebServiceTester(String fileName, HAPUserContext userContext){
		super(userContext);
		this.m_operations = new ArrayList<HAPEntityOperationInfo>();
		this.init(fileName);
	}
	
	public void init(String fileName){
		try {
			InputStream stream = new FileInputStream(new File(fileName));
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(stream);
			Element[] eles = HAPXMLUtility.getMultiChildElementByName(doc.getDocumentElement(), "command");
			for(Element ele : eles){
				HAPEntityOperationInfo operation = this.readOperation(ele);
				this.m_operations.add(operation);
			}
		} catch (Exception e) {    
			e.printStackTrace();
		}
	}

	public HAPEntityOperationInfo readOperation(Element ele){
		String op = ele.getAttribute("operation");
		String data = ele.getAttribute("data");
		String entityId = ele.getAttribute("entityId");
		String attrPath = ele.getAttribute("attrPath");
		String ac = ele.getAttribute("autoCommit");
		Boolean autoCommit = null;
		if(HAPEntityUtility.isStringNotEmpty(ac)){
			if(ac.equals("yes")||ac.equals("true"))  autoCommit=new Boolean(true);
			else autoCommit=new Boolean(false);
		}
		
		HAPServices services = this.getApplicationContext().getWebServices();
		HAPEntityOperationInfo out = null;
		if(op.equals("StartTransaction")){
			out = HAPEntityOperationFactory.createTransactionStartOperation(null);
		}
		else if(op.equals("Commit")){
			out = HAPEntityOperationFactory.createTransactionCommitOperation(null);
		}
		else if(op.equals("RollBack")){
			out = HAPEntityOperationFactory.createTransactionRollbackOperation(null);
		}
		else if(op.equals("NewEntity")){
			out = HAPEntityOperationFactory.createEntityNewOperation(data, entityId, autoCommit);
		}
		else if(op.equals("DeleteEntity")){
			out = HAPEntityOperationFactory.createEntityDeleteOperation(new HAPEntityID(this.getUserContext().getId(), data, entityId));
		}
		else if(op.equals("SetAttributeAtom")){
			out = HAPEntityOperationFactory.createAttributeAtomSetOperationByString(
					new HAPEntityID(this.getUserContext().getId(), entityId), 
					attrPath, 
					data);
		}
		else if(op.equals("SetAttributeReference")){
			out = HAPEntityOperationFactory.createAttributeReferenceSetOperation(
					new HAPEntityID(this.getUserContext().getId(), entityId), 
					attrPath, 
					new HAPEntityID(this.getUserContext().getId(), data));
		}
		else if(op.equals("NewContainerElement")){
			out = HAPEntityOperationFactory.createContainerElementNewOperation(new HAPEntityID(this.getUserContext().getId(), entityId), attrPath, data, autoCommit);
		}		
		else if(op.equals("createAttributeElementDeleteOperation")){
			out = HAPEntityOperationFactory.createAttributeElementDeleteOperation(
					new HAPEntityID(this.getUserContext().getId(), entityId), 
					attrPath, 
					data);
		}		
		return out;
	}
	
	public void run(){
		String transactionId = null;
		
		for(HAPEntityOperationInfo operation : this.m_operations){
			HAPEntityID entityID = operation.getEntityID();
			if(entityID!=null)  entityID.setUserContext(this.getUserContext().getId());
			
			HAPServiceData serviceData = null; 
			switch(operation.getOperation()){
			case ENTITYOPERATION_TRANSACTION_START:
				serviceData = this.getApplicationContext().getWebServices().startTransaction(transactionId, this.getUserContext().getUserContextInfo());
				break;
			case ENTITYOPERATION_TRANSACTION_COMMIT:
				serviceData = this.getApplicationContext().getWebServices().commit(transactionId, this.getUserContext().getUserContextInfo());
				break;
			case ENTITYOPERATION_TRANSACTION_ROLLBACK:
				serviceData = this.getApplicationContext().getWebServices().rollBack(transactionId, this.getUserContext().getUserContextInfo());
				break;
			default:
//				serviceData = this.getApplicationContext().getWebServices().operate("-1", operation, transactionId, this.getUserContext().getUserContextInfo());
			}
			
			HAPOperationAllResult result = (HAPOperationAllResult)serviceData.getData();
			transactionId = result.getTransactionId();
		}
	}
*/	
}
