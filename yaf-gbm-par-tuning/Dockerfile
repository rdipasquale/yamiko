FROM devinfra1.osdev.cammesa.com:5000/traininglightgbm:latest
MAINTAINER softwarefactory@cammesa.com.ar

ENV https_proxy ""
ENV http_proxy ""

COPY yaf-gbm-par-tuning-1.0.0-shaded.jar /usr/local/src/

WORKDIR /usr/local/src
ENTRYPOINT [ "/opt/entrypoint.sh" ]

bin/spark-submit --master k8s://https://192.168.230.184:6443 --deploy-mode cluster --name gbm-par-tuning \
--class ar.edu.uca.gbm.ParameterTuningGBM --conf spark.executor.instances=2 \
--conf spark.kubernetes.driver.label.run=gbmpartuningpod \
--conf spark.kubernetes.driver.pod.name=gbmpartuningpod \
--conf spark.kubernetes.namespace=desa \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
--conf spark.kubernetes.allocation.batch.size=8 \
--conf spark.driver.cores=1 \
--conf spark.executor.cores=1 \
--conf spark.kubernetes.container.image=devinfra1.osdev.cammesa.com:5000/yaf-gbm:latest \
local:///usr/local/src/yaf-gbm-par-tuning-1.0.0-shaded.jar 200
