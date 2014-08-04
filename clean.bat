@echo off
echo.
echo Cleaning binaries
rmdir /S /Q out\bin
cd out
mkdir bin
cd bin
echo "" > .gitkeep
cd ../../
echo Done
