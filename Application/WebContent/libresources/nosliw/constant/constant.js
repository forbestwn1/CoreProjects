//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_CONSTANT=
{
	  "SCRIPTOBJECT_VARIABLE_RESOURCEVIEW": "code",

	  "EVENT_EVENTNAME_ALL": "all",
	  
	  //wrapper event
	  "WRAPPER_EVENT_SET" : "EVENT_WRAPPER_SET",                        //the value get set					
	  "WRAPPER_EVENT_ADDELEMENT" : "EVENT_WRAPPER_ADDELEMENT",          //element is added to container
	  "WRAPPER_EVENT_DELETEELEMENT" : "EVENT_WRAPPER_DELETEELEMENT",    //element is removed from container.
	  "WRAPPER_EVENT_FORWARD" : "EVENT_WRAPPER_FORWARD",				//forward original event information to child
	  "WRAPPER_EVENT_CHANGE" : "EVENT_WRAPPER_CHANGE",					//indicate something changes on itself, need to update data 
	  "WRAPPER_EVENT_REFRESH" : "EVENT_WRAPPER_REFRESH",				//indicate children or itself has some change, sometimes need to refresh 
	  "WRAPPER_EVENT_DELETE" : "EVENT_WRAPPER_DELETE",                //delete means the path does not exist anymore. all the resources related with this wrapper should be destroy (variable, child wrapper)
	  "WRAPPER_EVENT_CLEARUP_BEFORE" : "EVENT_WRAPPER_CLEARUP_BEFORE",                //clear up means release resource
	  "WRAPPER_EVENT_CLEARUP_AFTER" : "EVENT_WRAPPER_CLEARUP_AFTER",                //clear up means release resource

	  "EACHELEMENTCONTAINER_EVENT_RESET" : "EACHELEMENTCONTAINER_EVENT_RESET",   				//element container need to loop again
	  "EACHELEMENTCONTAINER_EVENT_NEWELEMENT" : "EACHELEMENTCONTAINER_EVENT_NEWELEMENT",        //new element with element variable in event data. only on variable, not on wrapper 
	  "EACHELEMENTCONTAINER_EVENT_DELETEELEMENT" : "EACHELEMENTCONTAINER_EVENT_DELETEELEMENT",  //delete element with element variable in event data. only on variable, not on wrapper 

	  
	  //operation on wrapper
	  "WRAPPER_OPERATION_SET" : "WRAPPER_OPERATION_SET",
	  "WRAPPER_OPERATION_ADDELEMENT" : "WRAPPER_OPERATION_ADDELEMENT",      //
	  "WRAPPER_OPERATION_DELETEELEMENT" : "WRAPPER_OPERATION_DELETEELEMENT",
	  "WRAPPER_OPERATION_GET" : "WRAPPER_OPERATION_GET",
	  "WRAPPER_OPERATION_DELETE" : "WRAPPER_OPERATION_DELETE",		//delete means the path does not exist anymore. all the resources related with this wrapper should be destroy (variable, child wrapper) 

	  //wrapper variable events
	  "WRAPPERVARIABLE_EVENT_SETDATA" : "WRAPPERVARIABLE_EVENT_SETDATA",
	  "WRAPPERVARIABLE_EVENT_CLEARUP" : "WRAPPERVARIABLE_EVENT_CLEARUP",    //clear up resource

	  "WRAPPERVARIABLE_EVENT_NEW" : "WRAPPERVARIABLE_EVENT_NEW",
	  "WRAPPERVARIABLE_EVENT_DESTROY" : "WRAPPERVARIABLE_EVENT_DESTROY",
	  
	  //wrapper type
	  "WRAPPER_TYPE_OBJECT" : "object",
	  "WRAPPER_TYPE_APPDATA" : "appdata",
	  "WRAPPER_TYPE_ENTITY" : "entity",

	  "DATA_TYPE_OBJECT" : "object",
	  "DATA_TYPE_APPDATA" : "appdata",
	  "DATA_TYPE_DYNAMIC" : "dynamic",
	  
	  //context events
	  "CONTEXT_EVENT_BEFOREUPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_BEFOREUPDATE",
	  "CONTEXT_EVENT_UPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_UPDATE",
	  "CONTEXT_EVENT_AFTERUPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_AFTERUPDATE",
	  "CONTEXT_ATTRIBUTE_THISCONTEXT" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_ATTRIBUTE_THISCONTEXT",

	  //expression event
	  "EXPRESSION_EVENT_DONE" : "EXPRESSION_EVENT_DONE",
	  
	  
	  "LIFECYCLE_UIRESOURCE_EVENT_BEFOREINITCONTEXT" : "onBeforeInitContext",
	  "LIFECYCLE_UIRESOURCE_EVENT_AFTERINITCONTEXT" : "onAfterInitContext",

	  "LIFECYCLE_CONTEXT_EVENT_CLEARUP" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_CLEARUP",
	  "LIFECYCLE_CONTEXT_EVENT_BEFOREUPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_BEFOREUPDATE",
	  "LIFECYCLE_CONTEXT_EVENT_UPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_UPDATE",
	  "LIFECYCLE_CONTEXT_EVENT_AFTERUPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_AFTERUPDATE",
	  "LIFECYCLE_CONTEXT_ATTRIBUTE_THISCONTEXT" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_ATTRIBUTE_THISCONTEXT",
	  
	  
	  "LIFECYCLE_RESOURCE_EVENT_INIT" : "NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT",
	  "LIFECYCLE_RESOURCE_EVENT_DEACTIVE" : "NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE",
	  "LIFECYCLE_RESOURCE_EVENT_SUSPEND" : "NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND",
	  "LIFECYCLE_RESOURCE_EVENT_RESUME" : "NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME",
	  "LIFECYCLE_RESOURCE_EVENT_DESTROY" : "NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY",
	  "LIFECYCLE_RESOURCE_ATTRIBUTE_THISCONTEXT" : "resourceThisContext",
	  "LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION" : "LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION",
	  //status of resource : start - active - suspended - dead
	  "LIFECYCLE_RESOURCE_STATUS_START" : "START",
	  "LIFECYCLE_RESOURCE_STATUS_ACTIVE" : "ACTIVE",
	  "LIFECYCLE_RESOURCE_STATUS_SUSPENDED" : "SUSPENDED",
	  "LIFECYCLE_RESOURCE_STATUS_DEAD" : "DEAD",
	  
	  //the requester type
	  "REQUESTER_TYPE_UITAG" : "uiTag",						//requester is a ui tag
	  "REQUESTER_TYPE_SERVICE" : "service",					//requester is from service
	  
	  //request type
	  "REQUEST_TYPE_SERVICE" : 			"service",						//request type is service
	  "REQUEST_TYPE_SET" : 				"set",						//
	  "REQUEST_TYPE_SEQUENCE" : 		"sequence",						//
	  "REQUEST_TYPE_UITAG" : 			"uiTag",						//
	  "REQUEST_TYPE_DATAOPERATION" : 	"dataOperation",						//
	  "REQUEST_TYPE_SIMPLE" : 			"simple",						//
	  "REQUEST_TYPE_EXECUTOR" : 		"executor",						//
	  "REQUEST_TYPE_REMOTE" : 		"executor",						//

	  "REQUEST_TYPE_DATAOPERATION" : 	"dataoperation",			//request type is data operation
	  "REQUEST_TYPE_WRAPPEROPERATION" : "wrapperoperation",		//request type is operation on wrapper
	  
	  "REQUEST_STATUS_INIT" : 			"init",
	  "REQUEST_STATUS_ACTIVE" : 		"active",
	  "REQUEST_STATUS_ALMOSTDONE" : 	"almostDone",
	  "REQUEST_STATUS_DONE" : 			"done",
	  
	  "UITAG_ATTRIBUTE_ERRORHANDLER" : "errorHandler",

	  "UITAG_DATABINDING_NAME_DEFAULT" : "data",
	  
	  //remote service result 
	  "REMOTESERVICE_RESULT_SUCCESS" : 0,					//success result     		
	  "REMOTESERVICE_RESULT_ERROR" : 1,     				//error result (logic error)
	  "REMOTESERVICE_RESULT_EXCEPTION" : 2,     			//exception result (ajax error which is not logic error)

	  //remote service task status 
	  "REMOTESERVICE_SERVICESTATUS_PROCESSING" : 1,				//under processing
	  "REMOTESERVICE_SERVICESTATUS_SUCCESS" : 2,				//processed and get success result     		
	  "REMOTESERVICE_SERVICESTATUS_EXCEPTION" : 3,					//processed and get exception result     		
	  "REMOTESERVICE_SERVICESTATUS_QUEUE" : 4,					//service waiting for process     		
	  "REMOTESERVICE_SERVICESTATUS_FAIL" : 5,				//
	  
	  //remote service type
	  "REMOTESERVICE_TASKTYPE_NORMAL" : "normal",     		//single service
	  "REMOTESERVICE_TASKTYPE_GROUP" : "group",				//a group of service
	  "REMOTESERVICE_TASKTYPE_GROUPCHILD" : "groupchild",	//service within service group

	  //group task mode
	  "REMOTESERVICE_GROUPTASK_MODE_ALL" : "all",			//group task is success only when all child tasks are success
	  "REMOTESERVICE_GROUPTASK_MODE_ALWAYS" : "always",		//group task is always success 
	  
	  //context types
	  "CONTEXT_TYPE_QUERYENTITY" : "queryentity",			//query entity
	  "CONTEXT_TYPE_ENTITY" : "entity",						//entity data
	  "CONTEXT_TYPE_CONTAINER" : "container",				//container 
	  "CONTEXT_TYPE_DATA" : "data",							//any data
	  "CONTEXT_TYPE_OBJECT" : "object",						//normal object 
	  "CONTEXT_TYPE_SERVICE" : "service",					//service
	  
	  //type for different object
	  "TYPEDOBJECT_TYPE_EVENTOBJECT" : 			100, 
	  "TYPEDOBJECT_TYPE_VALUE" : 				0, 
	  "TYPEDOBJECT_TYPE_DATA" : 				1, 
	  "TYPEDOBJECT_TYPE_WRAPPER" :				2, 
	  "TYPEDOBJECT_TYPE_VARIABLE" : 			3, 
	  "TYPEDOBJECT_TYPE_VARIABLEWRAPPER" : 		20, 
	  "TYPEDOBJECT_TYPE_REQUEST" : 				4, 
	  "TYPEDOBJECT_TYPE_CONTEXTVARIABLE" : 		5, 
	  "TYPEDOBJECT_TYPE_CONTEXT" : 				6, 
	  "TYPEDOBJECT_TYPE_EXTENDEDCONTEXT" : 		7, 
	  "TYPEDOBJECT_TYPE_UIVIEW" :		 		8, 
	  "TYPEDOBJECT_TYPE_UIMODULE" :		 		9, 
	  "TYPEDOBJECT_TYPE_UIRESOURCEVIEW" : 		10, 
	  "TYPEDOBJECT_TYPE_UITAG" :		 		11, 
	  "TYPEDOBJECT_TYPE_APPMODULE" :		 	12, 
	  "TYPEDOBJECT_TYPE_APPMODULEUI" :		 	13, 
	  "TYPEDOBJECT_TYPE_CONFIGURES" :		 	14, 
	  "TYPEDOBJECT_TYPE_DATAASSOCIATION_DATASET" :	 			15, 
	  "TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICDATA" :	 		16, 
	  "TYPEDOBJECT_TYPE_DATAASSOCIATION" :	 					17, 
	  "TYPEDOBJECT_TYPE_PROCESS" :	 							18, 
	  "TYPEDOBJECT_TYPE_DATAASSOCIATION_EXTERNALMAPPING": 		19,
	  
	  //status of request
	  "REQUEST_STATUS_INIT" :			0, 
	  "REQUEST_STATUS_PROCESSING" :		1, 
	  "REQUEST_STATUS_DONE" :			2, 
	
	  "REQUEST_EVENT_NEW" : "NOSLIWCONSTANT.REQUEST_EVENT_NEW",
	  "REQUEST_EVENT_ACTIVE" : "NOSLIWCONSTANT.REQUEST_EVENT_ACTIVE",
	  "REQUEST_EVENT_ALMOSTDONE" : "NOSLIWCONSTANT.REQUEST_EVENT_ALMOSTDONE",
	  "REQUEST_EVENT_DONE" : "NOSLIWCONSTANT.REQUEST_EVENT_DONE",

	  "REQUESTRESULT_EVENT_SUCCESS" : "SUCCESS",
	  "REQUESTRESULT_EVENT_ERROR" : "ERROR",
	  "REQUESTRESULT_EVENT_EXCEPTION" : "EXCEPTION",
	  
	  "UIRESOURCE_FUNCTION_INIT" : 		"init",
	  "UIRESOURCE_FUNCTION_DESTROY" : 	"destroy",
	  
	  "PAGE_COMMAND_REFRESH" : "refresh"
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("CONSTANT", node_CONSTANT); 

})(packageObj);
