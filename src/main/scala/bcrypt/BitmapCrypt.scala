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

        val outputSource = new PrintWriter(outputFile, "ISO-8859-1")
        val dataSource = Source.fromFile(dataFile, "UTF8").toArray
        val fileSource = Source.fromFile(inputFile, "ISO-8859-1").toArray

        val dataIterator = dataSource.iterator
        val fileIterator = fileSource.iterator

        var dataByte: Int = 0x0
        var fileByte: Int = 0x0
        var resultByte: Int = 0x0

        // write header info without changes
        var offset = getHeaderSize(fileSource)
        for (i <- 0 until offset if fileIterator.hasNext) outputSource.write(fileIterator.next)

        // write data until it empty
        while (dataIterator.hasNext) {
            resultByte = 0x0
            dataByte = dataIterator.next
            Array((0x03, 0x00), (0x0c, 0x02), (0x30, 0x04), (0xc0, 0x06)).foreach(e => {
                val (m, i) = e
                if (fileIterator.hasNext) fileByte = fileIterator.next
                else throw new Exception("!")

                resultByte = (fileByte & 0xfc) + ((dataByte & m) >> i)
                outputSource.write(resultByte.toChar)
            })
        }

        // write the rest file unchanged
        while (fileIterator.hasNext) outputSource.write(fileIterator.next)

        outputSource.flush
        outputSource.close

        outputFile.getName
    }

    def decrypt(fileName: String, secretKey: String = "abcde"): String = {
        val inputFile =
            new File("/home/ashubin/projects/scala/bitmap-cryptor/tmp/output.bmp")
        val outputFile =
            new File("/home/ashubin/projects/scala/bitmap-cryptor/tmp/message.txt")

        val inputSource = Source.fromFile(inputFile, "ISO-8859-1").toArray
        val outputSource = new PrintWriter(outputFile, "UTF8")

        val inputIterator = inputSource.iterator

        val offset = getHeaderSize(inputSource)
        for (i <- 0 until offset if inputIterator.hasNext) None

        def iterate(ib: Int, db: Int, s: Int): Int = {
            if (inputIterator.hasNext && s < 0x08)
                iterate(inputIterator.next, db | ((ib & 0x03) << s), s + 0x02)
            else db
        }

        while (inputIterator.hasNext) {
            var dataByte = iterate(inputIterator.next, 0x0, 0x0)
            outputSource.write(dataByte.toChar)
        }

        outputSource.flush
        outputSource.close

        outputFile.getName
    }

    private def getHeaderSize(source: Array[Char]): Int =
        (10 until 14).map(i => { source(i) * math.pow(256, i - 10) }).reduceLeft(_ + _).toInt

}

// vim: set ts=4 sw=4 et:
