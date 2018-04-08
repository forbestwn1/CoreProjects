package com.nosliw.common.utils;

public class HAPConstant {


		public static final int SERVICECODE_SUCCESS = 200;
		public static final int SERVICECODE_FAILURE = 400;
		public static final int SERVICECODE_EXCEPTION = 5000;
		public static final int ERRORCODE_NEWDATAOPERATION_NOTDEFINED = 1000;
		public static final int ERRORCODE_DATAOPERATION_NOTDEFINED = 1005;
		public static final int ERRORCODE_DATAOPERATION_NOTEXIST = 1010;
		public static final int ERRORCODE_DATATYPE_WRONGTYPE = 1100;
		public static final int ERRORCODE_DATATYPE_WRONGVERSION = 1101;
		public static final int ERRORCODE_APPLICATION_INVALIDCLIENTID = 5201;
		public static final int ERRORCODE_DATA_INVALID = 1102;
		public static final int SERVICECODE_ENTITYOPERATION_FORWARD = 100;
		public static final int ERRORCODE_ENTITYOPERATION_AUTOCOMMIT = 2001;
		public static final int ERRORCODE_ENTITYOPERATION_INVALIDTRANSACTION = 2002;
		public static final int ERRORCODE_ENTITYOPERATION_INVALIDSCOPE = 2003;
		public static final int ERRORCODE_REMOTESERVICE_SUSPEND = 9000;
		public static final int ERRORCODE_REMOTESERVICE_EXCEPTION = 5000;



		public static final String OPERATIONDEF_SCRIPT_JAVASCRIPT = "javascript";
		public static final String OPERATIONDEF_PATH_VERSION = "version";
		public static final String OPERATIONDEF_PATH_PARENT = "parent";



		public static final int ENTITYOPERATION_SCOPE_UNDEFINED = -1;
		public static final int ENTITYOPERATION_SCOPE_GLOBAL = 1;
		public static final int ENTITYOPERATION_SCOPE_ENTITY = 2;
		public static final int ENTITYOPERATION_SCOPE_OPERATION = 3;



		public static final String SYMBOL_KEYWORD = "#";
		public static final String SYMBOL_GROUP = "@";
		public static final String SYMBOL_ENTITYNAME_COMMON = "..";
		public static final String SYMBOL_ENTITYNAME_CURRENT = ".";
		public static final String SEPERATOR_NAMEVALUE = "=";
		public static final String SEPERATOR_ELEMENT = ",";
		public static final String SEPERATOR_SEGMENT = ";";
		public static final String SEPERATOR_PART = ":";
		public static final String SEPERATOR_PATH = ".";
		public static final String SEPERATOR_FULLNAME = ".";
		public static final String SEPERATOR_DETAIL = "|";
		public static final String SEPERATOR_SURFIX = "_";
		public static final String SEPERATOR_PREFIX = "_";
		public static final String SEPERATOR_LEVEL1 = ";";
		public static final String SEPERATOR_LEVEL2 = "|";
		public static final String SEPERATOR_LEVEL3 = ":";
		public static final String SEPERATOR_LEVEL4 = ",";
		public static final String SEPERATOR_ARRAYSTART = "[";
		public static final String SEPERATOR_ARRAYEND = "]";
		public static final String SEPERATOR_VARSTART = "{{";
		public static final String SEPERATOR_VAREND = "}}";
		public static final String SEPERATOR_EXPRESSIONSTART = "${";
		public static final String SEPERATOR_EXPRESSIONEND = "}";



