//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_Requester;
//*******************************************   Start Node Definition  ************************************** 	

var node_buildServiceProvider = function(object, moduleName){
	var loc_moduleName = moduleName;
	
	//default requester 
	var loc_requester = new node_Requester(node_CONSTANT.REQUESTER_TYPE_SERVICE, loc_moduleName); 
	
	var loc_service = {
		getModuleName : function(){
			return loc_moduleName;
		},
	
		getRequestInfo : function(request){
			if(request==undefined)   return loc_requester;
			else return request;
		}
	};
	
	var loc_out = _.extend(object, loc_service);
	
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("buildServiceProvider", node_buildServiceProvider); 

var module = {
		start : function(packageObj){
			node_CONSTANT = packageObj.getNodeData("constant.CONSTANT");
			node_Requester = packageObj.getNodeData("request.entity.Requester");
		}
};
nosliw.registerModule(module, packageObj);

})(packageObj);
