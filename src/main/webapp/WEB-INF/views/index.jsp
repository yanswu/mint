<%@page import="idv.mint.context.AppSettings"%>
<%@page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>App ...</title>
		
		<style type="text/css">
			html, body {
				margin: 0px;
				padding: 0;
				height: 100%;
			}
			
			#content {
				width: 800px;
				margin: 0px auto;
			}
			#example1 {
				width: 800px;
				margin: 20px auto;
			}
		</style>
		
		<script>
			$(document).ready(function(){
				
				var arr =[];
				
				 
				$.ajax({
					url: "/mint/stock/" + 2330,
					contentType: 'application/json',
					type: "GET",
					dataType: 'json',
					async : false,
					success: function (data) {
						arr = data.stockSheetList;
						arr.unshift({});
						arr.unshift({});
					},
					error: function (e) {
						console.log("ERROR: ", e);
					},
					done: function (e) {
						console.log("DONE");
					}
				});
				//
					var x1 = math.format(math.eval('1.2 + 1.56 + 2.38+0.01'), 12);
					console.log(x1);
					
					
					var container1 = document.getElementById('example1');
					var hot1;

					hot1 = new Handsontable(container1, {
						data: arr,
					  	startRows: 10,
					  	startCols: 20,
					  	colHeaders: true,
						rowHeaders: true,
						//manualColumnResize : true,
						columns: [
							{ data: "baseDate.year" },
							{ data: "x" },
							{ data: "x" },
							{ data: "x" },
							{ data: "x" },
							{ data: "x" },
							{ data: "totalEps" },
							{ data: "cashDividend" },
							{ data: "stockDividend" },
						]  ,
					  	//minSpareRows: 1,
					  
					    afterChange: function(changes, source) {
					    	if(!changes || source === 'loadData') {
					      		return;
					  	    }
				       		$.each(changes, function(index, element) {
						       var row = element[0];
						       var col = element[1];
						       var oldVal = element[2];
						       var newVal = element[3];
						       
						       console.log("row["+row+"],col["+col+"],oldVal["+oldVal+"],newVal["+newVal+"],countCols["+hot1.countCols()+"],countRows["+hot1.countRows()+"]");
						       if(col+1<hot1.countCols()) {
						       		//data[row][col+1]=parseInt(data[row][col+1])+parseInt(newVal);
						         	hot1.loadData(arr);
						       }
				      		});
						}	  
						});
					
					 hot1.setDataAtCell(1, 1, "還原$");
					 hot1.setDataAtCell(1, 2, "ROE%");
					 hot1.setDataAtCell(1, 3, "4盈再%");
					 hot1.setDataAtCell(1, 4, "常利$m");
					 hot1.setDataAtCell(1, 5, "配息%");
					 hot1.setDataAtCell(1, 6, "常EPS$");
					 hot1.setDataAtCell(1, 7, "股息$");
					 hot1.setDataAtCell(1, 8, "股子");
					// var cell = hot1.getDataAtCell(3,2);
					// console.log("cell"+cell);
					// var arr = hot1.getDataAtCol(1);
					// console.log("arr"+arr);
					
					//hot1.alter('insert_row', 13);
					//hot1.alter('insert_col', 22);
					
					var stockCode = $('#stockCode').val();
					$('#epsBtn').click(function(){
						$.ajax({
							url: "/mint/stock/lastestEps/"+stockCode,
							contentType: 'application/json',
							type: "PUT",
							dataType: 'json',
							success: function (data) {

								//$("#tabulator-example").tabulator("setData", data);
							},
							error: function (e) {
								console.log("ERROR: ", e);
							},
							done: function (e) {
								console.log("DONE");
							}
						});
					});

					$('#dividendBtn').click(function () {
					$.ajax({
						url: "/mint/stock/lastestDividend/" + stockCode,
						//contentType: 'application/json',
						type: "PUT",
						dataType: 'json',
						success: function (data) {
							//$("#tabulator-example").tabulator("setData", data);
						},
						error: function (e) {
							console.log("ERROR: ", e);
						},
						done: function (e) {
							console.log("DONE");
						}
					});
				});
					
			});
		</script>
	</head>

	<body>
			<div id="content">
			
				<div id="example1"/>
				
			<hr noshade="noshade" style="border:1px #cccccc dotted;" size="2" />
			
			<div id="example"/>
			
			<div id="tabulator-example"/>
			
			</div>

			<div>
					<input type="text" id="stockCode" name="stockCode" />
					<input id="epsBtn" type="button" value="eps" />
					<input id="dividendBtn" type="button" value="dividend" />
			</div>
	</body>
</html> 