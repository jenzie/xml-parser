#!C:\Perl\bin\perl.exe

use strict;
use warnings;
use Cwd;        # Get current working directory

# Global variable used to store the root directory, this is updated
# with the setrootdir command.
our $root_dir = "";
our $debug_flag = 0;

# Set the root directory that will be used for the remaining operations
# performed by the script.
sub setrootdir {

	# Set root_dir to the first argument and exit
	$root_dir = $_[0];
	return;
}

# Synchronize the directory trees. This will walk down the directory tree
# making sure all of the directories are in sync. It will sync as many
# directories as you give it.
# Usage syncdirs([master dir], [directories to sync]...)
sub syncdirs {
	
	# Check to make sure there are enough arguments
	if(@_ < 2) { die("Syncdirs needs at least two arguments!\n"); }
	
	# Build the master directory path
	my $master_path = "$root_dir\\$_[0]";
	if($debug_flag == 1) { print "Master Directory: $master_path\n"; }
	
	# Build a list of slave directories, removing the master
	my @slaves = splice @_, 1;
	
	# Open and read in
	opendir(MASTER, $master_path) or die("Couldn't open master directory\n");
	my @items = readdir(MASTER);
	closedir(MASTER);
	
	# Loop through looking for directories
	foreach my $item (@items) {
	
		# Skip the . and .. directories
		next if($item =~ m/^(\.|\.\.)$/);
		
		# Check to see if it is a directory
		if(-d "$master_path\\$item") {
			
			# Print if in debug mode
			if($debug_flag == 1){ print "\tFound Dir: $master_path\\$item\n"; }
			
			# Loop through the slave directories syncing if the directory 
			# doesn't exist at the same time, create new slave list for 
			# recursing
			my @reclist = ();
			foreach my $slave (@slaves) {
				
				# Build string
				my $tpath = "$root_dir\\$slave\\$item";
				
				# Check if it exists
				if(!(-d $tpath)) {
				
					# Create and print if in debug mode
					mkdir($tpath) or die(
						"Couldn't create directory: $tpath\n");
					if($debug_flag == 1){
						print "\t\tMissing dir created: $tpath\n"; }
				}
				
				# Create new slave entry
				push(@reclist, "$slave\\$item");
			}
			
			# Push master onto new list
			unshift(@reclist, "$_[0]\\$item");
			
			# Recurse down to next level
			syncdirs(@reclist);
		}
	}
}

# Handle the actual processing of the file.
# This function uses REGEX to be more flexible
# and user friendly. Read attached readme for help.
sub procdir {

	# Split out input into vars
	my $rawcmd = $_[0];
	my $indir = "$root_dir\\$_[1]";
	my $outdir = "$root_dir\\$_[2]";
	my $outformat = $_[3];
	my @lookfor = splice @_, 4;
	
	# Read all the file in the indir
	opendir(DIR, $indir) or die("Couldn't open $indir\n");
	my @items = readdir(DIR);
	closedir(DIR);
	
	# Print if in debug
	if($debug_flag == 1) { print "Now Processing: $indir\n"; }
	
	# Loop through each item
	foreach my $item (@items) {
		
		# Skip the . and .. directories
		next if($item =~ m/^(\.|\.\.)$/);
		
		# Check if it is directory
		if(-d "$indir\\$item") {
		
			# Build recurse args
			my @args = ();
			push(@args, $_[0]);
			push(@args, "$_[1]\\$item");
			push(@args, "$_[2]\\$item");
			push(@args, $_[3]);
			push(@args, @lookfor);
			
			# Recurse
			procdir(@args);
			
			# Print newline if in debug mode
			if($debug_flag == 1) { print "\n\n"; }
		}
		elsif(-f "$indir\\$item") {
		
			# Check to see if it is one of the valid file types
			my $flag_valid = 0;
			for(my $x = 0; $x < @lookfor; $x++) {
			
				if($item =~ m/$lookfor[$x]$/) { $flag_valid = 1; }
			}
			
			# Next if not valid
			next if ($flag_valid == 0);
			
			# Build the command string
			$rawcmd =~ s/%INPUT_DIR%/$indir\\$item/;
			
			# Get the output root and extension
			my @tmppts = split(/\./, reverse($item), 2);
			my $root = reverse($tmppts[1]);
			my $ext = reverse($tmppts[0]);
			
			# Build the output item
			$outformat =~ s/%FILENAME%/$root/;
			$outformat =~ s/%ORIGEXT%/$ext/;
			
			# Finish the command string
			$rawcmd =~ s/%OUTPUT_DIR%/$outdir\\$outformat/;
			
			# Run the command or print it based on the debug flag
			if($debug_flag == 1) {
				print "\t\t$rawcmd\n";
			} else {
				system($rawcmd);
			}
			
			# Refresh the edited variables for next run
			$rawcmd = $_[0];
			$outformat = $_[3];
		}
	}
}


# Runs the program
sub main {

    print "(1/6) Setting the root and syncing all subdirectories.\n";

    # Set the root and create the directories
    setrootdir(getcwd());
    syncdirs("input_src", "input", "output", "output_src");

    print "(2/6) Converting source files to .xml files.\n";

    # Convert .cpp and .h files to .xml files
    procdir("\"srcml\\src2srcml.exe\" %INPUT_DIR% -o %OUTPUT_DIR%",
        "input_src", "input", "%FILENAME%.%ORIGEXT%.xml", ".cpp", ".h");

    print "(3/6) Performing approximation on .xml files.\n";

    # Perform approximation on .xml files
    procdir("java -jar \"xml-parser.jar\" %INPUT_DIR% %OUTPUT_DIR%",
        "input", "output", "%FILENAME%.%ORIGEXT%", ".xml");

    print "(4/6) Converting .xml files to source files.\n";

    # Convert .xml files to .cpp and .h files
    procdir("\"srcml\\srcml2src.exe\" %INPUT_DIR% -o %OUTPUT_DIR%",
        "output", "output_src", "%FILENAME%", ".xml");

    print "(5/6) Compiling the source files.\n";

    # Compile the .cpp files
    system("g++ \"output_src\\*.cpp\" -o \"output_src\\scimark_v1.exe\"");

    print "(6/6) Running the executable file.\n";

    # Run the .exe
    system("\"output_src\\scimark_v1.exe\" > \"output_src\\scimark_v1.txt\"");
}

main();

# 1. file structure change
# need to have it go through the directories in "results"
# each directory in "results" is like "foo1", "foo2", "foo3"
# each directory in "results" has "input", "input_src", "output", "output_src"

# 2. collect time usage results for exe in each "foo#"
# set a timer with highest precision of accuracy possible before running exe (6/6)
# get time after exe has been closed
# write to output file named "summary" the end time for each directory in "results"
# just the number, but document the units in the function

# 3. collect memory usage results for exe in each "foo#"
# write to output file named "summary" start and end memory usage

# 4. write java code to parse the "summary" file to produce nicer output
# make it an .exe