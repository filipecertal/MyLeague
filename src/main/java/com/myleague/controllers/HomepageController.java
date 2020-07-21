/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague.controllers;

import com.myleague.models.Competicao;
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
 * @author Maria Carlão <maria.clcarlao@gmail.com>
 */
public class HomepageController {
    
    public static void index(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("HomepageController.index()");

            Template t = config.getTemplate("index.ftl.html");

            Map root = new HashMap();
            
            root.put("adecorrer", Competicao.allADecorrerJSON());
            root.put("terminadas", Competicao.allTerminadaJSON());
            
            Writer out = new OutputStreamWriter(System.out);
            StringWriter writer = new StringWriter();
            t.process(root, writer);


            context.response().putHeader("conten-type", "text/html");
            context.response().end(writer.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
}
