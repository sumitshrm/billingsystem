Attribute VB_Name = "Utility_Module"
Function intToChar(i_input As Integer) As String
Dim vArr
vArr = Split(Cells(1, i_input).Address(True, False), "$")
intToChar = vArr(0)
End Function

Function charToInt(c_input As String) As Variant
If c_input = "-" Then
charToInt = 0
Else
charToInt = (Asc(c_input) - 64)
End If
End Function

Function Col_Letter(lngCol As Long) As String
Dim vArr
vArr = Split(Cells(1, lngCol).Address(True, False), "$")
Col_Letter = vArr(0)
End Function

