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

## How do I compile/run this project?

1. Make sure you are in the `/WSS` directory

2. Compile

- WINDOWS powershell user:

```powershell
javac -d bin (Get-ChildItem -Path src -Filter *.java -Recurse).FullName
```

- MAC user:

```bash
javac -d bin $(find src -name "*.java")
```

This will generate java bytecode files (\*.class) in the directory, `/WSS/bin`

3. Run

```bash
java -cp bin wss.Main
```

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
