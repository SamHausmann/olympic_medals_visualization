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
		

		medalList
		.filter(_.Country != "x")
		.map(x => x.Year -> (x.Country, x.Medal))
		.groupBy(_._1)
		.map(x => x._2.map(y => y._2))
		.foreach(x => println(x))
	}

}


