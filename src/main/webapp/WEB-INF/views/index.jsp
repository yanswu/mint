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
				
				
				
				$('#tabulator-example').tabulator({
					height: "350px", // set height of table, this enables the Virtual DOM and improves render speed dramatically (can be any valid css height value)
					//layout: "fitColumns", //fit columns to width of table (optional)
					columns: [ //Define Table Columns
						{ title: "ROCYear", field: "baseDate.year", width: 90 },
						{ title: "ESPQ1", field: "epsQ1", align: "right" , width: 90},
						{ title: "ESPQ2", field: "epsQ2", align: "right" , width: 90},
						{ title: "ESPQ3", field: "epsQ3", align: "right" , width: 90},
						{ title: "ESPQ4", field: "epsQ4", align: "right" , width: 90},
						{ title: "CASH_DIVIDEND", field: "cashDividend", align: "right" , width: 90},
						{ title: "STOCK_DIVIDEND", field: "stockDividend", align: "right" , width: 90}
					],
					rowClick: function (e, row) { //trigger an alert message when the row is clicked
						alert("Row " + row.getData().id + " Clicked!!!!");
					},
					// "data":data,
					// "columns":[
					// 	{"data":"baseDate.year"},
					// 	{ "data":"epsQ1"},
					// 	{ "data": "epsQ2" },
					// 	{ "data": "epsQ3" },
					// 	{ "data": "epsQ4" },
					// ]
				});
				 
				$.ajax({
					url: "/mint/crawl/stock/sheet/" + 2330,
					contentType: 'application/json',
					type: "GET",
					
					dataType: 'json',
					success: function (data) {
					
						//var arr  = $.parseJSON(data);
						//$('#container1').html(JSON.stringify(data));

						//load sample data into the table
						$("#tabulator-example").tabulator("setData", data);
					},
					error: function (e) {
						console.log("ERROR: ", e);
					},
					done: function (e) {
						console.log("DONE");
					}
				});		
				//

					
					
					var
					  data = [
					    ['', 'Kia', 'Nissan', 'Toyota', 'Honda'],
					    ['2008', 10, 11, 12, 13],
					    ['2009', 20, 11, 14, 13],
					    ['2019', 30, 15, 12, 13]
					  ],
					  container1 = document.getElementById('example1'),
					  hot1;

					hot1 = new Handsontable(container1, {
					  data: data,
					  startRows: 10,
					  startCols: 20,
					  colHeaders: true,
					  rowHeaders: true,
					  minSpareRows: 1,
						afterSelection: function(r,c){
					    	var data = this.getDataAtRow(r);
					      console.log(data)
					    }
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
	</body>
</html> 