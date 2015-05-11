# parallel-meanshift
MeanShift clustering algorithm implementation for Java language. It uses flat kernel and allows the customization of: seeds, quantile and maximum of iterations.<br/>
A parallel version is in current development. Patches and suggestions are welcome.
<br/>
OBS:<br/>
	Para build e uso, o programa requer oracle java 8 e maven.<br/>
		Instalação do oracle java 8 em Ubuntu:<br/>
			sudo add-apt-repository ppa:webupd8team/java<br/>
			sudo apt-get update<br/>
			sudo apt-get install oracle-java8-installer<br/>
		Instalação do maven em Ubuntu: apt-get install mvn<br/>
	Para profiling, usamos o VisualVM, com o plugin 'startup profiler' instalado.<br/>
		Instalação:<br/>
		1. sudo apt-get install visualvm<br/>
		2. Abrir o visualvm<br/>
		3. Ir no menu Tools -> Plugins, aba Available Plugins<br/>
		4. Instalar o plugin Startup Profiler. Para surtir efeito, reiniciar o VisualVM<br/>
<br/>
Instrucoes para compilação:<br/>
	Na raiz do projeto, executar o comando: mvn clean package<br/>
	Irá gerar o arquivo target/meanshift-complete.jar<br/>
<br/>
Executar o programa:<br/>
	java -jar target/meanshift-complete.jar arq1500.in<br/>
<br/>
Executar o programa com profiling VisualVM:<br/>
	1. abrir o visualvm e, acessar o menu Applications -> Profile Startup<br/>
		Selecionar:<br/>
			Platform: Java 8<br/>
			Profile: CPU<br/>
			Start profiling from classes: MeanShiftClusterer<br/>
			Profile only classes: MeanShiftClusterer, Matrix<br/>
		Clicar no botão Profile<br/>
	2: iniciar o programa, com suporte a profiling:<br/>
		java -agentpath:/usr/share/visualvm/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/usr/share/visualvm/profiler/lib,5140 -jar target/meanshift-complete.jar<br/>
	3: O VisualVM irá abrir uma tela de monitoramento, contendo hot spots.