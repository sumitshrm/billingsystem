Attribute VB_Name = "SaveItems_Module"
Sub saveExtraItems()
'
' saveExtraItems Macro
' saves extra items to database and also updates mesaruement sheet.
'
Dim updated_items As Collection
If checkOffline Then
    Exit Sub
    End If
'Set item_nums_updated = New Collection
'deletedItems = getItemsRemoved
'Set updated_items =
On Error GoTo ErrorHandler
populateItemNumbers
existingItems = range("EXISTING_ITEMS_IN_DATABASE").value
isItemSaved = saveItems
If isItemSaved = True Then
End If
range("M_ITEM_NUM").Replace "~~", "", xlPart
Exit Sub
ErrorHandler:
MsgBox Err.description
End Sub


Function populateItemNumbers() As Object
Set populateItemNumbers = New Collection
itemNumCounter = range("T_EXTRA_ITEM_COUNTER").Cells(1, 1).value + 1
endRow = range("TABLE_EXTRA_ITEMS_DESC").Cells(Rows.count, 1).End(xlUp).row
itemNum_suffix = range("T_EXTRA_ITEM_SUFFIX").Cells(1, 1).value
For i = 2 To endRow
Dim itemNum As String
itemNum = range("TABLE_EXTRA_ITEMS_ITEM_NUM").Cells(i, 1).value
itemNumCount = getMainItemNumber(i)
subItemNum = getSubItemNumber(i)
newItemNum = itemNum_suffix & itemNumCounter & subItemNum
If newItemNum <> itemNum Then
range("TABLE_EXTRA_ITEMS_ITEM_NUM").Cells(i, 1).value = newItemNum
'populateItemNumbers.Add Item:=newItemNum, key:=CStr(itemNum)
'update in measurementsheet
If itemNum <> "" Then
updateItemNumInMeasurementSheet itemNum, newItemNum
End If
End If
If isSubItem(i + 1) = False Then
itemNumCounter = itemNumCounter + 1
End If
Next i
End Function


Function updateItemNumInMeasurementSheet(itemNum, newItemNum)
Dim Sh As Worksheet
Dim Loc As range

'check if given itenNum is existing itemNumber entry or a new one
itemNumbers = Split(range("EXISTING_ITEMS_IN_DATABASE").Cells(1, 1).value, ",")
If IsInArray(itemNum, itemNumbers) Then
    With range("M_ITEM_NUM")
        Set Loc = .Cells.find(What:=itemNum, LookAt:=xlWhole)
        If Not Loc Is Nothing Then
            Do Until Loc Is Nothing
                range("M_ITEM_NUM").Cells(Loc.row, 1).value = newItemNum & "~~"
                Set Loc = .FindNext(Loc)
            Loop
        End If
    End With
End If
End Function


Function isSubItem(row)
itemNum = range("TABLE_EXTRA_ITEMS_ITEM_NUM").Cells(row, 1).value
pos = InStr(itemNum, ".")
If pos > 0 Then
isSubItem = True
Else
isSubItem = False
End If
End Function


Function getMainItemNumber(row)
itemNum = range("TABLE_EXTRA_ITEMS_ITEM_NUM").Cells(row, 1).value
If itemNum <> "" Then
    Dim result
    pos = InStr(itemNum, ".")
    If pos > 0 Then
        itemNum = Left(itemNum, pos - 1)
    End If
    If InStr(itemNum, range("T_EXTRA_ITEM_SUFFIX").value) > 0 Then
        getMainItemNumber = Split(itemNum, range("T_EXTRA_ITEM_SUFFIX").value)(1)
    Else
        getMainItemNumber = itemNum
    End If
End If
End Function

Function getSubItemNumber(rowNum)
itemNum = range("TABLE_EXTRA_ITEMS_ITEM_NUM").Cells(rowNum, 1).value
pos = InStr(itemNum, ".")
If pos > 0 Then
getSubItemNumber = Right(itemNum, Len(itemNum) - pos + 1)
Else
getSubItemNumber = ""
End If
End Function

Function saveItems()

