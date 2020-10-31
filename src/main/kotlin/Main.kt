import com.google.gson.JsonParser
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.system.exitProcess

const val API = "https://api.bilibili.com/"

object Main {
    private val okHttpClient = OkHttpClient()
    private var SESSDATA = ""
    private var csrf = ""
    private val list = mapOf<String, Long>(
        //hololive 你好强大
        "hololive" to 286700005,
        //0期生
        "时乃空" to 286179206,
        "萝卜子" to 20813493,
        "樱巫女" to 366690056,
        "星街彗星" to 9034870,
        //1期生
        "白上吹雪" to 332704117,
        "夏色祭" to 336731767,
        "夜空梅露" to 389856447,
        "赤井心" to 339567211,
        "亚绮-罗森" to 389857131,
        //2期生
        //洋葱 我的洋葱
        //如果还是要取关请自行解除吧...
//        "湊-阿库娅" to 375504219,
        "紫咲诗音" to 389857640,
        "百鬼绫目" to 389858027,
        "癒月巧可" to 389858754,
        "大空昴" to 389859190,
        //Games
        "大神澪" to 389862071,
        "猫又小粥" to 412135222,
        "戌神沁音" to 412135619,
        //3期生
        "兔田佩克拉" to 443305053,
        "润羽露西娅" to 443300418,
        "不知火芙蕾雅" to 454737600,
        "白银诺艾尔" to 454733056,
        "宝钟玛琳" to 454955503,
        //4期生
        "天音彼方" to 491474048,
        "蛆" to 491474049,
        "角卷绵芽" to 491474050,
        "常暗永远" to 491474051,
        "姬森璐娜" to 491474052,
        //5期生
        "狮白牡丹" to 624252710,
        "雪花菈米" to 624252706,
        "尾丸波尔卡" to 624252712,
        "桃铃音音" to 624252709,
        //INNK Music
        "AZKi" to 389056211,
        //holoen
        "森美声" to 674600645,
        "小鸟游琪亚拉" to 674600646,
        "一伊那尔栖" to 674600647,
        "噶呜·古拉" to 674600648,
        "阿米莉亚・华生" to 674600649,
        //已毕业
        "魔乃阿萝耶" to 624252711
    )

    private fun nextLine(): String {
        return readLine().toString()
    }

    fun init() {
        println("欢迎使用一键取关脚本")
        println("本脚本需要先获取SESSDATA和bili_jct才能执行取关")
        println("如果你不知道怎么获取请看readme.md")
        //获取SESSDATA
        do {
            println("请粘贴您的SESSDATA:")
            SESSDATA = nextLine()
        } while (SESSDATA.length != 32.also { if (SESSDATA.length != 32) println("SESSDATA不正确!请检查后重新输入") })

        //获取账号信息
        val getUserRequest = Request.Builder()
            .url("${API}x/space/myinfo")
            .addHeader("Cookie", "SESSDATA=$SESSDATA")
            .build()
        try {
            val getUserResponse = okHttpClient.newCall(getUserRequest).execute()
            if (getUserResponse.isSuccessful) {
                val jsonObject = JsonParser.parseString(getUserResponse.body?.string()).asJsonObject
                println("您的账号昵称为:${jsonObject.getAsJsonObject("data").get("name")}")
            } else {
                println("请求出错!")
                println("代码:${getUserResponse.code}")
            }
        } catch (e: Exception) {
            println("发生错误!请检查SESSDATA是否正确!!")
            e.printStackTrace()
            exitProcess(-1)
        }

        //获取csrf
        do {
            println("请粘贴您的bili_jct:")
            csrf = nextLine()
        } while (csrf.length != 32.also { if (csrf.length != 32) println("bili_jct不正确!请检查后重新输入") })

        //取关
        println("正在执行取关操作...")
        thread {
            for (m in list) {
                println("正在取关\"${m.key}\"")
                val unsubscribeRequestBody = FormBody.Builder()
                    .add("fid", "${m.value}")
                    .add("act", "2")
                    .add("re_src", "11")
                    .add("csrf", csrf)
                    .build()
                val unsubscribeRequest = Request.Builder()
                    .url("${API}x/relation/modify")
                    .post(unsubscribeRequestBody)
                    .addHeader("Cookie", "SESSDATA=$SESSDATA")
                    .build()
                try {
                    val unsubscribeResponse = okHttpClient.newCall(unsubscribeRequest).execute()
                    if (unsubscribeResponse.isSuccessful) {
                        println("取关\"${m.key}\"成功！")
                    } else {
                        println("请求出错!")
                        println("代码:${unsubscribeResponse.code}")
                    }
                    Thread.sleep(200)
                } catch (e: Exception) {
                    println("发生错误!请检查bili_jct是否正确!!")
                    e.printStackTrace()
                    exitProcess(-1)
                }
            }
            println("取关操作已完成")
            println("很喜欢蛆的一句话:FUCK U AND NEVER COME BACK")
            println("祝HOLOLIVE早日倒闭")
        }
    }
}

fun main() {
    Main.init()
}