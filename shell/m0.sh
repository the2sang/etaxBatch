#!/usr/bin/sh
export work_yyyymm=$1 
export log_date="_`date '+%Y%m%d'`"

echo "START" 
#JAVA_HOME=/opt/java/openjdk/bin
#JAVA_OPTS=-Dfile.encoding=UTF-8 -Dfile.client.encoding=UTF-8 -Dclient.encoding.override=UTF-8

LIBS_DIR=/data5/ebxml/kepcobill2/kepcobill2/WEB-INF/lib
CLASSPATH=${CLASSPATH}:${LIBS_DIR}/sggpki.jar:${LIBS_DIR}/sgkm.jar:${LIBS_DIR}/sgsecukit.jar:${LIBS_DIR}/signgate_common.jar:${LIBS_DIR}/signgateCrypto.jar:${LIBS_DIR}/ldapjdk.jar:${LIBS_DIR}/secu.jar:${LIBS_DIR}/xmlsecurity.jar:${LIBS_DIR}/libgpkiapi_jni.jar:${LIBS_DIR}/xalan.jar:${LIBS_DIR}/log4j-1.2.8.jar:${LIBS_DIR}/ojdbc8-19.3.0.0.jar:${LIBS_DIR}/orai18n.jar:${LIBS_DIR}/soap.jar:${LIBS_DIR}/xmlParserAPIs.jar:${LIBS_DIR}/xercesImpl.jar:/data5/ebxml/kepcobill2/kepcobill2/WEB-INF/batch/batchNew.jar


echo "Etax3.0_Offline_Demon_Service is dead and Service is starting";

#${JAVA_HOME}/bin/java -Dmachine=0 -Xms256m -Xmx2048m -Dfile.encoding=UTF-8 -Dfile.client.encoding=UTF-8 -Dclient.encoding.override=UTF-8 -classpath .:${CLASSPATH} kr.co.kepco.etax30.selling.batchoffline.BatchOffline ${work_yyyymm} >> /data5/ebxml/kepcobill2/kepcobill2/WEB-INF/batch/log/m0$log_date.log &

java -Dmachine=0 -Xms256m -Xmx2048m -Dfile.encoding=UTF-8 -Dfile.client.encoding=UTF-8 -Dclient.encoding.override=UTF-8 -classpath .:${CLASSPATH} kr.co.kepco.etax30.selling.batchoffline.BatchOffline ${work_yyyymm} >> /data5/ebxml/kepcobill2/kepcobill2/WEB-INF/batch/log/m0$log_date.log &

