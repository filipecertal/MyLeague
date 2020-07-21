/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague.controllers;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.vertx.ext.web.RoutingContext;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Filipe Certal <filipe.certal@gmail.com>
 */
public class GruposController {
    
    public static void index(RoutingContext context, Configuration config) {

        try {

            Template t = config.getTemplate("grupos/index.ftl");

            Map root = new HashMap();
            Writer out = new OutputStreamWriter(System.out);
            StringWriter w = new StringWriter();
            t.process(root, w);

            System.out.println(w.toString());

            context.response().putHeader("conten-type", "text/html");
            context.response().end(w.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
      }
    
}
