/**
 * 
 */
var nosliwErrorUtility = function(){
	var loc_out = {
		
			isSuccess : function(serviceData){
				if(serviceData==undefined)  return true;
				if(NOSLIWCOMMONCONSTANT.CONS_SERVICECODE_FAILURE > serviceData.code)  return true;
				else return false;
			},
		
			isFail : function(serviceData){
				if(nosliwErrorUtility.isSuccess(serviceData))  return false;
				else return true;
			},

			createSuccessServiceData : function(){
				var serviceData = {
					code : 	NOSLIWCOMMONCONSTANT.CONS_SERVICECODE_FAILURE,
					message : "",
					data : {
					},
				};
				return serviceData;
			},

			createErrorServiceData : function(){
				var serviceData = {
					code : 	NOSLIWCOMMONCONSTANT.CONS_SERVICECODE_FAILURE,
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
				if(nosliwErrorUtility.isSuccess(serviceData)){
					return NOSLIWCONSTANT.REMOTESERVICE_RESULT_SUCCESS;
				}
				else{
					var code = serviceData.code;
					if(code>=NOSLIWCOMMONCONSTANT.CONS_SERVICECODE_EXCEPTION){
						return NOSLIWCONSTANT.REMOTESERVICE_RESULT_EXCEPTION;
					}
					else{
						return NOSLIWCONSTANT.REMOTESERVICE_RESULT_ERROR;
					}
				}
			},
			
	};
	return loc_out;
}();

