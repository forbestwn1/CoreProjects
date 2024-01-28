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

var node_RequestResult = function(resultType, resultData){
	this.type = resultType;
	this.data = resultData;
}

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("RequestResult", node_RequestResult); 
packageObj.createChildNode("Requester", node_Requester); 
packageObj.createChildNode("ServiceRequestExecuteInfo", node_ServiceRequestExecuteInfo); 

})(packageObj);
