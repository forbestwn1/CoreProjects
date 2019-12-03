//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_requestServiceProcessor;
//*******************************************   Start Node Definition  ************************************** 	

var node_createErrorManager = function(){
	
	var loc_init = function(){
	};
	
	var loc_addErrorToStorage = function(error){
		if(typeof localStorage !== 'undefined'){
			var errorData = loc_getErrorFromStorage();
			if(errorData==undefined) errorData = [];
			
			try{
				var stackStr = JSON.stringify(error.stack);
			}
			catch(e){
				
			}
			
			errorData.push({
				time : new Date(),
				error : stackStr,
				line : e.lineNumber
			});
			localStorage.errorData = JSON.stringify(errorData);
			return errorData;
		}
	};
	
	var loc_clearErrorInStorage = function(){
		if(typeof localStorage !== 'undefined') localStorage.removeItem("errorData");
	};
	
	var loc_getErrorFromStorage = function(){
		if(typeof localStorage !== 'undefined'){
			var errorData;
			var errorDataStr = localStorage.errorData;
			if(errorDataStr!=undefined)  	errorData = JSON.parse(errorDataStr);
			return errorData;
		}
	};
	
	var loc_logError = function(errorData){
		//gateway request
		var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_ERRORLOG;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYERRORLOGGER_COMMAND_LOGERRRO;
		var parms = {};
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYERRORLOGGER_PARMS_ERROR] = errorData;
		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
			success : function(request, data){
				loc_clearErrorInStorage();
			},
			error : function(request, serviceData){
			},
			exception : function(request, serviceData){
			},
		});
		node_requestServiceProcessor.processRequest(gatewayRequest);
	};
	
	var loc_out = {
		logError : function(error){
			if (typeof console != "undefined") console.log(error);
			var errorData = loc_addErrorToStorage(error);
			loc_logError(errorData);
		},
		logErrorIfHasAny : function(){
			var errorData = loc_getErrorFromStorage();
			if(errorData!=undefined){
				//if have previous error, then log it
				loc_logError(errorData);
			}
		}
	};
	
	loc_init();
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createErrorManager", node_createErrorManager); 

})(packageObj);
