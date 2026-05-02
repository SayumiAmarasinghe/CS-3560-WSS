Set-Location (Split-Path $PSScriptRoot -Parent)
javac -d bin (Get-ChildItem -Recurse -Filter *.java src | % FullName)
java -cp bin wss.Main