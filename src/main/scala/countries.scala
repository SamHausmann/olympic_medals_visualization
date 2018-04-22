package olympics

import java.io.InputStream
import scala.io.Source
import java.io.File
import java.io.PrintWriter
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._

object Countries {

	case class Country(Code: String, Name: String) {
	}

	def getDataStream: List[String] = {
		val stream : InputStream = getClass.getResourceAsStream("/summer/dictionary.csv")
		val lines = scala.io.Source.fromInputStream(stream).getLines.toList
    	lines
  	}

  	// inital data projection into the form (Code, Name)
	def parse(line: String): Country = {
    	val l: List[String] = line.split(',').toList
    	val c: Country = Country(l(1), l(0))
    	c
  	}

	val countryList: List[Country] = getDataStream.map(x => parse(x))

	def main(args: Array[String]) {

		val writer = new PrintWriter(new File("src/main/resources/summer/countrylist.json"))

		val countryCodeMap: Map[String, String] = countryList
		.map(x => x.Code -> x.Name)
		.toMap

		// convert to json and write to file to be used by frontend
		val outputStr: String = compact(render(countryCodeMap))
		writer.write(outputStr)
    	writer.close()
	}

}


