#cd /home/devops/spark-2.4.5-bin-hadoop2.7
#docker build -t yaf-gbm:12 .
#docker tag yaf-gbm:12 devinfra1.osdev.cammesa.com:5000/yaf-gbm:12
#docker push devinfra1.osdev.cammesa.com:5000/yaf-gbm:12

#
#bin/spark-submit --master k8s://https://192.168.230.184:6443 --deploy-mode cluster --name gbm-par-tuning --class ar.edu.uca.gbm.ParameterTuningGBM \ 
#--conf spark.executor.instances=1 --conf spark.kubernetes.driver.volumes.hostPath.gbmpvc.mount.path=/datos/kubernetes/gbm \ 
#--conf spark.kubernetes.driver.volumes.hostPath.gbmpvc.mount.readOnly=false --conf spark.kubernetes.driver.volumes.hostPath.gbmpvc.options.path=/datos/kubernetes/gbm \
#--conf spark.kubernetes.executor.volumes.hostPath.gbmpvc.mount.path=/datos/kubernetes/gbm --conf spark.kubernetes.executor.volumes.hostPath.gbmpvc.mount.readOnly=false \
#--conf spark.kubernetes.executor.volumes.hostPath.gbmpvc.options.path=/datos/kubernetes/gbm --conf spark.kubernetes.driver.label.run=gbmpartuningpod \
#--conf spark.kubernetes.driver.pod.name=gbmpartuningpod --conf spark.kubernetes.namespace=desa \
#--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark --conf spark.kubernetes.allocation.batch.size=8 \ 
#--conf spark.driver.cores=1 --conf spark.executor.cores=1 --conf spark.kubernetes.container.image=devinfra1.osdev.cammesa.com:5000/yaf-gbm:16 \
#local:///datos/kubernetes/gbm/yaf-gbm-par-tuning-1.0.0.jar 

#bin/spark-submit --master k8s://https://192.168.230.184:6443 --deploy-mode cluster --name gbm-par-tuning --class ar.edu.uca.gbm.ParameterTuningGBM --conf spark.executor.instances=7 --conf spark.kubernetes.driver.volumes.hostPath.gbmpvc.mount.path=/datos/kubernetes/gbm --conf spark.kubernetes.driver.volumes.hostPath.gbmpvc.mount.readOnly=false --conf spark.kubernetes.driver.volumes.hostPath.gbmpvc.options.path=/datos/kubernetes/gbm --conf spark.kubernetes.executor.volumes.hostPath.gbmpvc.mount.path=/datos/kubernetes/gbm --conf spark.kubernetes.executor.volumes.hostPath.gbmpvc.mount.readOnly=false --conf spark.kubernetes.executor.volumes.hostPath.gbmpvc.options.path=/datos/kubernetes/gbm --conf spark.kubernetes.driver.label.run=gbmpartuningpod --conf spark.kubernetes.driver.pod.name=gbmpartuningpod --conf spark.kubernetes.namespace=desa --conf spark.kubernetes.authenticate.driver.serviceAccountName=spark --conf spark.kubernetes.allocation.batch.size=8 --conf spark.driver.cores=1 --conf spark.executor.cores=1 --conf spark.kubernetes.container.image=devinfra1.osdev.cammesa.com:5000/yaf-gbm:16 local:///datos/kubernetes/gbm/yaf-gbm-par-tuning-1.0.0.jar 
#con Always
#bin/spark-submit --master k8s://https://192.168.230.184:6443 --deploy-mode cluster --name gbm-par-tuning --class ar.edu.uca.gbm.ParameterTuningGBM --conf spark.executor.instances=2 --conf spark.kubernetes.driver.volumes.hostPath.gbmpvc.mount.path=/datos/kubernetes/gbm --conf spark.kubernetes.driver.volumes.hostPath.gbmpvc.mount.readOnly=false --conf spark.kubernetes.driver.volumes.hostPath.gbmpvc.options.path=/datos/kubernetes/gbm --conf spark.kubernetes.executor.volumes.hostPath.gbmpvc.mount.path=/datos/kubernetes/gbm --conf spark.kubernetes.executor.volumes.hostPath.gbmpvc.mount.readOnly=false --conf spark.kubernetes.executor.volumes.hostPath.gbmpvc.options.path=/datos/kubernetes/gbm --conf spark.kubernetes.container.image.pullPolicy=Always --conf spark.kubernetes.driver.label.run=gbmpartuningpod --conf spark.kubernetes.driver.pod.name=gbmpartuningpod --conf spark.kubernetes.namespace=desa --conf spark.kubernetes.authenticate.driver.serviceAccountName=spark --conf spark.kubernetes.allocation.batch.size=8 --conf spark.driver.cores=1 --conf spark.executor.cores=1 --conf spark.kubernetes.container.image=devinfra1.osdev.cammesa.com:5000/yaf-gbm:12 local:///datos/kubernetes/gbm/yaf-gbm-par-tuning-1.0.0.jar 1 1

