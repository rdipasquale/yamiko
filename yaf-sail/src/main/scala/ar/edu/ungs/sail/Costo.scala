package ar.edu.ungs.sail

object Costo {
  
  def calcCosto(u:Nodo,v:Nodo,metrosPorCelda:Int,nodosPorCelda:Int,valores:List[((Int, Int), Int, Int, Int)],vmg:VMG):Float={
    if (u.getId().startsWith("Inicial") || v.getId().startsWith("Inicial")) return 0f
    if (u.getId().startsWith("Final") || v.getId().startsWith("Final")) return 0f
    if (u.getCuadrante().intersect(v.getCuadrante())==null) return Float.MaxValue/2
    if (u.getManiobra().equals(v.getManiobra())) calcCostoNavegacion(u,v,metrosPorCelda,nodosPorCelda,valores,vmg) else calcCostoManiobra(u,v)
  }
    
  
  def calcCostoManiobra(u:Nodo,v:Nodo):Float=
    COSTOS_MANIOBRAS.valores(u.getManiobra().id)(v.getManiobra().id).floatValue()
  
  /**
   * Costo(u; v; t) = 0 si u es el nodo inicial y v esta en la linea de salida.
   * Costo(u; v; t) = 0 si v es el nodo final y u esta en la linea de llegada.
   * Costo(u; v; t) = min_c:cuadrante =u en c y v en c Costo(u; v; c; t) si u y v no son hermanos y comparten por lo menos un cuadrante.
   * Costo(u; v; t) = infinito en otro caso.
   */
  def calcCostoNavegacion(u:Nodo,v:Nodo,metrosPorCelda:Int,nodosPorCelda:Int,valores:List[((Int, Int), Int, Int, Int)],vmg:VMG):Float={

    //println(u.getX()+","+u.getY()+ " al Maniobra:" + u.getManiobra() + " al " + v.getX() + "," + v.getY() + " Maniobra:" + v.getManiobra()  + "....")

    // Determino cuadrante
    val cuadrante=u.getCuadrante().intersect(v.getCuadrante())(0)        
    val unidadDist2= (metrosPorCelda.doubleValue()/nodosPorCelda.doubleValue())*(metrosPorCelda.doubleValue()/nodosPorCelda.doubleValue())
    val distancia=Math.sqrt((u.getX()-v.getX())*(u.getX()-v.getX())*unidadDist2+(u.getY()-v.getY())*(u.getY()-v.getY())*unidadDist2)
    val vientos=valores.filter(f=>f._1._1==cuadrante._1 && f._1._2==cuadrante._2)
    // Si es tierra....
    if (vientos==null) return Float.MaxValue/2 else if (vientos.isEmpty) return Float.MaxValue/2
    
    var anguloNavegacion:Double=0d
    if(u.getX-v.getX==0) {
      if (Math.signum(v.getY-u.getY)>=0) 
        anguloNavegacion=0d 
      else 
        anguloNavegacion=180d}
    else if(v.getX>u.getX && v.getY>=u.getY) 
         anguloNavegacion=90d-(Math.toDegrees(Math.atan((v.getY().doubleValue()-u.getY().doubleValue())/(v.getX().doubleValue()-u.getX().doubleValue()))))
       else if(v.getX>u.getX && v.getY<=u.getY) 
         anguloNavegacion=90d+Math.abs(Math.toDegrees(Math.atan((v.getY().doubleValue()-u.getY().doubleValue())/(v.getX().doubleValue()-u.getX().doubleValue()))))
         else if(v.getX<u.getX && v.getY<=u.getY)
           if (v.getY-u.getY==0) anguloNavegacion=270d else anguloNavegacion=270d-(Math.toDegrees(Math.atan((v.getY().doubleValue()-u.getY().doubleValue())/(v.getX().doubleValue()-u.getX().doubleValue()))))
       else if(v.getX<u.getX && v.getY>=u.getY) 
           anguloNavegacion=270+Math.abs((Math.toDegrees(Math.atan((v.getY().doubleValue()-u.getY().doubleValue())/(v.getX().doubleValue()-u.getX().doubleValue())))))
    val anguloNormalizado=(540-vientos(0)._2+Math.round(anguloNavegacion))%360 
    val velocidadMaxima=vmg.getSpeed(anguloNormalizado.toInt, vientos(0)._3)

    //println(u.getX()+","+u.getY()+" al " + v.getX() + "," + v.getY() + " - Distancia: " + distancia + " Angulo Navegación=" + anguloNavegacion + " Angulo Viento=" + vientos(0)._2 + " AnguloNormalizado=" + anguloNormalizado)
    // Evaluo si hay que maniobrar
    if (u.getManiobra().equals(MANIOBRAS.CenidaBabor) && anguloNormalizado>90) return Float.MaxValue/2
    if (u.getManiobra().equals(MANIOBRAS.PopaBabor) && (anguloNormalizado>180 || anguloNormalizado<90)) return Float.MaxValue/2
    if (u.getManiobra().equals(MANIOBRAS.PopaEstribor) && (anguloNormalizado>270 || anguloNormalizado<180)) return Float.MaxValue/2
    if (u.getManiobra().equals(MANIOBRAS.CenidaEstribor) && anguloNormalizado<270) return Float.MaxValue/2
    
    if (velocidadMaxima.abs<=0.0001) return Float.MaxValue/2
    
    //println(u.getX()+","+u.getY()+" al " + v.getX() + "," + v.getY() + " - Costo no máximo: " + (distancia.floatValue()/(velocidadMaxima*CONSTANTS.METROS_POR_MILLA_NAUTICA/3600d).floatValue()  )  + " - Distancia: " + distancia + " - Velocidad " + velocidadMaxima*CONSTANTS.METROS_POR_MILLA_NAUTICA/3600d + "m/s" + " Angulo Navegación=" + anguloNavegacion + " Angulo Viento=" + vientos(0)._2 + " AnguloNormalizado=" + anguloNormalizado)
    
    return distancia.floatValue()/(velocidadMaxima*CONSTANTS.METROS_POR_MILLA_NAUTICA/3600d).floatValue()    
  }
}