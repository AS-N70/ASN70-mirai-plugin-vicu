package org.ASN70.vicu.text2pic

import org.slf4j.LoggerFactory
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import javax.imageio.ImageIO


class IGraphics2D {
    private var width: Int
    private var hight: Int
    private val font: Font? = null
    var bufferedImage: BufferedImage
    var graphics2D: Graphics2D
    var y = 0

    /**
     * 带背景图片
     */
    constructor(bufferedImage: BufferedImage) {
        width = bufferedImage.width
        hight = bufferedImage.height
        this.bufferedImage = bufferedImage
        graphics2D = bufferedImage.createGraphics()
        init(false)
    }

    /**
     * 背景为白色
     */
    constructor(width: Int, height: Int) : super() {
        this.width = width
        this.hight = height
        bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        graphics2D = bufferedImage.createGraphics()
        init(true)
    }

    /**
     * 初始化画笔颜色、大小和字体
     */
    private fun init(bool: Boolean) {
        if (bool) {
            graphics2D.background = Color.WHITE // 设置背景为白色
            graphics2D.clearRect(0, 0, width, hight) // 擦除默认背景(默认背景擦除后会显示自己设置的背景)
        }
        graphics2D.paint = Color.BLACK // 设置字体为黑色
        graphics2D.font = font // 设置 字体 斜率 字体大小
        graphics2D.stroke = BasicStroke(1f) // 画笔粗细
        graphics2D.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        ) // 消除文字锯齿
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) // 消除图片锯齿
    }

    @Throws(FileNotFoundException::class, IOException::class)
    fun drawImage(x: Int, y: Int, file: File?): IGraphics2D {
        val buff = ImageIO.read(FileInputStream(file))
        graphics2D.drawImage(buff, x, y, buff.width, buff.height, null) // 写入图片
        logger.info("图片宽: " + buff.width + "px, 图片高: " + buff.height + ", x: " + x + ", y: " + y)
        return this
    }
    @Throws(FileNotFoundException::class, IOException::class)
    fun drawImage(x: Int, y: Int, buff: BufferedImage): IGraphics2D {
        graphics2D.drawImage(buff, x, y, buff.width, buff.height, null) // 写入图片
        logger.info("图片宽: " + buff.width + "px, 图片高: " + buff.height + ", x: " + x + ", y: " + y)
        return this
    }


    fun drawWord(content: String, x: Int, y: Int, color: Color?): IGraphics2D {
        return drawWord(content, 0, x, y, font, 5, color)
    }

    /**
     * content 需要绘制的文字
     * interval 文字两端空余像素
     * oneLineWidth 绘制文字一行像素值
     * x 绘制起始点x
     * y 绘制起始点y
     * font 绘制使用字体(文字大小 文字加粗)
     * line 行间距
     * color 文字颜色
     */
    fun drawWord(
        content: String,
        interval: Int,
        oneLineWidth: Int,
        x: Int,
        y: Int,
        font: Font?,
        line: Int,
        color: Color?
    ): IGraphics2D {
        var startX = x
        graphics2D.paint = color
        graphics2D.font = font
        startX += interval
        val fontSize = font!!.size + line
        var result = 0
        if (isMoreThanOneLine(content, oneLineWidth)) { // 文字长度没有超出一行的长度
            result = y + fontSize
            graphics2D.drawString(content, startX, y + fontSize) // 文字画入画布中
        } else { // 文字超出一行处理
            val oneLineCharNumber = getOneLineCharNumber(content, oneLineWidth)
            for (i in oneLineCharNumber.indices) {
                var string = ""
                if (i == 0) {
                    result = y + fontSize
                    string = content.substring(0, oneLineCharNumber[i])
                } else {
                    result = y + fontSize + fontSize * i
                    string = content.substring(oneLineCharNumber[i - 1], oneLineCharNumber[i])
                }
                graphics2D.drawString(string, startX, result) // 文字画入画布中
                logger.info("画的文字为: {}", string)
            }
        }
        return this
    }

    fun drawWord(content: String, interval: Int, x: Int, y: Int, font: Font?, line: Int, color: Color?): IGraphics2D {
        return this.drawWord(content, interval, width - x - interval * 2, x, y, font, line, color)
    }

    /**
     * 获取文字需要画几行
     * @param cont
     * @return 行数
     */
    fun getMessageRowsNumber(message: String, oneLineWidth: Int, cont: Font?): Int {
        graphics2D.font = cont
        val charArray = message.toCharArray()
        val messagePX = getMessagePX(message, 0, message.length) // 获取文字所需总像素值
        var count = 0
        if (messagePX % oneLineWidth != 0) {
            count = 1
        }
        return messagePX / oneLineWidth + count // 总像素值/每行像素值
    }

    @Throws(IOException::class)
    fun flush(file: File?) {
        graphics2D.dispose()
        ImageIO.write(bufferedImage, "png", file)
    }

    /**
     * 写入字符 字符长度是否超过一行
     * @param graphics2D
     * @param message
     */
    private fun isMoreThanOneLine(message: String, oneLineWidth: Int): Boolean {
        return isMoreThanOneLine(message, 0, message.length, oneLineWidth)
    }

    /**
     * @param graphics2D 画布
     * @param message 画入的字符串
     * @param off 字符串的偏移量
     * @param charArrayLength 字符串的长度
     */
    private fun isMoreThanOneLine(message: String, off: Int, charArrayLength: Int, oneLineWidth: Int): Boolean {
        val charsWidth = getMessagePX(message, off, charArrayLength)
        logger.info("文字像素值长度:$charsWidth")
        return charsWidth <= oneLineWidth // 如果文字长度大于每行长度 返回false
    }

    /**
     * 获取文字像素长度
     * @param graphics2D 画布
     * @param message 画入的字符串
     * @param off 字符串的偏移量
     * @param charArrayLength 从偏移量开始 取多少个字符
     * @return 文字像素长度
     */
    private fun getMessagePX(message: String, off: Int, charArrayLength: Int): Int {
        return graphics2D.fontMetrics.charsWidth(message.toCharArray(), off, charArrayLength)
    }

    /**
     * 获取一行的文字数
     * @return 文字数
     */
    private fun getOneLineCharNumber(message: String, oneLineWidth: Int): List<Int> {
        val list: MutableList<Int> = ArrayList()
        var start = 0
        val charArray = message.toCharArray()
        for (i in 0 until charArray.size - 1) {
            val rowsLength = getMessagePX(message, start, i - start)
            if (rowsLength > oneLineWidth) {
                list.add(i)
                start = i
            } else if (rowsLength == oneLineWidth) {
                list.add(i + 1)
                start = i + 1
            }
        }
        list.add(charArray.size)
        return list
    }

    companion object {
        private val logger = LoggerFactory.getLogger(IGraphics2D::class.java)
    }
}






