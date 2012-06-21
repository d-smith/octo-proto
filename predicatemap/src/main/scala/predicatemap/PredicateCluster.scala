package predicatemap



import scala.collection.JavaConversions._
import PredicateBuilder.{listPredicates}
import com.hazelcast.core.Hazelcast
import clojure.lang.Var

object PredicateCluster
{

  def main(args: Array[String]): Unit =
  {
    val predicates = listPredicates
    val clusterMap= Hazelcast.getMap("predicate-map").asInstanceOf[java.util.Map[String,String]]

    predicates.foreach(t => clusterMap.put(t._1,t._2))
  }
}