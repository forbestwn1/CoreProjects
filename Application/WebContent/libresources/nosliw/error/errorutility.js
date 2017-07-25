//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	var loc_out = {
		
			isSuccess : function(serviceData){
				if(serviceData==undefined)  return true;
				if(node_COMMONCONSTANT.SERVICECODE_FAILURE > serviceData.code)  return true;
				else return false;
			},
		
			isFail : function(serviceData){
				if(this.isSuccess(serviceData))  return false;
				else return true;
			},

			createSuccessServiceData : function(){
				var serviceData = {
					code : 	node_COMMONCONSTANT.SERVICECODE_FAILURE,
					message : "",
					data : {
					},
				};
				return serviceData;
			},

			createErrorServiceData : function(){
				var serviceData = {
					code : 	node_COMMONCONSTANT.SERVICECODE_FAILURE,
					message : "",
					data : {
					},
				};
				return serviceData;
			},
			
			/*
			 * get result status from serviceData: success, exception, error
			 */
			getServiceDataStatus : function(serviceData){
				if(this.isSuccess(serviceData)){
					return node_CONSTANT.REMOTESERVICE_RESULT_SUCCESS;
				}
				else{
					var code = serviceData.code;
					if(code>=node_COMMONCONSTANT.SERVICECODE_EXCEPTION){
						return node_CONSTANT.REMOTESERVICE_RESULT_EXCEPTION;
					}
					else{
						return node_CONSTANT.REMOTESERVICE_RESULT_ERROR;
					}
				}
			},
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
