package com.nosliw.utils;

import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPReferenceInfoAbsolute;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPEntityDefinitionCritical;

public class HAPConstant1 {


	public static final String DATATYPE_CONTAINER_NORMAL = "normal";
	public static final String DATATYPE_CONTAINER_OPTIONS = "options";
	public static final String DATATYPE_CONTAINER_QUERY = "query";
	public static final String DATATYPE_CONTAINER_WRAPER = "wraper";

	public static final String DATATYPE_CONTAINER_LIST = "list";
	public static final String DATATYPE_CONTAINER_TABLE = "table";
	public static final String DATATYPE_CONTAINER_TABLECOLUMN = "tablecolumn";
	public static final String DATATYPE_CONTAINER_TABLEROW = "tablerow";
	public static final String DATATYPE_CONTAINER_MAP = "map";

	
	public static final String DATATYPE_QUERYENTITY_QUERY = "query";
	
	public static final String DATATYPE_ENTITY_REFERENCE_NORMAL = "normal";

	
	public static final String DATATYPE_SIMPLE_TEXT = "text";
	public static final String DATATYPE_SIMPLE_BOOL = "bool";
	public static final String DATATYPE_SIMPLE_INTEGER = "integer";
	public static final String DATATYPE_SIMPLE_FLOAT = "float";
	
	
	public static final String SYMBOL_ENTITY_GROUP = "@";
	public static final String SYMBOL_ATTRPATH_SYMBOL = "#";
	public static final String SYMBOL_ATTRPATH_CRITICAL = "critical";
	public static final String SYMBOL_ATTRPATH_ELEMENT = "element";
	
	public static final String SEPERATOR_USERCONTEXT = "#";
	public static final String SEPERATOR_ATTRPATH = ".";
	public static final String SEPERATOR_ID = ":";
	
	public static final int OPSCOPE_GLOBAL = 0;
	public static final int OPSCOPE_ENTITY = 1;
	public static final int OPSCOPE_OPERATION = 2;
		
	public static final String APPCONFIGURE_ISEMPTYONINIT="isEmptyOnInit";
	
	
	//event when do normal operation on wraper
	public final static int EVENTTYPE_ATTRIBUTEOPERATION = 1;
	//event when some attribute value of entity is change
	public final static int EVENTTYPE_ENTITYMODIFY = 2;
	//when entity is clearup, remove from system
	public final static int EVENTTYPE_CLEARUP = 3;
	
	public final static int EVENTTYPE_ENTITYNEW = 4;
	
	public final static String EVENT_ENTITY_CHANGE="entityChange";
	
	public static final int QUERY_OPERATION_OR = 10;
	public static final int QUERY_OPERATION_AND = 20;
	public static final int QUERY_OPERATION_NOT = 30;
	
	public static final int QUERY_OPERATION_EQUAL = 100;
	public static final int QUERY_OPERATION_MORE = 101;
	public static final int QUERY_OPERATION_LESS = 102;
	public static final int QUERY_OPERATION_NOTEQUAL = 103;
	public static final int QUERY_OPERATION_LIKE = 104;
	
	
	public static final int SERVICECODE_FAIL_OPERATION = 401;
	public static final int SERVICECODE_FAIL_COMMIT = 402;
	public static final int SERVICECODE_FAIL_ROLLBACK = 403;
	
	
	
	
	

	public static final String OPERATIONINFO_ATTRIBUTE_OPERATIONID = "operationId";
	public static final String OPERATIONINFO_ATTRIBUTE_OPERATION = "operation";
	public static final String OPERATIONINFO_ATTRIBUTE_AUTOCOMMIT = "isAutoCommit";
	public static final String OPERATIONINFO_ATTRIBUTE_VALIDATION = "isValidation";
	public static final String OPERATIONINFO_ATTRIBUTE_SCOPE = "scope";
	public static final String OPERATIONINFO_ATTRIBUTE_ROOTOPERATION = "rootOperation";
	public static final String OPERATIONINFO_ATTRIBUTE_SUBMITABLE = "submitable";

	public static final String OPERATIONINFO_ATTRIBUTE_EXTRA = "extra";

