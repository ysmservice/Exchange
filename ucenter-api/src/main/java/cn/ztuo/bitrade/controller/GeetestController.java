
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
        return  true;
    }
}
