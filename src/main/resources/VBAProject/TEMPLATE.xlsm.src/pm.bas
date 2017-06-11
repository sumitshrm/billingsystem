Attribute VB_Name = "pm"
Sub pgng()

If checkOffline Then
    Exit Sub
    End If
Dim wb As Workbook
Dim ws As Worksheet
Set wb = ActiveWorkbook
Set abstract_ws = wb.Sheets("ABSTRACT")
Dim ref_column As String
ref_column = Col_Letter(range("A_METADATA_TOTAL").Cells(1, 1).Column) 'A_METADATA_TOTAL
Dim source_column As String
source_column = Col_Letter(range("A_METADATA_PAGE_NUM").Cells(1, 1).Column)  'A_METADATA_PAGE_NUM
Dim page_num_list As Object
Set page_num_list = CreateObject("System.Collections.ArrayList")
Sheets("MEASUREMENT").Select
'scroll down to the last row
Dim last_row_str As String
last_row_str = range("A1").SpecialCells(xlCellTypeLastCell).Address
Dim last_row_int
last_row_int = Split(last_row_str, "$")(2)
ActiveWindow.ScrollRow = last_row_int

Dim NumPage As Integer
NumPage = 1
For Each HorizontalPageBreak In ActiveSheet.HPageBreaks
    page_num_list.Add HorizontalPageBreak.Location.row
    NumPage = NumPage + 1
Next HorizontalPageBreak
ActiveWindow.ScrollRow = 1
'------get last row--------------------
Sheets("ABSTRACT").Select
last_row_str = range("A1").SpecialCells(xlCellTypeLastCell).Address
last_row_int = Split(last_row_str, "$")(2)
'create a range
Dim rng As range
'MsgBox ref_column & "1:" & ref_column & last_row_int
Set rng = range(ref_column & "1:" & ref_column & last_row_int)
'----iterate over each cell in range----------
Dim page_num As Integer
For Each cell In rng
    If cell.value <> "" And cell.value <> "metadata" Then
    page_num = 1
    'iterate over list to get the page number of cell
    For Each element In page_num_list
        'MsgBox "elem : " & element & ", cellValue : " & cell.Value
        'Dim ref_range As Range
        'Set ref_range = Range(cell.Value)
        'MsgBox Split(ref_range.Address, "$")(2)
        If element > getRowNumFromCellWithFormula(cell) Then Exit For
        page_num = page_num + 1
    Next element
    abstract_ws.range(source_column & Split(cell.Address, "$")(2)).value = page_num
    End If
Next cell
ActiveSheet.DisplayPageBreaks = False
Call paging_measurement(NumPage)
End Sub

Sub paging_measurement(startpage As Integer)

Dim page_num_list As Object
Set page_num_list = CreateObject("System.Collections.ArrayList")
Sheets("ABSTRACT").Select
'scroll down to the last row
last_row_int = Cells.find("*", SearchOrder:=xlByRows, SearchDirection:=xlPrevious).row
ActiveWindow.ScrollRow = last_row_int
Dim NumPage As Integer
NumPage = startpage
For Each HorizontalPageBreak In ActiveSheet.HPageBreaks
    page_num_list.Add HorizontalPageBreak.Location.row
    NumPage = NumPage + 1
Next HorizontalPageBreak
ActiveWindow.ScrollRow = 1
'------get last row--------------------

Sheets("MEASUREMENT").Select
'----iterate over each cell in range----------
Dim page_num As Integer
page_num = 1
Dim pageNumCell As range
Dim Loc As range
With range("MEASUREMENT_ABSTRACT_REF")
        Set Loc = .Cells.find(What:="*", SearchDirection:=xlNext)
        If Not Loc Is Nothing Then
            Do Until Loc Is Nothing
                'if some value is found, get the page number for selected cell.
                For Each element In page_num_list
                    If element > getRowNumFromCellWithFormula(Loc) Then Exit For
                        page_num = page_num + 1
                Next element
                range("MEASUREMENT_PAGE_NUMBER").Cells(Loc.row, 1).value = page_num + startpage
                RowCount = Loc.row
                Set Loc = .FindNext(Loc)
                'chiec if find function is repeated.
                If Loc.row <= RowCount Then
                Set Loc = Nothing
                End If
                
            Loop
        End If
End With
ActiveSheet.DisplayPageBreaks = False
End Sub

