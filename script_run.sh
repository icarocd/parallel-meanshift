set -v

java -jar target/meanshift-complete.jar arq2500.in 1 12
java -jar target/meanshift-complete.jar arq2500.in 1 12
java -jar target/meanshift-complete.jar arq2500.in 1 12
java -jar target/meanshift-complete.jar arq2500.in 1 16
java -jar target/meanshift-complete.jar arq2500.in 1 16
java -jar target/meanshift-complete.jar arq2500.in 1 16

#java -jar target/meanshift-complete.jar arq3000.in 0
#java -jar target/meanshift-complete.jar arq3000.in 0
#java -jar target/meanshift-complete.jar arq3000.in 0
java -jar target/meanshift-complete.jar arq3000.in 1 12
java -jar target/meanshift-complete.jar arq3000.in 1 12
java -jar target/meanshift-complete.jar arq3000.in 1 12
java -jar target/meanshift-complete.jar arq3000.in 1 16
java -jar target/meanshift-complete.jar arq3000.in 1 16
java -jar target/meanshift-complete.jar arq3000.in 1 16

#java -jar target/meanshift-complete.jar arq3500.in 0
#java -jar target/meanshift-complete.jar arq3500.in 0
#java -jar target/meanshift-complete.jar arq3500.in 0
java -jar target/meanshift-complete.jar arq3500.in 1 12
java -jar target/meanshift-complete.jar arq3500.in 1 12
java -jar target/meanshift-complete.jar arq3500.in 1 12
java -jar target/meanshift-complete.jar arq3500.in 1 16
java -jar target/meanshift-complete.jar arq3500.in 1 16
java -jar target/meanshift-complete.jar arq3500.in 1 16

#java -agentpath:/opt/visualvm_138/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/opt/visualvm_138/profiler/lib,5140 -jar target/meanshift-complete.jar arq2500.in
#java -agentpath:/opt/visualvm_138/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/opt/visualvm_138/profiler/lib,5140 -jar target/meanshift-complete.jar arq3000.in
#java -agentpath:/opt/visualvm_138/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/opt/visualvm_138/profiler/lib,5140 -jar target/meanshift-complete.jar arq3500.in