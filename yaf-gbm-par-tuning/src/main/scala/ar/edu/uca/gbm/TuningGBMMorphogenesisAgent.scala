package ar.edu.uca.gbm

import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import ar.edu.ungs.yamiko.ga.exceptions.NullGenotypeException
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.impl.BasicPhenotype
import scala.collection.mutable.ListBuffer
import sys.process._
import scala.io.Source
import java.io._

class TuningGBMMorphogenesisAgent(pathbase:String, iparque:String,seed:Int) extends MorphogenesisAgent[Array[Int]]{
  
  @throws(classOf[YamikoException])
  override def develop(genome:Genome[Array[Int]] , ind:Individual[Array[Int]])=
	{
		if (genome==null) throw new IndividualNotDeveloped(this.getClass().getSimpleName()+" - develop -> Null genome");
		if (ind==null) throw new NullIndividualException(this.getClass().getSimpleName()+" - develop -> Null Individual");
		if (ind.getGenotype()==null) throw new NullGenotypeException(this.getClass().getSimpleName()+" - develop -> Null Genotype");

		val chromosome= ind.getGenotype().getChromosomes()(0);
		val allele=chromosome.getFullRawRepresentation()
		var alleles=Map[Gene, ParametrizacionGBM]()
		val g = genome.getStructure().head._2(0)
		val par=translate(allele)
		alleles+=( g -> par)
		val phenotype=new BasicPhenotype[Array[Int]]( chromosome , alleles);
		
    val proceso="r"+ind.getId().toString()
    //val command="python3 " + pathbase + "/trainingLightGBMParam.py " + pathbase + "/ "+proceso+" " + iparque + " gbdt " + seed + " " + alleles.get(g).get.toString() //" 31 20 0.1 0.0 0.0 1.0 1.0 0 100 5 "
    val command="python3 " + pathbase + "/trainingLightGBMParam.py " + pathbase + "/ "+proceso+" " + iparque + " gbdt " + seed + " " + alleles.get(g).get.parametrosOrdenados.slice(0,alleles.get(g).get.parametrosOrdenados.size-1).map(f=>f.toString()).mkString(" ") +" " + alleles.get(g).get.numIterations +" "+alleles.get(g).get.parametrosOrdenados.slice(alleles.get(g).get.parametrosOrdenados.size-1,alleles.get(g).get.parametrosOrdenados.size).map(f=>f.toString()).mkString(" ")
    val result:Int=(command !)            
    val filename = pathbase + "/" + proceso + "_" + iparque + "_errores.csv"
    var mae:Double=Double.MaxValue
    if (result==0) for (line <- Source.fromFile(filename).getLines) if (line.startsWith("MAE")) mae=line.substring(5).toDouble                        
    ind.setIntAttachment(List((mae*1000000).toInt))		
		
		ind.setPhenotype(phenotype);
    
    // TRack salida            
//    val pw = new PrintWriter(new File(pathbase + "/"+proceso+"_salidaMA.log"))
//    pw.write(command)
//    pw.write("\n")    
//    pw.write("MAE="+mae+"\n")
//    pw.write(par.toStringRepresentation)
//    pw.close
    
	}