	public static final String OPERATIONINFO_ATTRIBUTE_ENTITYID = "entityId";
	public static final String OPERATIONINFO_ATTRIBUTE_ATTRIBUTEPATH = "attributePath";
	public static final String OPERATIONINFO_ATTRIBUTE_DATA = "data";
	public static final String OPERATIONINFO_ATTRIBUTE_TRANSACTIONID = "transactionId";
	public static final String OPERATIONINFO_ATTRIBUTE_REFERENCEPATH = "referencePath";
	public static final String OPERATIONINFO_ATTRIBUTE_WRAPER = "wraper";
	public static final String OPERATIONINFO_ATTRIBUTE_ENTITYDEFINITION = "entityDefinition";
	public static final String OPERATIONINFO_ATTRIBUTE_ATTRIBUTEDEFINITION = "attributeDefinition";
	public static final String OPERATIONINFO_ATTRIBUTE_ENTITYTYPE = "entityType";
	public static final String OPERATIONINFO_ATTRIBUTE_ELEMENTID = "elementId";
	public static final String OPERATIONINFO_ATTRIBUTE_QUERYNAME = "queryName";
	public static final String OPERATIONINFO_ATTRIBUTE_QUERYENTITYWRAPER = "queryEntityWraper";
	public static final String OPERATIONINFO_ATTRIBUTE_VALUE = "value";
	public static final String OPERATIONINFO_ATTRIBUTE_REFENTITYID = "refEntityID";
	public static final String OPERATIONINFO_ATTRIBUTE_PARMS = "parms";

	
	
	public static final int ENTITY_STATUS_PREDEAD = 1;
	public static final int ENTITY_STATUS_DEAD = 3;
	public static final int ENTITY_STATUS_NORMAL = 0;
	public static final int ENTITY_STATUS_NEW = 2;
	

	public static final String ATTRPATH_CRITICAL = "#critical";
	public static final String ATTRPATH_ELEMENT = "#element";
	
	
	
	//default is also a fallback state: cannot find particular state, then use default instead

	public static final String WRAPER_ATTRIBUTE_DATA = "data";
	public static final String WRAPER_ATTRIBUTE_DATATYPE = "dataType";
	public static final String WRAPER_ATTRIBUTE_CHILDDATATYPE = "childDataType";
	public static final String WRAPER_ATTRIBUTE_id = "id";
	public static final String WRAPER_ATTRIBUTE_ID = "ID";
	public static final String WRAPER_ATTRIBUTE_ATTRPATH = "attrPath";
	public static final String WRAPER_ATTRIBUTE_PARENTID = "parentID";
	public static final String WRAPER_ATTRIBUTE_ATTRCONFIGURE = "attrConfigure";
	public static final String WRAPER_ATTRIBUTE_OPTIONS = "options";
	public static final String WRAPER_ATTRIBUTE_NEXTID = "nextId";
	public static final String WRAPER_ATTRIBUTE_ISREFERENCE = "isReference";
	public static final String WRAPER_ATTRIBUTE_REFERENCE = "reference";
	
	public static final String SERVICEDATA_ATTRIBUTE_CODE = "code";
	public static final String SERVICEDATA_ATTRIBUTE_MESSAGE = "message";
	public static final String SERVICEDATA_ATTRIBUTE_DATA = "data";
	public static final String SERVICEDATA_ATTRIBUTE_REQUESTID = "requestId";
	public static final String SERVICEDATA_ATTRIBUTE_TRANSACTIONID = "transactionId";
	
	public static final String QUERY_TYPE_ATTRIBUTE = "attribute";
	public static final String QUERY_TYPE_SINGLE = "single";
	public static final String QUERY_TYPE_DUAL = "dual";

	
	public static final String QUERYATTR_ATTRIBUTE_ATTRIBUTE = "attribute";
	public static final String QUERYATTR_ATTRIBUTE_DATA = "data";
	public static final String QUERYATTR_ATTRIBUTE_OPERATION = "operation";

	public static final String QUERYSINGLE_ATTRIBUTE_OPERATION = "operation";
	public static final String QUERYSINGLE_ATTRIBUTE_CHILD = "child";
	
