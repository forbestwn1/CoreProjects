
nosliwCommonUtility = function(){
	
	return {
		
		/*
		 * create an value with meaning of empty
		 */
		createEmptyValue : function(){return "alkfjalsdkfjsafjoweiurwerwelkjdlsjdf";},
		
		isEmptyValue : function(value){ return value== "alkfjalsdkfjsafjoweiurwerwelkjdlsjdf";},
		
		emptyStringIfUndefined : function(value){ 
			if(value==undefined)  return "";
			return value;
		},
		
		/*
		 * merge two object and create a new one
		 * specificOne will override the defaultone object
		 */
		mergeObjects : function(defaultOne, specificOne){
			
			var out = {};
			_.each(defaultOne, function(attr, name, list){
				if(specificOne===undefined || specificOne[name]===undefined){
					out[name] = attr;
				}
				else{
					out[name] = specificOne[name];
				}
			});
			return out;
		},
		
		cloneObject : function(object){
			var newObject = jQuery.extend(true, {}, object);	
			return newObject;
		},
		
		isStringEmpty : function(str){
			if(str==undefined || str+''=='')  return true;
			else false;
		},

		cascadePath : function(path1, path2){
			return this.cascadeParts(path1, path2, NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_PATH);
		},

		cascadePart : function(part1, part2){
			return this.cascadeParts(part1, part2, NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_PART);
		},
		
		cascadeParts : function(part1, part2, seperator){
			var out;
			if(this.isStringEmpty(part1)){
				out = part2;
			}
			else{
				if(this.isStringEmpty(part2))  out = part1;
				else	out = part1 + seperator + part2;
			}
			return out;
		},
		
		htmlDecode : function(input){
			var e = document.createElement('div');
			e.innerHTML = input;
			return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
		},

		isAtomData : function(data){
			if(_.isString(data) || _.isNumber(data) || _.isBoolean(data))  return true;
		},
		
	};
}();

