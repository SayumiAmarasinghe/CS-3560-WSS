# Group 4: CS 3650 WSS Game

Name: Jasper Liu, Samy Jimenez, Nicholas Garcia, Jonah Lin, Sayumi Amarasinghe, Issa El Lahib

## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

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

This will start our application GUI

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
