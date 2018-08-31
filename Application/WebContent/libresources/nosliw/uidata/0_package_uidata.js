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
 * 		variable can contain wrapper 
 * 		
 * 
 * 			a variable that can contain wrapper that can be set to differen value
 * 			variable only listen to data operation event from wrapper, NO lifecycle event. 
 * 			It is because all the all the wrapper lifecycle is driven by variable
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
 * 		wrapper of variable
 *		only variable wrapper is exposed to user, no data wrapper or variable
 *		no lifecycle event exposed, only data operation event exposed
 *		whoever create variable wrapper, please release it
 * 
 * context : 
 * 			a set of normal variables wrappers
 * 			event : 
 * 				BEFOREUPDATE
 * 				AFTERUPDATE
 * 				UPDATE
 * 
 * contextVariable info: 
 * 			not a real variable
 * 			name + path to describe the variable
 * 
 */


/*
wrapper:
wrapper is about data operation, get value
adapter :   provided by variable
	value adapter 
	path adapter
	

data operation event : (listened by variable, child wrapper)
all data operation is converted to data operation on root data, then trigue data operation event 	
root wrapper event trigue by data operation:	
	WRAPPER_EVENT_SET : WRAPPER_OPERATION_SET
	WRAPPER_EVENT_ADDELEMENT: WRAPPER_OPERATION_ADDELEMENT
	WRAPPER_EVENT_DELETEELEMENT:  WRAPPER_OPERATION_DELETEELEMENT
	WRAPPER_EVENT_DELETE :  WRAPPER_OPERATION_DELETE
	
leaf wrapper 
	WRAPPER_EVENT_CHANGE : inform child that the data is dirty
	WRAPPER_EVENT_FORWARD : event happened on child node, just forward event to child who maybe response to this event
	
	WRAPPER_EVENT_DELETE : when got WRAPPER_EVENT_DELETE event from parent, or grand parent ...
	WRAPPER_EVENT_ADDELEMENT : when got WRAPPER_EVENT_ADDELEMENT event from parent
	WRAPPER_EVENT_DELETEELEMENT : when got WRAPPER_EVENT_DELETEELEMENT event from parent
	WRAPPER_EVENT_SET : when got WRAPPER_EVENT_SET event from parent
	
lifecycle event : (listened by child wrapper only)
	WRAPPER_EVENT_CLEARUP    when wrapper is destroyed (which may trigued by destroy method call or parent CLEARUP event)

internal event : (listened by child wrapper only)
	WRAPPER_EVENT_DELETE	 when wrapper is informed by WRAPPER_EVENT_DELETEELEMENT to delete element within this wrapper, trigue delete event with path on child wrapper

when wrapper data is deleted, wrapper will trigue two event:
	DELETE
	CLEARUP
	
	
variable :
	variable is about 
	not like wrapper which only reference to parent wrapper, variable also contains child variables (loop through child)
	the reason containing child variable is that when create child variable, parent can reuse variable
	
adapter : 
	valueAdapter
	pathAdapter
	eventAdapter
	destroyAdapter
	dataOperationAdapter
	
variable only listen to operation event from wrapper, not lifecycle event, not internal event
variable does not need to listen to lifecycle event from wrapper, because 
	variable will determine lifecycle of wrapper (either by variable.destory or delete operation)

communication between parent variable and child variable is only about two event related with delete: WRAPPER_EVENT_DELETE, WRAPPER_EVENT_CLEARUP
	
lifecycle event: (listened by parent variable only, so that parent can remove child from itself)
	WRAPPER_EVENT_CLEARUP	when child variable is destroyed, 
	when parent variable is destroyed, parent will destroy all children
	
data operation event : (listened by variable wrapper)
	any data operation event from wrapper
	
	
	
for wrapper with any adapter (pathAdapter) does not allow child wrapper with empty path
it will cause issue when operate on child wrapper 	
	
	
	
	
	
	 * 
 */


