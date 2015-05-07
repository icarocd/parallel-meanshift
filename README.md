# parallel-meanshift

OBS:<br/>
	Para build e uso, o programa requer oracle java 8 e maven.<br/>
		Instalação do oracle java 8 em Ubuntu:<br/>
			sudo add-apt-repository ppa:webupd8team/java<br/>
			sudo apt-get update<br/>
			sudo apt-get install oracle-java8-installer<br/>
		Instalação do maven em Ubuntu:<br/>
			apt-get install mvn<br/>
	Para profiling, usamos o VisualVM, com o plugin 'startup profiler' instalado.<br/>
		Instalação: sudo apt-get install visualvm<br/>
		Abrir o visualvm<br/>
		Ir no menu Tools -> Plugins, aba Available Plugins<br/>
		Instalar o plugin Startup Profiler. Para surtir efeito, reiniciar o VisualVM<br/>
<br/>
Instrucoes para compilação:<br/>
	1. Na raiz do projeto, executar o comando: mvn clean package<br/>
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
		Clicar no botão Profile<br/>
	2: iniciar o programa, com suporte a profiling:<br/>
		java -agentpath:/usr/share/visualvm/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/usr/share/visualvm/profiler/lib,5140 -jar target/meanshift-complete.jar<br/>
	3: O VisualVM irá abrir uma tela de monitoramento, contendo hot spots.