		public static final String DATAOPERATION_NEWDATA = "new";
		public static final String DATAOPERATION_TOPARENTTYPE = "toParentType";
		public static final String DATAOPERATION_FROMPARENTTYPE = "fromParentType";
		public static final String DATAOPERATION_TOVERSION = "toVersion";
		public static final String DATAOPERATION_FROMVERSION = "fromVersion";
		public static final String DATAOPERATION_GETCHILD = "getChild";
		public static final String DATAOPERATION_GETCHILDDATATYPE = "getChildDatatype";
		public static final String DATAOPERATION_COMPARE = "compare";
		public static final String DATAOPERATION_PARSELITERAL = "parseLiteral";
		public static final String DATAOPERATION_COMPLEX_GETCHILDRENNAMES = "getChildrenNames";
		public static final String DATAOPERATION_COMPLEX_GETCHILDDATA = "getChildData";
		public static final String DATAOPERATION_COMPLEX_GETCHILDDATABYINDEX = "getChildDataByIndex";
		public static final String DATAOPERATION_COMPLEX_SETCHILDDATA = "setChildData";
		public static final String DATAOPERATION_COMPLEX_ISACCESSCHILDBYID = "isAccessChildById";
		public static final String DATAOPERATION_COMPLEX_LENGTH = "length";
		public static final String DATAOPERATION_COMPLEX_ADDCHILD = "addChild";
		public static final String DATAOPERATION_COMPLEX_REMOVECHILD = "removeChild";



		public static final String DATAOPERATION_PARM_BASENAME = "ABCDEFGHIJKLMN";



		public static final String DATAOPERATION_TYPE_NORMAL = "normal";
		public static final String DATAOPERATION_TYPE_NEW = "new";
		public static final String DATAOPERATION_TYPE_CONVERT = "convert";
		public static final String DATAOPERATION_TYPE_CONVERTFROM = "convertFrom";
		public static final String DATAOPERATION_TYPE_TOFORMAT = "toFormat";



		public static final String DATATYPECRITERIA_TYPE_ANY = "any";
		public static final String DATATYPECRITERIA_TYPE_DATATYPEID = "dataTypeId";
		public static final String DATATYPECRITERIA_TYPE_DATATYPEIDS = "dataTypeIds";
		public static final String DATATYPECRITERIA_TYPE_DATATYPERANGE = "dataTypeRange";
		public static final String DATATYPECRITERIA_TYPE_AND = "and";
		public static final String DATATYPECRITERIA_TYPE_OR = "or";
		public static final String DATATYPECRITERIA_TYPE_EXPRESSION = "expression";
		public static final String DATATYPECRITERIA_TYPE_REFERENCE = "reference";
		public static final String DATATYPECRITERIA_TYPE_LITERATE = "literate";



		public static final String EXPRESSION_OPERAND_CONSTANT = "constant";
		public static final String EXPRESSION_OPERAND_VARIABLE = "variable";
		public static final String EXPRESSION_OPERAND_REFERENCE = "reference";
		public static final String EXPRESSION_OPERAND_OPERATION = "operation";
		public static final String EXPRESSION_OPERAND_DATAOPERATION = "dataoperation";
		public static final String EXPRESSION_OPERAND_DATATYPEOPERATION = "datatypeoperation";
		public static final String EXPRESSION_OPERAND_ATTRIBUTEOPERATION = "attributeoperation";
		public static final String EXPRESSION_OPERAND_DATASOURCE = "datasource";
		public static final String EXPRESSION_OPERAND_PATHOPERATION = "pathoperation";
		public static final String EXPRESSION_OPERAND_NEWOPERATION = "newoperation";



		public static final String EXPRESSION_VARIABLE_STATUS_OPEN = "open";
		public static final String EXPRESSION_VARIABLE_STATUS_CLOSE = "close";



		public static final String EXPRESSION_OPERAND_STATUS_NEW = "new";
		public static final String EXPRESSION_OPERAND_STATUS_PROCESSED = "processed";
		public static final String EXPRESSION_OPERAND_STATUS_INVALID = "invalid";



		public static final String EXPRESSION_VARIABLE_THIS = "this";
		public static final String EXPRESSION_VARIABLE_PARENT = "parent";
		public static final String EXPRESSION_VARIABLE_ENTITY = "entity";
		public static final String EXPRESSION_VARIABLE_DATA1 = "data1";
		public static final String EXPRESSION_VARIABLE_DATA2 = "data2";



		public static final String DATATYPE_RELATIONSHIPTYPE_SELF = "self";
		public static final String DATATYPE_RELATIONSHIPTYPE_ROOT = "root";
		public static final String DATATYPE_RELATIONSHIPTYPE_INTERMEDIA = "intermedia";



