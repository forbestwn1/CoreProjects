/**
 * 
 */
var nosliwErrorManager = function(){
	
	var m_createServiceData = function(code, message, data){
		return {
			'code' : code,
			'message' : message,
			'data' : data,
		};
	};

	var m_init = function(){
		_.extend(m_manager, Backbone.Events);
	};
	
	var m_manager = {
		CODE_SUCCESS : 200,
		CODE_FAILURE : 400,

		CODE_ERROR : 800,
		CODE_INVALID_ATTRIBUTE : 8001,

		CODE_ERROR_UNCOVER : 1000,
		CODE_ERROR_NETWORK : 1010,
		CODE_ERROR_SERVER : 1020,

		CODE_ERROR_SERVICECLOSE : 1001,
		
			
		clearErrorMessage : function(reqInfo){
			m_manager.trigger('error', "clear", reqInfo);
			m_manager.trigger('reqId:'+reqInfo.eleId, "clear", reqInfo);
		},
		
		createErrorMessage : function(reqInfo, serviceData){
			var errorHandler = reqInfo.errorHandler;
			var errorCodes = reqInfo.errorCodes;
			var handleByRequester = true;
			
			if(errorHandler==undefined)   handleByRequester = false;
			
			var triggerEvent = true;
			if(handleByRequester==true){
				//have error handler
				if(errorHandler=='none')   triggerEvent = false;
				else triggerEvent = reqInfo.parentResouce.callFunction(errorHandler, reqInfo, serviceData);
			}
			
			if(triggerEvent==true){
				m_manager.trigger('error', "new", reqInfo, serviceData);
				m_manager.trigger('reqId:'+reqInfo.eleId, "new", reqInfo, serviceData);
			}
		},
		
		createValidationFailureServiceData : function(wraper, rule, value){
			var serviceData = {
				code : 	this.CODE_INVALID_ATTRIBUTE,
				message : rule.errorMsg,
				data : {
					wraper : wraper,
					rule : rule,
				},
			};
			return serviceData;
		},
	};
	
	m_init();
	
	return m_manager;
}();
