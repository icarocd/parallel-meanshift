# parallel-meanshift
Parallel implementation of MeanShift clustering algorithm, for Java language. This Meanshift implementation adopts a
distance matrix as its main input and uses flat kernel. It allows the customization of: seeds, quantile and maximum of iterations.<br/>
The parallelization is performed using Stream, from concurrent API present in Java 8. We also provide another parallel approach using thread pool.<br/><br/>
PS: the code requires java >= 8 and maven to be built.<br/>
	To install java 8 on Ubuntu:
		sudo add-apt-repository ppa:webupd8team/java<br/>
		sudo apt-get update<br/>
		sudo apt-get install oracle-java8-installer<br/>
	To install maven on Ubuntu: apt-get install mvn<br/>
<br/>
For profiling, we use and recommend VisualVM, along with its plugin plugin called 'startup profiler'.<br/>
	Installation procedure on Ubuntu:<br/>
		1. sudo apt-get install visualvm<br/>
		2. Open the visualvm<br/>
		3. Go to menu Tools -> Plugins, tab Available Plugins<br/>
		4. Install the plugin Startup Profiler. To make effect, close VisualVM and open it again.<br/>
<br/>
How to build:<br/>
	In root folder, execute the command: mvn clean package<br/>
	This will generate the resulting jar file: target/meanshift.jar<br/>
<br/>
How to use it:<br/>
	java -jar target/meanshift.jar input_file_name<br/>
	Example: java -jar target/meanshift.jar arq2500.in<br/>
<br/>
In order to test and program, we provide some distance matrix files as examples in the github project repository: see the files arq*.in.
We also provide a simple generator, see method generateRandomMatrix in MeanShiftClusterer class.
<br/>
How to profile the program with VisualVM:<br/>
	1. open visualvm and go to menu Applications -> Profile Startup<br/>
		Select:<br/>
			Platform: Java 8<br/>
			Profile: CPU<br/>
			Start profiling from classes: MeanShiftClusterer<br/>
			Profile only classes: MeanShiftClusterer, Matrix<br/>
		Copy the 'agentpath' provided by VisualVM, which will be required at program startup.<br/>
		Click on Profile button<br/>
	2: start the program, enabling the profile for VisualVM:<br/>
		The command may vary, but here it is a valid example:<br/>
		java -agentpath:/usr/share/visualvm/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/usr/share/visualvm/profiler/lib,5140 -jar target/meanshift.jar arq2500.in<br/><br/>
	3: VisualVM will open a monitor screen, containing hot spots and several metrics.