package olympics

import java.io.InputStream
import scala.io.Source
import java.io.File
import java.io.PrintWriter
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._

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
    	val m: Medal = Medal(l(0), l(6), l(l.length - 1))
    	m
  	}

	val medalList: List[Medal] = getDataStream.map(x => parse(x))

	def main(args: Array[String]) {

		val writer = new PrintWriter(new File("src/main/resources/summer/outputjson.json"))
	
		// group country and medal pairings by year
		val yearCountryMedalMap: Map[String, Map[String, List[String]]] = medalList
		.map(x => x.Year -> (x.Country, x.Medal))
		.groupBy(_._1)
		.map(x => (x._1, x._2.map(y => y._2)))

		// eliminate redundant country labels and then group medals by country, per year
		.map{case (k, v) => (k, v.groupBy(_._1))}
		.map{case (k, v) => (k, v.map(x => x._1 -> x._2.map(y => y._2)))}

		// get all the medal counts
		yearCountryMedalMap
		.map{case (k, v) => (k, v.map(x => x._1 -> x._2.groupBy(identity).mapValues(_.size)))}

		// convert to json and write to file to be used by frontend
		val outputStr: String = compact(render(yearCountryMedalMap))
		writer.write(outputStr)
    	writer.close()
	}

}


