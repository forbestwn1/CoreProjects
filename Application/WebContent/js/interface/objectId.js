/*
 * utility functions to build object with id
 */
var nosliwObjectWithIdUtility = function(){

	return {
		/*
		 * build an object to typed object
		 */
		makeObjectWithId : function(obj){
			var out = _.extend(obj, nosliwCreateObjectWithId());
			return out;
		},
	};
}();


/*
 * object with type information
 */
var nosliwCreateObjectWithId = function(){
	var loc_out = {
		objectId : nosliw.generateId(),
		getObjectId :function(){  return this.objectId; },
	};
	return loc_out;
};




