package ar.edu.ungs.sail.ml


// $example on$
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{GBTClassificationModel, GBTClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
// $example off$
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.ml.linalg.SparseVector
import com.microsoft.ml.spark.LightGBMClassifier
import com.microsoft.ml.spark.LightGBMClassifier
import org.apache.spark.ml.tuning.ParamGridBuilder
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.tuning.CrossValidator
import com.microsoft.ml.spark.LightGBMUtils
import com.microsoft.ml.spark.LightGBMClassificationModel
import com.microsoft.ml.spark.ComputeModelStatistics
import com.microsoft.ml.spark.LightGBMBooster
import com.microsoft.ml.spark.LightGBMBase
import com.microsoft.ml.spark.LightGBMRegressor
import com.microsoft.ml.spark.LightGBMRegressionModel

object testMLGBTPredictCV {
  def main(args: Array[String]): Unit = {
    
    
  	val URI_SPARK="local[8]"
    val conf=new SparkConf().setMaster(URI_SPARK)
    val spark = SparkSession
      .builder
      .appName("TestML")
      .config(conf)
      .getOrCreate()
    
    // Load the data stored in LIBSVM format as a DataFrame.
  //val data = spark.read.format("libsvm").load("/media/ricardo/hd1/Ricardo/UNGS/DSO2020/1/features3.csv")
  val data= spark.read.format("libsvm").load("/media/ricardo/hd1/Ricardo/UNGS/DSO2020/2/trainingPredict.txt")
  

  data.printSchema()
  data.show()
//  
//  val paramGrid = new ParamGridBuilder()
//  .addGrid(hashingTF.numFeatures, Array(10, 100, 1000))
//  .addGrid(lr.regParam, Array(0.1, 0.01))
//  .build()
//  
//  val pipeline = new Pipeline()
//  .setStages(Array(tokenizer, hashingTF, lr))
//  
//val cv = new CrossValidator()
//  .setEstimator(pipeline)
//  .setEvaluator(new BinaryClassificationEvaluator)
//  .setNumFolds(5)  // Use 3+ in practice
//  .setParallelism(2)  // Evaluate up to 2 parameter settings in parallel  
//  
//  
  

// Split the data into training and test sets (30% held out for testing).
val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

// Train a GBT model.

  val lgbm = new LightGBMRegressor()
      .setLabelCol("label")
      .setObjective("regression")
      .setBoostingType("gbdt")
      .setNumLeaves(31)
      .setLearningRate(0.2)
      .setVerbosity(1)
      .setLambdaL1(0.1)
      .setLambdaL2(0.1)
      .setBaggingFraction(0.9)
      .setBaggingFreq(9)
      .setFeaturesCol("features")
      .setMaxDepth(-1)
      .setNumIterations(2000)


//  val paramGrid = new ParamGridBuilder().addGrid(lgbm.lambdaL2, Array(1.0,101.0,10.0)).build
//
//  val evaluator = new BinaryClassificationEvaluator().setLabelCol("label").setMetricName("areaUnderROC")
//  
//  val crossValidator = new CrossValidator().setEstimator(lgbm).setEstimatorParamMaps(paramGrid).setEvaluator(evaluator).setNumFolds(2)
//
//  val pipelineModel = new Pipeline().setStages(Array(labelIndexer, featureIndexer, crossValidator))
  
  val Array(train, test )= data.randomSplit(Array(0.8, 0.2),7)
  
  val model=lgbm.fit(train)
    
  model.saveNativeModel("/tmp/mymodelPredict",true)
  val model2 = LightGBMRegressionModel.loadNativeModelFromFile("/tmp/mymodelPredict")
  
  val scoredData=model2.transform(test)
  scoredData.show(100,false)
  val metrics = new ComputeModelStatistics().setEvaluationMetric("regression").setLabelCol("label").setScoresCol("prediction").transform(scoredData)
  
  metrics.show(20,false)
  
//+------------------+-----------------------+------------------+-------------------+
//|mean_squared_error|root_mean_squared_error|R^2               |mean_absolute_error|
//+------------------+-----------------------+------------------+-------------------+
//|1173.8087060092103|34.26089178654301      |0.9833813477043791|16.07175137361102  |
//+------------------+-----------------------+------------------+-------------------+
  
  
  
  val sub=scoredData.rdd.map(s=>math.abs(s.getDouble(0)-s.getDouble(2))).histogram(10)
  for( i <- 0 to 9){
         println( sub._1(i) + " - " + sub._2(i) + " - " + (100*sub._2(i)/scoredData.count())+"%" );
      }

  	
  	
//3.548241140833852E-4 - 1409 - 82%
//26.323244600607588 - 151 - 8%
//52.64613437710109 - 73 - 4%
//78.96902415359459 - 34 - 1%
//105.2919139300881 - 14 - 0%
//131.6148037065816 - 18 - 1%
//157.9376934830751 - 9 - 0%
//184.2605832595686 - 1 - 0%
//210.58347303606212 - 1 - 0%
//236.9063628125556 - 3 - 0%
//Sobre 1713 casos heldout  	
//  sub.show(1000,false)
//  
  println(scoredData.count())
//  val model = pipelineModel.fit(train)
//  val preds = model.transform(test).toDF()
  
//  println("dataOrig")
//  dataOrig.show(20,false)
//  println("data")
//  data.show(20,false)
//  println("train")
//  train.show(20,false)
//  println("model")
//  train.show(20,false)
//  println("preds")
//  train.show(200,false)
  
  
  
  
  
//  val binaryEvaluator = new BinaryClassificationEvaluator().setLabelCol("label")
//  println ("Test Area Under ROC: " + binaryEvaluator.evaluate(preds))
//  //True positives
//  val tp = preds.filter(p=>p.get(0)== 1 & p.get(1)== 1).count() 
//  //True negatives
//  val tn = preds.filter(p=>p.get(0)== 0 & p.get(1)== 0).count()
////  #False positives
////  fp = preds[(preds.label == 0) & (preds.prediction == 1)].count()
////  #Falsel negatives
////  fn = preds[(preds.label == 1) & (preds.prediction == 0)].count()
//  println ("True Positives:", tp)
//  println ("True Negatives:", tn)
////  println ("False Positives:", fp)
////  println ("False Negatives:", fn)
//  println ("Total", preds.count())  
////  val r = (tp)/(tp + fn)  
////  println ("recall", r)  
////  val p = (tp) / (tp + fp)
////  println ("precision", p)
////  val f1 = 2 * p * r /(p + r)  
////  println ("f1", f1)
//      
//  //val fit=lgbm.fit(data)      
      
      


  }  
}