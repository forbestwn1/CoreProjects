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
var node_Requester = function(type, id, info){
	this.type = type;
	this.id = id;
	this.info = info;
};

/*
 * information about request execute 
 *     function to execute
 *     thisContext for function
 */
var node_ServiceRequestExecuteInfo = function(fun, thisContext){
	this.pri_method = fun;
	this.pri_thisContext = thisContext;
};

node_ServiceRequestExecuteInfo.prototype = {
	execute : function(requestInfo){
		return this.pri_method.call(this.pri_context, requestInfo);		
	},
};


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("Requester", node_Requester); 
packageObj.createNode("ServiceRequestExecuteInfo", node_ServiceRequestExecuteInfo); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
