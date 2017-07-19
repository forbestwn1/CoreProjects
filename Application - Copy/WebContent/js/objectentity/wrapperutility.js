/**
 * 
 */
nosliwWrapperUtility = function(){
	
	return {
		createDataOperationServiceSet : function(path, data){
			var parms = {
				path : path,
				data : data,
			};
			return new NosliwServiceInfo(NOSLIWCONSTANT.WRAPPER_OPERATION_SET, parms);
		},
		
		createDataOperationServiceDeleteElement : function(index){
			var parms = {
				index : index,
			}
			return new NosliwServiceInfo(NOSLIWCONSTANT.WRAPPER_OPERATION_DELETEELEMENT, parms);
		},

		createDataOperationServiceAddElement : function(index, data){
			var parms = {
				index : index,
				data : data,
			}
			return new NosliwServiceInfo(NOSLIWCONSTANT.WRAPPER_OPERATION_ADDELEMENT, parms);
		},
		
	};
}();


