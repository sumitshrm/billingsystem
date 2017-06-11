Attribute VB_Name = "irm"

Sub ir()

    ' If Selection.Rows.Count <> 1 Or Selection.Columns.Count <> ActiveSheet.Columns.Count Then
    '    MsgBox "No row or multiple rows selected"
    '    End
    ' End If
    If ActiveSheet.Name <> "MEASUREMENT" Then
        End
    End If
    
    
    Dim col, row, numOfRows
    numOfRows = InputBox("Enter number of rows to insert")
    If numOfRows = vbNullString Or Not IsNumeric(numOfRows) Then
        End
    End If
    
    col = Split(Selection.Address, "$")(1)
    row = Split(Selection.Address, "$")(2)
    
    Dim i As Integer
    'get last column
    Sheets("TEMPLATE").Select
    Dim LastCol_int As Integer
    Dim LastCol_str As String
    Dim last_col
    last_col = getLastColumn()
    With ActiveSheet
        LastCol_int = .Cells(1, .Columns.count).End(xlToLeft).Column
    End With
    LastCol_str = intToChar(range("MEASUREMENT_PAGE_NUMBER").Cells().Column)
    'MsgBox LastCol_str
    Dim Row_Range As String
    Row_Range = "A5:" + LastCol_str + "5"
    
    For i = 1 To numOfRows
        Sheets("MEASUREMENT").Select
        Selection.EntireRow.Select
        Selection.Insert Shift:=xlDown, CopyOrigin:=xlFormatFromLeftOrAbove
        Selection.EntireRow.RowHeight = ActiveSheet.StandardHeight
        Sheets("TEMPLATE").Select
        range(Row_Range).Select
        Selection.Copy
        Sheets("MEASUREMENT").Select
        
        range("A" & Selection.row & ":" & last_col & Selection.row).Select
        Selection.PasteSpecial Paste:=xlPasteFormulas, Operation:=xlNone, _
        SkipBlanks:=False, Transpose:=False
        Selection.PasteSpecial Paste:=xlPasteFormats
        'MsgBox ("A" & Selection.row & ":" & last_col & Selection.row)
        'set borders  M_TOTAL_QTY
        range("A" & Selection.row & ":" & intToChar(range("M_TOTAL_QTY").Column) & Selection.row).Select
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
    
    selectedCell = ActiveCell.Address
    Next i
'    Range("A" & row + i - 1 & ":G" & row + i - 1).Select

    'copy metadata format
    Application.Goto Reference:="T_METADATA_FORMAT"
    Selection.Copy
    Sheets("MEASUREMENT").Select
    
    range("M_ALL_METADATA_COLUMNS").Select
    Selection.PasteSpecial Paste:=xlPasteFormats, Operation:=xlNone, _
        SkipBlanks:=False, Transpose:=False
    Application.CutCopyMode = False
    range(selectedCell).Select
        
        
End Sub
