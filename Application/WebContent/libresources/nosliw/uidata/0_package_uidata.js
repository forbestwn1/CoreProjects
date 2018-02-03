var library = nosliw.getPackage("uidata");


/**
 * value : 	data itself, have no data type information

 * data : 	value + data type
 * 			data can only used for reading, not for writing
 * wrapper:
 * 			wrapper should not be exposed to user, only variable
 * 			wrapper has two ways to do it:  data based and parent wrapper based
 * 				data based : it store root data and the path from root data to this data
 * 				parent wrapper based: it store parent wrapper and the path from data in parent wrapper to this data
 * 			wrapper is for data operation request, when through wrapper, all other wrapper is informed of the data change
 * 			with wrapper, you can do followings:
 * 				get current data represented by wrapper
 * 				create child wrapper based on path to child wrapper
 * 				register listeners for data operation event
 * 				operate on data according to request infor and trigue event
 * 			three type of events: 
 * 				data operation event : inform children and variable about what happend within wrapper
 * 				lifecycle event : inform children about lifeccycle winthin wrapper : clearup
 * 				internal event : 
 * 					inform children about data operation event. 
 * 					for instance, for delete element operation, the container will receive the DELETEELEMENT event
 * 					then beside forward the same event, the container will triggue another event DELETE for delete child
 * 					this DELETE event should not be processed by variable, it should only be delivered to responding children  
 *  
 * variable: 
 * 			variable is exposed to user
 * 			a variable that can contain wrapper that can be set to differen value
 * 			variable only listen to data operation event from wrapper, no lifecycle event
 * 			two types of wrapper variables: normal and child 
 * 			child variable is dependent on normal variable: its wrapper is wrapper based on wrapper within parent variable
 * 			variable event : 
 * 				SETWRAPPER
 * 				CLEARUP
 * 			wrapper operation event:
 * 				CHANGE
 * 				ADDELEMENT
 * 				DELETEELEMENT	
 * 
 * variable wrapper : 
 * 			
 * 
 * context : 
 * 			a set of normal wrapper variables
 * 			event : 
 * 				BEFOREUPDATE
 * 				AFTERUPDATE
 * 				UPDATE
 * 
 * contextVariable: 
 * 			not a real variable
 * 			name + path to describe the variable
 * 
 */
