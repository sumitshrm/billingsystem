Attribute VB_Name = "NewMeasurementItemSub_Module"
Sub new_item_1(insertNewRow, selectedItem As String)
'
' new_item_1 Macro
'

'
    Dim last_row
    Dim numberOfRows
    last_row = Cells.find("*", SearchOrder:=xlByRows, SearchDirection:=xlPrevious).row + 1
    Dim selected_cell_range As String
    
    Dim last_col
    'TODO update getLastColumn
    
    'find number or rows needed default is 5
    numberOfRows = 5
    totalSubItems = countChrInString(selectedItem, ".")
    If totalSubItems > 1 Then
    numberOfRows = numberOfRows + totalSubItems
    End If
    
    If insertNewRow Then
    last_row = ActiveCell.row - 1
    ActiveCell.Offset(-1, 0).Select
    For indx = 1 To numberOfRows
    Selection.EntireRow.Insert
    Selection.ClearFormats
    Next indx
    
    End If
    selected_cell_range = intToChar(range("M_ITEM_DESC").Column) & last_row
    range(selected_cell_range).Select
    last_col = getLastColumn
    Sheets("MEASUREMENT").Select
    ActiveCell.value = range("T_DATE_OF_MEASUREMENT").Cells(1, 1).value
    Sheets("TEMPLATE").Select
    range("T_DATE_OF_MEASUREMENT").Select
    Selection.Copy
    Sheets("MEASUREMENT").Select
    Dim range_1 As range
    Set range_1 = range(intToChar(range("M_ITEM_DESC").Column) & last_row & ":" & last_col & last_row)
    range_1.Select
    Selection.PasteSpecial Paste:=xlPasteFormats, Operation:=xlNone, _
        SkipBlanks:=False, Transpose:=False
    Application.CutCopyMode = False
    Selection.Borders(xlDiagonalDown).LineStyle = xlNone
    Selection.Borders(xlDiagonalUp).LineStyle = xlNone
    With Selection.Borders(xlEdgeLeft)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeTop)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeBottom)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeRight)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    Selection.Borders(xlInsideVertical).LineStyle = xlNone
    Selection.Borders(xlInsideHorizontal).LineStyle = xlNone
    range(selected_cell_range).Select
    Selection.Offset(1, 0).Select

End Sub
Sub new_item_2(itemNum)
'
' new_item_2 Macro
'

