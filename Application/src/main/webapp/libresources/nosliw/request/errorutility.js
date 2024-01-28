//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_COMMONCONSTANT;
var node_ServiceData;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_errorUtility = function(){
	return {
		/*
		 * error service data for request process
		 */
		createRequestProcessErrorServiceData : function(errObj){
			nosliw.logging.error(errObj.stack);
			return new node_ServiceData(
					node_COMMONCONSTANT.ERRORCODE_ERROR_UI_REQUESTPROCESS, 
					'fatal error!!',
					errObj.stack); 
		},
		
		/*
		 * error service data for request process
		 */
		createRequestHandleErrorServiceData : function(errObj){
			nosliw.logging.error(errObj.stack);
			return new node_ServiceData(
					node_COMMONCONSTANT.ERRORCODE_ERROR_UI_REQUESTHANDLE, 
					'fatal error',
					errObj.stack); 
		},
	};
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("error.entity.ServiceData", function(){node_ServiceData = this.getData();});

//Register Node by Name
packageObj.createChildNode("errorUtility", node_errorUtility); 

})(packageObj);
