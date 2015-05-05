visualvm_configs="-agentpath:/usr/share/visualvm/profiler/lib/deployed/jdk16/linux-amd64/libprofilerinterface.so=/usr/share/visualvm/profiler/lib,5140"

set -v

java $visualvm_configs -jar target/meanshift-complete.jar