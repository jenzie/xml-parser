setrootdir: This sets the root directory to be used and should be called first. 
It takes one argument, which is the directory to use.
	Example --> setrootdir("C:\\Users\\justin\\script-test");

syncdirs: This takes a master directory tree and builds out the others to match 
it. It takes the master directory as the first argument and all the directories 
to fill out after that, as many as needed.
	Example --> syncdirs("input", "output", "output_src");
	
procdir: This actually processes the directory. It has several arguments. 
The first is the command string. Use %INPUT_DIR% in place of the input directory 
and %OUTPUT_DIR% in place of the output directory, as in 
	"java -jar "someprogram.jar" %INPUT_DIR% %OUTPUT_DIR%".
This allows for flexible usage. After that are the input and output directories 
to process as the second and third arguments. The fourth argument is the format 
string for the output file, using %FILENAME% for the name and %ORIGEXT% for the 
original extension. The final arguments are what extensions to look for while 
processing (1 or more).