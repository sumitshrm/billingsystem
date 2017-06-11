Attribute VB_Name = "efm"
Public Const FILENAME_SUFFIX As String = "_Export.xlsx"

Sub ef()
'
' exportFile Macro
'
' Keyboard Shortcut: Ctrl+Shift+E
'
'ChDir "D:\personel\obfuscator\output"
    Dim wb As Workbook
    Dim newfilename As String
    'On Error GoTo error_exit
  
    Set wb = ActiveWorkbook
    'ActiveWorkbook.SaveAs FileName:= _
    '    "D:\personel\obfuscator\output\MeasurementTemplate1.7_Export.xlsm", FileFormat _
    '    :=xlOpenXMLWorkbookMacroEnabled, CreateBackup:=False
    
    newfilename = Application.GetSaveAsFilename( _
             InitialFileName:=suffixed_filename(wb, FILENAME_SUFFIX), _
             FileFilter:="Microsoft Excel Workbook (*.xlsx),*.xlsx,All Files (*.*),*.*", _
             title:="Select file into which workbook will be saved")
    
    If newfilename = "False" Then
    Exit Sub
    End If
    'wb.SaveAs newfilename
    'wb.SaveAs filename:=newfilename, FileFormat _
    ':=xlOpenXMLWorkbookMacroEnabled, CreateBackup:=False
    wb.SaveAs filename:= _
        newfilename, FileFormat:= _
        xlOpenXMLWorkbook, CreateBackup:=False
    
    'Sheets("ABSTRACT").Select
    'Range("A_ALL_METADATA_COLUMNS").Select
    range("A_ALL_METADATA_COLUMNS").EntireColumn.Hidden = True
    Sheets("ABSTRACT").Shapes("add_page_num_btn").Delete
    'Selection.Delete
    Sheets("ABSTRACT").Shapes("remove_page_num_btn").Delete
    'Selection.Delete
    'Range("A13").Select
    
    'Sheets("EXTRA_ITEMS").Select
    Sheets("EXTRA_ITEMS").Shapes("save_extra_item_btn").Delete
    'Selection.Delete
    
    'Sheets("MEASUREMENT").Select
    'Range("M_ALL_METADATA_COLUMNS").Select
    range("M_ALL_METADATA_COLUMNS").EntireColumn.Hidden = True
    Sheets("MEASUREMENT").Shapes("new_measurement_btn").Delete
    'Selection.Delete
    Sheets("MEASUREMENT").Shapes("save_btn").Delete
    'Selection.Delete
    'ActiveSheet.Shapes("reload_btn").Select
    'Selection.Delete
    Sheets("MEASUREMENT").Shapes("export_btn").Delete
    'Selection.Delete
    'Range("A12").Select
    
    'Sheets("ITEMS").Select
    Sheets("ITEMS").Visible = False
    'Sheets("EXTRA_ITEMS").Select
    Sheets("EXTRA_ITEMS").Visible = False
    'Sheets("TEMPLATE").Select
    Sheets("TEMPLATE").Visible = False
    
    'ActiveWorkbook.save
End Sub

' default filename in which to store "invisible" version
Private Function suffixed_filename(wb As Workbook, suffix As String) As String
  Dim dot_position As Integer
  Dim result As String
  dot_position = last_substring_position(wb.Name, ".")
  If (dot_position = 0) Then
    result = wb.Path & PS() & wb.Name & suffix
  Else
    result = wb.Path & PS() & Left(wb.Name, dot_position - 1) & suffix
  End If
  suffixed_filename = result
End Function

' location of the last substring within the given string, or 0 if
' substring doesn't occur within given string.

Private Function last_substring_position(s As String, subS As String) As Integer
   Dim iFound As Integer
   Dim iNext As Integer

   iFound = 0
   iNext = InStr(1, s, subS)
   Do While (iNext > 0)
     iFound = iNext
     iNext = InStr(iFound + 1, s, subS)
   Loop
  
   last_substring_position = iFound
  
End Function

Private Function PS() As String  ' e.g. a "\" on Windows
  PS = Application.PathSeparator
End Function
