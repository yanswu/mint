function stockCodeRenderer(instance, td) {
    Handsontable.renderers.TextRenderer.apply(this, arguments);
        td.style.color = 'red';
    return td;
};

function getStockSheetList(stockCode){
    var arr = [];
    $.ajax({
        url: "/mint/stock/" + stockCode,
        contentType: 'application/json',
        type: "GET",
        dataType: 'json',
        async: false,
        success: function (data) {
            arr = data.stockSheetList;
            arr.unshift({});
            arr.unshift({});
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
    return arr;
};

function renderTable(hot,data){

    hot.loadData(data);
    hot.setDataAtCell(1, 1, "還原$");
    hot.setDataAtCell(1, 2, "ROE%");
    hot.setDataAtCell(1, 3, "4盈再%");
    hot.setDataAtCell(1, 4, "常利$m");
    hot.setDataAtCell(1, 5, "配息%");
    hot.setDataAtCell(1, 6, "常EPS$");
    hot.setDataAtCell(1, 7, "股息$");
    hot.setDataAtCell(1, 8, "股子");

    hot.setDataAtCell
}