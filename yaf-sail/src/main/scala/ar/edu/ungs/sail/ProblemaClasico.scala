package ar.edu.ungs.sail

import ar.edu.ungs.serialization.Deserializador
import scalax.collection.edge.WUnDiEdge
import scala.collection.mutable.ListBuffer
import scalax.collection.GraphTraversal

/**
 * Resuelve el problema clasico documentado en "Martinez, Sainz-Trapaga"
 */
object ProblemaClasico extends App {
   
  override def main(args : Array[String]) {

      val nodoInicial:Nodo=new Nodo(0,0,"Inicial - (0)(1)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(0,0,"Final - (195)(199)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null);
      val carr40:VMG=new Carr40()
    
     //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario50x50.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]      

      var arcos=ListBuffer[WUnDiEdge[Nodo]]()
      val g=rioDeLaPlata.getGraph()
      val ni=g get nodoInicial
      val nf=g get nodoFinal
      
      println("empieza en " + System.currentTimeMillis())
      ni.shortestPathTo(nf)
      println("termina en " + System.currentTimeMillis())
      
      def negWeight(e: g.EdgeT): Float = calcCosto(e._1,e._2,rioDeLaPlata.getMetrosPorLadoCelda(),t0,carr40)      
      
      println("empieza en " + System.currentTimeMillis())      
      val spNO = ni shortestPathTo (nf, negWeight) // Path(3, 2~3 %2, 2, 1~2 %4, 1)
      val spN = spNO.get                        // here we know spNO is defined
      spN.weight      
      println("termina " + spN.weight + " en " + System.currentTimeMillis())
      
      // Calcular Costos
//    val costos=rioDeLaPlata.getGraph()
//      rioDeLaPlata.getArcos().foreach(arco=>{
//        // Determinar si es un arco entre nodos hermanos o de navegacion
//        if (arco._1.getManiobra().equals(arco._2.getManiobra())) // Navegacion
//          arcos+=WUnDiEdge(arco._1,arco._2)(calcCostoNavegacion(arco._1,arco._2,rioDeLaPlata.getMetrosPorLadoCelda()))      
//      })
    }


  def calcCosto(u:Nodo,v:Nodo,metrosPorCelda:Int,valores:List[((Int, Int), Int, Int, Int)],vmg:VMG):Float=if (u.getManiobra().equals(v.getManiobra())) calcCostoNavegacion(u,v,metrosPorCelda,valores,vmg) else calcCostoManiobra(u,v)
  
  def calcCostoManiobra(u:Nodo,v:Nodo):Float=COSTOS_MANIOBRAS.valores(u.getManiobra().id)(v.getManiobra().id).floatValue()
  
  /**
   * Costo(u; v; t) = 0 si u es el nodo inicial y v esta en la linea de salida.
   * Costo(u; v; t) = 0 si v es el nodo final y u esta en la linea de llegada.
   * Costo(u; v; t) = min_c:cuadrante =u en c y v en c Costo(u; v; c; t) si u y v no son hermanos y comparten por lo menos un cuadrante.
   * Costo(u; v; t) = infinito en otro caso.
   */
  def calcCostoNavegacion(u:Nodo,v:Nodo,metrosPorCelda:Int,valores:List[((Int, Int), Int, Int, Int)],vmg:VMG):Float={
    if (u.getId().startsWith("Inicial") || v.getId().startsWith("Inicial")) return 0f
    if (u.getId().startsWith("Final") || v.getId().startsWith("Final")) return 0f
    // Determino cuadrante
    val cuadrante=u.getCuadrante().intersect(v.getCuadrante())(0)        
    val unidadDist2= (metrosPorCelda.doubleValue()/metrosPorCelda.doubleValue())*(metrosPorCelda.doubleValue()/metrosPorCelda.doubleValue())
    val distancia=Math.sqrt((u.getX()-v.getX())*(u.getX()-v.getX())*unidadDist2+(u.getY()-v.getY())*(u.getY()-v.getY()))*unidadDist2
    valores.filter(f=>f._1._1==cuadrante._1 && f._1._2==cuadrante._2)
    // Si es tierra....
    if (valores==null) Float.MaxValue/2 else if (valores.isEmpty) Float.MaxValue/2
    val anguloNormalizado=valores(0)._2+ (if(u.getX()-v.getX()==0) (if (Math.signum(v.getY()-u.getY())>=0) 0 else 180) else Math.toDegrees(Math.atan((v.getY()-u.getY())/(u.getX()-v.getX()))))
    val velocidadMaxima=vmg.getSpeed(anguloNormalizado.toInt, valores(0)._3)
    if (velocidadMaxima==0) Float.MaxValue/2
    distancia.floatValue()/(velocidadMaxima*CONSTANTS.METROS_POR_MILLA_NAUTICA).floatValue()    
  }
 
}