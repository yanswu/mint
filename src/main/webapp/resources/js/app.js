var helper = {

    initLayout : function (hot) {


        // init sheet columns header
        hot.setDataAtCell(1, 1, "還原$");
        hot.setDataAtCell(1, 2, "ROE%");
        hot.setDataAtCell(1, 3, "4盈再%");
        hot.setDataAtCell(1, 4, "淨利$m");
        hot.setDataAtCell(1, 5, "配息%");
        hot.setDataAtCell(1, 6, "EPS$");
        hot.setDataAtCell(1, 7, "股息$");
        hot.setDataAtCell(1, 8, "股子");
    },

    sheetRefresh : function(hot, data) {

        // stockCompany
        

        // stockSheet content
        $.each(data,function(index,el){
            var row = 2+index;
            hot.setDataAtCell(row,0,el.baseDate.year);
            hot.setDataAtCell(row,1,'');
            hot.setDataAtCell(row,2,'');
            hot.setDataAtCell(row,3,'');
            hot.setDataAtCell(row,4,'');
            hot.setDataAtCell(row,5,'');
            hot.setDataAtCell(row, 6, el.totalEps);
            hot.setDataAtCell(row, 7, el.cashDividend);
            hot.setDataAtCell(row, 8, el.stockDividend);
        });
        
    },



    getStockSheetList : function (stockCode) {
        var arr = [];
        $.ajax({
            url: "/mint/stock/" + stockCode,
            contentType: 'application/json',
            type: "GET",
            dataType: 'json',
            async: false,
            success: function (data) {
                arr = data.stockSheetList;
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
        return arr;
    },

    

    stockCodeRenderer: function (instance, td) {
        Handsontable.renderers.TextRenderer.apply(this, arguments);
        td.style.color = 'red';
        return td;
    },

};



