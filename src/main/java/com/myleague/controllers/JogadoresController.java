/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague.controllers;

import com.myleague.models.Equipa;
import com.myleague.models.Jogador;
import com.myleague.models.Modalidade;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Maria Carlão <maria.clcarlao@gmail.com>
 */
public class JogadoresController {
    
      public static void index(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("JogadoresController.index()");

            Template t = config.getTemplate("jogadores/index.ftl.html");

            Map root = new HashMap();
            
            root.put("jogadores", Jogador.allJSON());
                     
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
            System.out.println("JogadoresController.edit()");

            var jogador_id = context.request().getParam("jogador");

            int id = Integer.parseInt( jogador_id );

            Jogador jogador = Jogador.find(id);
         
            if (jogador == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("jogadores/edit.ftl.html");

            Map root = new HashMap();
            
            root.put("jogador", jogador.toString());
            root.put("equipas", Equipa.allJSON());
            
            Writer out = new OutputStreamWriter(System.out);
            StringWriter writer = new StringWriter();
            t.process(root, writer);


            context.response().putHeader("conten-type", "text/html");
            context.response().end(writer.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
        public static void update(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("JogadoresController.update()");
            
            var jogador_id = context.request().getParam("jogador");

            int id = Integer.parseInt( jogador_id );

            Jogador jogador = Jogador.find(id);
            
            if (jogador == null) {

                context.response()
                .setStatusCode(404)
                .end();

                return;
            }
            
            MultiMap params = context.request().params();
              
            String nome = params.get("nome");
            int equipa = Integer.parseInt( params.get("equipa") );
            
            boolean errors = false;
            List<String> display_errors = new ArrayList<>();
            
            if (nome.length() <= 3) {
                
                display_errors.add("\"nome\" : {\"mensagem\" : \"O nome do jogador deve conter no mínimo 4 carateres.\", \"valor\" : \"" + nome + "\"}");
                errors = true;
            }
            
            
            if (Equipa.find(equipa) == null) {
            
                display_errors.add("\"equipa\": {\"mensagem\" : \"A equipa selecionada não existe.\", \"valor\" : \"" + jogador.getEquipa().id() + "\" }");
                errors = true;
            }
            
            if (errors) {
                
                Map root = new HashMap();
                
                root.put("display_errors", "{ " + String.join(", ", display_errors) + " }");
                root.put("equipas", Equipa.allJSON());
                
                Template t = config.getTemplate("jogadores/edit.ftl.html");
                 
                Writer out = new OutputStreamWriter(System.out);
                StringWriter writer = new StringWriter();
                t.process(root, writer);

                context.response().putHeader("conten-type", "text/html");
                context.response().end(writer.toString());
                
            } else {
                
                jogador.setNome(nome);
                jogador.setEquipa(equipa);
                jogador.save();
                
                context.response()
                  .setStatusCode(303)
                  .putHeader("location", "/jogadores" + "?updated=" + jogador.getNome())
                  .end();
                
            }     

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    public static void create(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("JogadoresController.create()");

            Template t = config.getTemplate("jogadores/create.ftl.html");

            Map root = new HashMap();
            
            root.put("equipas", Equipa.allJSON());
            
           
            
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
            System.out.println("JogadoresController.store()");
            
            String nome = context.request().params().get("nome");
            int equipa = Integer.parseInt( context.request().params().get("equipa") );
            
            boolean errors = false;
            List<String> display_errors = new ArrayList<>();
            
            if (nome.length() <= 3) {
                
                display_errors.add("{\"nome\" : {\"mensagem\" : \"O nome do jogador deve conter no mínimo 4 carateres.\", \"valor\" : \"" + nome + "\"} }");
                errors = true;
            }
            
                
            if (errors) {
                
                Map root = new HashMap();
                
                root.put("display_errors", "[ " + String.join(", ", display_errors) + " ]");
                
                Template t = config.getTemplate("jogadores/create.ftl.html");
                 
                Writer out = new OutputStreamWriter(System.out);
                StringWriter writer = new StringWriter();
                t.process(root, writer);

                context.response().putHeader("conten-type", "text/html");
                context.response().end(writer.toString());
                
            } else {
                
                Jogador jogador = new Jogador(nome, Equipa.find( equipa ));
                
                context.response()
                  .setStatusCode(303)
                  .putHeader("location", "/jogadores" + "?created=" + jogador.getNome())
                  .end();
                
            }     

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    public static void show(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("JogadoresController.show()");
            
            
            var jogador_id = context.request().getParam("jogador");

            int id = Integer.parseInt( jogador_id );

            Jogador jogador = Jogador.find(id);

            if (jogador == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("jogadores/show.ftl.html");

            Map root = new HashMap();
            
            root.put("jogador", jogador.toString());
            
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
            System.out.println("JogadoresController.delete()");
            
            
            var jogador_id = context.request().getParam("jogador");

            int id = Integer.parseInt( jogador_id );

            Jogador jogador = Jogador.find(id);

            if (jogador == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("jogadores/delete.ftl.html");

            Map root = new HashMap();
            
            root.put("jogador", jogador.toString());
            
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
            System.out.println("JogadoresController.destroy()");
            
            
            var jogador_id = context.request().getParam("jogador");

            int id = Integer.parseInt( jogador_id );

             Jogador jogador = Jogador.find(id);

            if (jogador == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }
            

            jogador.delete();

            context.response()
                .setStatusCode(303)
                .putHeader("location", "/jogadores" + "?deleted=" + jogador.getNome())
                .end();

                     



        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    
}