		public static final String DATATYPE_CATEGARY_SIMPLE = "simple";
		public static final String DATATYPE_CATEGARY_BLOCK = "block";
		public static final String DATATYPE_CATEGARY_CONTAINER = "container";
		public static final String DATATYPE_CATEGARY_ENTITY = "entity";
		public static final String DATATYPE_CATEGARY_QUERYENTITY = "queryentity";
		public static final String DATATYPE_CATEGARY_REFERENCE = "reference";
		public static final String DATATYPE_CATEGARY_OBJECT = "object";
		public static final String DATATYPE_CATEGARY_DATA = "data";
		public static final String DATATYPE_CATEGARY_SERVICE = "service";



		public static final String DATATYPE_TYPE_INTEGER = "integer";
		public static final String DATATYPE_TYPE_FLOAT = "float";
		public static final String DATATYPE_TYPE_BOOLEAN = "boolean";
		public static final String DATATYPE_TYPE_STRING = "string";
		public static final String DATATYPE_TYPE_CONTAINER_ENTITYATTRIBUTE = "normal";
		public static final String DATATYPE_TYPE_CONTAINER_OPTIONS = "options";
		public static final String DATATYPE_TYPE_CONTAINER_QUERY = "query";
		public static final String DATATYPE_TYPE_CONTAINER_WRAPPER = "wrapper";
		public static final String DATATYPE_TYPE_REFERENCE_NORMAL = "normal";
		public static final String DATATYPE_TYPE_QUERYENTITY_NORMAL = "normal";



		public static final String DATATYPE_PATHSEGMENT_PARENT = "parent";
		public static final String DATATYPE_PATHSEGMENT_LINKED = "linked";



		public static final String UIRESOURCE_TYPE_RESOURCE = "resource";
		public static final String UIRESOURCE_TYPE_TAG = "tag";



		public static final String UIRESOURCE_ROOTTYPE_RELATIVE = "relative";
		public static final String UIRESOURCE_ROOTTYPE_ABSOLUTE = "absolute";



		public static final String ENTITY_CRITICALVALUE_OTHER = "other";



		public static final String ATTRIBUTE_PATH_CRITICAL = "critical";
		public static final String ATTRIBUTE_PATH_ELEMENT = "element";
		public static final String ATTRIBUTE_PATH_ENTITY = "entity";
		public static final String ATTRIBUTE_PATH_EACH = "each";



		public static final String OPTIONS_TYPE_STATIC = "static";
		public static final String OPTIONS_TYPE_DYNAMIC = "dynamic";
		public static final String OPTIONS_TYPE_DYNAMIC_EXPRESSION = "expression";
		public static final String OPTIONS_TYPE_DYNAMIC_EXPRESSION_ATTRIBUTE = "attribute";
		public static final String OPTIONS_TYPE_QUERY = "query";
		public static final String OPTIONS_TYPE_COMPLEX = "complex";



		public static final String CONFIGUREITEM_ENTITY_ISEMPTYONINIT = "emptyOnInit";



		public static final int EVENTTYPE_ENTITY_OPERATION = 1;
		public static final int EVENTTYPE_ENTITY_MODIFY = 2;
		public static final int EVENTTYPE_ENTITY_NEW  = 3;
		public static final int EVENTTYPE_ENTITY_CLEARUP  = 4;
		public static final String EVENT_ENTITY_CHANGE  = "entityChange";



		public static final String WRAPECLEARUP_PARM_SCOPE = "scope";



		public static final int SORTING_ORDER_ASCEND = 0;
		public static final int SORTING_ORDER_DESCEND = 1;



		public static final String SORTING_TYPE_EXPRESSION = "expression";
		public static final String SORTING_TYPE_ATTRIBUTE = "attribute";
		public static final String SORTING_TYPE_PROGRAMMING = "programming";



		public static final int COMPARE_LESS = -1;
		public static final int COMPARE_LARGER = 1;
		public static final int COMPARE_EQUAL = 0;



		public static final String SERVICEDATA_METADATA_TRANSACTIONID = "transactionId";



