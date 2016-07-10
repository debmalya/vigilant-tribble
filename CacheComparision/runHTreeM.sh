# limit on memory that can be reserved for all Direct Byte Buffers. If a value is set for this option, the sum of all Direct Byte Buffer sizes cannot exceed the limit. After the limit is reached, a new Direct Byte Buffer can be allocated only when enough old buffers are freed to provide enough space to allocate the new buffer
export MAVEN_OPTS="-XX:MaxDirectMemorySize=8G"
mvn exec:java -Dexec.mainClass="com.deb.cache.StressTestMemoryDBHTreeMap"