  def translate(allele:Array[Int]):ParametrizacionGBM ={
		if (allele==null) return null
		if (allele.length==0) return null
	  val salida=new ParametrizacionGBM(pathbase, "", iparque,seed)
	  salida.numLeaves.setValue(allele(0))
	  salida.minDataInLeaf.setValue(allele(1))
	  salida.learningRate.setValue(allele(2).toDouble/100)
	  salida.lambdaL1.setValue(allele(3).toDouble/100)
	  salida.lambdaL2.setValue(allele(4).toDouble/100)
	  salida.featureFraction.setValue(allele(5).toDouble/100)
	  salida.bagginFraction.setValue(allele(6).toDouble/100)
	  salida.bagginFreq.setValue(allele(7))
	  salida.nFolds.setValue(allele(8))
//	  salida.numIterations.setValue(allele(8))
//	  salida.nFolds.setValue(allele(9))
	  salida

    //pathbase           = sys.argv[1]         # Path donde estan todos los archivos
    //experimento = sys.argv[1]+sys.argv[2]    # nombre del identificador del experimento para guardar los resultados
    //
    //iparque            = sys.argv[3]         # NEMO del parque eolico (necesitamos los archivos de entrada de cada parque. 
    //                                         # Ej: MANAEO
    //
    //p_boosting         = sys.argv[4]         # gbdt = Gradient Boosting Decision Tree
    //                                         # rf   = Random Forest 
    //                                         # dart = Dropouts meet Multiple Additive Regression Trees
    //                                         # goss = Gradient-based One-Side Sampling 
    //                                 
    //p_seed             = int(sys.argv[5])    # This seed is used to generate other seeds, e.g. data_random_seed, feature_fraction_seed, etc. (default = None)
    //                                         # by default, this seed is unused in favor of default values of other seeds
    //                                         # this seed has lower priority in comparison with other seeds, which means that it will be overridden, if you set other seeds explicitly
    //                                         # aliases: random_seed, random_state   
    //                                                                     
    //p_num_leaves       = int(sys.argv[6])    # Max number of leaves in one tree (Default = 31)
    //                                         # Aliases: num_leaf, max_leaves, max_leaf
    //                                         # Constraints: 1 < num_leaves <= 131072  
    //                                 
    //p_min_data_in_leaf =  int(sys.argv[7])   # Minimal number of data in one leaf. Can be used to deal with over-fitting (Default = 20)
    //                                         # Aliases: min_data_per_leaf, min_data, min_child_samples
    //                                         # Constraints: min_data_in_leaf >= 0
    //                                    
    //p_learning_rate    = float(sys.argv[8])  # Shrinkage rate(Default = 0.1)
    //                                         # Aliases: shrinkage_rate, eta. 
    //                                         # In dart (p_boosting), it also affects on normalization weights of dropped trees
    //                                         # Constraints: learning_rate > 0.0 
    //
    //p_lambda_l1        = float(sys.argv[9])  # L1 regularization (Default = 0.0)
    //                                         # Aliases: reg_alpha,
    //                                         # Constraints: lambda_l1 >= 0.0  
    //                                    
    //p_lambda_l2        = float(sys.argv[10])  # L2 regularization (Default = 0.0)
    //                                         # Aliases: reg_alpha,
    //                                         # Constraints: lambda_l2 >= 0.0  
    //                                    
    //p_feature_fraction = float(sys.argv[11])  # LightGBM will randomly select part of features on each iteration (tree) if feature_fraction smaller than 1.0. (Default = 1.0)
    //                                         # For example, if you set it to 0.8, LightGBM will select 80% of features before training each tree
    //                                         # can be used to speed up training
    //                                         # can be used to deal with over-fitting
    //                                         # Aliases: sub_feature, colsample_bytree
    //                                         # Constraints: 0.0 < feature_fraction <= 1.0                                                                                                       
    //
    //p_bagging_fraction = float(sys.argv[12]) # Like feature_fraction, but this will randomly select part of data without resampling (Default = 1.0)
    //                                         # can be used to speed up training
    //                                         # can be used to deal with over-fitting
    //                                         # Note: to enable bagging, bagging_freq should be set to a non zero value as well
    //                                         # Aliases: sub_row, subsample, bagging
    //                                         # Constraints: 0.0 < bagging_fraction <= 1.0
    //                                    
    //p_bagging_freq     = int(sys.argv[13])   # frequency for bagging (Default = 0)
    //                                         # 0 means disable bagging; k means perform bagging at every k iteration
    //                                         # Note: to enable bagging, bagging_fraction should be set to value smaller than 1.0 as well
    //                                         # Aliases: subsample_freq
    //p_num_iteration    = int(sys.argv[14])   # Numero de iteraciones en el entrenamiento: (Default = 100)
    //                                         # Aliases:  num_iteration, n_iter, num_tree, num_trees,num_round, num_rounds, num_boost_round,
    //                                         # n_estimators 
    //                                         # Constraints: >= 0                                      
    //                                    
    //nfolds             = int(sys.argv[15])   # Numero de "folds" para Cross Validation. 
    //                                      	  
	}  
}