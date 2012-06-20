package predicatemap


object PredicateBuilder {
  val itemTypes: List[String] = "alpha" :: "bravo" :: "charlie" :: "delta" :: "echo" ::
    "foxtrot" :: "golf" :: "hotel" :: "india" :: "juliet" :: "kilo" :: "lima" ::
    "mike" :: "november" :: "oscar" :: "papa" :: "quebec" :: "romeo" :: "sierra" ::
    "tango" :: "uniform" :: "victor" :: "whiskey" :: "x-ray" :: "yankee" ::
    "zulu" :: Nil

  def listPredicates: List[Tuple2[String, String]] = {
    itemTypes map {
      case x: String => {

        val predicateText =
          "(ns records-mgmt)(defn " + x + "? [item](= \"" +
            x + "\"(item :itemtype)))"
        (x + "?", predicateText)
      }
    }
  }
}