		public static final String ENTITYOPERATION_ATTR_ATOM_SET = "ENTITYOPERATION_ATTR_ATOM_SET";
		public static final int ENTITYOPERATIONCODE_ATTR_ATOM_SET = 101;
		public static final String ENTITYOPERATION_ATTR_CRITICAL_SET = "ENTITYOPERATION_ATTR_CRITICAL_SET";
		public static final int ENTITYOPERATIONCODE_ATTR_CRITICAL_SET = 112;
		public static final String ENTITYOPERATION_ATTR_ELEMENT_NEW = "ENTITYOPERATION_ATTR_ELEMENT_NEW";
		public static final int ENTITYOPERATIONCODE_ATTR_ELEMENT_NEW = 113;
		public static final String ENTITYOPERATION_ATTR_ELEMENT_DELETE = "ENTITYOPERATION_ATTR_ELEMENT_DELETE";
		public static final int ENTITYOPERATIONCODE_ATTR_ELEMENT_DELETE = 153;
		public static final String ENTITYOPERATION_ENTITY_OPERATIONS = "ENTITYOPERATION_ENTITY_OPERATIONS";
		public static final int ENTITYOPERATIONCODE_ENTITY_OPERATIONS = 114;
		public static final String ENTITYOPERATION_ENTITY_NEW = "ENTITYOPERATION_ENTITY_NEW";
		public static final int ENTITYOPERATIONCODE_ENTITY_NEW = 102;
		public static final String ENTITYOPERATION_ENTITY_DELETE = "ENTITYOPERATION_ENTITY_DELETE";
		public static final int ENTITYOPERATIONCODE_ENTITY_DELETE = 103;
		public static final String ENTITYOPERATION_TRANSACTION_START = "ENTITYOPERATION_TRANSACTION_START";
		public static final int ENTITYOPERATIONCODE_TRANSACTION_START = 104;
		public static final String ENTITYOPERATION_TRANSACTION_COMMIT = "ENTITYOPERATION_TRANSACTION_COMMIT";
		public static final int ENTITYOPERATIONCODE_TRANSACTION_COMMIT = 105;
		public static final String ENTITYOPERATION_TRANSACTION_ROLLBACK = "ENTITYOPERATION_TRANSACTION_ROLLBACK";
		public static final int ENTITYOPERATIONCODE_TRANSACTION_ROLLBACK = 106;
		public static final String ENTITYOPERATION_QUERY_ENTITY_ADD = "ENTITYOPERATION_QUERY_ENTITY_ADD";
		public static final int ENTITYOPERATIONCODE_QUERY_ENTITY_ADD = 107;
		public static final String ENTITYOPERATION_QUERY_ENTITY_REMOVE = "ENTITYOPERATION_QUERY_ENTITY_REMOVE";
		public static final int ENTITYOPERATIONCODE_QUERY_ENTITY_REMOVE = 108;
		public static final String ENTITYOPERATION_QUERY_ENTITY_MODIFY = "ENTITYOPERATION_QUERY_ENTITY_MODIFY";
		public static final int ENTITYOPERATIONCODE_QUERY_ENTITY_MODIFY = 109;
		public static final String ENTITYOPERATION_ATTR_REFERENCE_SET = "ENTITYOPERATION_ATTR_REFERENCE_SET";
		public static final int ENTITYOPERATIONCODE_ATTR_REFERENCE_SET = 110;
		public static final String ENTITYOPERATION_ENTITYATTR_ADD = "ENTITYOPERATION_ENTITYATTR_ADD";
		public static final int ENTITYOPERATIONCODE_ENTITYATTR_ADD = 111;
		public static final String ENTITYOPERATION_ENTITYATTR_REMOVE = "ENTITYOPERATION_ENTITYATTR_REMOVE";
		public static final int ENTITYOPERATIONCODE_ENTITYATTR_REMOVE = 113;



