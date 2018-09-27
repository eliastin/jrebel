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

        int port = request.getServerPort();
        String html = "<h1>Hello,This is a Jrebel & JetBrains License Server!</h1>";
        html += "<p>License Server started at http://localhost:" + port;
        html += "<p>JetBrains Activation address was: <span style='color:red'>http://localhost:" + port + "/";
        html += "<p>JRebel 7.1 and earlier version Activation address was: <span style='color:red'>http://localhost:" + port + "/{tokenname}</span>, with any email.";
        html += "<p>JRebel 2018.1 and later version Activation address was: http://localhost:" + port + "/{guid}(eg:<span style='color:red'>http://localhost:" + port + "/"+ UUID.randomUUID().toString()+"</span>), with any email.";

        resp.getWriter().println(html);
    }
}
