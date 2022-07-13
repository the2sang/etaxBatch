
pid=$(ps -ef |grep -v grep | grep "Donline=0" | awk {'print $2'})

if [[ $pid = "" ]]
then
    echo "Etax3.0_Online_Demon_Service is not alive!";
  
else
    kill -9 $pid
    echo "Etax3.0_Online_Demon_Service is killed!";
fi

