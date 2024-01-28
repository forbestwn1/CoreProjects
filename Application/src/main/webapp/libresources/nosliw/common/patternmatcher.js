//get/create package
var packageObj = library.getChildPackage("patternmatcher");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_Pattern = function(pattern, onMatch){
	this.pattern = pattern;
	this.onMatch = onMatch;
};
	
var node_createPatternMatcher = function(patterns){
	
	var loc_patterns = patterns;
	if(loc_patterns==undefined)   loc_patterns = [];
	
	var loc_results = {};
	
	var loc_out = {
		
		addPattern : function(pattern){
			loc_patterns.push(pattern);
		},	
			
		match : function(content){
			var resultInfo = loc_results[content];
			if(resultInfo!=undefined){
				return loc_patterns[resultInfo.index].onMatch(resultInfo.result);
			}
			
			for(var i in loc_patterns){
				var result = loc_patterns[i].pattern.exec(content);
				if(result!=undefined){
					loc_results[content] = {
						index : i,
						result : result
					};
					return loc_patterns[i].onMatch(result);
				}
			}
		}
			
	};
	
	return loc_out;
};	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("createPatternMatcher", node_createPatternMatcher); 
packageObj.createChildNode("Pattern", node_Pattern); 

})(packageObj);
