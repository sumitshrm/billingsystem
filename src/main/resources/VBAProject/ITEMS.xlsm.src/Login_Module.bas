Attribute VB_Name = "Login_Module"
Function doLogin(doRrefresh As Boolean) As Boolean
  
    'take user credentials
  loginsuccess = False
  LoginForm.command.Value = "false"
  LoginForm.Show
  Application.Cursor = xlWait
  Application.ScreenUpdating = False
  Set WebBrowser = CreateObject(shell_application).Windows(getIE(True))
  'WebBrowser.Visible = True
  If LoginForm.command.Value = "false" Then
  MsgBox "going offline! you will not be able to use online features."
  GoTo Complete
  End If
  
  PostRequest getFormUrl & "resources/j_spring_security_check", _
    Array("j_username", "j_password"), _
    Array(LoginForm.username, LoginForm.password)
  
  'if login is succesfull then check for measurenment sheet id.
  If isOffline = False Then
   'check if this measurement sheet belongs to this user
   Set element = WebBrowser.document.getelementbyid("aggreementId")
   If element Is Nothing Then
     MsgBox "EXCEPTION : missing element 'id'"
   Else
     'validate measurement sheed id
     If element.Value = "" Then
     MsgBox "ACCESS DENIED: Invalid user for this document"
     navigateToUrl (getFormUrl & logoutUrl)
     Application.Cursor = xlDefault
    doLogin (False)
     GoTo Complete
     End If
   End If
   'MsgBox "login success"
   loginsuccess = True
   Else
  
  MsgBox "login failed. Reason : " & WebBrowser.document.forms("f").j_error_message.Value
  WebBrowser.document.title = window_title
  Application.Cursor = xlDefault
  doLogin (False)
  End If

Complete:
  Application.Cursor = xlDefault
  Application.ScreenUpdating = True
  doLogin = loginsuccess
End Function


Function getIE(Optional ByVal createnew As Boolean) As Integer
marker = 0
Set objshell = CreateObject("Shell.Application")
IE_count = objshell.Windows.Count
'MsgBox IE_count
For x = 0 To (IE_count - 1)
    On Error Resume Next    ' sometimes more web pages are counted than are open
    my_url = objshell.Windows(x).document.Location
    my_title = objshell.Windows(x).document.title

    If my_title = window_title Then 'compare to find if the desired web page is already open
        Set ie = objshell.Windows(x)
        'ie.Visible = True
        'if currently displayed page is not excelgateway page then navigate
        Set element = ie.document.getelementbyid("aggreementId")
            If element Is Nothing Then
                ie.Navigate getExcelGatewayUrl & getPostFixUrl
                While ie.busy Or ie.readyState <> 4: DoEvents: Wend
                ie.document.title = window_title
            End If
        marker = 1
        Exit For
    Else
    End If
Next

If marker = 0 Then
    If createnew Then
        Dim WebBrowser: Set WebBrowser = CreateObject("InternetExplorer.Application")
        WebBrowser.Navigate getExcelGatewayUrl & getPostFixUrl
        While WebBrowser.busy Or WebBrowser.readyState <> 4: DoEvents: Wend
        WebBrowser.document.title = window_title
        'WebBrowser.Visible = True
    Else
        x = -1
    End If
End If

'ie.Visible = True
'WebBrowser.Visible = True
getIE = x
End Function

Function isOffline() As Boolean
'testing ------------------
'isOffline = False
'Exit Function
'testing ------------------
Dim result As Boolean
ieIndex = getIE()
If ieIndex = -1 Then
result = True
Else
Set WebBrowser = CreateObject("Shell.Application").Windows(ieIndex)
While WebBrowser.busy Or WebBrowser.readyState <> 4: DoEvents: Wend
Set element = WebBrowser.document.getelementbyid("j_username")
  If element Is Nothing Then
     result = False
   Else
   result = True
  End If
End If
'MsgBox InStr(WebBrowser.LocationURL, getFormUrl & loginUrl)
isOffline = result
End Function


 

'**************************************** Post form data - begin
'sends form fields specified In Names/Values arrays To the URL
Sub PostRequest(url, Names, Values)
  Dim i, FormData, Name, Value
  
  'Enumerate form names And it's values
  'and built string representaion of the form data
  For i = 0 To UBound(Names)
    'URL encode source fields
    Name = URLEncode(Names(i))
    Value = URLEncode(Values(i))
    If FormData <> "" Then FormData = FormData & "&"
    FormData = FormData & Name & "=" & Value
  Next
  
  IEPostLoginStringRequest url, FormData
End Sub

'sends URL encoded form data To the URL using IE
Sub IEPostLoginStringRequest(url, FormData)
  'Create InternetExplorer
  Set WebBrowser = CreateObject(shell_application).Windows(getIE)
  
  'You can uncoment Next line To see form results As HTML
  'WebBrowser.Visible = True
  
  'Send the form data To URL As POST request
  Dim bFormData() As Byte
  ReDim bFormData(Len(FormData) - 1)
  bFormData = StrConv(FormData, vbFromUnicode)
  WebBrowser.Navigate url, 2 + 4 + 8, , bFormData, _
    "Content-type: application/x-www-form-urlencoded" + Chr(10) + Chr(13)

  While WebBrowser.busy Or WebBrowser.readyState <> 4: DoEvents: Wend
  WebBrowser.document.title = window_title
  'MsgBox "complete"
End Sub

'URL encode of a string data
Function URLEncode(Data)
  Dim i, c, Out
  
  For i = 1 To Len(Data)
    c = Asc(Mid(Data, i, 1))
    If c = 32 Then
      Out = Out + "+"
    ElseIf c < 48 Then
      Out = Out + "%" + Hex(c)
    Else
      Out = Out + Mid(Data, i, 1)
    End If
  Next
  URLEncode = Out
End Function
'**************************************** Post form data - end

Function checkOffline() As Boolean
Dim result As Boolean
If isOffline Then
'MsgBox "please login first"
result = Not doLogin(False)
Else
result = False
End If
checkOffline = result
End Function

Function getFormUrl()
getFormUrl = Range("T_URL").Cells(1, 1).Value
End Function

Function getExcelGatewayUrl() As String
getExcelGatewayUrl = Range("T_EXCELGATEWAY_URL").Cells(1, 1).Value
End Function

Function getPostFixUrl()
getPostFixUrl = "?aggreement=" & Range("T_AGGREEMENT_ID").Value
End Function

Function getMsheetUrl()
Dim rng As Range
    Set rng = Range("T_MEASUREMENT_SHEET_ID")
    msheet_id = rng.Value
getMsheetUrl = getFormUrl & msheetUrl & msheet_id & "/excel"
End Function

Function navigateToUrl(url As String)
Set WebBrowser = CreateObject(shell_application).Windows(getIE)
While WebBrowser.busy Or WebBrowser.readyState <> 4: DoEvents: Wend
WebBrowser.Navigate url, 2 + 4 + 8, , "", _
    "Content-type: application/x-www-form-urlencoded" + Chr(10) + Chr(13)
While WebBrowser.busy Or WebBrowser.readyState <> 4: DoEvents: Wend
WebBrowser.document.title = window_title
End Function

Function test()
doLogin (False)
End Function




