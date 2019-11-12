package com.ahtusi.controller;

import com.tencent.tusi.mfa.TUSIMfaWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("mfademo/")
public class LoginControler {

    private static final Logger LOG = LoggerFactory.getLogger(LoginControler.class);

    @RequestMapping("login")
    public String checkok(@RequestParam(value = "username", defaultValue = "")String username,
                          @RequestParam(value = "password", defaultValue = "")String password){

        if (username.equals("admin") && password.equals("admin") ){
            final String secretId = "8778210956007260556";
            final String secretKey = "9104D293BC764DBA8DFBC0E169753244BZY8YW5A";
            final String userId = "tre"; //当前登录账户的userId
            final String callbackUrl = "http://localhost:8082/mfademo_war_exploded/mfademo/result"; //用于接收MFA二次认证后的认证结果
            TUSIMfaWebClient.RequestCreator requestCreator = new TUSIMfaWebClient.RequestCreator()
                    .secretId(secretId)
                    .secretKey(secretKey)
                    .userId(userId)
                    .callbackUrl(callbackUrl);
            String requestCode =  requestCreator.create(); //生成requestCode
            // 2.重定向到MFA:
            return "redirect:http://localhost:8080/MFA_war_exploded/open/mfa/start?code=" + requestCode;
        }
        return "error";
    }

    @RequestMapping("result")
    public String getResult(@RequestParam(value = "code",defaultValue = "")String code){
//        1.解析responseCode:
//        final String responseCode = "xxxx"; //callbackUrl中拿到的responseCode
        final String secretKey = "9104D293BC764DBA8DFBC0E169753244BZY8YW5A";
        TUSIMfaWebClient.ResponseVerifier verifier = new TUSIMfaWebClient.ResponseVerifier()
                .code(code)
                .secretKey(secretKey);
        String userId = verifier.verify(); //认证成功，则返回userId
        if (userId.equals("tre")){
        return "success";
        }
        return "error";
    }
}
