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
		props : ['question', 'story'],
		components : {
		},
		methods : {
		},
		computed: {
			choices : {
				get : function(){
					var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
					return element[node_COMMONATRIBUTECONSTANT.ELEMENTGROUP_ELEMENTS];
				}
			},
			choiceId : {
				get : function(){
					return this.question.element.choice;
				},
				
				set : function(choiceName){
					node_storyChangeUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.ELEMENTGROUPSWITCH_CHOICE, choiceName, this.question.answer);
					this.$emit("answerChange");
				}
			}
		},
		template : `
			<select style="display:inline;" v-model="choiceId">
			  <option v-for="choice in choices" v-bind:value="choice.name">
			    {{ choice.name }}
			  </option>
			</select>			
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
