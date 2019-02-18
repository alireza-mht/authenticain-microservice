package io.github.alirezamht.authentication.controller;

import io.github.alirezamht.authentication.model.Session;
import io.github.alirezamht.authentication.model.User;
import io.github.alirezamht.authentication.service.SessionService;
import io.github.alirezamht.authentication.service.UserService;
import io.github.alirezamht.authentication.util.CryptoHelper;
import io.github.alirezamht.authentication.util.ResponseFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Comparator;


@RestController
@RequestMapping("/user")
public class UserAuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;


    @RequestMapping(value = "login" , params = {"username","password"})
    public @ResponseBody JSONObject login(@RequestParam("username") String userName,
                                          @RequestParam("password") String password,
                                          @RequestParam("manager") String manager,
                                          HttpServletResponse response){
       /// response.addHeader("Access-Control-Allow-Origin","*");
       // response.addHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS");
      //  response.addHeader("Access-Control-Allow-Headers","Origin, Content-Type, X-Auth-Token");
        User s= userService.getUser(userName);
        if(s==null){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseFactory.getErrorResponse(HttpStatus.NOT_FOUND.value()
                    ,"username not found"
                    ,"no user exists with entered username"
                    ,"/student/login");

        }else if(!s.getPassword().equals(password)){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return ResponseFactory.getErrorResponse(HttpStatus.UNAUTHORIZED.value()
                    ,"wrong password"
                    ,"login for User {USER}: invalid password".replace("{USER}",userName)
                    ,"/user/login");
        }else {
            if (s.getManager() == Long.parseLong(manager)) {
                response.setStatus(HttpStatus.OK.value());
                JSONObject result = new JSONObject();
                Session session;
                ArrayList<Session> savedSessions = (ArrayList<Session>) sessionService.getSessionByUserIdentifier(userName);
                if (savedSessions != null)
                    savedSessions.sort(Comparator.comparing(Session::getExpireTime));
                Session savedSession = savedSessions != null && savedSessions.size() > 0 ? savedSessions.get(0) : null;

                if (savedSession != null && savedSession.getExpireTime() > System.currentTimeMillis()) {
                    savedSession.setExpireTime(System.currentTimeMillis() + (60 * 10 * 1000));
                    session = savedSession;
                    sessionService.update(session);
                    result.put("status", "refreshed");
                } else {
                    session = getNewSession(userName);
                    sessionService.save(session);
                    result.put("status", "new");
                }
                String ss = session.getJson().toString();
                try {
                    ss = CryptoHelper.encrypt(ss);
                    result.put("session", ss);
                } catch (Exception e) {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return ResponseFactory.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value()
                            , "internal server error"
                            , e.getMessage()
                            , "/authenticate");
                }
                return ResponseFactory.getSuccessResponse(HttpStatus.OK.value(), result);
            }else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return ResponseFactory.getErrorResponse(HttpStatus.UNAUTHORIZED.value()
                        ,"you set the wrong role"
                        ,"login for User {USER}:".replace("{USER}",userName)
                        ,"/user/login");
            }
        }
    }

    @GetMapping("/")
    public String root(){
        return null;
    }


    private Session getNewSession(String userIdentifier){
        Session session=new Session(System.currentTimeMillis()
                ,System.currentTimeMillis()+(10*60*1000)
                ,CryptoHelper.getRandomSession(),userIdentifier,"All");
        return session;
    }
}
