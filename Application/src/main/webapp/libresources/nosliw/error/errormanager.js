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
	var loc_MAX_ERROR = 5;
	
	var loc_init = function(){
		
		if (typeof window != "undefined"){
			//global error process
			window.onerror = function(msg, url, line, col, error) {
				loc_consoleError(error);
				var errorEle = {
					type : "uncaughtError",
					message : msg,
					url : url,
					line : line,
					col :col,
					error : loc_buildErrorEle(error),
				};
				var errorData = loc_addErrorToStorage(errorEle);
				loc_logErrorToServer(errorData);
			   return true;
			};
		}
	};
	
	var loc_addErrorToStorage = function(errorEle){
		if(typeof localStorage !== 'undefined'){
			var errorData = loc_getErrorFromStorage();
			if(errorData==undefined) errorData = [];
			
			errorData.push(errorEle);
			if(errorData.length>loc_MAX_ERROR){
				//keep less than max errors 
				errorData = errorData.slice(0, errorData.length);
			}
			localStorage.errorData = JSON.stringify(errorData);
			return errorData;
		}
	};
	
	var loc_buildErrorEle = function(error){
		var out = {
			name : error.name,
			line : error.line,
			message : error.message,
			errorStack : JSON.stringify(error.stack),
			time : new Date(),
		};
		return out;
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
	
	var loc_consoleError = function(error){
		if (typeof console != "undefined") console.log(error);
	}
	
	var loc_logErrorToServer = function(errorData){
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
			loc_consoleError(error);
			var errorData = loc_addErrorToStorage(loc_buildErrorEle(error));
			loc_logErrorToServer(errorData);
		},
		
		logErrorIfHasAny : function(){
			var errorData = loc_getErrorFromStorage();
			if(errorData!=undefined){
				//if have previous error, then log it
				loc_logErrorToServer(errorData);
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
