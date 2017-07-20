//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_eventUtility;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_errorUtility = function(){
	return {
		/*
		 * exception service data for suspended reason
		 */
		createRemoteServiceSuspendedServiceData : function(reason){
			return new NosliwServiceData(
					NOSLIWCOMMONCONSTANT.CONS_ERRORCODE_REMOTESERVICE_SUSPEND, 
					'No service call allowed this time, please try later!!',
					reason); 
		},
		
		/*
		 * exception service data for ajax call error
		 */
		createRemoteServiceExceptionServiceData : function(obj, textStatus, errorThrown){
			var serviceData = new NosliwServiceData(
					NOSLIWCOMMONCONSTANT.CONS_ERRORCODE_REMOTESERVICE_EXCEPTION,
				textStatus+'-'+errorThrown
			);
			return serviceData;
		},
	};
}();

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createChildNode("errorUtility", node_errorUtility); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
