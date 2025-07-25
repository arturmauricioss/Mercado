# build.ps1
$env:JAVA_HOME = "C:\Users\trabalho\Documents\jdk\jdk-17.0.15+6"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path

Write-Output "Usando JAVA_HOME: $env:JAVA_HOME"
java -version

./gradlew clean build --warning-mode all
