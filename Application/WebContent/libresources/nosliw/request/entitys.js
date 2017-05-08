/*
 * store requester object
 * 	type: requestor type (tag, resoruce, system, ...)
 * 	id : requestor id
 * 	info: other information related with type
 */
var NosliwRequester = function(type, id, info){
	this.type = type;
	this.id = id;
	this.info = info;
};

/*
 * information about request execute 
 *     function to execute
 *     thisContext for function
 */
var NosliwServiceRequestExecuteInfo = function(fun, thisContext){
	this.pri_method = fun;
	this.pri_thisContext = thisContext;
};

NosliwServiceRequestExecuteInfo.prototype = {
	execute : function(requestInfo){
		return this.pri_method.call(this.pri_context, requestInfo);		
	},
};

