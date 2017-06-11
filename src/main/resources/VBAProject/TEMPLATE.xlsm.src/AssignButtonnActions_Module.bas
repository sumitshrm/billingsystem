Attribute VB_Name = "AssignButtonnActions_Module"
Sub AssignButtonActions()
'
' AssignButtonActions Macro
'
' Keyboard Shortcut: Ctrl+Shift+B
'
Sheets("ABSTRACT").Select
ActiveSheet.Shapes("add_page_num_btn").Select
Selection.OnAction = getAction(Selection.OnAction)
ActiveSheet.Shapes("remove_page_num_btn").Select
Selection.OnAction = getAction(Selection.OnAction)
range("A13").Select
Sheets("EXTRA_ITEMS").Select
ActiveSheet.Shapes("save_extra_item_btn").Select
Selection.OnAction = getAction(Selection.OnAction)
Sheets("MEASUREMENT").Select
ActiveSheet.Shapes("new_measurement_btn").Select
Selection.OnAction = getAction(Selection.OnAction)
ActiveSheet.Shapes("save_btn").Select
Selection.OnAction = getAction(Selection.OnAction)
range("A12").Select
ActiveSheet.Shapes("reload_btn").Select
Selection.Delete
End Sub


Function getAction(current As String) As String
Dim result
Dim act_arr
result = current
act_arr = Split(current, "!")
If InStr(current, "!") > 0 Then
    result = act_arr(1)
End If
getAction = result
End Function
