/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague.controllers;

import com.myleague.models.Competicao;
import com.myleague.models.Modalidade;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maria Carlão <maria.clcarlao@gmail.com>
 */
public class ModalidadesController {
    
    public static void index(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("ModalidadesController.index()");

            Template t = config.getTemplate("modalidades/index.ftl.html");

            Map root = new HashMap();
            
            root.put("modalidades", Modalidade.allJSON());
            
            Writer out = new OutputStreamWriter(System.out);
            StringWriter writer = new StringWriter();
            t.process(root, writer);


            context.response().putHeader("conten-type", "text/html");
            context.response().end(writer.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
        public static void edit(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("ModalidadesController.edit()");

            var modalidade_id = context.request().getParam("modalidade");

            int id = Integer.parseInt( modalidade_id );

            Modalidade modalidade = Modalidade.find(id);

            if (modalidade == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("modalidades/edit.ftl.html");

            Map root = new HashMap();
            
            root.put("modalidade", modalidade.toString());
            
            Writer out = new OutputStreamWriter(System.out);
            StringWriter writer = new StringWriter();
            t.process(root, writer);


            context.response().putHeader("conten-type", "text/html");
            context.response().end(writer.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    public static void create(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("ModalidadesController.create()");

            Template t = config.getTemplate("modalidades/create.ftl.html");

            Map root = new HashMap();
            
           
            
            Writer out = new OutputStreamWriter(System.out);
            StringWriter writer = new StringWriter();
            t.process(root, writer);


            context.response().putHeader("conten-type", "text/html");
            context.response().end(writer.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    public static void store(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("ModalidadesController.store()");
            
            String designacao = context.request().params().get("designacao");
            
            if (designacao.length() <= 3) {
                
                Template t = config.getTemplate("modalidades/create.ftl.html");
                
                Map root = new HashMap();
                
                root.put("display_errors", "{'designacao' : {'mensagem' : 'A designação da modalidade deve conter no mínimo 4 carateres.', 'valor' : '" + designacao + "'} }");
                
                Writer out = new OutputStreamWriter(System.out);
                StringWriter writer = new StringWriter();
                t.process(root, writer);

                context.response().putHeader("conten-type", "text/html");
                context.response().end(writer.toString());
                
            } else {
                
                Modalidade modalidade = new Modalidade(designacao);
                
                
                context.response()
                  .setStatusCode(303)
                  .putHeader("location", "/modalidades" + "?created=" + modalidade.getDesignacao())
                  .end();
                
            }     

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    public static void update(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("ModalidadesController.update()");
            
            var modalidade_id = context.request().getParam("modalidade");

            int id = Integer.parseInt( modalidade_id );

            Modalidade modalidade = Modalidade.find(id);
            
            if (modalidade == null) {

                context.response()
                .setStatusCode(404)
                .end();

                return;
            }
            
            
            MultiMap params = context.request().params();
            
            String designacao = params.get("designacao");
            
            if (designacao.length() <= 3) {

               Template t = config.getTemplate("modalidades/edit.ftl.html");

               Map root = new HashMap();

               root.put("modalidade", modalidade.toString());
               root.put("display_errors", "{'designacao' : {'mensagem' : 'A designação da modalidade deve conter no mínimo 4 carateres.', 'valor' : '" + designacao + "'} }");

               Writer out = new OutputStreamWriter(System.out);
               StringWriter writer = new StringWriter();
               t.process(root, writer);

               context.response().putHeader("conten-type", "text/html");
               context.response().end(writer.toString());

            } else {

                modalidade.setDesignacao(designacao);
                modalidade.save();

                context.response()
                    .setStatusCode(303)
                    .putHeader("location", "/modalidades" + "?updated=" + modalidade.getDesignacao())
                    .end();

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    
    public static void show(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("ModalidadesController.show()");
            
            
            var modalidade_id = context.request().getParam("modalidade");

            int id = Integer.parseInt( modalidade_id );

            Modalidade modalidade = Modalidade.find(id);

            if (modalidade == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("modalidades/show.ftl.html");

            Map root = new HashMap();
            
            root.put("modalidade", modalidade.toString());
            
            Writer out = new OutputStreamWriter(System.out);
            StringWriter writer = new StringWriter();
            t.process(root, writer);


            context.response().putHeader("conten-type", "text/html");
            context.response().end(writer.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    public static void delete(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("ModalidadesController.delete()");
            
            
            var modalidade_id = context.request().getParam("modalidade");

            int id = Integer.parseInt( modalidade_id );

            Modalidade modalidade = Modalidade.find(id);

            if (modalidade == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("modalidades/delete.ftl.html");

            Map root = new HashMap();
            
            root.put("modalidade", modalidade.toString());
            
            Writer out = new OutputStreamWriter(System.out);
            StringWriter writer = new StringWriter();
            t.process(root, writer);


            context.response().putHeader("conten-type", "text/html");
            context.response().end(writer.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    public static void destroy(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("ModalidadesController.destroy()");
            
            
            var modalidade_id = context.request().getParam("modalidade");

            int id = Integer.parseInt( modalidade_id );

            Modalidade modalidade = Modalidade.find(id);

            if (modalidade == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }
                     
            modalidade.delete();

            context.response()
            .setStatusCode(303)
            .putHeader("location", "/modalidades" + "?deleted=" + modalidade.getDesignacao())
            .end();


        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
}
