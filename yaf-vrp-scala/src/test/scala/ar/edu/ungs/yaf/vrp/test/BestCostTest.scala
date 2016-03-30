package ar.edu.ungs.yaf.vrp.test

import org.junit._
import org.junit.Assert._
import ar.edu.ungs.yaf.vrp.BestCostMatrix
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauParser
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWSimpleFitnessEvaluator
import ar.edu.ungs.yaf.vrp.SBXCrossOverScala
import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.problems.vrp.Customer
import ar.edu.ungs.yamiko.problems.vrp.utils.ScalaAdaptor

@Test
class BestCostTest {

  val WORK_PATH="/media/ricardo/hd/logs/"
	val INDIVIDUALS=200
	val MAX_GENERATIONS=10000	
	//private static final String URI_SPARK="spark://192.168.1.40:7077";
	val URI_SPARK="local[8]"

	val lat01Ini= -34.481013
	val lat02Ini= -34.930460
	val lon01Ini= -58.325518
	val lon02Ini= -58.870122

    @Test
    def testBuildBCMatrix() = 
    {
      val holder=new Array[Int](3)
	    val customers=CordeauGeodesicParser.parse(WORK_PATH+"c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60)
	    val optInd=CordeauParser.parseSolution(WORK_PATH+"c101.res");

	    val m=holder(0) // Vehiculos
	    val n=holder(1) // Customers
	    val c=holder(2) // Capacidad (max)

	    val matrix=new DistanceMatrix(customers.values());
	    val fit:VRPFitnessEvaluator= new CVRPTWSimpleFitnessEvaluator(c,30d,m,matrix,1000000000d,10);
	    
	    val bcMatrix:Array[List[(Int,Double)]]=BestCostMatrix.build(matrix.getMatrix, ScalaAdaptor.toScala(customers));
	    
			val cross=new SBXCrossOverScala(30d, c, m, m-2,fit,matrix.getMatrix,bcMatrix);
	    
      
      assertTrue(true)
      
    }

}


