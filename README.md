# parallel-meanshift

OBS:
	Para build e uso, o programa requer oracle java 8 e maven.
		Instalação do oracle java 8 em Ubuntu:
			sudo add-apt-repository ppa:webupd8team/java
			sudo apt-get update
			sudo apt-get install oracle-java8-installer
		Instalação do maven em Ubuntu:
			apt-get install mvn
	Para profiling, usamos o VisualVM, com o plugin 'startup profiler' instalado.
		Instalação: sudo apt-get install visualvm
		Abrir o visualvm
		Ir no menu Tools -> Plugins, aba Available Plugins
		Instalar o plugin Startup Profiler. Para surtir efeito, reiniciar o VisualVM

Instrucoes para compilação:
	1. Na raiz do projeto, executar o comando: mvn clean package
		Irá gerar o arquivo target/meanshift-complete.jar

Executar o programa:
	...

Executar o programa com profiling VisualVM:
	1. abrir o visualvm e, acessar o menu Applications -> Profile Startup
		Selecionar:
			Platform: Java 8
			Profile: CPU
			Start profiling from classes: MeanShiftClusterer
		Clicar no botão Profile
	2: iniciar o programa, com suporte a profiling:
		java -agentpath:/usr/share/visualvm/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/usr/share/visualvm/profiler/lib,5140 -jar target/meanshift-complete.jar
	3: O VisualVM irá abrir uma tela de monitoramento, contendo hot spots.