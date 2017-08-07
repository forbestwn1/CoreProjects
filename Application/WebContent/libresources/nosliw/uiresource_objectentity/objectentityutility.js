/**
 * 
 */
nosliwObjectEntityUtility = function(){
	
	return {
		
		/*
		 * get data according to object
		 */
		getObjectEntityData : function(object){
			var type = nosliwTypedObjectUtility.getObjectType(object);
			if(type==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_DATA){
				return object;
			}
			else if(type==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
				return object.getData();
			}
			else if(type==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
				return object.getData();
			}
			else if(type==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VALUE){
				return nosliwDataUtility.createDataByValue(object);
			}
		},
		
	};
	
}();


