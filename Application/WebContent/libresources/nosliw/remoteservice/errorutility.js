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
		 * exception service data for suspended reason
		 */
		createRemoteServiceSuspendedServiceData : function(reason){
			return new node_ServiceData(
					node_COMMONCONSTANT.CONS_ERRORCODE_REMOTESERVICE_SUSPEND, 
					'No service call allowed this time, please try later!!',
					reason); 
		},
		
		/*
		 * exception service data for ajax call error
		 */
		createRemoteServiceExceptionServiceData : function(obj, textStatus, errorThrown){
			var serviceData = new node_ServiceData(
					node_COMMONCONSTANT.CONS_ERRORCODE_REMOTESERVICE_EXCEPTION,
				textStatus+'-'+errorThrown
			);
			return serviceData;
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
