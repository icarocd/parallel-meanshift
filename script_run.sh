set -v

#java -agentpath:/opt/visualvm_138/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/opt/visualvm_138/profiler/lib,5140 -jar target/meanshift-complete.jar arq500.in
#java -agentpath:/opt/visualvm_138/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/opt/visualvm_138/profiler/lib,5140 -jar target/meanshift-complete.jar arq1000.in
java -agentpath:/opt/visualvm_138/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/opt/visualvm_138/profiler/lib,5140 -jar target/meanshift-complete.jar arq2500.in