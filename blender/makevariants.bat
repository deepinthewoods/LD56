@echo off
setlocal enabledelayedexpansion

set "palette_folder=..\pallettes_with_transparency"
set "source_image=..\assets\pack.png"
set "output_prefix=..\assets\pack"

set /a counter=1

for %%F in ("%palette_folder%\*.png") do (
    magick "%source_image%"  -remap "%%F" -dither FloydSteinberg -alpha on "%output_prefix%!counter!.png"
    set /a counter+=1
)

echo Remapping complete. Created %counter% remapped images.