Function getRowNumFromCellWithFormula(ByVal cell As range) As Integer
Dim ref_cell_formula As String
Dim ref_cell As range
ref_cell_formula = CStr(cell.formula)
ref_cell_formula = (Split(ref_cell_formula, "!")(1))
Set ref_cell = range(ref_cell_formula)
'MsgBox ref_cell.row
getRowNumFromCellWithFormula = ref_cell.row
End Function

Function getFullItemDescription(itemNum)
itemNum_arr = Split(itemNum, ".")
Dim desc
Dim validItemNum
subItemNum = ""
newLine = ""
For count_y = LBound(itemNum_arr) To UBound(itemNum_arr)
    
    validItemNum = ""
    seprator = ""
    Dim count_x
    For count_x = LBound(itemNum_arr) To count_y
    validItemNum = validItemNum & seprator & itemNum_arr(count_x)
    seprator = "."
    Next count_x
    If count_y > 0 Then
            newLine = vbNewLine
           desc = desc & newLine & itemNum_arr(count_x - 1) & ") "
    Else
            desc = desc & newLine
    End If
    desc = desc & ITEMDESCRIPTION(validItemNum)
Next count_y
getFullItemDescription = desc
End Function


Function changeItemNum()

End Function

Function ITEMDESCRIPTION(itemNum)
Dim subItem_result
Dim item_result
Dim lookuup_val
Dim subitem_bullet As String

rowNum = Application.Match(itemNum, range("TABLE_ITEMS_ITEM_NUM"), 0)
If IsError(rowNum) Then
rowNum = Application.Match(itemNum, range("TABLE_EXTRA_ITEMS_ITEM_NUM"), 0)
If IsError(rowNum) Then
ITEMDESCRIPTION = ""
Else
ITEMDESCRIPTION = range("TABLE_EXTRA_ITEMS_DESC").Cells(rowNum, 1).value
End If
Else
ITEMDESCRIPTION = range("TABLE_ITEMS_DESC").Cells(rowNum, 1).value
End If
End Function

Function getSubItemNumberFromItemNum(itemNum) As String
pos = InStrRev(itemNum, ".")
If pos > 0 Then
getSubItemNumberFromItemNum = Right(itemNum, Len(itemNum) - pos) & ") "
Else
getSubItemNumberFromItemNum = ""
End If
End Function

Function ItemNumber(itemNum As range, subItemNum As range)
Dim title
Dim rowNum
Dim itemNum_val
Dim subItemNum_val
Dim subItemNum_render
title = range("T_AGGREEMENT_NUMBER").Cells(1, 1).value
rowNum = Application.Caller.row
itemNum_val = itemNum.Cells(rowNum, 1).value
subItemNum_val = subItemNum.Cells(rowNum, 1).value
If Trim(subItemNum_val) <> "" Then
    subItemNum_val = "(" & subItemNum_val & ")"
End If
ItemNumber = title & itemNum_val & subItemNum_val
End Function

Function PARTRATEABSTRACT(itemNum As String, subItemNum As String, existingPartRate As String, newPartRate As range)
If newPartRate.value = existingPartRate Then
PARTRATEABSTRACT = ""
Else
PARTRATEABSTRACT = itemNum & " " & subItemNum & "," & newPartRate.value
End If
End Function

Function getUnit(itemNum As String)
Dim rowNum
Dim itemColNum
Dim extraItemColNum
Dim un
'Dim fullItemNum
'If IsNumeric(itemNum) Then
'fullItemNum = val(itemNum)
'Else
'fullItemNum = itemNum
'End If
rowNum = Application.Match(itemNum, range("TABLE_ITEMS_ITEM_NUM"), 0)
If IsError(rowNum) Then
rowNum = Application.Match(itemNum, range("TABLE_EXTRA_ITEMS_ITEM_NUM"), 0)
un = range("TABLE_EXTRA_ITEMS_UNIT").Cells(rowNum, 1).value
Else
un = range("TABLE_ITEMS_UNIT").Cells(rowNum, 1).value
End If
'convert 'each' to 'nos'
If "each" = LCase(un) Then
un = "nos"
End If
getUnit = un
End Function

Function unit(itemNum As range, subItemNum As range, items As range, extraItems As range)
unit = getUnit(itemNum.value & " " & subItemNum)
End Function
