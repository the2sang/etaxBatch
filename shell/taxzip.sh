#!/usr/bin/sh

work_yyyymm=$1
grp_no=$2
file_name=$3
cmd=$4
#FILE_PATH='/data5/ebxml/kepcobill2/kepcobill2/xmldownload/xml/'$work_yyyymm
FILE_PATH='/data7/kepcobill2/xml/'$work_yyyymm$grp_no
#FILE_NAME='/data5/ebxml/kepcobill2/kepcobill2/xmldownload/xml/'$file_name
FILE_NAME='/data7/kepcobill2/xml/'$file_name
echo $FILE_PATH
echo $cmd
cd $FILE_PATH
tar cf $FILE_NAME.tar .
gzip -5 $FILE_NAME.tar
#2011.3.15 변경 벡업폴더용량때문 KDY
#mv $FILE_NAME.tar.gz /data7/kepcobill2/xml/gz/$file_name.tar.gz
cp $FILE_NAME.tar.gz /data7/kepcobill2/xml/gz/$file_name.tar.gz
