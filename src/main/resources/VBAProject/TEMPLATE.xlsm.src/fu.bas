Attribute VB_Name = "fu"
Sub window_Open(strLocation As String, Menubar As Boolean, height As Long, width As Long, resizable As Boolean)

With CreateObject("InternetExplorer.Application")
    .Visible = False
    .height = height
    .width = width
    .Menubar = Menubar
    .Visible = True
    .resizable = resizable
    .Navigate strLocation
End With


End Sub

Sub UploadFile(DestURL As String, filename As String, _
  Optional ByVal FieldName As String = "File")
  Dim sFormData As String, d As String
  
  'Boundary of fields.
  'Be sure this string is Not In the source file
  Const Boundary As String = "---------------------------0123456789012"

  'Get source file As a string.
  sFormData = GetFile(filename)
  
  'Build source form with file contents
  d = "--" + Boundary + vbCrLf
  d = d + "Content-Disposition: form-data; name=""" + FieldName + """;"
  d = d + " filename=""" + filename + """" + vbCrLf
  d = d + "Content-Type: application/upload" + vbCrLf + vbCrLf
  d = d + sFormData
  d = d + vbCrLf + "--" + Boundary + "--" + vbCrLf
  
  'Post the data To the destination URL
  IEPostStringRequest DestURL, d, Boundary
End Sub

'sends URL encoded form data To the URL using IE
Sub IEPostStringRequest(url As String, FormData As String, Boundary As String)
  If isOffline Then
   MsgBox "data cannot be saved in offline mode. go online"
   Exit Sub
  End If
  Set WebBrowser = CreateObject(shell_application).Windows(getIE)
  'Send the form data To URL As POST request
  Dim bFormData() As Byte
  ReDim bFormData(Len(FormData) - 1)
  bFormData = StrConv(FormData, vbFromUnicode)

  WebBrowser.Navigate url, , , bFormData, _
    "Content-Type: multipart/form-data; boundary=" + Boundary + vbCrLf
  While WebBrowser.busy Or WebBrowser.readyState <> 4: DoEvents: Wend
  WebBrowser.document.title = window_title
  Application.cursor = xlDefault
  
End Sub

'read binary file As a string value
Function GetFile(filename As String) As String
  Dim FileContents() As Byte, FileNumber As Integer
  ReDim FileContents(FileLen(filename) - 1)
  FileNumber = FreeFile
  Open filename For Binary As FileNumber
    Get FileNumber, , FileContents
  Close FileNumber
  GetFile = StrConv(FileContents, vbUnicode)
End Function

Sub so()
 If ActiveSheet.Name = "EXTRA_ITEMS" Then
    saveExtraItems
 Else
    saveOnline
 End If
End Sub

Sub saveOnline()
    ClearExcessRowsAndColumns
    ActiveWorkbook.save
    If checkOffline Then
    Exit Sub
    End If
    loginForm.command.value = "false"
    Dim filepath As String
    Dim upload_url As String
    Dim msheet_id As String
    msheet_id = "1"
    'get mesheet id
    Dim rng As range
    Set rng = range("T_MEASUREMENT_SHEET_ID")
    msheet_id = rng.value
    Application.cursor = xlWait
    filepath = Application.ActiveWorkbook.FullName
    upload_url = getExcelGatewayUrl & "/updatedocxl/" & msheet_id
    UploadFile upload_url, _
    filepath, "FileField"
    'validate if the session was not expired. if expired then ask user for login.
    Set WebBrowser = CreateObject(shell_application).Windows(getIE)
    If WebBrowser.document.getelementbyid("message") Is Nothing Then
        MsgBox "ERROR:Session expired! please try again."
    Else
        Application.cursor = xlDefault
        oncomplete True
    End If
    
    End
End Sub

Sub oncomplete(showMessage)
If showMessage = True Then
Set WebBrowser = CreateObject(shell_application).Windows(getIE)
MsgBox WebBrowser.document.getelementbyid("message").value
End If
End Sub