#bin/spark-submit --master k8s://https://192.168.230.184:6443 --deploy-mode cluster --name gbm-par-tuning \
#--class ar.edu.uca.gbm.ParameterTuningGBM --conf spark.executor.instances=7 \
#--conf spark.kubernetes.driver.label.run=gbmpartuningpod \
#--conf spark.kubernetes.driver.pod.name=gbmpartuningpod \
#--conf spark.kubernetes.namespace=desa \
#--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
#--conf spark.kubernetes.allocation.batch.size=8 \
#--conf spark.driver.cores=1 \
#--conf spark.executor.cores=1 \
#--conf spark.kubernetes.container.image=devinfra1.osdev.cammesa.com:5000/yaf-gbm:13 \
#local:////opt/spark/work-dir/yaf-gbm-par-tuning-1.0.0.jar 200

#bin/spark-submit --master k8s://https://192.168.230.184:6443 --deploy-mode cluster --name gbm-par-tuning \
#--class ar.edu.uca.gbm.ParameterTuningGBM --conf spark.executor.instances=2 \
#--conf spark.kubernetes.container.image.pullPolicy=Always \
#--conf spark.kubernetes.driver.label.run=gbmpartuningpod \
#--conf spark.kubernetes.driver.pod.name=gbmpartuningpod \
#--conf spark.kubernetes.namespace=desa \
#--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
#--conf spark.kubernetes.allocation.batch.size=8 \
#--conf spark.driver.cores=1 \
#--conf spark.executor.cores=1 \
#--conf spark.kubernetes.container.image=devinfra1.osdev.cammesa.com:5000/yaf-gbm:latest \
#local:///opt/spark/work-dir/yaf-gbm-par-tuning-1.0.0-shaded.jar 200
FROM openjdk:8-jdk-slim

MAINTAINER softwarefactory@cammesa.com.ar


ARG spark_jars=jars
ARG img_path=kubernetes/dockerfiles
ARG k8s_tests=kubernetes/tests

# Before building the docker image, first build and make a Spark distribution following
# the instructions in http://spark.apache.org/docs/latest/building-spark.html.
# If this docker file is being used in the context of building your images from a Spark
# distribution, the docker build command should be invoked from the top level directory
# of the Spark distribution. E.g.:
# docker build -t spark:latest -f kubernetes/dockerfiles/spark/Dockerfile .
ENV http_proxy 'http://192.1.1.215:8080'
ENV https_proxy 'http://192.1.1.215:8080'

RUN set -ex && \
    apt-get update && \
    ln -s /lib /lib64 && \
    apt install -y bash tini libc6 libpam-modules libnss3 && \
    mkdir -p /opt/spark && \
    mkdir -p /opt/spark/work-dir && \
    touch /opt/spark/RELEASE && \
    rm /bin/sh && \
    ln -sv /bin/bash /bin/sh && \
    echo "auth required pam_wheel.so use_uid" >> /etc/pam.d/su && \
    chgrp root /etc/passwd && chmod ug+rw /etc/passwd && \
    rm -rf /var/cache/apt/*

COPY ${spark_jars} /opt/spark/jars
COPY bin /opt/spark/bin
COPY sbin /opt/spark/sbin
COPY ${img_path}/spark/entrypoint.sh /opt/
#COPY examples /opt/spark/examples
COPY ${k8s_tests} /opt/spark/tests
COPY data /opt/spark/data

ENV SPARK_HOME /opt/spark

WORKDIR /opt/spark/work-dir

RUN apt-get install -y python3-pip
#RUN apt-get install -y python3
#RUN apt-get install -y curl
#RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
#RUN apt-get remove curl -y
RUN apt-get autoremove -y
RUN python3 --version
#RUN python get-pip.py

RUN pip3 install pandas
RUN pip3 install matplotlib
RUN pip3 install sklearn
RUN pip3 install lightgbm

ENV http_proxy ''
ENV https_proxy ''

#COPY yaf-gbm-par-tuning-1.0.0.jar /opt/spark/work-dir

ENTRYPOINT [ "/opt/entrypoint.sh" ]