		public static final String UIRESOURCE_ATTRIBUTE_CONTEXT = "context";
		public static final String UIRESOURCE_ATTRIBUTE_UIID = "nosliwid";
		public static final String UIRESOURCE_ATTRIBUTE_EVENT = "event";
		public static final String UIRESOURCE_ATTRIBUTE_DATABINDING = "data";
		public static final String UIRESOURCE_TAG_PLACEHOLDER = "nosliw";
		public static final String UIRESOURCE_CUSTOMTAG_KEYATTRIBUTE_PREFIX = "nosliw-";
		public static final String UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX = "-tag-start";
		public static final String UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX = "-tag-end";
		public static final String UIRESOURCE_SCRIPTBLOCK_TOKEN_OPEN = "{";
		public static final String UIRESOURCE_SCRIPTBLOCK_TOKEN_CLOSE = "}";
		public static final String UIRESOURCE_FUNCTION_EXCECUTEEXPRESSION = "excecuteExpression";
		public static final String UIRESOURCE_UIEXPRESSIONFUNCTION_PREFIX = "expression";



		public static final String UIRESOURCE_CONTEXTTYPE_PUBLIC = "public";
		public static final String UIRESOURCE_CONTEXTTYPE_INTERNAL = "internal";
		public static final String UIRESOURCE_CONTEXTTYPE_PRIVATE = "private";
		public static final String UIRESOURCE_CONTEXTTYPE_EXCLUDED = "excluded";



		public static final String UIRESOURCEMAN_SETTINGNAME_SCRIPTLOCATION = "temp.scriptLocation";



		public static final String DATATYPEMAN_SETTINGNAME_SCRIPTLOCATION = "temp.scriptLocation";



		public static final String REMOTESERVICE_LOGIN = "login";
		public static final String REMOTESERVICE_GETUIRESOURCE = "getUIResource";
		public static final String REMOTESERVICE_GETDATATYPES = "getDataTypes";
		public static final String REMOTESERVICE_EXECUTEEXPRESSION = "executeExpression";
		public static final String REMOTESERVICE_GETALLENTITYDEFINITIONS = "getAllEntityDefinitions";
		public static final String REMOTESERVICE_GETENTITYDEFINITIONBYNAMES = "getEntityDefinitionByNames";



		public static final int DATAACCESS_ENTITYSTATUS_NORMAL = 0;
		public static final int DATAACCESS_ENTITYSTATUS_CHANGED = 1;
		public static final int DATAACCESS_ENTITYSTATUS_NEW = 2;
		public static final int DATAACCESS_ENTITYSTATUS_DEAD = 3;



		public static final String APPLICATION_CONFIGURE_DATATYPE = "dataType";
		public static final String APPLICATION_CONFIGURE_ENTITYDEFINITION = "entityDefinition";
		public static final String APPLICATION_CONFIGURE_UIRESOURCE = "uiResource";
		public static final String APPLICATION_CONFIGURE_UITAG = "uiTag";
		public static final String APPLICATION_CONFIGURE_QUERYDEFINITION = "queryDefinition";
		public static final String APPLICATION_CONFIGURE_USERENV = "userEnv";
		public static final String APPLICATION_CONFIGURE_LOGGER = "logger";



		public static final String REMOTESERVICE_TASKTYPE_NORMAL = "normal";
		public static final String REMOTESERVICE_TASKTYPE_GROUP = "group";
		public static final String REMOTESERVICE_TASKTYPE_GROUPCHILD = "groupchild";



		public static final String REMOTESERVICE_GROUPTASK_MODE_ALL = "all";
		public static final String REMOTESERVICE_GROUPTASK_MODE_ALWAYS = "always";



		public static final String SERVICENAME_LOGIN = "login";
		public static final String SERVICENAME_SERVICE = "service";



		public static final String SERVICECOMMAND_GROUPREQUEST = "groupRequest";



		public static final String SCRIPTTYPE_UIRESOURCE = "uiResource";
		public static final String SCRIPTTYPE_DATAOPERATIONS = "dataOperations";
		public static final String SCRIPTTYPE_UITAGS = "uiTags";



		public static final int REFERENCE_TYPE_ABSOLUTE = 0;
		public static final int REFERENCE_TYPE_RELATIVE = 1;



		public static final String TESTRESULT_TYPE_SUITE = "SUITE";
		public static final String TESTRESULT_TYPE_CASE = "CASE";
		public static final String TESTRESULT_TYPE_ITEM = "ITEM";



		public static final String TEST_TYPE_SUITE = "SUITE";
		public static final String TEST_TYPE_CASE = "CASE";
		public static final String TEST_TYPE_ITEM = "ITEM";



