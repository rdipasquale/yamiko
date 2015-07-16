cd /home/ricardo/git/yamiko/yaf-vrp
/home/ricardo/cluster/spark-1.3.1-bin-hadoop2.6/bin/spark-submit --class ar.edu.ungs.yamiko.problems.vrp.problems.CVRPTWCordeau101Parallel --master spark://192.168.1.40:7077 --jars \
/home/ricardo/git/yamiko/yaf-gadomain/target/yaf-gadomain-0.0.1-SNAPSHOT.jar,\
/home/ricardo/git/yamiko/geodesy/target/gavahanGeodesy-1.1.2.jar,\
/home/ricardo/git/yamiko/yaf-workflow-controller/target/yaf-workflow-controller-1.0.0.jar,\
/home/ricardo/.m2/repository/org/jgrapht/jgrapht-core/0.9.1/jgrapht-core-0.9.1.jar,\
/home/ricardo/.m2/repository/org/jgrapht/jgrapht-ext/0.9.1/jgrapht-ext-0.9.1.jar,\
/home/ricardo/.m2/repository/org/tinyjee/jgraphx/jgraphx/2.0.0.1/jgraphx-2.0.0.1.jar,\
/home/ricardo/.m2/repository/jgraph/jgraph/5.13.0.0/jgraph-5.13.0.0.jar,\
/home/ricardo/.m2/repository/com/graphhopper/graphhopper/0.5-SNAPSHOT/graphhopper-0.5-SNAPSHOT.jar,\
/home/ricardo/.m2/repository/com/google/guava/guava/18.0/guava-18.0.jar \
/home/ricardo/git/yamiko/yaf-vrp/target/yaf-vrp-1.0.0.jar

