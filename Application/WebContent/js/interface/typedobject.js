/*
 * utility functions to build lifecycle object
 */
var nosliwTypedObjectUtility = function(){

	return {
		/*
		 * build an object to typed object
		 */
		makeTypedObject : function(obj, type){
			var out = _.extend(obj, nosliwCreateTypedObject(type));
			return out;
		},
		
		/*
		 * get object's type info
		 * if no type info, the use VALUE as type  
		 */
		getObjectType : function(object){
			if(object==undefined)   return NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VALUE;
			var type = undefined;
			if(typeof object.getObjectType === 'function'){
				type = object.getObjectType();
			}
			
			if(type!=undefined)    return type;
			else return NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VALUE;
		},
	};
}();


/*
 * object with type information
 */
var nosliwCreateTypedObject = function(type){
	var loc_type = type;
	
	var loc_out = {
		objectId : nosliw.generateId(),
		getObjectType :function(){  return loc_type; },
	};
	return loc_out;
};

