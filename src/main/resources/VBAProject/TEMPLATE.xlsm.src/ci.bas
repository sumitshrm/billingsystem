Attribute VB_Name = "ci"

Sub cnie()

    If checkOffline Then
    Exit Sub
    End If
    Call populateItems2
    showNewItemForm ("")

End Sub


Public Sub populateItems2()
Dim lo As Excel.ListObject
Dim lr As Excel.ListRow
Dim fullrate
'Add item numbers
NewMeasurementWindow.ItemNumber_ComboBox.Clear
endRow = range("TABLE_ITEMS_ITEM_NUM").Cells(Rows.count, 1).End(xlUp).row

For i = 2 To endRow
'search_value = Chr(34) & Range("TABLE_EXTRA_ITEMS_ITEM_NUM").Cells(I, 1).value & Chr(34) & "&" & Chr(34) & "*" & Chr(34)
search_value = range("TABLE_ITEMS_ITEM_NUM").Cells(i, 1).value & ".*" '"EI1*"
count = Application.WorksheetFunction.CountIf(range("TABLE_ITEMS_ITEM_NUM"), search_value)
'remove existing value first
If count < 1 Then
NewMeasurementWindow.ItemNumber_ComboBox.AddItem range("TABLE_ITEMS_ITEM_NUM").Cells(i, 1).value
End If
Next i
'Add extra item numbers
endRow = range("TABLE_EXTRA_ITEMS_ITEM_NUM").Cells(Rows.count, 1).End(xlUp).row
For i = 2 To endRow
'search_value = Chr(34) & Range("TABLE_EXTRA_ITEMS_ITEM_NUM").Cells(I, 1).value & Chr(34) & "&" & Chr(34) & "*" & Chr(34)
search_value = range("TABLE_EXTRA_ITEMS_ITEM_NUM").Cells(i, 1).value & ".*" '"EI1*"
count = Application.WorksheetFunction.CountIf(range("TABLE_EXTRA_ITEMS_ITEM_NUM"), search_value)
'remove existing value first
If count < 1 Then
NewMeasurementWindow.ItemNumber_ComboBox.AddItem range("TABLE_EXTRA_ITEMS_ITEM_NUM").Cells(i, 1).value
End If
Next i
End Sub

Function FindLastColumn() As String
    Dim LastCol_int As Integer
    Dim LastCol_str As String
    With ActiveSheet
        LastCol_int = .Cells(1, .Columns.count).End(xlToLeft).Column
    End With
    LastCol_str = Chr(64 + LastCol_int) 'convert to abphabet
End Function

Function updateItemNum(itemNumRow, itemNumValue)
Dim selectedItem As String
Dim previousItemNum As String
Dim totalRowStart As Integer
Dim totalRowStartCell As range
Dim totalRowCell As range
selectedItem = itemNumValue
previousItemNum = Selection.value
Dim cell_format
'update item number
range("M_ITEM_NUM").Cells(itemNumRow, 1).value = itemNumValue
'update description
totalSubItemsPrevious = countChrInString(previousItemNum, ".")
totalSubItemsNew = countChrInString(selectedItem, ".")
'delete existing item description rows.
startRow = itemNumRow + 1
endRow = startRow + totalSubItemsPrevious
If startRow <= endRow Then
rowsRange = startRow & ":" & endRow
Rows(rowsRange).Select
Selection.Delete Shift:=xlUp
End If

Cells(Application.ActiveCell.row, 1).Select

'insert new description rows
For indx = 1 To totalSubItemsNew + 1
    Selection.EntireRow.Insert
    Selection.ClearFormats
    pos = getCharPosition(itemNumValue, indx, ".")
    fullItemNum = selectedItem
    If pos > 0 Then
    fullItemNum = Left(itemNumValue, pos)
    End If
    Call new_item_3(fullItemNum)
    Next indx
totalRowStart = Selection.row
'update total formula and  unit format
Dim unit_value As String
unit_value = getUnit(CStr(itemNumValue))
cell_format = "0.00 """ & unit_value & """"
totalRow = itemNumRow + 1
Do While Not range("M_ITEM_NUM_METADATA").Cells(totalRow, 1).value = "t"
totalRow = totalRow + 1
Loop
Set totalRowCell = range("M_TOTAL_QTY").Cells(totalRow, 1)
Set totalRowStartCell = range("M_TOTAL_QTY").Cells(totalRowStart - 1, 1)
Dim total_formula As String
total_formula = "=SUM(" & totalRowStartCell.Address(False, False) & ":offset(" & totalRowCell.Address(False, False) & ",-1,0))"

range("M_TOTAL_QTY").Cells(totalRow, 1).value = total_formula
range("M_TOTAL_QTY").Cells(totalRow, 1).NumberFormat = cell_format
Selection.Offset(1, 0).Select
autofitrow
End Function

Function showNewItemForm(Optional rNum)
If rNum <> "" Then
NewMeasurementWindow.Activity = "update"
NewMeasurementWindow.ItemNumRowNum = rNum
NewMeasurementWindow.message = " Update:" & vbCrLf & " Item Number"
Else
NewMeasurementWindow.Activity = "create"
NewMeasurementWindow.message = " Add:" & vbCrLf & " Items entry at the end of sheet"
End If
NewMeasurementWindow.ItemNumber_ComboBox.SetFocus
If itemsRowSelected Then
NewMeasurementWindow.message = " Add:" & vbCrLf & " Items entry before selected item."
End If
NewMeasurementWindow.Show
End Function

Function itemsRowSelected()
result = False
If Selection.Address = ActiveCell.EntireRow.Address Then
If range("MEASUREMENT_PAGE_NUMBER").Cells(ActiveCell.row, 1).value = "i" Then
result = True
End If
End If
itemsRowSelected = result
End Function


