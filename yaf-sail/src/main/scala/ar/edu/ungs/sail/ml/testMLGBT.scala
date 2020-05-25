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

object testMLGBT {
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
  
  dataOrig.collect()
  
  val data2=dataOrig.rdd.map(d=>(if(d.getDouble(0)==0) 0 else 1,d.get(1).asInstanceOf[SparseVector]))
  val data=spark.createDataFrame(data2).toDF("label","features")

  
// Index labels, adding metadata to the label column.
// Fit on whole dataset to include all labels in index.
val labelIndexer = new StringIndexer()
  .setInputCol("label")
  .setOutputCol("indexedLabel")
  .fit(data)
// Automatically identify categorical features, and index them.
// Set maxCategories so features with > 4 distinct values are treated as continuous.
val featureIndexer = new VectorIndexer()
  .setInputCol("features")
  .setOutputCol("indexedFeatures")
  .setMaxCategories(128)
  .fit(data)

// Split the data into training and test sets (30% held out for testing).
val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

// Train a GBT model.

val gbt = new GBTClassifier()
  .setLabelCol("indexedLabel")
  .setFeaturesCol("indexedFeatures")
  .setMaxIter(100)
  .setFeatureSubsetStrategy("auto")
  .setMaxBins(128)


// Convert indexed labels back to original labels.
val labelConverter = new IndexToString()
  .setInputCol("prediction")
  .setOutputCol("predictedLabel")
  .setLabels(labelIndexer.labels)

// Chain indexers and GBT in a Pipeline.
val pipeline = new Pipeline()
  .setStages(Array(labelIndexer, featureIndexer, gbt, labelConverter))

// Train model. This also runs the indexers.
val model = pipeline.fit(trainingData)

// Make predictions.
val predictions = model.transform(testData)

// Select example rows to display.
predictions.select("predictedLabel", "label", "features").show(5)

// Select (prediction, true label) and compute test error.
val evaluator = new MulticlassClassificationEvaluator()
  .setLabelCol("indexedLabel")
  .setPredictionCol("prediction")
  .setMetricName("accuracy")
val accuracy = evaluator.evaluate(predictions)
println(s"Test Error = ${1.0 - accuracy}")

val gbtModel = model.stages(2).asInstanceOf[GBTClassificationModel]
println(s"Learned classification GBT model:\n ${gbtModel.toDebugString}")

  }  
}