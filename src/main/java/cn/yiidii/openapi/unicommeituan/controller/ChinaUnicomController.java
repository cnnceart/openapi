package cn.yiidii.openapi.unicommeituan.controller;

import cn.yiidii.openapi.base.vo.Result;
import cn.yiidii.openapi.unicommeituan.controller.form.ChinaUnicomLoginForm;
import cn.yiidii.openapi.unicommeituan.controller.form.TelecomForm;
import cn.yiidii.openapi.unicommeituan.service.ChinaUnicomService;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * <p>
 * 中国联通
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-11-29 16:00
 */
@RestController
@Validated
@RequestMapping("chinaUnicom")
@RequiredArgsConstructor
public class ChinaUnicomController {

    private final ChinaUnicomService chinaUnicomService;

    @GetMapping("sendRandomNum")
    public Result sendRandomNum(@RequestParam(required = false) @NotBlank(message = "请输入手机号码") @Pattern(regexp = "\\d{11,}", message = "手机号格式不正确") String mobile) {
        return Result.success(null, chinaUnicomService.sendRandomNum(mobile));
    }

    @PostMapping("randomLogin")
    public Result randomLogin(@RequestBody @Valid ChinaUnicomLoginForm chinaUnicomLoginForm) {
        Integer type = chinaUnicomLoginForm.getType();
        if(Objects.isNull(type)){
            type = 0;
        }
        Result result = null;
        if (type == 1) {
            result = Result.success(getMockData(), "模拟登陆成功");
        } else {
            result = chinaUnicomService.randomLogin(chinaUnicomLoginForm);
        }
        return result;
    }

    @PostMapping("queryUserInfo4HomePage")
    public JSONObject queryUserInfo4HomePage(@RequestBody @Valid TelecomForm telecomForm) {

        return chinaUnicomService.queryUserInfo4HomePage(telecomForm);
    }

    @PostMapping("queryUserInfo")
    public JSONObject queryUserInfo(@RequestBody @Valid TelecomForm telecomForm) {

        return chinaUnicomService.queryUserInfo(telecomForm);
    }

