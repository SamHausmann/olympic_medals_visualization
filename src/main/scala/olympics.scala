package olympics

import java.io.InputStream
import scala.io.Source

object Olympics {

	case class Medal(Year: String, Country: String, Medal: String) {
	}

	def getDataStream: List[String] = {
		val stream : InputStream = getClass.getResourceAsStream("/summer/summer.csv")
		val lines = scala.io.Source.fromInputStream(stream).getLines.toList
    	lines
  	}

  	// inital data projection into the form (Year, Country, Medal)
	def parse(line: String): Medal = {
    	val l: List[String] = line.split(',').toList

    	if (l.length == 10) {
			val m: Medal = Medal(l(0), l(6), l(9))
			m
    	} else if (l.length == 9){
    		val m: Medal = Medal(l(0), l(6), l(8))
    		m
    	} else {
    		// maybe change this later
    		Medal("x", "x", "x")
    	}
  	}


	val medalList: List[Medal] = getDataStream.map(x => parse(x))

	def main(args: Array[String]) {
		
		// group country and medal pairings by year
		val yearCountryMedalMap: Map[String, Map[String, List[String]]] = medalList
		.filter(_.Country != "x")
		.map(x => x.Year -> (x.Country, x.Medal))
		.groupBy(_._1)
		.map(x => (x._1, x._2.map(y => y._2)))

		// eliminate redundant country labels and then group medals by country, per year
		.map{case (k, v) => (k, v.groupBy(_._1))}
		.map{case (k, v) => (k, v.map(x => x._1 -> x._2.map(y => y._2)))}

		// get all the medal counts
		yearCountryMedalMap
		.map{case (k, v) => (k, v.map(x => x._1 -> x._2.groupBy(identity).mapValues(_.size)))}
		.foreach(x => println(x))
	}

}


