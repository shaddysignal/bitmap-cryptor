package bcrypt

import scala.io._
import java.io.File
import java.io.PrintWriter
import java.lang.Exception

object BitmapCrypt {

    def crypt(dataFile: String, secretKey: String = "abcde"): String = {
        val inputFile =
            new File("/home/ashubin/projects/scala/bitmap-cryptor/tmp/input.bmp")
        val outputFile =
            new File("/home/ashubin/projects/scala/bitmap-cryptor/tmp/output.bmp")
        outputFile.createNewFile

        val outputSource = new PrintWriter(outputFile)

        val dataIterator = Source.fromFile(dataFile).toArray.map(_.toByte).iterator
        val fileIterator = Source.fromFile(inputFile).toArray.map(_.toByte).iterator

        var dataByte: Byte = 0.toByte
        var fileByte: Byte = 0.toByte
        var resultByte: Byte = 0.toByte

        while (dataIterator.hasNext) {
            resultByte = 0.toByte
            dataByte = dataIterator.next
            Array((15,4), (240,0)).foreach(e => {
                val (m, i) = e
                if (fileIterator.hasNext) fileByte = fileIterator.next
                else throw new Exception("!")

                resultByte = (resultByte | (fileByte & 240 + dataByte & m >> i)).toByte
            })

            outputSource
        }

        outputFile.getName
    }

    def decrypt(fileName: String, secretKey: String = "abcde"): String = {
        ""
    }

}

// vim: set ts=4 sw=4 et:
