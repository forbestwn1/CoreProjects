//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
//*******************************************   Start Node Definition  ************************************** 	

/*
 * store requester object
 * 	type: requestor type (tag, resoruce, system, ...)
 * 	id : requestor id
 * 	info: other information related with type
 */
var Requester = function(type, id, info){
	this.type = type;
	this.id = id;
	this.info = info;
};

/*
 * information about request execute 
 *     function to execute
 *     thisContext for function
 */
var ServiceRequestExecuteInfo = function(fun, thisContext){
	this.pri_method = fun;
	this.pri_thisContext = thisContext;
};

ServiceRequestExecuteInfo.prototype = {
	execute : function(requestInfo){
		return this.pri_method.call(this.pri_context, requestInfo);		
	},
};

/*
 * information about child service
 * child service and parent have the same reqeuster
 * 		requestInfo : 	request infor for child service
 * 		processor: 		do something after child request return
 */
DependentServiceRequestInfo = function(requestInfo, processors){
	this.requestInfo = requestInfo;
	this.processors = processors;
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("Requester", Requester); 
packageObj.createNode("ServiceRequestExecuteInfo", ServiceRequestExecuteInfo); 
packageObj.createNode("DependentServiceRequestInfo", DependentServiceRequestInfo); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
