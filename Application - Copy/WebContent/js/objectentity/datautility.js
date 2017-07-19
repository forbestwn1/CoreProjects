/**
 * 
 */
nosliwDataUtility = function(){
	
	return {
		/*
		 * create object data by value
		 * if object is already data, then do nothing
		 * otherwise, wraper it to data 
		 */
		createDataByObject : function(object){
			var out = object;
			if(nosliwTypedObjectUtility.getObjectType(object)!=NOSLIWCONSTANT.TYPEDOBJECT_TYPE_DATA){
				out = this.createDataByValue(object);
			}
			return out;
		},
		
		/*
		 * create object data by value 
		 */
		createDataByValue : function(value){
			return nosliwCreateData(value, nosliwObjectUtility.dataTypeInfo);
		},
		
		createEmptyData : function(){
			return nosliwCreateData("");
		},
		
		isEmptyData : function(data){
			if(data==undefined)  return true;
			if(data.dataTypeInfo==undefined)  return true;
			return false;
		},
		
	};	
}();

