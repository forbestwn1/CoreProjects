Parent Data Type:
   
Linked Data Type:   
	

all the method of data should provided through operation
	getDefaultData();
	getDomainDatas();
	validate(HAPData data);
	clone


Design stage: 
	Decide how to build expression to solve problem
	In this stage, it is language (implementation) independent
	1. Write the expression. Find proper data type and operations to build expression
	2. Compile the expression.    
	3. Optimize the expression. 

1. Expression as literate
2. Expression in object structure
3. Variables in expression and their data type
4. Operations involved in expression
5. Add data type converting in expression  


Runtime stage: 
	When decide to execute expression in particular environment
	Prepard:
		1. ExecuteManager request ResourceManager for all resources in order to execute expression   
		2. Discovery. ResourceManager find all resource required, and return back to execute manager
		3. ExecuteManager process required resources. After it find out what it lack, it ask ResourceManager to load them to ExecuteManager 
		3. When some resource cannot be loaded, it means that it cannot run in environment
	Execute: When expression is prepared
		1. Execute expression in the runtime env by ExecuteManager

	
	
Three managers:
1. DataTypeManager. 
	It is used in design time
	It has all the information for data type and operations belong to data type
	It provides function:
		a. Query data type
		b. Query operations belong to data type
	
2. ResourceManager. 
	Depend on DataTypeManager
	Resource means all the things needed in order to execute expression
	Examples: 
		class file in java
		js file in javascript
		third party library
		resource file (image, )
	It manage all the resources required in order to execute expression
	
3. ExecuteManager. 
	It is located in Runtime Env
	Depend on ResourceManager
		a. It manage resources loaded in Runtime Env. It knows what resources are lack so that should be loaded from ResourceManager
		b. It execute expression in Runtime Env
	
