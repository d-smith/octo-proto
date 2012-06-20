package predicatemap

/*
 *    Copyright (c) 2011 XTRAC LLC. All Rights Reserved.
 *
 *    This software and all information contained herein is the property of
 *    XTRAC LLC.  Any dissemination, disclosure, use, or reproduction of this
 *    material for any reason inconsistent with express purpose for which it
 *    has been disclosed is strictly forbidden.
 */

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