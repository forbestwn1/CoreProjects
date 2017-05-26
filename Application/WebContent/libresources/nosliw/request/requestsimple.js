//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * The request's process is done by a function
 */
var createServiceRequestInfoSimple = function(service, processor, handlers, requester_parent){

	var loc_processorFun = processor;
	
	/*
	 * exectue function 
	 */
	var loc_process = function(requestInfo){
		var out = loc_processorFun.call(this, requestInfo);
		loc_out.executeSuccessHandler(out, loc_out);
	};
		
	var loc_out = {
			
	};
	
	loc_out = _.extend(nosliwCreateServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_SIMPLE);
	
	loc_out.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createServiceRequestInfoSimple", createServiceRequestInfoSimple); 

	var module = {
		start : function(packageObj){
			node_CONSTANT = packageObj.getNodeData("constant.NOSLIWCONSTANT");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
