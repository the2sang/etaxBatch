
pid=$(ps -ef |grep -v grep | grep "Donline=0" | awk {'print $2'})

if [[ $pid = "" ]]
then
    echo "Etax3.0_Online_Demon_Service is not alive --> starting...!";
  
LIBS_DIR=/data5/ebxml/kepcobill2/kepcobill2/WEB-INF/lib
CLASSPATH=${CLASSPATH}:${LIBS_DIR}/sggpki.jar:${LIBS_DIR}/sgkm.jar:${LIBS_DIR}/sgsecukit.jar:${LIBS_DIR}/signgate_common.jar:${LIBS_DIR}/signgateCrypto.jar:${LIBS_DIR}/ldapjdk.jar:${LIBS_DIR}/secu.jar:${LIBS_DIR}/xmlsecurity.jar:${LIBS_DIR}/libgpkiapi_jni.jar:${LIBS_DIR}/xalan.jar:${LIBS_DIR}/log4j-1.2.8.jar:${LIBS_DIR}/ojdbc14.jar:${LIBS_DIR}/soap.jar:${LIBS_DIR}/xmlParserAPIs.jar:${LIBS_DIR}/xercesImpl.jar:/data5/ebxml/kepcobill2/kepcobill2/WEB-INF/batch/batch08.jar

${JAVA_HOME}/bin/java -Donline=0 -Xms512m -Xmx1024m -classpath .:${CLASSPATH} kr.co.kepco.etax30.selling.batch.RunningScheduler  >> /data5/ebxml/kepcobill2/kepcobill2/WEB-INF/batch/log/batch.log &
#${JAVA_HOME}/bin/java -Donline=0 -Xms256m -Xmx512m -classpath .:${CLASSPATH} kr.co.kepco.etax30.selling.batch.RunningScheduler  >> /data5/ebxml/kepcobill2/kepcobill2/WEB-INF/batch/log/batch.log &
#${JAVA_HOME}/bin/java -Donline=0 -Xms256m -Xmx1024m -classpath .:${CLASSPATH} kr.co.kepco.etax30.selling.batch.RunningScheduler  >> /data5/ebxml/kepcobill2/kepcobill2/WEB-INF/batch/log/batch.log &
    
else
    echo "Etax3.0_Online_Demon_Service is already started!! --> skip!";
fi

