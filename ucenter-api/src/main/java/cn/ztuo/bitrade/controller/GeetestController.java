
package cn.ztuo.bitrade.controller;

import cn.ztuo.bitrade.util.MessageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GeetestController {

    @Value("${hcaptcha.secret}")
    private String hcaptchaSecret;

    @PostMapping("/submit-captcha")
    @ResponseBody
    public MessageResult submitCaptcha(@RequestParam("h-captcha-response") String hCaptchaResponse) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://hcaptcha.com/siteverify";

        Map<String, String> body = new HashMap<>();
        body.put("secret", hcaptchaSecret);
        body.put("response", hCaptchaResponse);

        Map<String, Object> response = restTemplate.postForObject(url, body, Map.class);

        boolean success = (boolean) response.get("success");
        if (success) {
            return new MessageResult(0, "hCaptcha verification succeeded.");
        } else {
            return new MessageResult(500, "hCaptcha verification failed.");
        }
    }
    public  Boolean  watherProof( String ticket,  String randStr, String ip) throws Exception {
        String response = null;
        GetMethod getMethod = null;
        Boolean responseBool = false;
        try {
            log.info("watherProof>>>>>start>>>ip>>>" + ip);
            StringBuilder sb = new StringBuilder();
            sb.append(url).append("?aid=").append(appId)
                    .append("&AppSecretKey=").append(appSecretKey)
                    .append("&Ticket=").append(ticket).
                    append("&Randstr=").append(randStr).
                    append("&UserIP=").append(ip);
            getMethod = new GetMethod(sb.toString());
            int code = client.executeMethod(getMethod);
            if (code == 200) {
                response = getMethod.getResponseBodyAsString();
            } else {
                log.info("状态响应码为>>>>>>" + code);
            }
        } catch (HttpException e) {
            log.info("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
        } catch (IOException e) {
            log.error("发生网络异常", e);
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
                getMethod = null;
            }
        }
        log.info(">>>>>>>>发送校验结果响应为>>>>>>"+response);
        if(!StringUtils.isEmpty(response)){
            JSONObject responseJson = JSONObject.parseObject(response);
            String code = responseJson.getString("response");
            if("1".equals(code)){
                responseBool = true ;
            }
        }
        return  responseBool;
    }
}
