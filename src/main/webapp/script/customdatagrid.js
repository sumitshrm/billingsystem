    
    function move(itemId){
    	itemToMove = itemId;
    }
    
    function insert(targetItem, isInsertBefore, msheetId, url){
    	window.location.href= url+"?msheetId="+msheetId+"&sourceItem="+itemToMove+"&targetItem="+targetItem+"&isInsertBefore="+isInsertBefore;
    	
    }
    
   

function createDataGrig(gridItemId, data){
            // prepare the data
           var source =
            {
                datatype: "json",
                datafields: [
                             { name: 'Particular', type: 'string' },
                             { name: 'Number', type: 'int' },
                             { name: 'Length', type: 'number' },
                             { name: 'Width', type: 'number' },
                             { name: 'Height', type: 'number' },
                             { name: 'Quantity', type: 'number' }
                         ],
                localdata: data
            };
            var dataAdapter = new $.jqx.dataAdapter(source);
            $("#"+gridItemId).jqxGrid(
            {
                width: '100%',
                autoheight: true,
                source: dataAdapter,
                columnsresize: true,
                columns: [
                          { text: 'Particular', datafield: 'Particular', width: 300 },
                          { text: 'Number', datafield: 'Number', width: 100 },
                          { text: 'Length', datafield: 'Length', width: 100 , cellsformat: 'd2'},
                          { text: 'Width', datafield: 'Width', width: 100 , cellsformat: 'd2'},
                          { text: 'Height', datafield: 'Height', width: 100, cellsformat: 'd2'},
                          { text: 'Quantity',editable:false, datafield: 'Quantity', width: 100, cellsformat: 'd2',
                        	cellsrenderer: function (index, datafield, value, defaultvalue, column, rowdata) {
                        		var n = read(rowdata.Number);
                        		var l = read(rowdata.Length);
                        		var w = read(rowdata.Width);
                        		var h = read(rowdata.Height);
                        		var num=n==""?1:n;
                        		var len=l==""?1:l;
                        		var wid=w==""?1:w;
                        		var hei=h==""?1:h;
                        		if(n!="" || l !="" ||w!=""||h!=""){
                        			var total = parseFloat(num) * parseFloat(len) * parseFloat(wid) * parseFloat(hei);
                        			return "<div style='margin: 4px;' class='jqx-right-align'>" + total + "</div>";
                        		}   

                        	}
                        }
                      ]
            });
           }


function createEditableDataGrig(gridItemId, data){
    // prepare the data
    var source =
    {
        datatype: "json",
        datafields: [
            { name: 'Particular', type: 'string' },
            { name: 'Number', type: 'int' },
            { name: 'Length', type: 'number' },
            { name: 'Width', type: 'number' },
            { name: 'Height', type: 'number' },
            { name: 'Quantity', type: 'number' }
        ],
        localdata: data
    };
    var dataAdapter = new $.jqx.dataAdapter(source);
    for (var i = 0; i < 5; i++) {
        var row = generaterow(i);
        data[i] = row;
    }
    $("#"+gridItemId).jqxGrid(
    {
        width: 803,
        autoheight: true,
        
        source: dataAdapter,
        columnsresize: true,
        editable: true,
        showtoolbar: true,
        rendertoolbar: function (toolbar) {
            var me = this;
            var container = $("<div style='margin: 5px;'></div>");
            toolbar.append(container);
            container.append('<input id="addrowbutton" type="button" value="Add New Row" />');
            container.append('<input style="margin-left: 5px;" id="addmultiplerowsbutton" type="button" value="Add Multiple New Rows" />');
            container.append('<input style="margin-left: 5px;" id="deleterowbutton" type="button" value="Delete Selected Row" />');
            // update row.
           
            // create new row.
            $("#addrowbutton").on('click', function () {
                var datarow = generaterow();
                var commit = $("#jqxgrid").jqxGrid('addrow', null, datarow);
            });
            // create new rows.
            $("#addmultiplerowsbutton").on('click', function () {
                $("#jqxgrid").jqxGrid('beginupdate');
                for (var i = 0; i < 5; i++) {
                    var datarow = generaterow();
                    var commit = $("#jqxgrid").jqxGrid('addrow', null, datarow);
                }
                $("#jqxgrid").jqxGrid('endupdate');
            });
            // delete row.
            $("#deleterowbutton").on('click', function () {
                var selectedrowindex = $("#jqxgrid").jqxGrid('getselectedrowindex');
                var rowscount = $("#jqxgrid").jqxGrid('getdatainformation').rowscount;
                if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
                    var id = $("#jqxgrid").jqxGrid('getrowid', selectedrowindex);
                    var commit = $("#jqxgrid").jqxGrid('deleterow', id);
                }
            });
        },
        
        columns: [
            { text: 'Particular', datafield: 'Particular', width: 300},
            { text: 'Number', datafield: 'Number', width: 100 },
            { text: 'Length', datafield: 'Length', width: 100, cellsformat: 'd2' },
            { text: 'Width', datafield: 'Width', width: 100 , cellsformat: 'd2' },
            { text: 'Height', datafield: 'Height', width: 100, cellsformat: 'd2' },
            { text: 'Quantity', datafield: 'Quantity', width: 100, editable:false, cellsformat: 'd2' }
        ]
    });  
    }

function read(val){
	if(val==undefined)
		return "";
	else 
		return val;
}

var generaterow = function (i) {
    var row = {};
    /*var productindex = Math.floor(Math.random() * productNames.length);
    var price = parseFloat(priceValues[productindex]);
    var quantity = 1 + Math.round(Math.random() * 10);
    row["firstname"] = firstNames[Math.floor(Math.random() * firstNames.length)];
    row["lastname"] = lastNames[Math.floor(Math.random() * lastNames.length)];
    row["productname"] = productNames[productindex];
    row["price"] = price;
    row["quantity"] = quantity;
    row["total"] = price * quantity;*/
    return row;
}