    private JSONObject getMockData() {
        String mockData = "{\n" +
                "        \"cookieMap\": {\n" +
                "            \"t3_token\": \"b6c88280b36cef69da20c7cec1eba93d\",\n" +
                "            \"login_type\": \"06\",\n" +
                "            \"cw_mutual\": \"6ff66a046d4cb9a67af6f2af5f74c321e5880d9521ce1cc40788c83d49f7f83afc3a16a394cebcb9f53bf56204d3ee5e69838561e5b1b23cf17c2ece6dd9c276\",\n" +
                "            \"city\": \"013|130|90311178|-99\",\n" +
                "            \"jwt\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtb2JpbGUiOiIxNTUxMDgwNDI3MiIsInBybyI6IjAxMyIsImNpdHkiOiIxMzAiLCJpZCI6IjAyYTE3OTAwYmFkYTA1ZTliY2NmN2U0NjA3NThhNDQ1In0.gwDoBIfpkkNHkiJc23Q24sZzD-iYeMZM2bw4ovewcmw\",\n" +
                "            \"c_version\": \"android@7.0601\",\n" +
                "            \"u_account\": \"15510804272\",\n" +
                "            \"invalid_at\": \"5a65f76e51f9ea7f8ddde33470079653ae9b6dc91dc6b1240ea81b91a42ad012\",\n" +
                "            \"c_mobile\": \"15510804272\",\n" +
                "            \"wo_family\": \"0\",\n" +
                "            \"third_token\": \"eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDI1NDBlOTA0OTFiMDFhOWJhNjQ3YWQ1YzFhMDUwNGFkNGU5ZWYwNmFiODk0NzViMjc0ZWY1NTQ3Y2QxNGE0ZTlkY2ZhMzEzNmM5M2MzOGQxNGZlODY5ZDY4ZDM1OTc1NzQzNTdhMDYzNzliZTc1NGE4MGNkNDcxZThhNGRkMjUyMiIsInZlcnNpb24iOiIwMCJ9\",\n" +
                "            \"ecs_acc\": \"BCu2K+DWJdeUygN5f1BODBdx87f5TiT1mstpRYo6MrgtMCuraBVMUfc1e+XoRCj4+IKKDNjH1vgbbJhwZMd+XV0jkCn+ukWijtfDqamCjnca9kIJ8GqiOvbTqAGHHF/wDAt/GygMLM+MHx4GTFtbkmtZ852IWlQoddiuYLVRI4s=\",\n" +
                "            \"random_login\": \"1\",\n" +
                "            \"a_token\": \"eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDcyNDk4MzgsInRva2VuIjp7ImxvZ2luVXNlciI6IjE1NTEwODA0MjcyIiwicmFuZG9tU3RyIjoieWgxMDNlNzgxNjA2NjQ1MDM4In0sImlhdCI6MTYwNjY0NTAzOH0.XkHk9Y5V7AoutmqKMZqH34x1xKXZEaRE7N3wxtcKKqhS7hSgYSwsxzHkLUoaPv_BIBoL9UNrtqWBn_ELrfstUg\",\n" +
                "            \"d_deviceCode\": \"7865969553f94e9c9fe6654e89cbefc0\",\n" +
                "            \"enc_acc\": \"BCu2K+DWJdeUygN5f1BODBdx87f5TiT1mstpRYo6MrgtMCuraBVMUfc1e+XoRCj4+IKKDNjH1vgbbJhwZMd+XV0jkCn+ukWijtfDqamCjnca9kIJ8GqiOvbTqAGHHF/wDAt/GygMLM+MHx4GTFtbkmtZ852IWlQoddiuYLVRI4s=\",\n" +
                "            \"c_id\": \"b1d9a947ec07d4e3ff1b2c4e8b854f23f385d2855d4ba426c45e490028a42257\",\n" +
                "            \"u_areaCode\": \"130\",\n" +
                "            \"c_sfbm\": \"234g_00\",\n" +
                "            \"u_type\": \"11\",\n" +
                "            \"ecs_token\": \"eyJkYXRhIjoiNWVjMzc1MzNjZDhiYmJhZTEwYWQ1NDMzYjIyNDJkODc2M2Q4ZWU2M2U4ZjAxYTk5OGEzNTQ2NDcwMDFmNzI3NDQ0ODU1NTIwYTU3OTI0MjI4YjhjMjhhMjFmNDIzMmM4NzFlYzg5ODA0ZjE5ZjFmZWRkMDI4NGY0NDY0ZjNkMjEwNjdlMDJkNjljOGMxOWNjNWQ4NTMxYmQ4ZDZiMWZmNzhhMjllYTc0NGJjNTQ0NWI1M2ZjZWZkMTVkZGRjYzc5IiwidmVyc2lvbiI6IjAwIn0=\"\n" +
                "        },\n" +
                "        \"chinaUnicomResp\": {\n" +
                "            \"t3_token\": \"b6c88280b36cef69da20c7cec1eba93d\",\n" +
                "            \"yw_code\": \"\",\n" +
                "            \"code\": \"0\",\n" +
                "            \"head_img\": \"\",\n" +
                "            \"list\": [\n" +
                "                {\n" +
                "                    \"proCode\": \"013\",\n" +
                "                    \"cityName\": \"天津\",\n" +
                "                    \"cityCode\": \"130\",\n" +
                "                    \"area_code\": \"\",\n" +
                "                    \"num\": \"15510804272\",\n" +
                "                    \"proName\": \"天津\",\n" +
                "                    \"type\": \"01\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"type\": \"06\",\n" +
                "            \"menuNetType\": \"112_2002\",\n" +
                "            \"token_online\": \"040cfb8bdd5d5202e639e6f1596986338d1bede32bb6225f2ad099f70ac4e7e1b39120cb83edd0c915d33ed8a81092ed9a9d758b1c4a19d41fb2314db6b0571ab5bc1ef4af53dedb8cf46f7f49de399cc80bc69b0a5ac467bf47456f958898780111064de14d36cd2216b1a235039719da86ce577752d438dacfbe48e813795c00dbb8a33274f55dba5272ee8698d7bf43c9056cfad663db7659046a6bd0e6fd8bfb891d1ba32ae8bb834091c6a887cfc2d34370162d070c8dd2025e0fde6eeb4672f534b4f7bc334d33648c683cd5bc214d028627cb1bc562e54a322c540f74420d9ce788d901edddca65594cd1df23e7726c9e9eebfcba52744cde77e4aae1055ee84088bc4bd20f86a0649f74c8dd71beeeedeaaf9095f62842fe35ea74a08469b0d93a776bf57ace019be6611577\",\n" +
                "            \"invalidat\": \"2020-11-29 18:22:18\",\n" +
                "            \"default\": \"15510804272\",\n" +
                "            \"filename\": \"\",\n" +
                "            \"showFlower\": \"0\",\n" +
                "            \"keyVersion\": \"1\",\n" +
                "            \"menuurl\": \"http://m.client.10010.com:80/mobileService/templates/sitemap/\",\n" +
                "            \"appId\": \"cb648e652626d4cb7d0ec3f706b4a33de83a82107ab73300adbc3571575ddada50a9af5985f8ab85d31052ffb1f3c2a23f84a95aed72bf6461e38596903a2b0d829df62bac8a4091fd290f3494013b47\",\n" +
                "            \"member\": [\n" +
                "                {\n" +
                "                    \"main_flag\": \"0\",\n" +
                "                    \"service_class_code_name\": \"主卡\",\n" +
                "                    \"encryption\": \"e096376acfe5846675d642ce10a061eee82c8e47412b0937d3f7f3885fdea394\",\n" +
                "                    \"num\": \"186****0802\",\n" +
                "                    \"type\": \"0050\",\n" +
                "                    \"service_class_cod\": \"0050\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"main_flag\": \"1\",\n" +
                "                    \"service_class_code_name\": \"成员\",\n" +
                "                    \"encryption\": \"92eeabc0aa1b24cac46bc2d1412589b842f10922941f8b7fcd494fe45f22702b\",\n" +
                "                    \"num\": \"176****6400\",\n" +
                "                    \"type\": \"0050\",\n" +
                "                    \"service_class_cod\": \"0050\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"main_flag\": \"1\",\n" +
                "                    \"service_class_code_name\": \"成员\",\n" +
                "                    \"encryption\": \"3ebdabfd80673a010eb6f9d81056c7f7a69cea4d415f0d76217ea21010357912\",\n" +
                "                    \"num\": \"155****4272\",\n" +
                "                    \"current_num\": \"0\",\n" +
                "                    \"type\": \"0050\",\n" +
                "                    \"service_class_cod\": \"0050\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"main_flag\": \"1\",\n" +
                "                    \"service_class_code_name\": \"宽带\",\n" +
                "                    \"encryption\": \"1753a724a4cdd7d6e83da33e113e8b276a1053bd34fee327f360ec29aacd2631\",\n" +
                "                    \"num\": \"022****8725\",\n" +
                "                    \"type\": \"0040\",\n" +
                "                    \"service_class_cod\": \"0040\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"main_flag\": \"1\",\n" +
                "                    \"service_class_code_name\": \"固话\",\n" +
                "                    \"encryption\": \"5a245bbc7951bfa2cbfdea4b5f7a868f5cbb85d207415cf1a1fa155ef8f79cd9\",\n" +
                "                    \"num\": \"022****8864\",\n" +
                "                    \"type\": \"0030\",\n" +
                "                    \"service_class_cod\": \"0030\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"aha\": \"bdee38b9319c8577f8bfabd8b9880f5cd9146a4422f4becc65b7d8929293e6a1447d87267a55143765072c7ae76cf2c2\",\n" +
                "            \"pwd\": \"GGGlDsGj/45mz0kj+dWdOPX3+qxh+rwDJUR7j4+pLDtiAJT/x3Oqvd65gur4swpoKfpoZoaSwIQBvYclnVb6kzTt43R3WUpyJ6mGrvYQhOC8vfvio7vUjdSYRK2a8eILMZ161JLxFswascCdrzHxCRgllBuhp18pLB3up/tOuEI=\",\n" +
                "            \"ecs_token\": \"eyJkYXRhIjoiNWVjMzc1MzNjZDhiYmJhZTEwYWQ1NDMzYjIyNDJkODc2M2Q4ZWU2M2U4ZjAxYTk5OGEzNTQ2NDcwMDFmNzI3NDQ0ODU1NTIwYTU3OTI0MjI4YjhjMjhhMjFmNDIzMmM4NzFlYzg5ODA0ZjE5ZjFmZWRkMDI4NGY0NDY0ZjNkMjEwNjdlMDJkNjljOGMxOWNjNWQ4NTMxYmQ4ZDZiMWZmNzhhMjllYTc0NGJjNTQ0NWI1M2ZjZWZkMTVkZGRjYzc5IiwidmVyc2lvbiI6IjAwIn0=\",\n" +
                "            \"desmobile\": \"15510804272\"\n" +
                "        },\n" +
                "        \"cookieStr\": \"a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDc0NDM0NTgsInRva2VuIjp7ImxvZ2luVXNlciI6IjE1NjAwMDE1ODAwIiwicmFuZG9tU3RyIjoieWgyY2NlNWIxNjA2ODM4NjU4In0sImlhdCI6MTYwNjgzODY1OH0.WJq2U4Dm6r16-CJ5pPB0aQc4FmplCCZSMQuS3sM_bztfkhOEpjdBArJeP2SSyvDgVAMx3V3m8PrSLXOQj-HIKg;c_id=7634d33a3ca6ce48a2f1128abb3a6397543a95869f9c7e5d5b009b15a5ab65c9;c_mobile=15600015800;c_sfbm=234g_00;c_version=android@7.0601;c_version=android@7.0601;city=011|110|90356344|-99;cw_mutual=6ff66a046d4cb9a67af6f2af5f74c321e5880d9521ce1cc40788c83d49f7f83ab3a3319edad000874a7032c84921f4bbc54a6fb938fa52a78d8c60cae61c54f6;d_deviceCode=7865969553f94e9c9fe6654e89cbefc0;ecs_acc=DuDRhtHCwTGYm3rPtt6LA3g+fQ7DwEvXQ+PRr8A9XwpiZgDFgfr/XHhyCw7+qYmo5d3O7dIxBWGKRxZy5yPVQcZw2zMcNqlpDNFVcWMfLMvnjacf+5bgysN68SHnRsXrh61PJFQHxU2VGIqFkPn7itdtyaSgvAA+RcXMvAJTLzY=;ecs_token=eyJkYXRhIjoiNWVjMzc1MzNjZDhiYmJhZTEwYWQ1NDMzYjIyNDJkODc2M2Q4ZWU2M2U4ZjAxYTk5OGEzNTQ2NDcwMDFmNzI3NDNmZmViMjJjYWNkODUwZWJiMDMzNTdhNWRkY2ExMGQ4NDM5OTc4MzJkYmI2ODkzZDdiMjQzNmRhN2JjYzNhNGVjNzE3ZTJmMDZiYWEzOWNjOTMwMmY2Yzc1ZjZmNjY3MjQxNDZiNWZjZGZiMjkxM2RiNWI4Mzc1NWM2YjE3OGUwIiwidmVyc2lvbiI6IjAwIn0=;enc_acc=DuDRhtHCwTGYm3rPtt6LA3g+fQ7DwEvXQ+PRr8A9XwpiZgDFgfr/XHhyCw7+qYmo5d3O7dIxBWGKRxZy5yPVQcZw2zMcNqlpDNFVcWMfLMvnjacf+5bgysN68SHnRsXrh61PJFQHxU2VGIqFkPn7itdtyaSgvAA+RcXMvAJTLzY=;invalid_at=887c5b820f347a88058d861c3aeb2bbca46f8ab12b47b2e0dc95b9b1b9436414;jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtb2JpbGUiOiIxNTYwMDAxNTgwMCIsInBybyI6IjAxMSIsImNpdHkiOiIxMTAiLCJpZCI6ImU2YWU3MzVjZDAwOTdmMDQ2NWU0ODdmYTM0MWJjMmY3In0.n3eqZK4-ZRxoATp40iIEJ_9QSwBwdXJvki7MhGk6mfE;login_type=06;login_type=06;random_login=1;t3_token=d221cadfdf63940413ec1a71e172745e;third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDIzMDEwZDJlMDQ2NzgwMmI0ZGQ1NmMxY2I1Yjc0OWU4NWYwMmZhYzdmZTNhZGRhMjE5Zjc4Mzc3NjgzOWU3NDg1YTAxODk1ODUxODY2ZGFkOTc5YThmY2RmYjY4YjgzMTQ3OWRiNGY2N2ViOWE0YzI5ZTFiZmZkNzQyYWRiYTVkOCIsInZlcnNpb24iOiIwMCJ9;u_account=15600015800;u_areaCode=110;u_type=11;wo_family=0\"\n" +
                "    }";
        return JSONObject.parseObject(mockData);
    }
}