request = getJSONRequest("true")
request = Replace(request, Chr(10), "\\n")
vNames = Array("itemsAsJSON")
vValues = Array(request)
Set WebBrowser = CreateObject(shell_application).Windows(getIE(True))
PostRequest getExcelGatewayUrl & "/" & range("T_AGGREEMENT_ID").value & "/updateitems", _
   vNames, _
   vValues
'Do While WebBrowser.document.getelementbyid("excel_command") Is Nothing
'       Application.Wait DateAdd("s", 1, Now)
'Loop
Set element = WebBrowser.document.getelementbyid("status")
 If element Is Nothing Then
  MsgBox "'status' element not found"
  Exit Function
 End If
If (element.value = "SUCCESS") Then
Set itemsElement = WebBrowser.document.getelementbyid("itemNumbers")
range("EXISTING_ITEMS_IN_DATABASE").Cells(1, 1).value = itemsElement.value

End If
MsgBox WebBrowser.document.getelementbyid("message").value
If element.value = "SUCCESS" Then
saveItems = True
Else
saveItems = False
End If
End Function

Function getJSONRequest(isExtraItem)
json = "{"
json = json & itemsToJson(isExtraItem) & "," & getIsExtraItem("true") & "," & getMsheetId()
json = json & "}"
getJSONRequest = json
End Function

Function itemsToJson(isExtraItem)
Dim endRow
Dim json
vHeaders = Array("itemNumber", "description", "quantity", "unit", "quantityPerUnit", "dsrRate", "fullRate", "partRate", "drsCode", "isExtraItem")
vColumns = Array("TABLE_EXTRA_ITEMS_ITEM_NUM", "TABLE_EXTRA_ITEMS_DESC", "TABLE_EXTRA_ITEMS_QTY", "TABLE_EXTRA_ITEMS_UNIT", "TABLE_EXTRA_ITEMS_QTY_PER_UNIT", "TABLE_EXTRA_ITEMS_DSR_RATE", "TABLE_EXTRA_ITEMS_FULL_RATE", "TABLE_EXTRA_ITEMS_PART_RATE", "TABLE_EXTRA_ITEMS_DSR_CODE")
startRow = 2
endRow = Cells.find("*", SearchOrder:=xlByRows, SearchDirection:=xlPrevious).row

outerComma = ""
json = Chr(34) & "items" & Chr(34) & ":["
For i = 2 To endRow
'validate item if description is empty
If Trim(range(vColumns(1)).Cells(i, 1).value) = "" Then
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
       hCell = range(vColumns(currentIndex)).Cells(i, 1).value
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

Dim itemsInDatabase As New Collection
Dim itemsInSheet As New Collection
Dim json As String
Set existingItemNumRange = range("TABLE_EXTRA_ITEMS_ITEM_NUM")
json = Chr(34) & "deletedItems" & Chr(34) & ":["
' get item numbers from ITEMS sheet
startRow = 1
endRow = existingItemNumRange.Cells(Rows.count, 1).End(xlUp).row
rowCounter = 0
For Each cell In existingItemNumRange
   If rowCounter >= endRow Then
       Exit For
   End If
   If rowCounter <> 0 Then 'avoid title
   itemsInSheet.Add Item:=cell, key:=CStr(cell.value)
   End If
   rowCounter = rowCounter + 1
Next cell
' get item numbers from ITEMS_DATABASE sheet
itemNumbers = Split(range("EXISTING_ITEMS_IN_DATABASE").Cells(1, 1).value, ",")

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
End Function

Function getIsExtraItem(isExtraItem)
json = Chr(34) & "extraItem" & Chr(34) & ":"
json = json & Chr(34) & isExtraItem & Chr(34)
getIsExtraItem = json
End Function

Function getMsheetId()
json = Chr(34) & "msheetId" & Chr(34) & ":"
json = json & Chr(34) & range("T_MEASUREMENT_SHEET_ID").Cells(1, 1).value & Chr(34)
getMsheetId = json
End Function

Function addJsonElement(json, key, value, isFirstElement)
If isFirstElement = False Then
json = json & ","
End If
json = Chr(34) & key & Chr(34) & ":"
json = json & Chr(34) & value & Chr(34)
End Function

Function IsInArray(stringToBeFound, arr As Variant) As Boolean
  IsInArray = (UBound(Filter(arr, stringToBeFound)) > -1)
End Function


