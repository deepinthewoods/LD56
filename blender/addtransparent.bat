@echo off
setlocal enabledelayedexpansion

REM Check if ImageMagick is installed
where magick >nul 2>nul
if %errorlevel% neq 0 (
    echo ImageMagick is not installed or not in the system PATH.
    echo Please install ImageMagick and make sure it's in your system PATH.
    exit /b 1
)

REM Process all PNG files in the current directory
for %%F in (*.png) do (
    echo Processing: %%F
    magick "%%F" -background none -gravity south -splice 0x1 "processed_%%F"
)

echo All files have been processed.
pause