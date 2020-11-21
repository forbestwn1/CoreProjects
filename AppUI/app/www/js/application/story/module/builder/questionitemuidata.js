/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_storyChangeUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItemUIData = function(){

	var loc_vueComponent = {
		data : function(){
			return {
			};
		},
		props : ['question', 'story'],
		components : {
		},
		methods : {
		},
		template : `
			<div>
				{{question.question}}:
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemUIData", node_createComponentQuestionItemUIData); 

})(packageObj);
