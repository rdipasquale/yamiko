package ar.edu.uca.gbm

@SerialVersionUID(1L)
abstract class ParametroGBM[T](id:String,_value:T,rangeFrom:T,rangeTo:T) extends Serializable{
  var value:T=_value
  def getValue=value
  def getMin=rangeFrom
  def getMax=rangeTo
  def setValue(_value:T)={value=_value}  
  override def toString:String="id="+id+";value="+value
}

@SerialVersionUID(1L)
class ParametroGBMCuantInt(id:String,_value:Int,rangeFrom:Int,rangeTo:Int) extends ParametroGBM[Int](id,_value,rangeFrom,rangeTo){
    override def toString:String="%03.2f".format(value)
}

@SerialVersionUID(1L)
class ParametroGBMCuantDouble(id:String,_value:Double,rangeFrom:Double,rangeTo:Double) extends ParametroGBM[Double](id,_value,rangeFrom,rangeTo){
  override def toString:String=value.toString()  
}

@SerialVersionUID(1L)
class ParametroGBMCualitativo(id:String,_value:Int,posibles:Array[String]) extends ParametroGBM[Int](id,_value,0,posibles.length){
  override def toString:String=posibles(value)
}

@SerialVersionUID(1L)
class ParametrizacionGBM(pathbase:String, experimento:String, iparque:String,seed:Int) extends Serializable{
  
  /*
    p_boosting      = "gbdt"                # gbdt = Gradient Boosting Decision Tree
                                           # rf   = Random Forest 
                                           # dart = Dropouts meet Multiple Additive Regression Trees
                                           # goss = Gradient-based One-Side Sampling  
  */
  val boosting=new ParametroGBMCualitativo("p_boosting",0,Array("gbdt","rf","dart","goss"))
  
  //# p_num_leaves    = 61                    # Max number of leaves in one tree (Default = 31)
  //#                                         # Aliases: num_leaf, max_leaves, max_leaf
  //#                                         # Constraints: 1 < num_leaves <= 131072
  val numLeaves=new ParametroGBMCuantInt("p_num_leaves",31,2,131072)

  //p_min_data_in_leaf =  int(sys.argv[7])   # Minimal number of data in one leaf. Can be used to deal with over-fitting (Default = 20)
  //                                         # Aliases: min_data_per_leaf, min_data, min_child_samples
  //                                         # Constraints: min_data_in_leaf >= 0
  val minDataInLeaf=new ParametroGBMCuantInt("p_min_data_in_leaf",20,0,100)
  
  //# p_learning_rate = 0.02                  # Shrinkage rate(Default = 0.1)
  //#                                         # Aliases: shrinkage_rate, eta. 
  //#                                         # In dart (p_boosting), it also affects on normalization weights of dropped trees
  //#                                         # Constraints: learning_rate > 0.0   
  val learningRate=new ParametroGBMCuantDouble("p_learning_rate",0.1,0.01,0.99)

  //p_lambda_l1        = float(sys.argv[9])  # L1 regularization (Default = 0.0)
  //                                         # Aliases: reg_alpha,
  //                                         # Constraints: lambda_l1 >= 0.0    
  val lambdaL1=new ParametroGBMCuantDouble("p_lambda_l1",0d,0d,1d)
  
  //p_lambda_l2        = float(sys.argv[10])  # L2 regularization (Default = 0.0)
  //                                         # Aliases: reg_alpha,
  //                                         # Constraints: lambda_l2 >= 0.0   
  val lambdaL2=new ParametroGBMCuantDouble("p_lambda_l2",0d,0d,1d)
  
  //p_feature_fraction = float(sys.argv[11])  # LightGBM will randomly select part of features on each iteration (tree) if feature_fraction smaller than 1.0. (Default = 1.0)
  //                                         # For example, if you set it to 0.8, LightGBM will select 80% of features before training each tree
  //                                         # can be used to speed up training
  //                                         # can be used to deal with over-fitting
  //                                         # Aliases: sub_feature, colsample_bytree
  //                                         # Constraints: 0.0 < feature_fraction <= 1.0                                                                                                       
  val featureFraction=new ParametroGBMCuantDouble("p_feature_fraction",1d,0.01d,1d)
  
  //p_bagging_fraction = float(sys.argv[12]) # Like feature_fraction, but this will randomly select part of data without resampling (Default = 1.0)
  //                                         # can be used to speed up training
  //                                         # can be used to deal with over-fitting
  //                                         # Note: to enable bagging, bagging_freq should be set to a non zero value as well
  //                                         # Aliases: sub_row, subsample, bagging
  //                                         # Constraints: 0.0 < bagging_fraction <= 1.0  
  val bagginFraction=new ParametroGBMCuantDouble("p_bagging_fraction",1d,0.01d,1d)
  
  //p_bagging_freq     = int(sys.argv[13])   # frequency for bagging (Default = 0)
  //                                         # 0 means disable bagging; k means perform bagging at every k iteration
  //                                         # Note: to enable bagging, bagging_fraction should be set to value smaller than 1.0 as well
  //                                         # Aliases: subsample_freq  
  val bagginFreq=new ParametroGBMCuantDouble("p_bagging_freq",0d,0d,1d)
  
  //# p_num_iteration = 5000                  # Numero de iteraciones en el entrenamiento: (Default = 100)
  //#                                         # Aliases:  num_iteration, n_iter, num_tree, num_trees,num_round, num_rounds, num_boost_round,
  //#                                         #           n_estimators 
  //#                                         # Constraints: >= 0   
  val numIterations=new ParametroGBMCuantInt("p_num_iteration",100,50,10000)

  //nfolds             = int(sys.argv[15])   # Numero de "folds" para Cross Validation. 
  //                                         # Numero grande tiende a LOOUT (mayor BIAS), valor bajo aumenta varianza- 
  //                                         # Valores tradicionales: entre 5 y 10 esta ok  
  val nFolds=new ParametroGBMCuantInt("nfolds",5,5,10)
  
  val parametrosOrdenados=List(numLeaves,minDataInLeaf,learningRate,lambdaL1,lambdaL2,featureFraction,bagginFraction,bagginFreq,numIterations,nFolds)
  
}