	public static final String QUERYDUAL_ATTRIBUTE_OPERATION = "operation";
	public static final String QUERYDUAL_ATTRIBUTE_CHILD1 = "child1";
	public static final String QUERYDUAL_ATTRIBUTE_CHILD2 = "child2";
	
	public static final String QUERY_ATTRIBUTE_OPERATIONS = "operations";
	public static final String QUERY_ATTRIBUTE_ATTRQUERYS = "attrQuerys";
	public static final String QUERY_ATTRIBUTE_ENTITYTYPE = "entityType";
	public static final String QUERY_ATTRIBUTE_NAME = "name";
	public static final String QUERY_ATTRIBUTE_QUERY = "query";
	public static final String QUERY_ATTRIBUTE_TYPE = "type";
	
	public static final String OPERATIONRESULT_ATTRIBUTE_TRANSACTIONID = "transactionId";
	public static final String OPERATIONRESULT_ATTRIBUTE_REQUESTID = "requestId";
	public static final String OPERATIONRESULT_ATTRIBUTE_OPERATION = "operation";
	public static final String OPERATIONRESULT_ATTRIBUTE_CODE = "code";
	public static final String OPERATIONRESULT_ATTRIBUTE_MESSAGE = "message";
	public static final String OPERATIONRESULT_ATTRIBUTE_RESULTS = "results";
	public static final String OPERATIONRESULT_ATTRIBUTE_UPDATED = "updated";
	public static final String OPERATIONRESULT_ATTRIBUTE_DELETED = "deleted";
	public static final String OPERATIONRESULT_ATTRIBUTE_NEW = "new";
	
	public static final String ENTITYID_ATTRIBUTE_USERCONTEXT = "userContext";
	public static final String ENTITYID_ATTRIBUTE_TYPE = "type";
	public static final String ENTITYID_ATTRIBUTE_ID = "id";
	public static final String ENTITYID_ATTRIBUTE_ATTRPATH = "attrPath";
	public static final String ENTITYID_ATTRIBUTE_FULLNAME = "fullName";
	public static final String ENTITYID_ATTRIBUTE_DATA = "data";
	
	
	public static final String JSON_ATTRIBUTE_DATA_DATA = "data";
	public static final String JSON_ATTRIBUTE_DATA_CATEGARY = "categary";
	public static final String JSON_ATTRIBUTE_DATA_TYPE = "type";
	
	public static final Integer JSON_DATATYPE_STRING = new Integer(0);
	public static final Integer JSON_DATATYPE_BOOLEAN = new Integer(1);
	public static final Integer JSON_DATATYPE_INTEGER = new Integer(2);
	
	
	

	
	public static final String UIRESOURCE_TYPE_RESOURCE = "Resource";
	public static final String UIRESOURCE_TYPE_TAG = "Tag";
	
	
	
	public final static String CATEGARY_SIMPLE = "simple";      //for data type : text, url, html, ...
	public final static String CATEGARY_BLOCK = "block";		//for data type containing block of data for content : image, video, audio, 

	public final static String CATEGARY_CONTAINER = "container";	//for data contaning other data 
	public final static String CATEGARY_ENTITY = "entity";		//for entity
	public final static String CATEGARY_QUERYENTITY = "queryentity";		//for entity
	public final static String CATEGARY_ENTITY_REFERENCE = "reference";		//for entity
	
	
	public static final int EXPRESSION_TYPE_CONSTANT = 1;
	public static final int EXPRESSION_TYPE_VARIABLE = 2;
	public static final int EXPRESSION_TYPE_EXPRESSION = 3;
	
	public final static String SCRIPT = "javascript";
	
	public static final String DATATYPE_CATEGARY = "categary";
	public static final String DATATYPE_TYPE = "type";
	public static final String DATATYPE_CHILD_CATEGARY = "childCategary";
	public static final String DATATYPE_CHILD_TYPE = "childType";
	public static final String DATATYPE_ENTITYGROUP = "entityGroup";
	

	
	//format to store the data
	public final static String FORMAT_JSON = "json";
	public final static String FORMAT_JSON_DATATYPE = "json_data";
	public final static String FORMAT_XML = "xml";
	public final static String FORMAT_TEXT = "text";
	
}
