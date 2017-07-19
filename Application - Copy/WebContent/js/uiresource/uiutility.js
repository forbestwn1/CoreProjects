
var nosliwUIResourceUtility = function(){

	return {
		/*
		 * create place holder html with special ui id 
		 */
		createPlaceHolderWithId : function(id){
			return "<nosliw style=\"display:none;\" nosliwid=\"" + id + "\"></nosliw>";
		},
		
		/*
		 * build context
		 * 		1. read context information for this resource from uiResource
		 * 		2. add extra element infos
		 */
		buildUIResourceContext : function(uiResource, contextElementInfoArray){
			var contextElementsInf = [];
			
			//get element info from resource definition
			var resourceAttrs = uiResource[NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_UIRESOURCE_ATTRIBUTES];
			if(resourceAttrs!=undefined){
				var contextStr = resourceAttrs.contexts;
				var contextSegs = nosliwCreateSegmentParser(contextStr, NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_ELEMENT);
				while(contextSegs.hasNext()){
					var name = undefined;
					var element = undefined;
					var contextSeg = contextSegs.next();
					var index = contextSeg.indexOf("@");
					if(index==-1){
						name = contextSeg;
						info = {};
					}
					else{
						name = contextSeg.substring(0, index);
						var type = contextSeg.substring(index+1);
						info = {wrapperType:type};
					}
					contextElementsInf.push(nosliwCreateContextElementInfo(name, info));
				}
			}

			//add extra element info
			_.each(contextElementInfoArray, function(contextElementInfo, key){
				contextElementsInf.push(contextElementInfo);
			}, this);
			
			return nosliwCreateContext(contextElementsInf);
		},
		
		/*
		 * update all the ui id within html by adding space name ahead of them
		 */
		updateHtmlUIId : function(html, idNameSpace){
			var find = NOSLIWCOMMONCONSTANT.CONS_UIRESOURCE_ATTRIBUTE_UIID+"=\"";
			return html.replace(new RegExp(find, 'g'), NOSLIWCOMMONCONSTANT.CONS_UIRESOURCE_ATTRIBUTE_UIID+"=\""+idNameSpace+NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_FULLNAME);
		},

		
	};		
}();
