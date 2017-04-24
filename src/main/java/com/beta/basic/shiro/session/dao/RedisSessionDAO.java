package com.beta.basic.shiro.session.dao;

import com.beta.basic.redis.RedisKeyPrefixs;
import com.beta.basic.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.web.util.SavedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yaoyt on 17/4/20.
 *
 * @author yaoyt
 */
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

    private static long SESSION_EXPIRE_SECONDS = 60*60*24*7;

    private static final ThreadLocal<Session> localSession = new ThreadLocal<Session>();


    @Autowired
    private RedisUtil redisUtil;

    /**
     *方法的功能描述:使用redis_shiro_session_的前缀,重新生成sessionId
     *@author yaoyt
     *@time 17/4/21 下午3:21
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = RedisKeyPrefixs.REDIS_SHIRO_SESSION.concat(this.generateSessionId(session).toString());
        /*Serializable sessionId = this.generateSessionId(session);*/
        logger.debug("do Create 方法: 创建session,sessionId:{}",sessionId);
        this.assignSessionId(session,sessionId);
        this.saveSession(session);
        localSession.set(session);
        return sessionId;
    }

    @Override
    protected void doUpdate(Session session) {
        logger.debug("do Update 方法: 更新Session,sessionId:{}",session.getId());
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()){
            return; //如果会话过期/停止 没必要再更新了
        }
        this.saveSession(session);
        localSession.set(session);
    }

    @Override
    protected void doDelete(Session session) {
        logger.debug("do Delete 方法: 删除session,sessionID:{}",session.getId());
        this.delete(session);
        localSession.remove();
    }


    @Override
    protected Session doReadSession(Serializable sessionId) {
        logger.debug("do Read 方法: 读取Session,sessionId:{}",sessionId);
        if(null == sessionId){
            logger.error("session id is null");
            return null;
        }
        Session session = localSession.get();
        if(null == session ){
            session = (Session) this.redisUtil.get(String.valueOf(sessionId));
            localSession.set(session);
        }
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if(!checkUriValidation(session)){
            return;
        }
        logger.debug("update方法: 更新Session,sessionId:{}",session.getId());
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()){
            return; //如果会话过期/停止 没必要再更新了
        }
        this.saveSession(session);
        localSession.set(session);
    }

    /**
     * save session
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException{
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }
        this.redisUtil.setex(session.getId().toString(), session, SESSION_EXPIRE_SECONDS);
        localSession.set(session);
    }


    @Override
    public void delete(Session session) {
        if(null == session || null == session.getId()){
            logger.error("session or session id is null");
            return;
        }
        this.redisUtil.delete(String.valueOf(session.getId()));
    }

    /**
     *方法getActiveSessions的功能描述:统计当前活动的session
     *@author yaoyt
     *@time 17/4/21 上午11:34
     */ 
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();
        Set<String> keys = this.redisUtil.keys(RedisKeyPrefixs.REDIS_SHIRO_SESSION.concat("*"));
        if(null != keys && keys.size() > 0){
            for(String key: keys){
                Session s = (Session) this.redisUtil.get(key);
                sessions.add(s);
            }
        }
        return sessions;
    }

    private boolean checkUriValidation(Session session){
        SimpleSession ss = (SimpleSession)session;
        SavedRequest sq = (SavedRequest)ss.getAttribute("shiroSavedRequest");
        if(null != sq && !StringUtils.isBlank(sq.getRequestURI()) && sq.getRequestURI().indexOf("/index.js") >=0){
            return false;
        }
        return true;
    }

}
