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

        var dataByte: Int = 0x0
        var fileByte: Int = 0x0
        var resultByte: Int = 0x0

        while (dataIterator.hasNext) {
            resultByte = 0x0
            dataByte = dataIterator.next
            Array((0x0f, 0x04), (0xf0, 0x00)).foreach(e => {
                val (m, i) = e
                if (fileIterator.hasNext) fileByte = fileIterator.next
                else throw new Exception("!")

                resultByte = resultByte | (fileByte & 0xf0 + dataByte & m >> i)
            })

            outputSource.write(resultByte.toByte)
        }

        outputSource.flush
        outputSource.close

        outputFile.getName
    }

    def decrypt(fileName: String, secretKey: String = "abcde"): String = {
        ""
    }

}

// vim: set ts=4 sw=4 et:
