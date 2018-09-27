package com.icuge.jrebel.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author Elias 2018/09/27 11:39
 */
@Controller
public class IndexController {

    @Value("${server.port}")
    private String port;

    @GetMapping("")
    public void index(HttpServletResponse resp, HttpServletRequest request) throws Exception {
        resp.setContentType("text/html; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        String html = "<h1>Hello,This is a Jrebel & JetBrains License Server!</h1>";
        html += "<p>License Server started at http://jrebel.icuge.com";
        html += "<p>JetBrains Activation address was: <span style='color:red'>http://idea.icuge.com";
        html += "<p>JRebel 7.1 and earlier version Activation address was: <span style='color:red'>http://jrebel.icuge.com/{tokenname}</span>, with any email.";
        html += "<p>JRebel 2018.1 and later version Activation address was: http://jrebel.icuge.com/{guid}(eg:<span style='color:red'>http://jrebel.icuge.com/"+ UUID.randomUUID().toString()+"</span>), with any email.";
        html += "<p>Any problem, email to elias@icuge.com<p>";

        resp.getWriter().println(html);
    }
}
