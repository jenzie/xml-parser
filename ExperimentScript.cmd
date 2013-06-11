@echo

:: Run the program and output the results to a file
for /r input %%F in (*.xml) do java -jar "xml-parser.jar" "%%F" "%%F"

:: Convert the XML files back to source
for /r output %%F in (*.xml) do (
	for /f "tokens=1-7 delims=_" %a in %%~nxF do (
		Set filename=%a
	)
	"srcml/srcml2src.exe" "%%F" -o "output_src/%filename%"
)

goto comment
..\srcml2src.exe output\array.cpp.xml -o output_src\array.cpp
..\srcml2src.exe output\FFT.cpp.xml -o output_src\FFT.cpp
..\srcml2src.exe output\kernel.cpp.xml -o output_src\kernel.cpp
..\srcml2src.exe output\LU.cpp.xml -o output_src\LU.cpp
..\srcml2src.exe output\MonteCarlo.cpp.xml -o output_src\MonteCarlo.cpp
..\srcml2src.exe output\Random.cpp.xml -o output_src\Random.cpp
..\srcml2src.exe output\scimark2.cpp.xml -o output_src\scimark2.cpp
..\srcml2src.exe output\SOR.cpp.xml -o output_src\SOR.cpp
..\srcml2src.exe output\SparseCompRow.cpp.xml -o output_src\SparseCompRow.cpp

:: Compile the modified source code files
g++ *.cpp -o scimark

:: Compare the results to results from the original program
diff input\array.cpp.xml output\array.cpp.xml
diff input\FFT.cpp.xml output\FFT.cpp.xml
diff input\kernel.cpp.xml output\kernel.cpp.xml
diff input\LU.cpp.xml output\LU.cpp.xml
diff input\MonteCarlo.cpp.xml output\MonteCarlo.cpp.xml
diff input\Random.cpp.xml output\Random.cpp.xml
diff input\scimark2.cpp.xml output\scimark2.cpp.xml
diff input\SOR.cpp.xml output\SOR.cpp.xml
diff input\SparseCompRow.cpp.xml output\SparseCompRow.cpp.xml
:comment

pause