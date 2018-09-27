package com.icuge.jrebel.api;

import com.icuge.jrebel.util.JrebelSign;
import com.icuge.jrebel.util.RSASign;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Elias 2018/09/27 11:39
 */
@RestController
@RequestMapping({"jrebel", "agent"})
public class JrebelController {

    private static final String SERVER_GUID = "a1b4aea8-b031-4302-b602-670a990272cb";

    private static final String SERVER_VERSION = "3.2.4";

    private static final String SERVER_PROTOCOL_VERSION = "1.1";

    @RequestMapping("validate-connection")
    public Map<String, Object> valid() {
        Map<String, Object> map = new HashMap<>();
        map.put("serverVersion", SERVER_VERSION);
        map.put("serverProtocolVersion", SERVER_PROTOCOL_VERSION);
        map.put("serverGuid", SERVER_GUID);
        map.put("groupType", "managed");
        map.put("statusCode", "SUCCESS");
        map.put("company", "Administrator");
        map.put("canGetLease", true);
        map.put("licenseType", 1);
        map.put("evaluationLicense", false);
        map.put("seatPoolType", "standalone");
        return map;
    }

    @RequestMapping("leases/1")
    public Map<String, Object> lease1(String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("serverVersion", SERVER_VERSION);
        map.put("serverProtocolVersion", SERVER_PROTOCOL_VERSION);
        map.put("serverGuid", SERVER_GUID);
        map.put("groupType", "managed");
        map.put("statusCode", "SUCCESS");
        map.put("msg", null);
        map.put("statusMessage", null);
        if (!StringUtils.isEmpty(username)) {
            map.put("company", username);
        }
        return map;
    }

    @RequestMapping("leases")
    public Map<String, Object> lease(HttpServletResponse response, String randomness, String username, String guid, String offline, Long clientTime,
                                     Long offlineDays) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String validFrom = null;
        String validUntil = null;
        boolean ol = Boolean.parseBoolean(offline);
        if (ol) {
            long clinetTimeUntil = clientTime + 180L * 24 * 60 * 60 * 1000;
            validFrom = String.valueOf(clientTime);
            validUntil = String.valueOf(clinetTimeUntil);
        }
        map.put("serverVersion", SERVER_VERSION);
        map.put("serverProtocolVersion", SERVER_PROTOCOL_VERSION);
        map.put("serverGuid", SERVER_GUID);
        map.put("groupType", "managed");
        map.put("id", 1);
        map.put("licenseType", 1);
        map.put("evaluationLicense", false);
        map.put("signature", "OJE9wGg2xncSb+VgnYT+9HGCFaLOk28tneMFhCbpVMKoC/Iq4LuaDKPirBjG4o394/UjCDGgTBpIrzcXNPdVxVr8PnQzpy7ZSToGO8wv/KIWZT9/ba7bDbA8/RZ4B37YkCeXhjaixpmoyz/CIZMnei4q7oWR7DYUOlOcEWDQhiY=");
        map.put("serverRandomness", "H2ulzLlh7E0=");
        map.put("seatPoolType", "standalone");
        map.put("statusCode", "SUCCESS");
        map.put("offline", String.valueOf(ol));
        map.put("validFrom", validFrom);
        map.put("validUntil", validUntil);
        map.put("company", "Administrator");
        map.put("orderId", "");
        map.put("zeroIds", new String[]{""});
        map.put("licenseValidFrom", 1490544001000L);
        map.put("licenseValidUntil", 1691839999000L);
        if (randomness == null || username == null || guid == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } else {
            JrebelSign jrebelSign = new JrebelSign();
            jrebelSign.toLeaseCreateJson(randomness, guid, ol, validFrom, validUntil);
            String signature = jrebelSign.getSignature();
            map.put("signature", signature);
            map.put("company", username);
            return map;
        }
    }

    @RequestMapping("ping.action")
    public void ping(HttpServletResponse response, String salt) throws Exception {
        response.setContentType("text/html; charset=utf-8");
        if (salt==null){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            String xmlContent = "<PingResponse><message></message><responseCode>OK</responseCode><salt>" + salt + "</salt></PingResponse>";
            String xmlSignature = RSASign.Sign(xmlContent);
            String body = "<!-- " + xmlSignature + " -->\n" + xmlContent;
            response.getWriter().print(body);
        }
    }

    @RequestMapping("obtainTicket.action")
    public void ticket(HttpServletResponse response, String salt, String userName) throws Exception {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        SimpleDateFormat fm=new SimpleDateFormat("EEE,d MMM yyyy hh:mm:ss Z", Locale.ENGLISH);
        String date =fm.format(new Date())+" GMT";
        //response.setHeader("Date", date);
        //response.setHeader("Server", "fasthttp");
        String prolongationPeriod = "607875500";
        if (salt == null || userName == null){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            String xmlContent = "<ObtainTicketResponse><message></message><prolongationPeriod>" + prolongationPeriod + "</prolongationPeriod><responseCode>OK</responseCode><salt>" + salt + "</salt><ticketId>1</ticketId><ticketProperties>licensee=" + userName + "\tlicenseType=0\t</ticketProperties></ObtainTicketResponse>";
            String xmlSignature = RSASign.Sign(xmlContent);
            String body = "<!-- " + xmlSignature + " -->\n" + xmlContent;
            response.getWriter().print(body);
        }
    }

    @RequestMapping("releaseTicket.action")
    public void releaseTicket(HttpServletResponse response, String salt) throws Exception {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        if(salt==null){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }else{
            String xmlContent = "<ReleaseTicketResponse><message></message><responseCode>OK</responseCode><salt>" + salt + "</salt></ReleaseTicketResponse>";
            String xmlSignature = RSASign.Sign(xmlContent);
            String body = "<!-- " + xmlSignature + " -->\n" + xmlContent;
            response.getWriter().print(body);
        }
    }
}
