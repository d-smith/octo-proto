package predicatemap

import collection.mutable
import clojure.lang.{RT, Var}
import java.io.StringReader



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