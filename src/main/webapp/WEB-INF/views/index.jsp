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
							html,
							body {
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
							$(document).ready(function () {

								// var x1 = math.format(math.eval('1.2 + 1.56 + 2.38+0.01'), 12);
								// console.log(x1);

								var container = document.getElementById('example1');

								var hot = new Handsontable(container, {
									startRows: 11,
									startCols: 6,
									rowHeaders: true,
									colHeaders: true,
									colWidths: 70,
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
									],
									// minSpareRows: 1,

									afterChange: function (changes, source) {

										if (!changes || source === 'loadData') {
											return;
										}

										$.each(changes, function (index, element) {
											var row = element[0];
											var col = element[1];
											var oldVal = element[2];
											var newVal = element[3];

											console.log("row[" + row + "],col[" + col + "],oldVal[" + oldVal + "],newVal[" + newVal + "],countCols[" + hot.countCols() + "],countRows[" + hot.countRows() + "]");

											if (row == 1 && col == 'baseDate.year') {
												data = helper.getStockSheetList(newVal);
												helper.sheetRefresh(hot, data);
											}
										});
									},
									cells: function (row, col, prop) {
										var cellProperties = {};
										if (row === 1 && col === 0) {
											cellProperties.renderer = helper.stockCodeRenderer;
										}
										return cellProperties;
									}
								});

								helper.initLayout(hot);
								// var cell = hot.getDataAtCell(3,2);
								// console.log("cell"+cell);
								// var arr = hot.getDataAtCol(1);
								// console.log("arr"+arr);

								$('#epsBtn').click(function () {

									var stockCode = $('#stockCode').val();
									
									$.ajax({
										url: "/mint/stock/lastestEps/" + stockCode,
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
									
									var stockCode = $('#stockCode').val();
									
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

							<div id="example1" />

							<hr noshade="noshade" style="border:1px #cccccc dotted;" size="2" />

							<div id="example" />

						</div>

						<div>
							<input type="text" id="stockCode" name="stockCode" />
							<input id="epsBtn" type="button" value="eps" />
							<input id="dividendBtn" type="button" value="dividend" />
						</div>
					</body>

					</html>