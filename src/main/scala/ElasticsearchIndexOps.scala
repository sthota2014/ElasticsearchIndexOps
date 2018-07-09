object ElasticsearchIndexOps {

  def get_idsForFileInIndex(index:String, fileName:String) {

   case class Shards(total:Int,successful:Int,failed:Int)
   case class source(EVENT_ID:String)
   case class Hits( _index:String, _type:String,_id:String,_score:Long, _source:source)
   case class HitsOuter(total:Int,max_score:Long,hits:Array[Hits])
   case class Response ( took:Int,timed_out:Boolean, _shards:Shards, hits:HitsOuter)
   import net.liftweb.json._
   import net.liftweb.json.JsonDSL._
   import scala.sys.process._
   implicit val formats = DefaultFormats

   //val esSite="https://256.256.256.256:9200/"
   val esSite="http://localhost:9200/"
   val credentials = "-uelastic:changeme"
   import scala.sys.process._
   //val idx ="firewall999"
   //val idx ="firewall_live"

   //val idx ="webproxy_web"
   //val file_name="Access_1708010120-2610..106.log.gz"
   val idx =index
   val file_name=fileName

   val input_str="{\n   " + "\"size\":10000,\n   " + "\"_source\":[\"\"],\n   " + "\"query\":{\n    " + "\"match\":{\n      " + "\"source\":\""+file_name  +"\"\n    " + "}\n   " + "}\n" + "}"
   val cmd1 =Seq("curl", "-H", "Content-Type: application/x-ndjson", "--data-binary", input_str, credentials, "-k" , "-X","GET",  esSite + idx +"/_search?scroll=2m")

   //val cmd1 =Seq("curl", "-H", "Content-Type: application/x-ndjson", "--data-binary", "@/tmp/forids", credentials, "-k" , "-X","GET",  esSite + idx +"/_search?scroll=2m")
   var r = cmd1.!!
   val jsonString =r
   val json = parse(jsonString)
   val scroll_id = (json \\"_scroll_id").children(0).toString.stripPrefix("JString(").stripSuffix(")")

   val hits = (json \\ "hits").children(0)
   val total = (hits \\ "total").children(0).values.toString.toInt

   var elements = (json \\ "hits").children
   var reals = (elements \\ "hits").children
   //var hits = ( reals \\ "_id").children.size
   val ids = (reals \\"_id").children
   val obj_ids = ids.map(x => x.toString.stripPrefix("JString(").stripSuffix(")"))

   val scroll_term = "{"+"\n \"scroll\":\"5m\","+ "\n \"scroll_id\":\"" + scroll_id+ "\"" +"\n}"
   import java.io._
   val writer = new PrintWriter(new File("/tmp/temp2"))
   obj_ids.map(x => writer.println(x))

   var scroll_id2 = scroll_id

  // Elasticsearch allows up to 10000

   for(i <- 1 to (total/10000 + 1)) {
      var scroll_term2:String = "{"+"\n \"scroll\":\"5m\","+ "\n \"scroll_id\":\"" + scroll_id2+ "\"" +"\n}"
      var cmd2 =Seq("curl", "-H", "Content-Type: application/x-ndjson", "--data-binary", scroll_term2, credentials, "-k" , "-X","GET",  esSite + "_search/scroll")
      var r2 = cmd2.!!
      var jsonString2 =r2
      var json2 = parse(jsonString2)
      var elements2 = (json2 \\ "hits").children
      var reals2 = (elements2 \\ "hits").children
      var hits2 = ( reals2 \\ "_id").children.size
      val ids2 = (reals2 \\"_id").children
      val obj_ids2 = ids2.map(x => x.toString.stripPrefix("JString(").stripSuffix(")"))
      //sc.parallelize(obj_ids2).saveAsTextFile ("file:///tmp/obj_ids_"+i+1)
      obj_ids2.map(x => writer.println(x))

      println("Hits are :"+ hits2)
      scroll_id2 = (json2 \\"_scroll_id").children(0).toString.stripPrefix("JString(").stripSuffix(")")
   }
   writer.close

  }

   def main(args:Array[String]):Unit = {
    args(0) match {
      case "get_idsForFileInIndex" => get_idsForFileInIndex(args(1), args(2))
    }
   }
}
