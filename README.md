COMPILE INSTRUCTIONS

MAC
compile:
javac -d bin $(find src -name "*.java")

run:
java -cp bin wss.Main


WINDOWS
powershell-
compile:
javac -d bin (Get-ChildItem -Path src -Filter *.java -Recurse).FullName

run:
java -cp bin wss.Main

command prompt-
compile:
dir /s /B src\*.java > sources.txt
javac -d bin @sources.txt
del sources.txt

run:
java -cp bin wss.Main
