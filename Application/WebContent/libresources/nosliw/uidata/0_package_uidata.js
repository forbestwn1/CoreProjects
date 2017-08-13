var library = nosliw.getPackage("uidata");


/**
 * value : 	data itself, have no data type information

 * data : 	value + data type
 * 			data can only used for reading, not for writing
 * wrapper:
 * 			wrapper has two ways to do it:  data based and parent wrapper based
 * 				data based : it store root data and the path from root data to this data
 * 				parent wrapper based: it store parent wrapper and the path from data in parent wrapper to this data
 * 			wrapper is for data operation request, when through wrapper, all other wrapper is informed of the data change
 * 			with wrapper, you can do followings:
 * 				get current data represented by wrapper
 * 				create child wrapper based on path to child wrapper
 * 				register listeners for data operation event
 * 				operate on data according to request infor and trigue event
 *  
 * wrapper variable: 
 * 			a variable that can contain wrapper that can be set to differen value
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
 * context : 
 * 			a set of normal wrapper variables
 * 			event : 
 * 				BEFOREUPDATE
 * 				AFTERUPDATE
 * 				UPDATE
 * 
 */
