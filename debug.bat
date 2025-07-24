@echo off
echo ===================================
echo Gerando SHA-1 do projeto...
echo ===================================
call .\gradlew signingReport
echo ===================================
echo Pressione qualquer tecla para sair
echo ===================================
pause > nul
