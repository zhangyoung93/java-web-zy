import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Gatling压测工具类
 *
 * Maven项目
 * mvn gatling:test
 *
 * 指定具体Simulation
 * mvn gatling:test -Dgatling.simulationClass=GatlingTest
 *
 * @author zy
 */
class GatlingTest extends Simulation {

  val httpContentType: String = "application/json"

  val httpBaseUrl: String = "http://localhost:8082/zy"

  val sceneName: String = "batchDataApi"

  val httpReqName: String = "POST /batch/data/save/user"

  val httpReqUrl: String = "/batch/data/save/user"

  val bodyStr: String = """[{"mobile":"13333333333"}]"""

  //HTTP协议配置
  val httpProtocol: HttpProtocolBuilder = http.baseUrl(this.httpBaseUrl)
    .acceptHeader(this.httpContentType).contentTypeHeader(this.httpContentType)

  //定义压测场景
  val scene: ScenarioBuilder = scenario(this.sceneName).exec(http(this.httpReqName)
    .post(this.httpReqUrl).body(StringBody(this.bodyStr)).check(status.is(200))).pause(1 second)

  //设置虚拟用户注入策略
  setUp(
    scene.inject(
      //立即启动10个并发请求
      atOnceUsers(10),
      //30秒内线性增加到90个并发请求
      rampUsers(90) during (30 seconds),
      //每秒增加2个新请求，持续60秒
      constantUsersPerSec(2) during (60 seconds)
    ).protocols(httpProtocol)
  )
}