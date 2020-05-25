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

object testMLGBT2 {
  def main(args: Array[String]): Unit = {
    
    
  	val URI_SPARK="local[4]"
    val conf=new SparkConf().setMaster(URI_SPARK)
    val spark = SparkSession
      .builder
      .appName("TestML")
      .config(conf)
      .getOrCreate()
    
    // Load the data stored in LIBSVM format as a DataFrame.
  //val data = spark.read.format("libsvm").load("/media/ricardo/hd1/Ricardo/UNGS/DSO2020/1/features3.csv")
  val dataOrig = spark.read.format("libsvm").load("/media/ricardo/hd1/Ricardo/UNGS/DSO2020/2/training.txt")
  
  val data2=dataOrig.rdd.map(d=>(if(d.getDouble(0)==0) 0 else 1,d.get(1).asInstanceOf[SparseVector]))
  val data=spark.createDataFrame(data2).toDF("label","features")

  data.printSchema()
  
// Index labels, adding metadata to the label column.
// Fit on whole dataset to include all labels in index.
//val labelIndexer = new StringIndexer()
//  .setInputCol("label")
//  .setOutputCol("indexedLabel")
//  .fit(data)
// Automatically identify categorical features, and index them.
// Set maxCategories so features with > 4 distinct values are treated as continuous.
//val featureIndexer = new VectorIndexer()
//  .setInputCol("features")
//  .setOutputCol("indexedFeatures")
//  .setMaxCategories(128)
//  .fit(data)

// Split the data into training and test sets (30% held out for testing).
val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

// Train a GBT model.


  val lgbm = new LightGBMClassifier()
      .setLabelCol("label")
      .setObjective("binary")
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
      .setNumIterations(1000)
      .setIsUnbalance(true)

//  val paramGrid = new ParamGridBuilder().addGrid(lgbm.lambdaL2, Array(1.0,101.0,10.0)).build
//
//  val evaluator = new BinaryClassificationEvaluator().setLabelCol("label").setMetricName("areaUnderROC")
//  
//  val crossValidator = new CrossValidator().setEstimator(lgbm).setEstimatorParamMaps(paramGrid).setEvaluator(evaluator).setNumFolds(2)
//
//  val pipelineModel = new Pipeline().setStages(Array(labelIndexer, featureIndexer, crossValidator))
  
  val Array(train, test )= data.randomSplit(Array(0.8, 0.2),7)
  
  val model=lgbm.fit(train)
    
  model.saveNativeModel("/tmp/mymodel",true)
  val model2 = LightGBMClassificationModel.loadNativeModelFromFile("/tmp/mymodel")
  
  val scoredData=model2.transform(test)
  scoredData.show(100,false)
  val metrics = new ComputeModelStatistics().setEvaluationMetric("classification").setLabelCol("label").setScoredLabelsCol("prediction").transform(scoredData)
  
  metrics.show(200,false)
  
//                  tp      fn                     eficacia (correctos/total)            exhaustividad = tp/(tp+fn)
//                  fp      tn                                        tp/(tp+fp)         sensitivity (enfermos correctamente diagnosticados)
//+---------------+-------------------------------+------------------+------------------+------------------+------------------+
//|evaluation_type|confusion_matrix               |accuracy          |precision         |recall            |AUC               |
//+---------------+-------------------------------+------------------+------------------+------------------+------------------+
//|Classification |165.0    14.0    
//                  16.0  1518.0                  |0.9824868651488616|0.9908616187989556|0.9895697522816167|0.9556787308894116|
//+---------------+-------------------------------+------------------+------------------+------------------+------------------+
  
  
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