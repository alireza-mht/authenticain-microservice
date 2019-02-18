package io.github.alirezamht.authentication.controller;

import io.github.alirezamht.authentication.model.Session;
import io.github.alirezamht.authentication.service.SessionService;
import io.github.alirezamht.authentication.util.CryptoHelper;
import io.github.alirezamht.authentication.util.ResponseFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Controller
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping(value = "authenticate",params = {"username","session"})
    public @ResponseBody JSONObject authenticate(@RequestParam("username") String userName
            ,@RequestParam("session") String session, HttpServletResponse response){

        try {
            String raw=CryptoHelper.decrypt(session);
            JSONObject object= (JSONObject) new JSONParser().parse(raw);
            Session s=Session.getModel(object);
            if(!s.getUserIdentifier().equals(userName)){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return ResponseFactory.getErrorResponse(HttpStatus.UNAUTHORIZED.value()
                ,"invalid session"
                ,"user identifier {"+userName+"} is not matches with session"
                ,"/authenticate");
            }else {
                Session savedSession=sessionService.getSessionBySS(s.getSessionString());
                ArrayList<Session> userSessions= (ArrayList<Session>) sessionService.getSessionByUserIdentifier(userName);
                userSessions.forEach(item -> {
                    if(item.getExpireTime()<System.currentTimeMillis()){
                        sessionService.delete(item);
                    }
                });
                if(savedSession==null){
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return ResponseFactory.getErrorResponse(HttpStatus.UNAUTHORIZED.value()
                            ,"session expired"
                            ,"user session is not valid!need login to access."
                            ,"/authenticate");
                }else if(!s.getUserIdentifier().equals(savedSession.getUserIdentifier())){
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return ResponseFactory.getErrorResponse(HttpStatus.UNAUTHORIZED.value()
                            ,"invalid session"
                            ,"user identifier {"+userName+"} is not matches with session"
                            ,"/authenticate");
                }else if(savedSession.getExpireTime()<System.currentTimeMillis()){
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    sessionService.delete(savedSession);
                    return ResponseFactory.getErrorResponse(HttpStatus.UNAUTHORIZED.value()
                            ,"session expired"
                            ,"user session is not valid!need login to access."
                            ,"/authenticate");
                }else {
                    response.setStatus(HttpStatus.OK.value());
                    JSONObject result=new JSONObject();
                    savedSession.setExpireTime(System.currentTimeMillis()+(10*60*1000));
                    sessionService.update(savedSession);
                    result.put("message","user session is valid and refreshed.");
                    return ResponseFactory.getSuccessResponse(HttpStatus.OK.value()
                    ,result);
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseFactory.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value()
                    ,"internal server error"
                    ,e.getMessage()
                    ,"/authenticate");
        }



    }

}
