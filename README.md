# ElasticsearchIndexOps

#To do the test, we need to put an index with a field "source" in Elasticsearch

#Open Kibana and perform this operation

POST object_id_test/product/1 

{
 "name":"pencils",
 "price":0.50,
 "quantity":20,
 "source": "pencilFiles.txt"
}


#----------------------------------------------------------------------------------------------------

#Submit the job and this fetches all object Ids in the Index 
#and writes it to /tmp/temp2

#----------------------------------------------------------------------------------------------------

bin/spark-submit --class ElasticsearchIndexOps /tmp/something.jar "get_idsForFileInIndex" "object_id_test" "pencilFiles.txt"

  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   502  100   392  100   110  27107   7606 --:--:-- --:--:-- --:--:-- 28000
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   307  100   208  100    99  19853   9449 --:--:-- --:--:-- --:--:-- 20800
Hits are :0


#----------------------------------------------------------------------------------------------------

# Check file contents

#----------------------------------------------------------------------------------------------------
$ cat /tmp/temp2

1

"abcd"
