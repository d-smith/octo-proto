package predicatemap

import collection.mutable
import clojure.lang.{RT, Var}
import java.io.StringReader

/*
 *    Copyright (c) 2011 XTRAC LLC. All Rights Reserved.
 *
 *    This software and all information contained herein is the property of
 *    XTRAC LLC.  Any dissemination, disclosure, use, or reproduction of this
 *    material for any reason inconsistent with express purpose for which it
 *    has been disclosed is strictly forbidden.
 */

object PredicateCompiler
{
    val compiled = new mutable.HashMap[String,Var]

     def getCompiledPredicate(name: String, body: String) : Var = {
          compiled.get(name) match {
            case Some(p) => p
            case _ => compileAndCachePredicate(name, body)
          }
     }

    def compileAndCachePredicate(name: String, body: String) : Var = {
      println("compiling and caching " + name)
      clojure.lang.Compiler.load(new StringReader(body))
      val compiledPred =   RT.`var`("records-mgmt", name)
      compiled + (name -> compiledPred)
      compiledPred
    }

    def compileItemAndReturnVar(varname: String, vardef: String) : Var = {
      clojure.lang.Compiler.load(new StringReader(vardef))
      RT.`var`("records-mgmt", varname)
    }
}