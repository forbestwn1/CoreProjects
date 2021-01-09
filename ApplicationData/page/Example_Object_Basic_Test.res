<!DOCTYPE html>
<html>
<body>
		<nosliw-map data="arraylist" element="ele" index="index">  
		<span class="intag">BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB</span>
			<br>
			Index: <%=?(index)?%>
			<br>

			<%=?(ele.schoolName)?.value + '   7777 ' %>
			<br>
			TextInput:<nosliw-string data="ele.schoolName"/> 
			<br>

		</nosliw-map>
  

</body>

	<scripts>
	{

	}
	</scripts>
	
		<!-- This part can be used to define context (variable)
				it describle data type criteria for each context element and its default value
		-->
	<contexts>
	{
		group : {
			public : {
				element : {
					arraylist : {
						definition: {
							criteria:"test.array;1.0.0%%||element:test.map;1.0.0%%||schoolName:test.string;1.0.0,schoolLocation:test.geo;1.0.0||%%||%%",
						},
						defaultValue : {
							dataTypeId: "test.array;1.0.0",
							value: [
								{
									dataTypeId: "test.map;1.0.0",
									value : {
										schoolName : {
											dataTypeId: "test.string;1.0.0",
											value : "School1"
										},
										geo : {
											dataTypeId: "test.geo;1.0.0",
											value : {
												latitude : 43.8100763,
												longitude : -79.3558245,
											}
										}
									}
								},
								{
									dataTypeId: "test.map;1.0.0",
									value : {
										schoolName : {
											dataTypeId: "test.string;1.0.0",
											value : "School2"
										},
										geo : {
											dataTypeId: "test.geo;1.0.0",
											value : {
												latitude : 43.7541051,
												longitude : -79.3089712,
											}
										}
									}
								},
							]							
						}
					},
					business : {
						definition: {
							child : {
								a : {
									child : {
										aa : {criteria:"test.string;1.0.0"},
										cc : {criteria:"test.array;1.0.0%%||element:test.map;1.0.0%%||schoolName:test.string;1.0.0,schoolLocation:test.geo;1.0.0||%%||%%",},
									}
								}
							}
						},
						defaultValue: {
							a : {
								aa : {
									dataTypeId: "test.string;1.0.0",
									value: "This is my world!"
								},
								cc : {
									dataTypeId: "test.array;1.0.0",
									value: [
										{
											dataTypeId: "test.map;1.0.0",
											value : {
												schoolName : {
													dataTypeId: "test.string;1.0.0",
													value : "School1"
												},
												geo : {
													dataTypeId: "test.geo;1.0.0",
													value : {
														latitude : 43.8100763,
														longitude : -79.3558245,
													}
												}
											}
										},
										{
											dataTypeId: "test.map;1.0.0",
											value : {
												schoolName : {
													dataTypeId: "test.string;1.0.0",
													value : "School2"
												},
												geo : {
													dataTypeId: "test.geo;1.0.0",
													value : {
														latitude : 43.7541051,
														longitude : -79.3089712,
													}
												}
											}
										},
									]
								},
							}
						}
					},

				}
			}
		}
	}
	</contexts>
	
	<events>
	[
		{
			name : "changeInputText",
			data : {
				element : {
					data : {
						definition : {
							path: "business.a.aa"
						}
					}
				}
			}
		}
	]
	</events>
	
	<commands>
	[
		{
			name : "Start",
			parm : {
				element : {
					data : {
						definition : {
							path: "business.a.aa"
						}
					}
				}
			}
		}
	]
	</commands>
	
	<attachment>
	{
		"expression" : [
		],
		
		"data": [
					
		]
	}
	</attachment>

</html>
