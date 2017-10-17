<!DOCTYPE html>
<html>
	<!-- Contexts define the variables input for this ui resource -->
	<body contexts="business@object;key@object">

	<%= #|?(a)?.aa.largerThan(?(dddd)?)|#.value + ' 6666 ' %>  tttttttttt  


		<br><br>
		Context variable data used in content:<br>
		<%= #|?(key)?.a.aa|#.value%>   

		<br><br>
		Constant value:<br>
		{{hh.a}}
		<%={{hh.b}}[1]%>


		<br><br>
		Attribute Expression in stardand tag:<br>
		<span  style="color:<%=#|?(key)?.a.aa|#.value=='4165628566'?'blue':'red'%>">Phone Number : </span>
		
		<br><br>
		Expression content using constants defined in constants part<br>
		Also when the variable (key)'s value changed, the expression should be recalcuated
		<br> 	

	<%= #|?(key)?.a.aa.largerThan(?(dddd)?)|#.value + ' 6666 ' %>  tttttttttt  
		<br>
	<%= #|?(key)?.a.aa.largerThan(?(cccc)?)|#.value + ' 6666 ' %>  tttttttttt  
		<br>
	<%= #|?(key)?.a.aa.largerThan(?(eeee)?)|#.value + ' 6666 ' %>  tttttttttt  
		<br>
	<%= #|?(ffff)?.subString(?(key)?.a.aa)|#.value + ' 6666 ' %>  tttttttttt  
		<br>
<!--	<%= #|?(ffff)?.subStringServer(?(key)?.a.aa)|#.value + ' 6666 ' %>  tttttttttt  -->


<!--		<%= :(test1):.cascade(!(string:simple)!.toString(?(bbbb)?).cascade(?(aaaa)?)) %>    -->

		<br><br>
		Event in standard tag:<br>
		<a href='' nosliw-event="click:testLinkEvent:">New Department</a>

		<br><br>
		Input Tag to change the variable value:<br>
		nosliw-data is used to define the binded variable (two way)<br>
		for regular attribute, the value can be expression and can only readable <br>
		nosliw-event is used to define the event response
		<nosliw-textinput nosliw-data="key.a.aa" nosliw-event="change:testTextInputChangeEvent:"/>

		<br><br>
		Customer tag : loop:<br>
		It is an example that a customer tag has its own content
		Regarding variables scope in tag conent
			All the variables in parent is visible
			Customer tag may define new variable and user can name the variable through attribute, for instance "loopValue", "name" 
		
		<nosliw-loop nosliw-data="key" data="loopValue" key="name">
			<br>
			In the loop
			<br>
			Varaibles in the loop content			
			<%= #|?(name)?|#.value%> :
			<%= #|?(loopValue)?.aa|#.value%>
			<br><br>
			Event in the loop content
			<a href='' nosliw-event="click:deleteElement:">Delete</a>
			<br>
			
			<!-- Script part in customer tag content -->
			<script>
			{
				deleteElement : function(data, info){
					info.event.preventDefault();
					var context = resourceView.getContext();
					var key = context.getContextElementData("name").value;
					var container = context.getContextElementVariable("key");
					
					container.requestDataOperation(new NosliwServiceInfo("WRAPPER_OPERATION_DELETEELEMENT", {index:key}));
				},
	
			}			
			</script>
		</nosliw-loop>
		<a href='' nosliw-event="click:addElement:">Add</a>

		<br><br>
		Customer tag : switch/case<br>
		<br>Switch:<br>
		<nosliw-switch evaluate="<%=#|?(key)?.a.aa|#.value%>">
			Test Switch Case: <br>
			<nosliw-case value='11'> 
				Red
			</nosliw-case>
			<nosliw-case value='4165628566'> 
				Blue
			</nosliw-case>
			<nosliw-default> 
				Default
			</nosliw-default>
			<br>
		</nosliw-switch>

	</body>

	<script>
	{
		<!-- Script define all the functions used by event call -->
		testLinkEvent : function(data, info){
			event.preventDefault();
			alert("aaaaa");
		},

		testTextInputChangeEvent : function(data, info){
			alert("bbbb");
		},

		index : 555,
		addElement : function(data, info){
			info.event.preventDefault();
			var context = resourceView.getContext();
			var container = context.getContextElementVariable("key");
			this.index++;
			container.requestDataOperation(new NosliwServiceInfo("WRAPPER_OPERATION_ADDELEMENT", {index:this.index+"", data:{aa:nosliwCreateData(11, new NosliwDataTypeInfo("simple", "integer"))}}));
		},
	    
	}
	</script>
	
	
		<!-- This part can be used to define constants: js objects that can be calculated out during compilation of the ui resource file
			 it can be defined in any form:
			 	js value
			 	ui expression
			 since every constant is basically a js value, so it can be used anywhere that need js value 

			hh : {
				a : "heloop",
				b : [1, 2, 3]
			},
			aaaa : ":(#string:simple:  constantValue  ):",
			cccc : ":({
	    		dataTypeInfo:{
	    			categary : 'simple',
		    		type : 'integer',
		    	},
	    		value : 8,
	    	}):",
			dddd : ":(#integer:simple:8):",
			eeee : "!(integer:simple)!.new(:(#string:simple:8):)",
			ffff : ":(#string:simple:11223344):",


			 -->
	<constants>
	{
			aaaa : "<%=5+6+7%>",
			bbbb : "<%=(5+6+7)>5%>",
			cccc : {
						a : 12345,
						b : true,
						c : "good",
						d : "<%=5+6+7%>"
					},
			dddd : "<%=&(cccc)&.a+6%>",

			ffff : "<%=#|&(#test##string___Thisismyworldabcdef)&|#%>",
			eeee : "<%=#|&(ffff)&.subString(from:&(#test##integer___3)&,to:&(#test##integer___7)&)|#%>",
	}
	</constants>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<context>
	{
		business : {
			definition: {
				a : {
					aa : "abc.abc;1.0.0%||a:cde;1.1.1,b:dfe;1.0.0||%",
					bb : "test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&(expression)&;;&(parms)&)||@||%"
				}
			},
			default: {
				
			}
		},
		b : {
			definition: "*",
			default: {
			
			}
		}		
	}
	</context>
	
		<!-- This part can be used to define expressions
		-->
	<expressions>
	{
		
	
	}
	</expressions>
	
</html>

