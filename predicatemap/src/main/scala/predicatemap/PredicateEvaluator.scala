package predicatemap

import scala.collection.JavaConversions._
import scala.collection.mutable._
import java.util.concurrent.{ExecutorService, Callable}
import com.hazelcast.core.{IMap, MultiTask, Hazelcast}
import clojure.lang.Var



class PredicateLister(item: String) extends Callable[String] with Serializable
{
  def call: String =
  {
    val clusterName = Hazelcast.getCluster.getLocalMember.toString
    val kset = Hazelcast.getMap("predicate-map").localKeySet.asInstanceOf[java.util.Set[String]]
    kset.foldLeft("")((s, k) =>
    {
      s + (s match
      {
        case "" => s + clusterName + ": " + k
        case _ => ", " + k
      })
    })

  }
}

class Matcher(itemName: String, itemDef: String) extends Callable[String] with Serializable
{

  import PredicateCompiler.{compileItemAndReturnVar, getCompiledPredicate}

  def getMatchForKey(k: String, predicateMap: java.util.Map[String,String], itemToMatch: Var) : String = {
    val predicateDef = predicateMap.get(k)
    val predicateVar = getCompiledPredicate(k, predicateDef)
          predicateVar.invoke(itemToMatch) match
          {
            case java.lang.Boolean.TRUE => "[" + k + " matches]"
            case java.lang.Boolean.FALSE => ""
            case _ => "[your code has a sad]"
          }
  }

  def call: String =
  {
    val itemVar = compileItemAndReturnVar(itemName, itemDef)
    val predicateMap = Hazelcast.getMap("predicate-map").asInstanceOf[IMap[String,String]]
    val kset = predicateMap.localKeySet.asInstanceOf[java.util.Set[String]]
    kset.foldLeft("")((s, k) =>
    {
      s + getMatchForKey(k, predicateMap,itemVar)
    })
  }
}

object PredicateEvaluator
{
  def main(args: Array[String]): Unit =
  {
    val itemDef = "(ns records-mgmt)(def item1 {:itemtype \"sierra\" :country \"country\"})"

    val task = new MultiTask[String](new Matcher("item1", itemDef),
      Hazelcast.getCluster.getMembers)

    val executorService: ExecutorService = Hazelcast.getExecutorService

    executorService.execute(task)

    val results = task.get
    results.map(println)
  }
}