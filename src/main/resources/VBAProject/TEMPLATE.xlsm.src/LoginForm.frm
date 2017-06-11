VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} LoginForm 
   Caption         =   "Login"
   ClientHeight    =   2220
   ClientLeft      =   36
   ClientTop       =   360
   ClientWidth     =   3444
   OleObjectBlob   =   "LoginForm.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "LoginForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False


Private Sub CancelButton_Click()
loginForm.command = "false"
loginForm.Hide
End Sub


Private Sub LoginButton_Click()
loginForm.command = "true"
loginForm.Hide
End Sub
