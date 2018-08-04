<!DOCTYPE html>
<html>
<body>
		<br>
		Sum:<%=#|?(schoolList)?.length()|#.value%>  

		<br>

		<nosliw-loop data="schoolList" element="ele" index="index">  
			<br>
			SchoolName: <%=#|?(ele)?.getChildData(name:&(schoolAttribute)&)|#.value%>   
			<br>
			<event>
			[
				{
					name : "selectSchool",
					parms : {
						schoolData : {
							path: "ele"
						}
					},
				}
			]
			</event>
		</nosliw-loop>

</body>

	<event>
	[
		{
			name : "selectSchool",
			parms : {
				schoolData : {
					path: "schoolList.element"
				}
			},
		}
	]
	</event>
	
	<service>
	[
	]
	</service>

	<command>
	[
	]
	</command>

	<script>
	{
	
	}
	</script>
	
	<constants>
	{
		schoolAttribute : {
			dataTypeId: "test.string;1.0.0",
			value: "schoolName"
		}
	}
	</constants>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<context>
	{
		public : {
			schoolList : {
				definition : "test.array;1.0.0%%||element:test.map;1.0.0%%||geo:test.geo;1.0.0,schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0||%%||%%",
				default : {
					dataTypeId: "test.array;1.0.0",
					value: [
						{
							dataTypeId: "test.map;1.0.0",
							value: {
								schoolName : {
									dataTypeId: "test.string;1.0.0",
									value: "School1"
								},
								schoolRating : {
									dataTypeId: "test.float;1.0.0",
									value: 6.0
								},
								geo : {
									dataTypeId: "test.geo;1.0.0",
									value: {
										"latitude" :  43.651299,
										"longitude" : -79.579473
									}
								}
							}
						},
						{
							dataTypeId: "test.map;1.0.0",
							value: {
								schoolName : {
									dataTypeId: "test.string;1.0.0",
									value: "School2"
								},
								schoolRating : {
									dataTypeId: "test.float;1.0.0",
									value: 8.5
								},
								geo : {
									dataTypeId: "test.geo;1.0.0",
									value: {
										"latitude" :  43.649016, 
										"longitude" : -79.485059
									}
								}
							}
						}					
					]
				}
			},
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

