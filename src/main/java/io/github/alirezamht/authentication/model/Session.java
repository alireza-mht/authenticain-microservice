package io.github.alirezamht.authentication.model;

import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(nullable = false,updatable = false)
    private Long createTime;
    @Column(nullable = false)
    private Long expireTime;
    @Column(nullable = false)
    private String sessionString;
    @Column(nullable = false)
    private String userIdentifier;
    @Column(nullable = false)
    private String scope;

    public Session(Long createTime, Long expireTime, String sessionString, String userIdentifier,String scope) {
        this.createTime = createTime;
        this.expireTime = expireTime;
        this.sessionString = sessionString;
        this.userIdentifier = userIdentifier;
        this.scope=scope;
    }

    public Session() {
    }

    public Long getId() {
        return id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public String getSessionString() {
        return sessionString;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getScope() {
        return scope;
    }

    public JSONObject getJson(){
        JSONObject object=new JSONObject();
        //object.put("id",id);
        object.put("createTime",createTime);
        object.put("expireTime",expireTime);
        object.put("userIdentifier",userIdentifier);
        object.put("sessionString",sessionString);
        object.put("scope",scope);
        return object;
    }

    public static Session getModel(JSONObject object){
        //Long id= (Long) object.get("id");
        Long createTime= (Long) object.get("createTime");
        Long expireTime= (Long) object.get("expireTime");
        String sessionString= (String) object.get("sessionString");
        String userIdentifier= (String) object.get("userIdentifier");
        String scope= (String) object.get("scope");

        Session session=new Session(createTime,expireTime,sessionString,userIdentifier,scope);
        //session.id=id;

        return session;
    }
}
