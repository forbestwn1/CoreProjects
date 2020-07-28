/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_storyChangeUtility;
	var node_storyUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItemSwitch = function(){

	var loc_vueComponent = {
		data : function(){
			return {
			};
		},
		props : ['data', 'story'],
		components : {
		},
		methods : {
		},
		computed: {
			choices : {
				get : function(){
					var element = node_storyUtility.getQuestionTargetElement(this.story, this.data);
					return element[node_COMMONATRIBUTECONSTANT.ELEMENTGROUP_ELEMENTS];
				}
			},
			choiceId : {
				get : function(){
					return this.data.element.choice;
				},
				
				set : function(choiceId){
					node_storyChangeUtility.applyPatchFromQuestion(this.story, this.data, node_COMMONATRIBUTECONSTANT.ELEMENTGROUPSWITCH_CHOICE, choiceId, this.data.changes);
				}
			}
		},
		template : `
			<div>
				<select v-model="choiceId">
				  <option v-for="choice in choices" v-bind:value="choice.id">
				    {{ choice.name }}
				  </option>
				</select>			

				<br>
				Question Switch
				<br>
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemSwitch", node_createComponentQuestionItemSwitch); 

})(packageObj);
