package cn.yiidii.openapi.unicommeituan.Test;

import cn.yiidii.openapi.base.Constant;
import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.common.util.ServerJNotify;
import cn.yiidii.openapi.common.util.dto.HttpClientResult;
import cn.yiidii.openapi.entity.uincommeituan.ChinaUnicomInfo;
import cn.yiidii.openapi.unicommeituan.dto.MeiTuanGoods;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class MeiTuan4Main {


    static {
        // 关闭log
        Set<String> loggers = new HashSet<>(Arrays.asList("org.apache.http"));
        for (String log : loggers) {
            Logger logger = (Logger) LoggerFactory.getLogger(log);
            logger.setLevel(Level.INFO);
            logger.setAdditive(false);
        }
    }

    public static void main1(String[] args) throws Exception {
        ChinaUnicomInfo info1 = new ChinaUnicomInfo();
        info1.setPhoneNum("18622080802");
        info1.setCookie("wmf=960a71aba033f66993be9932f4a94d52;MUT_S=android8.1.0;devicedId=865300046557938;logHostIP=null;c_sfbm=234g_00;welfareroute=58e6c8db5da65d38abb9b66b7d81d17a66a38972;ecs_acc=YTExIhPbhGS/Jz0YfYIL40rPcLXT8gLpArlDFzz+Bj5K+hhHGj342KAblLl24efoqjFeIJpxczQDeKeGKQG1QzFE95LkwXK+acMqxKzlDI1rgoHm7iQ3zUJV4JdStRDeAW8LtBc4GNsaDoLyKnizBjhHsRpxnM98CZR890Xy1RI=;req_mobile=18622080802;req_serial=;clientid=98|0;req_wheel=ssss;a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDM0MTQzNDIsInRva2VuIjp7ImxvZ2luVXNlciI6IjE4NjIyMDgwODAyIiwicmFuZG9tU3RyIjoieWhjOTI4NGExNjAyODA5NTQyIn0sImlhdCI6MTYwMjgwOTU0Mn0.Zg1-Q1QR9jxUBESoa2gTWB8zB-XmTq6pGV4Hq_dsQt6Yz6Q-Y8IensIBE7rVD01QMZQCNwz2RY9bZlRWdzr_nA;c_id=4f3bd10e020141e9693e4082111b00ae06e2e7cf1dc990dd24e9a155bd6aed8f;u_type=11;login_type=06;login_type=06;u_account=18622080802;city=013|130|90311178|-99;c_version=android@7.0601;d_deviceCode=865300046557938;enc_acc=YTExIhPbhGS/Jz0YfYIL40rPcLXT8gLpArlDFzz+Bj5K+hhHGj342KAblLl24efoqjFeIJpxczQDeKeGKQG1QzFE95LkwXK+acMqxKzlDI1rgoHm7iQ3zUJV4JdStRDeAW8LtBc4GNsaDoLyKnizBjhHsRpxnM98CZR890Xy1RI=;ecs_acc=YTExIhPbhGS/Jz0YfYIL40rPcLXT8gLpArlDFzz+Bj5K+hhHGj342KAblLl24efoqjFeIJpxczQDeKeGKQG1QzFE95LkwXK+acMqxKzlDI1rgoHm7iQ3zUJV4JdStRDeAW8LtBc4GNsaDoLyKnizBjhHsRpxnM98CZR890Xy1RI=;random_login=0;cw_mutual=6ff66a046d4cb9a67af6f2af5f74c321e5880d9521ce1cc40788c83d49f7f83a6001ff1274f2efffd6f4bfd53caf1ffaa6fe6b2055fd764e0e0e36cf780f46bf;t3_token=ceaa18f9689d533a232ae8b1ea75b6de;invalid_at=f5e0a2ea7d2f727fbd342ccf7e6b7a9311e84cc5baab5f7ec3fe028ed3d49804;c_mobile=18622080802;wo_family=0;u_areaCode=130;third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDIyN2IwMjU1NzcyZmRiM2ViOWQ0OWEyYjk2MWU1OTZkZGM2NzhmMmNhNjU4Yzg2YjRhNTdiOTRmNGFiZjdhMGU4MTRiYTA1MzNhODliODNjOGYzZjg0YWNmYzRlNjg1NjFiNWNhZDJkNTdlNGZkYTE5ODQ0NjhlYjYyYTNkMDY2YiIsInZlcnNpb24iOiIwMCJ9;ecs_token=eyJkYXRhIjoiNWVjMzc1MzNjZDhiYmJhZTEwYWQ1NDMzYjIyNDJkODc2M2Q4ZWU2M2U4ZjAxYTk5OGEzNTQ2NDcwMDFmNzI3NDZiYmYyMmVlZmM4MmZjNDkyYTBmNDEyZDE4M2Y5MmE4YzJiZTRjMmZiZDM0YjcxNjI2Y2FmNDQxNDlkMWY2YTc3N2M5YThmNDQxZjUyMzU0YWU3YmIwYWYzYTQxODU3MTE4MTgyNzY3ZDViZmFiZTFhMzBlMzJhYTJlNzQxODAxIiwidmVyc2lvbiI6IjAwIn0=;jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtb2JpbGUiOiIxODYyMjA4MDgwMiIsInBybyI6IjAxMyIsImNpdHkiOiIxMzAiLCJpZCI6IjdlMWQ5Yjc2NmU3ZWRkNmEwM2U5MDMzNmU0ZDI3NjdlIn0.UeBASO6tFeJefTwEMidFe6xBpS0aImsPfkmPAGFCK0w;on_info=6e287cc855950ad0751dbd7f4ae6ffa1;mobileServiceAll=aebc1dc11b73b572a5b54f3a3d5d7c2d;mobileService1=QP0u5LpLJYu4oPZDA-Fsm-yptIBrPWvmAExm-lV1N-nLXZYFU-MQ!-534537287;mactivity=1602809544.511.186722.462589;smallvideoroute=1602809544.521.294049.232259;importantroute=1602809545.934.147754.62655;JSESSIONID=2120F6AE3274372E4991F7E78245C5D3;route=4409263098d857a3cc010d262182161c;ecs_acc=YTExIhPbhGS/Jz0YfYIL40rPcLXT8gLpArlDFzz+Bj5K+hhHGj342KAblLl24efoqjFeIJpxczQDeKeGKQG1QzFE95LkwXK+acMqxKzlDI1rgoHm7iQ3zUJV4JdStRDeAW8LtBc4GNsaDoLyKnizBjhHsRpxnM98CZR890Xy1RI=;logHostIP=null;c_sfbm=234g_00");
        getGoodsList(info1);
    }

    public static void main(String[] args) throws Exception {
        List<ChinaUnicomInfo> chinaUnicomInfoList = new ArrayList<>();
        // 156000015800
        ChinaUnicomInfo info1 = new ChinaUnicomInfo();
        info1.setPhoneNum("15600015800");
        info1.setCookie("wmf=1993c8784ca0774e61b799fea19e593e;MUT_S=android8.1.0;on_info=b7ebdc40ed1791ad2f844a90b2dc06c5;welfareroute=209653824c881b49644fb84e922a423d508ce1a3;devicedId=865300046557938;logHostIP=null;req_mobile=15600015800;req_serial=;clientid=98|0;req_wheel=ssss;ecs_acc=rR8I7VLvtTPNmmlM6BbFSwdz4OL95g4bIB4ti4dL0kqILcV4fsEFKl0teQmXNDb6p7/NPeauOiENQfIkTYmOxSfM0vCtoQFQP/DSvIsIVK+o+AqSY2xqi8UABLJrhgjlJv525uGdvG77HZwvyWldpPXE4Fdn6GLEaheATLtyvhY=;paymanager=4fbe4ed2244def288579471e7141279d;c_sfbm=234g_00;mobileServiceAll=bfdbc6093d2832d6f573e36fdea0c406;mobileService1=Otsu3Hy9w3KXTSPge_1eiRwBzR91FaNIzXVQL4NRrzEsNEIJd8RB!-1074170694;u_type=11;a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDM0MTM4MDcsInRva2VuIjp7ImxvZ2luVXNlciI6IjE1NjAwMDE1ODAwIiwicmFuZG9tU3RyIjoieWg1NDFlZDAxNjAyODA5MDA3In0sImlhdCI6MTYwMjgwOTAwN30.edoNGPW30VKa1Qpx0bKIEU0pAjXPqk4Z26a_3gcKUvDeWSOxuambsUSnvt2hs3QV52uu0lDucuNzFrr2hoP3tg;c_id=7634d33a3ca6ce48a2f1128abb3a63975203b9d5a9f46a440d064cdd156e6f04;login_type=06;login_type=06;u_account=15600015800;city=011|110|90356344|-99;c_version=android@7.0601;d_deviceCode=865300046557938;enc_acc=rR8I7VLvtTPNmmlM6BbFSwdz4OL95g4bIB4ti4dL0kqILcV4fsEFKl0teQmXNDb6p7/NPeauOiENQfIkTYmOxSfM0vCtoQFQP/DSvIsIVK+o+AqSY2xqi8UABLJrhgjlJv525uGdvG77HZwvyWldpPXE4Fdn6GLEaheATLtyvhY=;ecs_acc=rR8I7VLvtTPNmmlM6BbFSwdz4OL95g4bIB4ti4dL0kqILcV4fsEFKl0teQmXNDb6p7/NPeauOiENQfIkTYmOxSfM0vCtoQFQP/DSvIsIVK+o+AqSY2xqi8UABLJrhgjlJv525uGdvG77HZwvyWldpPXE4Fdn6GLEaheATLtyvhY=;random_login=0;cw_mutual=6ff66a046d4cb9a67af6f2af5f74c321e5880d9521ce1cc40788c83d49f7f83ab3a3319edad000874a7032c84921f4bbc54a6fb938fa52a78d8c60cae61c54f6;t3_token=01f5546ba63b648dfe0c1d536e3674d6;invalid_at=5d3dc4a7a45e2be0dcbf37ca3e037453240821d9393af9ac0965b79b6f0b3393;c_mobile=15600015800;wo_family=0;u_areaCode=110;third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDJmMmVlNDZmNmFhZGE0ZmMyYTJkM2NkNTU3NTA2NTkwZGNhYjc5OTQxOTFjYzRhMGE3ODhkYTQxMTI5OGQyOTdlMWVhOGU3YWVkNDIxNDE0YzdiZjgxODQwYjkzZjllZjE1ZWYxZWY2NjRhNGFkODg0NGVmOTE4Yjc3ZmRlMWU3NiIsInZlcnNpb24iOiIwMCJ9;ecs_token=eyJkYXRhIjoiNWVjMzc1MzNjZDhiYmJhZTEwYWQ1NDMzYjIyNDJkODc2M2Q4ZWU2M2U4ZjAxYTk5OGEzNTQ2NDcwMDFmNzI3NDQyY2NiM2ZhNTQ3ZmJjYjBlNTZjNDJkY2Q4MjMzMzIwYzU0NDc3ZWM5ZDUwZTQ1ODM0NzBkNTk2NTg3NTVjNjg1YjEyZmVmNTQ5ZDFlNjBjNGU1OTYyZTJiY2YwNzQ1Y2Q1MGM5NTUyYmE3Y2JiZmQ2ODhkZmU1Y2U1ODQwYmRjIiwidmVyc2lvbiI6IjAwIn0=;jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtb2JpbGUiOiIxNTYwMDAxNTgwMCIsInBybyI6IjAxMSIsImNpdHkiOiIxMTAiLCJpZCI6ImFkYWRjM2U1YTBhM2E2N2QwYzhiMjRkZTIyMDllNzdlIn0.H5gDvt5JX4m6nVNxudaB9BRdkJrVvJQwYm-2p8GrKQk;dNG=408b2eee65c5e2ac8345f84109ee7eb9;smallvideoroute=1602809014.89.186397.742502;mactivity=1602809014.875.185481.745721;importantroute=1602809019.93.294053.420604;JSESSIONID=A1257AFA09F8F291DF8DD75718C76830;route=1cb499e1e9387df9f55a2f1c6c297bca;ecs_acc=rR8I7VLvtTPNmmlM6BbFSwdz4OL95g4bIB4ti4dL0kqILcV4fsEFKl0teQmXNDb6p7/NPeauOiENQfIkTYmOxSfM0vCtoQFQP/DSvIsIVK+o+AqSY2xqi8UABLJrhgjlJv525uGdvG77HZwvyWldpPXE4Fdn6GLEaheATLtyvhY=;logHostIP=null;c_sfbm=234g_00");
        chinaUnicomInfoList.add(info1);
        //15510804272
        ChinaUnicomInfo info3 = new ChinaUnicomInfo();
        info3.setPhoneNum("15510804272");
        info1.setCookie("wmf=2fca4c30345c1edc16518abcb05ef7a7;MUT_S=android8.1.0;devicedId=865300046557938;logHostIP=null;c_sfbm=234g_00;ecs_acc=mkBJRpT8F+ReRsfMJh03KqZiJiwJ2d7qXOFTPu5uHFBH6vP7rpBGshwJllWYzH6OqSi+b/zbh9cwP624ZImclp1Y/fnj6B2MlKdjLyL+Kbh/mGkKazx4bA9quOK9mjm+jzJDsp0Tq1IbaIDGY/n/cPKUzQ84UxRLWM41Jbz5mPs=;req_mobile=15510804272;req_serial=;clientid=98|0;req_wheel=ssss;a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDM0MTQyNTAsInRva2VuIjp7ImxvZ2luVXNlciI6IjE1NTEwODA0MjcyIiwicmFuZG9tU3RyIjoieWhlZDVhYWIxNjAyODA5NDUwIn0sImlhdCI6MTYwMjgwOTQ1MH0.EkUpwm2BYBE3BsbCnCfxLxzS8lx41JP9_0RXa6ShhWCxgnMTACeFTLDulZLqtKI8ZQM1PCzX2Xtac40M8dFpvA;c_id=b1d9a947ec07d4e3ff1b2c4e8b854f233d99439f5cc1acbb94ddc45921fd654e;u_type=11;login_type=06;login_type=06;u_account=15510804272;city=013|130|90311178|-99;c_version=android@7.0601;d_deviceCode=865300046557938;enc_acc=mkBJRpT8F+ReRsfMJh03KqZiJiwJ2d7qXOFTPu5uHFBH6vP7rpBGshwJllWYzH6OqSi+b/zbh9cwP624ZImclp1Y/fnj6B2MlKdjLyL+Kbh/mGkKazx4bA9quOK9mjm+jzJDsp0Tq1IbaIDGY/n/cPKUzQ84UxRLWM41Jbz5mPs=;ecs_acc=mkBJRpT8F+ReRsfMJh03KqZiJiwJ2d7qXOFTPu5uHFBH6vP7rpBGshwJllWYzH6OqSi+b/zbh9cwP624ZImclp1Y/fnj6B2MlKdjLyL+Kbh/mGkKazx4bA9quOK9mjm+jzJDsp0Tq1IbaIDGY/n/cPKUzQ84UxRLWM41Jbz5mPs=;random_login=0;cw_mutual=6ff66a046d4cb9a67af6f2af5f74c321e5880d9521ce1cc40788c83d49f7f83afc3a16a394cebcb9f53bf56204d3ee5e69838561e5b1b23cf17c2ece6dd9c276;t3_token=eb635b679c2cb8fb15509b354211f30f;invalid_at=c2661d2429432fee1bb0d95ca83c45a93fa5f152136756938424fd6b1b083e86;c_mobile=15510804272;wo_family=0;u_areaCode=130;third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDJiMDMwZmYxZWU2Mzc2MWNlZGE0YTEyODQ4MjYwM2VlY2NlNDU1ZjY5ZTZiZmViMTMxMWIwYTcyZmI5NjM4ZDU1YzkxZjJmZWM3Mjc5OWYzYTJhMTI1ZWFlMzNkZjMxODVlZmRjNzNkZmM4ZmNiOGI0YzZiZWQ0NmU5MTBmNTUzYiIsInZlcnNpb24iOiIwMCJ9;ecs_token=eyJkYXRhIjoiNWVjMzc1MzNjZDhiYmJhZTEwYWQ1NDMzYjIyNDJkODc2M2Q4ZWU2M2U4ZjAxYTk5OGEzNTQ2NDcwMDFmNzI3NDZiYmYyMmVlZmM4MmZjNDkyYTBmNDEyZDE4M2Y5MmE4NTlkMGI5NGU3MThkNmNjMGI1ODhmMzcyNDBjZmZmNGRiZTIxNjM3ZTIwZmM4YWQ1ZTFiYmYwMWVhOGM0MTdkODJjNGU4NjYwNzdhYmJiYjU0NjVjNzhlNWU5MGIzOGRkIiwidmVyc2lvbiI6IjAwIn0=;jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtb2JpbGUiOiIxNTUxMDgwNDI3MiIsInBybyI6IjAxMyIsImNpdHkiOiIxMzAiLCJpZCI6IjU3ZWJjMWU5OTBiMjc1ZGI1N2I4MGViNzA2OWNjMjQ0In0.ZTIMZ8EFqIXWA1LjpR3XHZgwFVcJBMdoNjL80u8BXG8;on_info=b7ebdc40ed1791ad2f844a90b2dc06c5;mobileServiceAll=88de1645dca164d92ddf60a56bfc5926;mobileService1=R-Eu41KtGD-3mE80Z8Q8QU79I92QBJNBqgSbyj4dJ2zDQ8rQq_wq!-1611534819;mactivity=1602809453.078.185515.420923;smallvideoroute=1602809453.042.148555.16102;route=1cb499e1e9387df9f55a2f1c6c297bca;importantroute=1602809462.48.246386.839265;JSESSIONID=B4BE290BCFEC1601643AD53C45C37CE2;ecs_acc=mkBJRpT8F+ReRsfMJh03KqZiJiwJ2d7qXOFTPu5uHFBH6vP7rpBGshwJllWYzH6OqSi+b/zbh9cwP624ZImclp1Y/fnj6B2MlKdjLyL+Kbh/mGkKazx4bA9quOK9mjm+jzJDsp0Tq1IbaIDGY/n/cPKUzQ84UxRLWM41Jbz5mPs=;logHostIP=null;c_sfbm=234g_00");
        chinaUnicomInfoList.add(info3);
        //18622080802
        ChinaUnicomInfo info4 = new ChinaUnicomInfo();
        info4.setPhoneNum("18622080802");
        info4.setCookie("wmf=960a71aba033f66993be9932f4a94d52;MUT_S=android8.1.0;devicedId=865300046557938;logHostIP=null;c_sfbm=234g_00;welfareroute=58e6c8db5da65d38abb9b66b7d81d17a66a38972;ecs_acc=YTExIhPbhGS/Jz0YfYIL40rPcLXT8gLpArlDFzz+Bj5K+hhHGj342KAblLl24efoqjFeIJpxczQDeKeGKQG1QzFE95LkwXK+acMqxKzlDI1rgoHm7iQ3zUJV4JdStRDeAW8LtBc4GNsaDoLyKnizBjhHsRpxnM98CZR890Xy1RI=;req_mobile=18622080802;req_serial=;clientid=98|0;req_wheel=ssss;a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDM0MTQzNDIsInRva2VuIjp7ImxvZ2luVXNlciI6IjE4NjIyMDgwODAyIiwicmFuZG9tU3RyIjoieWhjOTI4NGExNjAyODA5NTQyIn0sImlhdCI6MTYwMjgwOTU0Mn0.Zg1-Q1QR9jxUBESoa2gTWB8zB-XmTq6pGV4Hq_dsQt6Yz6Q-Y8IensIBE7rVD01QMZQCNwz2RY9bZlRWdzr_nA;c_id=4f3bd10e020141e9693e4082111b00ae06e2e7cf1dc990dd24e9a155bd6aed8f;u_type=11;login_type=06;login_type=06;u_account=18622080802;city=013|130|90311178|-99;c_version=android@7.0601;d_deviceCode=865300046557938;enc_acc=YTExIhPbhGS/Jz0YfYIL40rPcLXT8gLpArlDFzz+Bj5K+hhHGj342KAblLl24efoqjFeIJpxczQDeKeGKQG1QzFE95LkwXK+acMqxKzlDI1rgoHm7iQ3zUJV4JdStRDeAW8LtBc4GNsaDoLyKnizBjhHsRpxnM98CZR890Xy1RI=;ecs_acc=YTExIhPbhGS/Jz0YfYIL40rPcLXT8gLpArlDFzz+Bj5K+hhHGj342KAblLl24efoqjFeIJpxczQDeKeGKQG1QzFE95LkwXK+acMqxKzlDI1rgoHm7iQ3zUJV4JdStRDeAW8LtBc4GNsaDoLyKnizBjhHsRpxnM98CZR890Xy1RI=;random_login=0;cw_mutual=6ff66a046d4cb9a67af6f2af5f74c321e5880d9521ce1cc40788c83d49f7f83a6001ff1274f2efffd6f4bfd53caf1ffaa6fe6b2055fd764e0e0e36cf780f46bf;t3_token=ceaa18f9689d533a232ae8b1ea75b6de;invalid_at=f5e0a2ea7d2f727fbd342ccf7e6b7a9311e84cc5baab5f7ec3fe028ed3d49804;c_mobile=18622080802;wo_family=0;u_areaCode=130;third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDIyN2IwMjU1NzcyZmRiM2ViOWQ0OWEyYjk2MWU1OTZkZGM2NzhmMmNhNjU4Yzg2YjRhNTdiOTRmNGFiZjdhMGU4MTRiYTA1MzNhODliODNjOGYzZjg0YWNmYzRlNjg1NjFiNWNhZDJkNTdlNGZkYTE5ODQ0NjhlYjYyYTNkMDY2YiIsInZlcnNpb24iOiIwMCJ9;ecs_token=eyJkYXRhIjoiNWVjMzc1MzNjZDhiYmJhZTEwYWQ1NDMzYjIyNDJkODc2M2Q4ZWU2M2U4ZjAxYTk5OGEzNTQ2NDcwMDFmNzI3NDZiYmYyMmVlZmM4MmZjNDkyYTBmNDEyZDE4M2Y5MmE4YzJiZTRjMmZiZDM0YjcxNjI2Y2FmNDQxNDlkMWY2YTc3N2M5YThmNDQxZjUyMzU0YWU3YmIwYWYzYTQxODU3MTE4MTgyNzY3ZDViZmFiZTFhMzBlMzJhYTJlNzQxODAxIiwidmVyc2lvbiI6IjAwIn0=;jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtb2JpbGUiOiIxODYyMjA4MDgwMiIsInBybyI6IjAxMyIsImNpdHkiOiIxMzAiLCJpZCI6IjdlMWQ5Yjc2NmU3ZWRkNmEwM2U5MDMzNmU0ZDI3NjdlIn0.UeBASO6tFeJefTwEMidFe6xBpS0aImsPfkmPAGFCK0w;on_info=6e287cc855950ad0751dbd7f4ae6ffa1;mobileServiceAll=aebc1dc11b73b572a5b54f3a3d5d7c2d;mobileService1=QP0u5LpLJYu4oPZDA-Fsm-yptIBrPWvmAExm-lV1N-nLXZYFU-MQ!-534537287;mactivity=1602809544.511.186722.462589;smallvideoroute=1602809544.521.294049.232259;importantroute=1602809545.934.147754.62655;JSESSIONID=2120F6AE3274372E4991F7E78245C5D3;route=4409263098d857a3cc010d262182161c;ecs_acc=YTExIhPbhGS/Jz0YfYIL40rPcLXT8gLpArlDFzz+Bj5K+hhHGj342KAblLl24efoqjFeIJpxczQDeKeGKQG1QzFE95LkwXK+acMqxKzlDI1rgoHm7iQ3zUJV4JdStRDeAW8LtBc4GNsaDoLyKnizBjhHsRpxnM98CZR890Xy1RI=;logHostIP=null;c_sfbm=234g_00");
        chinaUnicomInfoList.add(info4);

        robTicket(chinaUnicomInfoList);
    }

    private static void robTicket(List<ChinaUnicomInfo> chinaUnicomInfoList) throws Exception {
        List<MeituanThread> meituanThreadList = new ArrayList<>();
        List<MeiTuanGoods> goodsList = getGoodsList(chinaUnicomInfoList.get(0));
        System.out.println(JSONObject.toJSONString(goodsList));
        for (ChinaUnicomInfo info : chinaUnicomInfoList) {
            goodsList.forEach(singleGoods -> {
                MeituanThread thread = new MeituanThread();
                thread.setChinaUnicomInfo(info);
                thread.setMeiTuanGoods(singleGoods);
                meituanThreadList.add(thread);
            });
        }
        ScheduledExecutorService service = Executors.newScheduledThreadPool(30);
        while (true) {
            meituanThreadList.forEach(thread -> {
                service.schedule(thread, 200, TimeUnit.MILLISECONDS);
            });
            TimeUnit.MILLISECONDS.sleep(1L);
            if (System.currentTimeMillis() - 1602813720000L > 0) {
                service.shutdownNow();
                break;
            }
        }

    }

    /**
     * 獲取商品信息
     */
    private static List<MeiTuanGoods> getGoodsList(ChinaUnicomInfo chinaUnicomInfo) throws Exception {
        String url = "https://m.client.10010.com/welfare-mall-front-activity/mobile/activity/get619Activity/v1?whetherFriday=YES&from=955000006";
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Cookie", chinaUnicomInfo.getCookie());
        headers.put("User-Agent", Constant.USER_AGENT);
        HttpClientResult httpClientResult = new HttpClientUtil().doGet(url, headers, null);
        System.out.println(chinaUnicomInfo.getPhoneNum());
        if (httpClientResult.getCode() == 200) {
            JSONObject respJo = JSONObject.parseObject(httpClientResult.getContent());
            JSONObject resdata = respJo.getJSONObject("resdata");
            JSONArray activityList = resdata.getJSONArray("activityList");
            JSONArray goodsListJa = null;
            for (Object o : activityList) {
                JSONObject jo = (JSONObject) o;
                String navClock = jo.getString("navClock");
                if (StringUtils.equals("10:00", navClock)) {
                    goodsListJa = jo.getJSONArray("goodsList");
                }
            }
            if (Objects.isNull(goodsListJa)) {
                return getGoodsList(chinaUnicomInfo);
            }
            List<MeiTuanGoods> goodsList = new LinkedList<>();
            goodsListJa.forEach(obj -> {
                JSONObject jo = (JSONObject) obj;
                MeiTuanGoods info = new MeiTuanGoods();
                String goodsName = jo.getString("goodsName");
                if (!StringUtils.contains(goodsName, "满30")) {
                    return;
                }
                info.setGoodsName(jo.getString("goodsName"));
                info.setLinkUrl(jo.getString("linkURL"));
                String goodsSkuId = jo.getString("goodsId");
                System.out.println(goodsSkuId);
                info.setGoodsSkuId(goodsSkuId);
                info.setMarketPrice(jo.getDouble("marketPrice"));
                info.setBeginTime(jo.getLong("beginTime"));
                info.setEndTime(jo.getLong("endTime"));
                info.setState(jo.getInteger("state"));
                try {
//                    info.setCurrSalePrice(getCurrSalePrice(chinaUnicomInfo, goodsSkuId));
                    info.setCurrSalePrice(10D);
                } catch (Exception e) {
                }
                goodsList.add(info);
            });
            return goodsList;
        } else {
            return null;
        }
    }

    private static double getCurrSalePrice(ChinaUnicomInfo chinaUnicomInfo, String goodsSkuId) throws Exception {
        String url = "https://m.client.10010.com/welfare-mall-front-activity/mobile/activity/getGoodsTradePrice/v2";
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Cookie", chinaUnicomInfo.getCookie());
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36");
        HttpClientResult httpClientResult = new HttpClientUtil().doGet(url, headers, null);
        if (httpClientResult.getCode() == 200) {
            JSONObject respJo = JSONObject.parseObject(httpClientResult.getContent());
            JSONObject resdata = respJo.getJSONObject("resdata");
            double currSalePrice = resdata.getDouble(goodsSkuId);
            return currSalePrice;
        } else {
            return 0D;
        }
    }

    /**
     * 下单
     */
    private static HttpClientResult submitOrder(ChinaUnicomInfo chinaUnicomInfo, MeiTuanGoods meiTuanGoods) throws Exception {
        String url = "https://m.client.10010.com/welfare-mall-front/mobile/api/bj2402/v1";
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Cookie", chinaUnicomInfo.getCookie());
        headers.put("User-Agent", Constant.USER_AGENT);
        Map<String, String> params = new TreeMap<>();
        Map<String, Object> reqDataMap = new TreeMap<>();
        reqDataMap.put("goodsId", meiTuanGoods.getGoodsSkuId());
        reqDataMap.put("reChangeNo", chinaUnicomInfo.getPhoneNum());
        reqDataMap.put("payWay", "01");
        reqDataMap.put("amount", meiTuanGoods.getCurrSalePrice());
        reqDataMap.put("saleTypes", "C");
        reqDataMap.put("points", 0);
        reqDataMap.put("beginTime", meiTuanGoods.getBeginTime());
        reqDataMap.put("imei", "d2575c3322c14c4cbda477bda4cc519e");
        reqDataMap.put("sourceChannel", "955000300");
        reqDataMap.put("proFlag", "");
        reqDataMap.put("scene", "");
        reqDataMap.put("promoterCode", "");
        reqDataMap.put("maxcash", "");
        params.put("reqdata", JSONObject.toJSONString(reqDataMap));
        return new HttpClientUtil().doWWWFormUrlencodePost(url, headers, params);
    }

    @Data
    static class MeituanThread implements Callable<Object> {

        private ChinaUnicomInfo chinaUnicomInfo;
        private MeiTuanGoods meiTuanGoods;
        private ServerJNotify serverJNotify = new ServerJNotify();

        @Override
        public Object call() throws Exception {
            String msg = "";
            try {
                HttpClientResult httpClientResult = submitOrder(chinaUnicomInfo, meiTuanGoods);
                JSONObject respJo = JSONObject.parseObject(httpClientResult.getContent());
                System.out.println(respJo);
                String code = respJo.getString("code");
                String message = respJo.getString("msg");
                msg = String.format("手机号:%s,商品:%s 下单结果:[code: %s, msg: %s]",
                        this.chinaUnicomInfo.getPhoneNum(),
                        this.meiTuanGoods.getGoodsName(),
                        code,
                        message);
                if (StringUtils.equals("0", code)) {
                    System.out.println(new Date() + msg);
                } else if (StringUtils.equals("1", code)) {
                    System.err.println(new Date() + msg);
                }

            } catch (Exception e) {

            }
            return msg;
        }
    }
}
