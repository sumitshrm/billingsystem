Attribute VB_Name = "Module1"


Sub SaveItems()
'
' SaveItems Macro
' validates and saves the data entered
'
' Keyboard Shortcut: Ctrl+Shift+S
'
If checkOffline = True Then
Exit Sub
End If
On Error GoTo ErrorHandler
request = getJSONRequest("false")
request = Replace(request, Chr(10), "\\n")
vNames = Array("itemsAsJSON")
vValues = Array(request)
Set WebBrowser = CreateObject(shell_application).Windows(getIE(True))
 PostRequest getExcelGatewayUrl & "/" & Range("T_AGGREEMENT_ID").Value & "/" & "updateitems", _
    vNames, _
    vValues

Do While WebBrowser.document.getelementbyid("excel_command") Is Nothing
        Application.Wait DateAdd("s", 1, Now)
Loop
Set element = WebBrowser.document.getelementbyid("status")
  If element Is Nothing Then
   MsgBox "'status' element not found"
   Exit Sub
  End If
If (element.Value = "SUCCESS") Then
Set itemsElement = WebBrowser.document.getelementbyid("itemNumbers")
Range("EXISTING_ITEMS_IN_DATABASE").Cells(1, 1).Value = itemsElement.Value
End If
MsgBox WebBrowser.document.getelementbyid("message").Value
Exit Sub
ErrorHandler:
MsgBox Err.Description
End Sub

Function getJSONRequest(isExtraItem)
json = "{"
json = json & itemsToJson(isExtraItem) & "," & getItemsRemoved
json = json & "}"
getJSONRequest = json
End Function

Function itemsToJson(isExtraItem)
Dim endRow
Dim json
vHeaders = Array("itemNumber", "description", "quantity", "unit", "quantityPerUnit", "fullRate", "partRate", "drsCode", "isExtraItem")
vColumns = Array("TABLE_ITEMS_ITEM_NUM", "TABLE_ITEMS_DESC", "TABLE_ITEMS_QTY", "TABLE_ITEMS_UNIT", "TABLE_ITEMS_QTY_PER_UNIT", "TABLE_ITEMS_FULL_RATE", "TABLE_ITEMS_PART_RATE", "TABLE_ITEMS_DSR_CODE")
startRow = 2
endRow = Cells.Find("*", SearchOrder:=xlByRows, SearchDirection:=xlPrevious).Row
outerComma = ""
json = Chr(34) & "items" & Chr(34) & ":["
For i = 2 To endRow
'validate if Item Number Or Description  is missing for this item
If Trim(Range(vColumns(0)).Cells(i, 1).Value) = "" Then
    Rows(i).Select
    Err.Raise Error_InvalidItemData, Err.Source, "Item number can not be empty."
End If
If Trim(Range(vColumns(1)).Cells(i, 1).Value) = "" Then
    Rows(i).Select
    Err.Raise Error_InvalidItemData, Err.Source, "Description can not be empty."
End If
innercomma = ""
currentIndex = 0
json = json & outerComma & "{"
    For Each element In vHeaders
        If element = "isExtraItem" Then
        json = json & innercomma & Chr(34) & element & Chr(34) & ":" & Chr(34) & isExtraItem & Chr(34)
        Else
        hCell = Range(vColumns(currentIndex)).Cells(i, 1).Value
        json = json & innercomma & Chr(34) & element & Chr(34) & ":" & Chr(34) & hCell & Chr(34)
        innercomma = ","
        currentIndex = currentIndex + 1
        End If
    Next element
json = json & "}"
outerComma = ","
Next i
json = json & "]"
itemsToJson = json
End Function

Function getItemsRemoved()
On Error GoTo ErrorHandler
Dim itemsInDatabase As New Collection
Dim itemsInSheet As New Collection
Dim json As String
Set existingItemNumRange = Range("TABLE_ITEMS_ITEM_NUM")
json = Chr(34) & "deletedItems" & Chr(34) & ":["
' get item numbers from ITEMS sheet
startRow = 1
endRow = existingItemNumRange.Cells(Rows.Count, 1).End(xlUp).Row
rowCounter = 0
For Each cell In existingItemNumRange
    If rowCounter >= endRow Then
        Exit For
    End If
    If rowCounter <> 0 Then 'avoid title
    itemsInSheet.Add Item:=cell, Key:=CStr(cell.Value)
    End If
    rowCounter = rowCounter + 1
Next cell
' get item numbers from ITEMS_DATABASE sheet

itemNumbers = Split(Range("EXISTING_ITEMS_IN_DATABASE").Cells(1, 1).Value, ",")


'get list of removed items
innercomma = ""
For Each element In itemNumbers
itemExist = False
On Error Resume Next
itemsInSheet.Item (CStr(element))
itemExist = (Err.Number = 0)
If itemExist = False Then
json = json & innercomma & Chr(34) & CStr(element) & Chr(34)
innercomma = ","
End If
Next element
json = json & "]"
getItemsRemoved = json

Exit Function
ErrorHandler:
Select Case Err.Number
Case 457
Rows(rowCounter + 1).Select
Err.Raise Error_DuplicateItemNumber, Err.Source, "Duplicate item number : " & cell.Value
End Select

End Function


