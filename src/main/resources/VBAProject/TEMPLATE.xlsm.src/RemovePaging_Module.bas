Attribute VB_Name = "RemovePaging_Module"
Sub removePageNumber()
'
' removePageNumber Macro
' removes page numbers from abstract sheet
'

'
If checkOffline Then
    Exit Sub
    End If
    range("MEASUREMENT_PAGE_NUMBER").ClearContents
    range("A_METADATA_PAGE_NUM").ClearContents
End Sub
