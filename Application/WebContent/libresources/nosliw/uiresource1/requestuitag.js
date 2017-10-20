/**
 * 
 */
var nosliwCreateRequestUITag = function(service, dataBindings, handlers, requester_parent){

	var loc_constructor = function(service, dataBindings, handlers, requester_parent){
		//all the child requests service  
		loc_out.pri_childRequestServices = [];
		
		//data bindings
		loc_out.pri_dataBindings = dataBindings;
	};
	
	/*
	 * exectue function 
	 */
	var loc_process = function(requestInfo){
		_.each(loc_out.pri_childRequestServices, function(childRequestService, index){
			var dataBinding = loc_out.pri_dataBindings[childRequestService.parms.dataBinding];
			dataBinding.requestDataOperation(childRequestService, requestInfo);
		}, this);

		loc_out.executeSuccessHandler({}, loc_out);
	};
		
	var loc_out = {
		addOperationRequest : function(childRequestService){
			this.pri_childRequestServices.push(childRequestService);
		},
			
	};
	
	loc_out = _.extend(nosliwCreateServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	loc_constructor(service, dataBindings, handlers, requester_parent);
	
	//request type
	loc_out.setType(NOSLIWCONSTANT.REQUEST_TYPE_UITAG);
	
	loc_out.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};
