@echo off

set uipath="..\..\taxe\core\assets\data"

REM Remove all current skin files from the project
del %uipath% /q

REM Copy new files across
mkdir %uipath%
robocopy projects/FVS4 %uipath% uiskin.json uiskin.atlas *.png *.fnt
