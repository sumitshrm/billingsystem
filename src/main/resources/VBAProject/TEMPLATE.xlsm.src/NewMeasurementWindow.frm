VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} NewMeasurementWindow 
   Caption         =   "New measurement"
   ClientHeight    =   3792
   ClientLeft      =   36
   ClientTop       =   360
   ClientWidth     =   3252
   OleObjectBlob   =   "NewMeasurementWindow.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "NewMeasurementWindow"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False


Private Sub ItemNumber_ComboBox_Change()
item_num = ItemNumber_ComboBox.value
Description_TextBox.value = getFullItemDescription(item_num)
End Sub



Private Sub Select_Button_Click()
Dim selectedItem As String
Dim itemNumCell As range
If ItemNumber_ComboBox.ListIndex > -1 Then
    NewMeasurementWindow.Hide
    Else
    NewMeasurementWindow.ItemNumber_ComboBox.SetFocus
    MsgBox "invalid item number"
    Exit Sub
End If
selectedItem = NewMeasurementWindow.ItemNumber_ComboBox.value
insertNewRow = itemsRowSelected
totalSubItems = countChrInString(selectedItem, ".")
If NewMeasurementWindow.Activity = "create" Then
    Call new_item_1(insertNewRow, selectedItem)
    'capture selected row number for "itemNum" and "subItem" num reference
    ItemNumRowNum = Selection.row
    Call new_item_2(selectedItem)
    For indx = 1 To totalSubItems + 1
    pos = getCharPosition(selectedItem, indx, ".")
    fullItemNum = selectedItem
    If pos > 0 Then
    fullItemNum = Left(selectedItem, pos)
    End If
    Call new_item_3(fullItemNum)
    Next indx
    Call new_item_4
    'fetch itemNum cell and subItemNum cell to send as parameter
    Set itemNumCell = range("M_ITEM_NUM").Cells(ItemNumRowNum, 1)
    Call new_item_5(itemNumCell)
    'call insert row
    Call ir
    
    Else
    itemNumRow = NewMeasurementWindow.ItemNumRowNum
    itemNumValue = NewMeasurementWindow.ItemNumber_ComboBox.value
    updateItemNum itemNumRow, itemNumValue
    
End If

End Sub
