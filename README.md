MMTk-gc
=======

This code was written with MMTk Tutorial. This tutorial will build up a sophisticated garbage collector from scratch.
MMTk was build with two collectors:

1. Mark-Sweep Collector,
2. Hybrid Copying/Mark-Sweep Collector.

To build it:
bin/buildit localhost BaseBaseTutorial 

If you have problems with $JAVA_HOME add the next line:
-j /usr/lib/jvm/java-1.6.0-openjdk-i386

To run it:
dist/BaseBaseTutorial_ia32-linux/rvm HelloWorld

There is interesting benchmark test/GCBench.java
If you use -X:gc:verbose=3 you can see the movement of data among the spaces at each garbage collection.
