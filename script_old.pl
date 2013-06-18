#!C:\Perl\bin\perl.exe

use strict;
use warnings;
use Cwd;        # Get current working directory

# Global settings variables
our $insrc_proc = "\"srcml\\src2srcml.exe\""; 		# Run for each input_src
our $input_proc = "java -jar \"xml-parser.jar\"";	# Run for each input file
our $output_proc = "\"srcml\\srcml2src.exe\""; 		# Run on each output file
our $outsrc_proc = "g++";							# Run on each output_src
my $debug_flag = 0;									# If true, print commands
													# instead of executing them.

# Step through input_src directory to convert source files to source xml 
# files in the input directory.
sub inputsrc_handler {

	# Get the root directory, first function arg
    # and the current directory, second function
    my $root_dir = $_[0];
    my $cur_dir = $_[1];
    my $path = "$root_dir\\input_src\\$cur_dir";

    # If debugging print current dir
    if($debug_flag == 1) { print "Current directory: $path\n"; }

    # Open the current directory and read into array
    opendir(DIR, $path) or die "Fatal Error: Couldn't open $path\n";
    my @items = readdir(DIR);
    closedir(DIR);

    # Remove "." from path
    if($cur_dir eq ".") { $path = "$root_dir\\input_src" };

    foreach my $item (@items) {

        # Skip the . and .. directories
        next if($item =~ m/\.{1,2}$/);

        # Recurse for directories
        if(-d "$path\\$item") {

            # Recurse down into the subdirectory, don't pass . or ..
            if($cur_dir ne ".") {
                inputsrc_handler($root_dir, "$cur_dir\\$item");
            } else {
                inputsrc_handler($root_dir, "$item");
            }
        }

        # Handle xml files
        if((-f "$path\\$item") && ($item =~ m/(\.cpp|\.h)$/)) {

            # Example file name: array.cpp => array.cpp.xml
            # Build corresponding input path, add ending .xml
            my $insrc_path = "$root_dir\\input\\$cur_dir\\$item";
			if($cur_dir eq ".") { $insrc_path = "$root_dir\\input\\$item"; }
			$insrc_path = "$insrc_path.xml";
            #$insrc_path =~ s/\.xml$//;

            # Build command string
            my $cmdstr = "$insrc_proc \"$path\\$item\" -o \"$insrc_path\"";

            # If debug mode print, else run
            if($debug_flag == 1) {
                print "\t$cmdstr\n";
            } else {
                system($cmdstr);
            }
        }
    }
}									
													
# Step through input directory creating the proper directories in output and
# output_src if they don't exist. Then run the input command on all xml files.
sub input_handler {

	# Get the root directory, first function arg
	# and the current directory, second function
	my $root_dir = $_[0];
	my $cur_dir = $_[1];
	my $path = "$root_dir\\input\\$cur_dir";
	
	# If debugging print current dir
	if($debug_flag == 1) { print "Current directory: $path\n"; }
	
	# Open the current directory and read into array
	opendir(DIR, $path) or die "Fatal Error: Couldn't open $path\n";
	my @items = readdir(DIR);
	closedir(DIR);
	
	# Remove "." from path
	if($cur_dir eq ".") { $path = "$root_dir\\input" };
	
	# Loop through every item in the directory
	foreach my $item (@items) {
	
		# Skip the . and .. directories
		next if($item =~ m/\.{1,2}$/);
	
		# Handle items that are files that end in .xml
		if((-f "$path\\$item") && ($item =~ m/\.xml$/)) {
			
			# Form command string
			my $cmdstr = "$input_proc \"$path\\$item\" \"$path\\$item\"";
			
			# If debug mode print, else run
			if($debug_flag == 1) {
				print "\t$cmdstr\n";
			} else {
				system($cmdstr);
			}
		} elsif(-d "$path\\$item") {
		
			# Build output and output_src equivalents of this path
			my $out_path = "$root_dir\\output\\$cur_dir\\$item";
			my $outsrc_path = "$root_dir\\output_src\\$cur_dir\\$item";
			
			# Check to see if these items exist
			if(!(-d $out_path)) {
				
				# Make directory
				mkdir $out_path or die "Can't create dir $out_path\n";
			}
			
			if(!(-d $outsrc_path)) {
				
				# Make directory
				mkdir $outsrc_path or die "Can't create dir $outsrc_path\n";
			}
			
			# Recurse down into the subdirectory, don't pass . or ..
			if($cur_dir ne ".") {
				input_handler($root_dir, "$cur_dir\\$item");
			} else {
				input_handler($root_dir, "$item");
			}
		}
	}
}


# Step through the output directory processing all of the files in
# output into their corresponding output_src files
sub output_handler {

	# Get the root directory, first function arg
	# and the current directory, second function
	my $root_dir = $_[0];
	my $cur_dir = $_[1];
	my $path = "$root_dir\\output\\$cur_dir";
	
	# If debugging print current dir
	if($debug_flag == 1) { print "Current directory: $path\n"; }
	
	# Open the current directory and read into array
	opendir(DIR, $path) or die "Fatal Error: Couldn't open $path\n";
	my @items = readdir(DIR);
	closedir(DIR);
	
	# Remove "." from path
	if($cur_dir eq ".") { $path = "$root_dir\\output" };
	
	foreach my $item (@items) {
		
		# Skip the . and .. directories
		next if($item =~ m/\.{1,2}$/);
		
		# Recurse for directories
		if(-d "$path\\$item") {
		
			# Recurse down into the subdirectory, don't pass . or ..
			if($cur_dir ne ".") {
				output_handler($root_dir, "$cur_dir\\$item");
			} else {
				output_handler($root_dir, "$item");
			}
		}
		
		# Handle xml files
		if((-f "$path\\$item") && ($item =~ m/\.xml$/)) {

		    # Example file name: array.cpp.xml
			# Build corresponding output_src path, remove ending .xml
			my $outsrc_path = "$root_dir\\output_src\\$cur_dir\\$item";
			$outsrc_path =~ s/\.xml$//;
			
			# Build command string
			my $cmdstr = "$output_proc \"$path\\$item\" > \"$outsrc_path\"";
		
			# If debug mode print, else run
			if($debug_flag == 1) {
				print "\t$cmdstr\n";
			} else {
				system($cmdstr);
			}
		}
	}
}

# Step through the output_src directory, compiling all of the cpp files
# found.
sub outputsrc_handler {

	# Get the root directory, first function arg
	# and the current directory, second function
	my $root_dir = $_[0];
	my $cur_dir = $_[1];
	my $path = "$root_dir\\output_src\\$cur_dir";
	
	# If debugging print current dir
	if($debug_flag == 1) { print "Current directory: $path\n"; }
	
	# If debug mode print, else compile
	my $cmdstr = "$outsrc_proc \"$path\\*.cpp\" -o \"$path\\scimark_v1.exe\"";
	if($debug_flag == 1) {
		print "\t$cmdstr\n";
	} else {
		system($cmdstr);
	}
	
	# if debug mode print, else redirect results
	$cmdstr = "\"$path\\scimark_v1.exe\" > \"$path\\scimark_v1.txt\"";
	if($debug_flag == 1) {
		print "\t$cmdstr\n";
	} else {
		system($cmdstr);
	}
}

# Process the current working directory using the three functions.
sub process_dir {

	# Get current working directory to use as argument
	my $cwd = getcwd();
	$cwd =~ s/\//\\/g;

	inputsrc_handler($cwd, ".");
	input_handler($cwd, ".");
	output_handler($cwd, ".");
	outputsrc_handler($cwd, ".");
}


# Run the main function.
process_dir();