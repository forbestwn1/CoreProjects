/**
 * 
 */
var nosliwCreateRequestSimple = function(service, processor, handlers, requester_parent){

	var loc_processorFun = processor;
	
	/*
	 * exectue function 
	 */
	var loc_process = function(requestInfo){
		var out = loc_processorFun.call(this, requestInfo);
		loc_out.executeSuccessHandler(out, loc_out);
	};
		
	var loc_out = {
			
	};
	
	loc_out = _.extend(nosliwCreateServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(NOSLIWCONSTANT.REQUEST_TYPE_SIMPLE);
	
	loc_out.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};
