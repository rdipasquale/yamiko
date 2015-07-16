echo "Inicializando -> $(date)" > /media/ricardo/hd/logs/orquestador.log

/home/ricardo/cluster/stat.sh

for i in {1..2}
do
   echo "Corrida Cordeau101 Paralelo Cluster Nro. $i -> $(date)" >> /media/ricardo/hd/logs/orquestador.log
   cd /home/ricardo/cluster/spark-1.3.1-bin-hadoop2.6
   ./runCVRPTWCordeau101Parallel.sh 
   echo "Fin Corrida Cordeau101 Paralelo Cluster Nro. $i -> $(date)" >> /media/ricardo/hd/logs/orquestador.log
done


for i in {1..2}
do
   echo "Corrida Cordeau101 Serial Nro. $i -> $(date)" >> /media/ricardo/hd/logs/orquestador.log
   cd /home/ricardo/cluster/spark-1.3.1-bin-hadoop2.6
   ./runCVRPTWCordeau101.sh
   echo "Fin Corrida Cordeau101 Serial Nro. $i -> $(date)" >> /media/ricardo/hd/logs/orquestador.log
done

for i in {1..2}
do
   echo "Corrida Cordeau101 Paralelo local[8] Nro. $i -> $(date)" >> /media/ricardo/hd/logs/orquestador.log
   cd /home/ricardo/cluster/spark-1.3.1-bin-hadoop2.6
   ./runCVRPTWCordeau101Local8.sh
   echo "Fin Corrida Cordeau101 Paralelo local[8] Nro. $i -> $(date)" >> /media/ricardo/hd/logs/orquestador.log
done



echo "Fin -> $(date)" >> /media/ricardo/hd/logs/orquestador.log

