@echo

:: Convert the XML files back to source
..\srcml2src.exe input\array.cpp.xml -o input_src\array.cpp
..\srcml2src.exe input\FFT.cpp.xml -o input_src\FFT.cpp
..\srcml2src.exe input\kernel.cpp.xml -o input_src\kernel.cpp
..\srcml2src.exe input\LU.cpp.xml -o input_src\LU.cpp
..\srcml2src.exe input\MonteCarlo.cpp.xml -o input_src\MonteCarlo.cpp
..\srcml2src.exe input\Random.cpp.xml -o input_src\Random.cpp
..\srcml2src.exe input\scimark2.cpp.xml -o input_src\scimark2.cpp
..\srcml2src.exe input\SOR.cpp.xml -o input_src\SOR.cpp
..\srcml2src.exe input\SparseCompRow.cpp.xml -o input_src\SparseCompRow.cpp

:: Compile the modified source code files


:: Run the program and output the results to a file

:: Compare the results to results from the original program

:: Append an entry for this run to the experiment report

pause