		public static final String CONFIGURATION_DEFAULTBASE = "default";



		public static final String STRINGABLE_ATOMICVALUETYPE_STRING = "string";
		public static final String STRINGABLE_ATOMICVALUETYPE_BOOLEAN = "boolean";
		public static final String STRINGABLE_ATOMICVALUETYPE_INTEGER = "integer";
		public static final String STRINGABLE_ATOMICVALUETYPE_FLOAT = "float";
		public static final String STRINGABLE_ATOMICVALUETYPE_ARRAY = "array";
		public static final String STRINGABLE_ATOMICVALUETYPE_MAP = "map";
		public static final String STRINGABLE_ATOMICVALUETYPE_OBJECT = "object";



		public static final String STRINGABLE_VALUESTRUCTURE_ATOMIC = "atomic";
		public static final String STRINGABLE_VALUESTRUCTURE_MAP = "map";
		public static final String STRINGABLE_VALUESTRUCTURE_LIST = "list";
		public static final String STRINGABLE_VALUESTRUCTURE_ENTITY = "entity";
		public static final String STRINGABLE_VALUESTRUCTURE_OBJECT = "object";



		public static final String STRINGALBE_VALUEINFO_ATOMIC = "atomic";
		public static final String STRINGALBE_VALUEINFO_REFERENCE = "reference";
		public static final String STRINGALBE_VALUEINFO_ENTITY = "entity";
		public static final String STRINGALBE_VALUEINFO_ENTITYOPTIONS = "entityOptions";
		public static final String STRINGALBE_VALUEINFO_LIST = "list";
		public static final String STRINGALBE_VALUEINFO_MAP = "map";
		public static final String STRINGALBE_VALUEINFO_OBJECT = "object";
		public static final String STRINGALBE_VALUEINFO_MODE = "mode";



		public static final String STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_ABSOLUTE = "absolute";
		public static final String STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_PROPERTY = "property";
		public static final String STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_PROPERTYASPATH = "propertyAsPath";



		public static final String UITAG_NAME_INCLUDE = "include";
		public static final String UITAG_NAME_INCLUDE_PARM_SOURCE = "source";
		public static final String UITAG_NAME_INCLUDE_PARM_CONTEXT = "context";



		public static final String DATAOPERATION_VAR_TYPE_IN = "parm";
		public static final String DATAOPERATION_VAR_TYPE_OUT = "out";



		public static final String RUNTIME_RESOURCE_TYPE_OPERATION = "operation";
		public static final String RUNTIME_RESOURCE_TYPE_DATATYPE = "datatype";
		public static final String RUNTIME_RESOURCE_TYPE_CONVERTER = "converter";
		public static final String RUNTIME_RESOURCE_TYPE_JSLIBRARY = "jslibrary";
		public static final String RUNTIME_RESOURCE_TYPE_JSHELPER = "jshelper";
		public static final String RUNTIME_RESOURCE_TYPE_JSGATEWAY = "jsGateway";
		public static final String RUNTIME_RESOURCE_TYPE_UIRESOURCE = "uiResource";
		public static final String RUNTIME_RESOURCE_TYPE_UITAG = "uiTag";
		public static final String RUNTIME_RESOURCE_TYPE_MATCHER = "matcher";



		public static final String RUNTIME_LANGUAGE_JS = "javascript";
		public static final String RUNTIME_ENVIRONMENT_RHINO = "rhino";
		public static final String RUNTIME_ENVIRONMENT_BROWSER = "browser";



		public static final String RUNTIME_LANGUAGE_JS_GATEWAY = "runtime.gateway";



		public static final String CATEGARY_NAME = "CATEGARY_NAME";



		public static final String DATATASK_TYPE_EXPRESSION = "expression";
		public static final String DATATASK_TYPE_DATASOURCE = "dataSource";



		public static final String EXPRESSIONTASK_STEPTYPE_EXPRESSION = "expression";
		public static final String EXPRESSIONTASK_STEPTYPE_LOOP = "loop";
		public static final String EXPRESSIONTASK_STEPTYPE_BRANCH = "branch";


}