'
    Dim selected_row
    selected_row = Selection.row
    Dim selected_col
    selected_col = Chr(Selection.Column + 64)
    Dim last_col
    'TODO
    last_col = getLastColumn
    Sheets("MEASUREMENT").Select
    Dim formula As String
    Dim label As String
    'check if item is extra item.
    If InStr(1, itemNum, range("T_EXTRA_ITEM_SUFFIX").Cells(1, 1).value) = 1 Then
    label = range("T_AGGREEMENT_EXTRA_ITEM_NUMBER_LABEL").Cells(1, 1).value
    Else
    label = range("T_AGGREEMENT_ITEM_NUMBER_LABEL").Cells(1, 1).value
    End If
    formula = "=""" & label & """ & M_ITEM_NUM"
        ActiveCell.formula = formula
    Sheets("TEMPLATE").Select
    range("T_AGGREEMENT_ITEM_NUMBER_LABEL").Select
    Selection.Copy
    Sheets("MEASUREMENT").Select
    Selection.PasteSpecial Paste:=xlPasteFormats, Operation:=xlNone, _
        SkipBlanks:=False, Transpose:=False
    Dim range_1 As range
    range("M_ITEM_NUM").Cells(selected_row, 1).value = itemNum
    range("MEASUREMENT_PAGE_NUMBER").Cells(selected_row, 1).value = "i"
    range("M_ITEM_NUM_METADATA").Cells(selected_row, 1).value = "i"
    Set range_1 = range(intToChar(range("M_ITEM_DESC").Column + 1) & selected_row & ":" & last_col & selected_row)
    range_1.Select
    Application.CutCopyMode = False
    Selection.Merge
    Set range_1 = range(intToChar(range("M_ITEM_DESC").Column) & selected_row & ":" & last_col & selected_row)
    range_1.Select
    Selection.Borders(xlDiagonalDown).LineStyle = xlNone
    Selection.Borders(xlDiagonalUp).LineStyle = xlNone
    With Selection.Borders(xlEdgeLeft)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeTop)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeBottom)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeRight)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    Selection.Borders(xlInsideVertical).LineStyle = xlNone
    Selection.Borders(xlInsideHorizontal).LineStyle = xlNone
    range(selected_col & selected_row).Select
    Selection.Offset(1, 0).Select
End Sub

Sub new_item_3(itemNum)
'
' new_item_3 Macro
'

'
    Dim selected_row
    selected_row = Selection.row
    Dim selected_col
    selected_col = intToChar(Selection.Column)
    Dim first_col
    Dim last_col
    last_col = getLastColumn
    
       
    Sheets("MEASUREMENT").Select
    first_col = intToChar(range("M_ITEM_DESC").Column)
    range(first_col & selected_row & ":" & last_col & selected_row).Select
    With Selection
        .HorizontalAlignment = xlCenter
        .VerticalAlignment = xlBottom
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
    Selection.Merge
    With Selection
        .HorizontalAlignment = xlLeft
        .VerticalAlignment = xlBottom
        .WrapText = False
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = True
    End With
    Selection.Borders(xlDiagonalDown).LineStyle = xlNone
    Selection.Borders(xlDiagonalUp).LineStyle = xlNone
    With Selection.Borders(xlEdgeLeft)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeTop)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeBottom)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeRight)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    Selection.Borders(xlInsideVertical).LineStyle = xlNone
    Selection.Borders(xlInsideHorizontal).LineStyle = xlNone
    Dim itemNum_address As String
    Dim sub_itemNum_address As String
    Dim subItemNum_range
    Dim vl_item_column
    Dim vl_row
    Dim vl_item_cell
    vl_row = selected_row - 1
    itemNum_address = range("M_ITEM_NUM").Cells(vl_row, 1).Address
    vl_item_column = Split(itemNum_address, "$")(1)
    vl_item_cell = vl_item_column & vl_row
    ActiveCell.value = getSubItemNumberFromItemNum(itemNum) & ITEMDESCRIPTION(itemNum)
    autofitrow
    Selection.Font.Bold = False
    range(selected_col & selected_row).Select
    Selection.Offset(1, 0).Select
    
End Sub

Sub autofitrow()
'To autofit the row
    Dim first_col
    first_col = intToChar(range("M_ITEM_DESC").Column)
    Dim initial_width As Double
    Dim combine_width As Double
    combine_width = 0
    initial_width = Columns(first_col & ":" & first_col).ColumnWidth
    Dim i As Integer
    Dim last_col_num
    last_col_num = getLastColumnNum()
    For i = 1 To last_col_num
        combine_width = combine_width + Columns(i).ColumnWidth
    Next i
    With Selection
        .HorizontalAlignment = xlGeneral
        .VerticalAlignment = xlTop
        .WrapText = True
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = True
    End With
    Selection.UnMerge
    range("A:A").ColumnWidth = combine_width
    Selection.Rows.EntireRow.AutoFit
    range("A:A").ColumnWidth = initial_width
    Selection.Rows.RowHeight = Selection.Rows.RowHeight + 1
    With Selection
        .HorizontalAlignment = xlCenter
        .VerticalAlignment = xlTop
        .WrapText = True
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = False
    End With
    Selection.Merge
    With Selection
        .HorizontalAlignment = xlJustify
        .VerticalAlignment = xlTop
        .WrapText = True
        .Orientation = 0
        .AddIndent = False
        .IndentLevel = 0
        .ShrinkToFit = False
        .ReadingOrder = xlContext
        .MergeCells = True
    End With
    
End Sub
Sub new_item_4()
'
' new_item_4 Macro
'

'
    Dim selected_row
    selected_row = Selection.row
    Dim selected_col
    selected_col = intToChar(Selection.Column)
    Dim last_col
    last_col = getLastColumn
   
    
    Sheets("MEASUREMENT").Select
    range(intToChar(range("M_ITEM_DESC").Column) & selected_row & ":" & last_col & selected_row).Select
    'copy template
    Sheets("TEMPLATE").Select
    range("A5:" & getLastColumn() & "5").Select
    Selection.Copy
    Sheets("MEASUREMENT").Select
    Selection.PasteSpecial Paste:=xlPasteFormulas
    Selection.PasteSpecial Paste:=xlPasteFormats, Operation:=xlNone, _
        SkipBlanks:=False, Transpose:=False
    Selection.Borders(xlDiagonalDown).LineStyle = xlNone
    Selection.Borders(xlDiagonalUp).LineStyle = xlNone
    With Selection.Borders(xlEdgeLeft)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeTop)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeBottom)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeRight)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    Selection.Borders(xlInsideVertical).LineStyle = xlNone
    Selection.Borders(xlInsideHorizontal).LineStyle = xlNone
    
    range(selected_col & selected_row).Select
    Selection.Offset(1, 0).Select
End Sub
Sub new_item_5(itemNumCell As range)
'
' new_item_5 Macro
'

'
    Dim selected_row
    selected_row = Selection.row
    Dim selected_col
    selected_col = intToChar(Selection.Column)
    Dim last_col
    last_col = getLastColumn
    
    Sheets("MEASUREMENT").Select
    ActiveCell.value = range("T_PAGE_NUMBER").Cells(1, 1).value
    'move right to set cells display none format for page number
    Selection.Offset(0, 1).Select
    ActiveCell.NumberFormat = ";;;"
    Selection.Offset(0, -1).Select
    Sheets("TEMPLATE").Select
    range("A6").Select
    Selection.Copy
    Sheets("MEASUREMENT").Select
    Selection.PasteSpecial Paste:=xlPasteFormats, Operation:=xlNone, _
        SkipBlanks:=False, Transpose:=False
    'select cell in last column
    range(last_col & selected_row).Select
    'move left
    Selection.Offset(0, -1).Select
    Application.CutCopyMode = False
    ActiveCell.FormulaR1C1 = range("T_TOTAL").Cells(1, 1).value
    Sheets("TEMPLATE").Select
    range("B6").Select
    Selection.Copy
    Sheets("MEASUREMENT").Select
    Selection.PasteSpecial Paste:=xlPasteFormats, Operation:=xlNone, _
    SkipBlanks:=False, Transpose:=False
    'move 1 cell right
    Selection.Offset(0, 1).Select
    Application.CutCopyMode = False
    Dim total_formula As String
    Dim cell_format As String
    Dim unit_value As String
    unit_value = getUnit(itemNumCell.value)
    cell_format = "0.00 """ & unit_value & """"
    total_formula = "=SUM(" & last_col & selected_row - 2 & ":offset(" & last_col & selected_row & ",-1,0))"
    'unit_formula = "& UNIT(" & itemNumCell.Address(0, 0) & "," & subItemNumCell.Address(0, 0) & ", IUNIT, EIUNIT)"
    ActiveCell.value = total_formula
    ActiveCell.NumberFormat = cell_format
    'move 1 cell right
    Dim total_cell
    total_cell = last_col & selected_row
    range("M_ITEM_NUM").Cells(selected_row, 1).Select
    ActiveCell.formula = "=" & total_cell
    range("A" & selected_row & ":" & last_col & selected_row).Select
    Selection.Borders(xlDiagonalDown).LineStyle = xlNone
    Selection.Borders(xlDiagonalUp).LineStyle = xlNone
    With Selection.Borders(xlEdgeLeft)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeTop)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeBottom)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeRight)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    Selection.Borders(xlInsideVertical).LineStyle = xlNone
    Selection.Borders(xlInsideHorizontal).LineStyle = xlNone
    range("M_ITEM_NUM_METADATA").Cells(selected_row, 1).value = "t"
    range(selected_col & selected_row).Select
    Selection.Offset(-1, 0).Select
End Sub

Function getLastColumn() As String
    getLastColumn = intToChar(range("M_TOTAL_QTY").Cells(1, 1).Column)
End Function

Function getLastColumnNum() As Integer
    getLastColumnNum = range("M_TOTAL_QTY").Cells(1, 1).Column
End Function

Function getMetaDataCol() As String
    getMetaDataCol = intToChar(range("M_ITEM_NUM").Cells(1, 1).Column)
End Function

Function getMetaDataColNum() As String
    getMetaDataColNum = range("M_ITEM_NUM").Cells(1, 1).Column
End Function

Function getpageNumCol() As String
Dim rng As range
Set rng = range("MEASUREMENT_PAGE_NUMBER")
getpageNumCol = intToChar(rng.Cells(1, 1).Column)
End Function

Public Function countChrInString(exp As String, Character As String) As Long
    Dim iResult As Long
    Dim sParts() As String
    sParts = Split(exp, Character)
    iResult = UBound(sParts, 1)
    If (iResult = -1) Then
    iResult = 0
    End If
    countChrInString = iResult
End Function

Function getCharPosition(ByVal stringToSearch As String, ByVal nthOccurence As Integer, ByVal stringToFind As String) As Integer
        getCharPosition = -1
        While Len(stringToSearch) And (nthOccurence > 0)
            If InStr(1, stringToSearch, stringToFind) Then
                getCharPosition = getCharPosition + InStr(1, stringToSearch, stringToFind)
                stringToSearch = Mid(stringToSearch, InStr(1, stringToSearch, stringToFind) + 1)
                nthOccurence = nthOccurence - 1
            Else
                getCharPosition = -1
                Exit Function
            End If
        Wend

        If nthOccurence = 1 Then
            getCharPosition = -1
        End If

    End Function



