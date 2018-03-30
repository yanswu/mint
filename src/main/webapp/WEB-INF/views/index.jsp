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
				
				var arr =[{},{}];
				//
					var x1 = math.format(math.eval('1.2 + 1.56 + 2.38+0.01'), 12);
					console.log(x1);
					
					
					var container1 = document.getElementById('example1');
					var hot1;

					hot1 = new Handsontable(container1, {
						//data: arr,
					  	startRows: 12,
					  	startCols: 12,
					  	colHeaders: true,
						rowHeaders: true,
						colWidths:  70,
						columns: [
							{ data: "baseDate.year" },
							{ data: "baseDate.a" },
							{ data: "baseDate.b" },
							{ data: "baseDate.c" },
							{ data: "baseDate.d" },
							{ data: "baseDate.e" },
							{ data: "totalEps" },
							{ data: "cashDividend" },
							{ data: "stockDividend" },
						]  ,
					  	minSpareRows: 1,
					  
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
						    //    if(col+1<hot1.countCols()) {
								   if(row == 1 && col =='baseDate.year'){
									   console.log('hello['+ newVal+"]");
									   data = getStockSheetList(newVal);
									   renderTable(hot1,data);
								   }
						    //    }
				      		});
						},
						cells: function (row, col, prop) {
							var cellProperties = {};
							if (row === 1 && col === 0) {
								cellProperties.renderer = stockCodeRenderer; 
							}
							return cellProperties;
						}	  
					});
					
					renderTable(hot1,arr);
					// var cell = hot1.getDataAtCell(3,2);
					// console.log("cell"+cell);
					// var arr = hot1.getDataAtCol(1);
					// console.log("arr"+arr);
					
					var stockCode = $('#stockCode').val();
					$('#epsBtn').click(function(){
						
						$.ajax({
							url: "/mint/stock/lastestEps/"+stockCode,
							contentType: 'application/json',
							type: "PUT",
							dataType: 'json',
							success: function (data) {

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
			
			</div>

			<div>
					<input type="text" id="stockCode" name="stockCode" />
					<input id="epsBtn" type="button" value="eps" />
					<input id="dividendBtn" type="button" value="dividend" />
			</div>
	</body>
</html> 