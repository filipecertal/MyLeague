/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague.controllers;

import com.myleague.models.Equipa;
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
 * @author Filipe Certal <filipe.certal@gmail.com>
 */
public class EquipasController {
    
    public static void index(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("EquipasController.index()");

            Template t = config.getTemplate("equipas/index.ftl.html");

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
    
    public static void edit(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("EquipasController.edit()");

            var equipa_id = context.request().getParam("equipa");

            int id = Integer.parseInt( equipa_id );

            Equipa equipa = Equipa.find(id);
         
            if (equipa == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("equipas/edit.ftl.html");

            Map root = new HashMap();
            
            root.put("equipa", equipa.toString());
            root.put("modalidades", Modalidade.allJSON());
            root.put("jogadores", equipa.getJogadoresJSON());
            
            
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
            System.out.println("EquipasController.update()");
            
            var equipa_id = context.request().getParam("equipa");

            int id = Integer.parseInt( equipa_id );

            Equipa equipa = Equipa.find(id);
            
            if (equipa == null) {

                context.response()
                .setStatusCode(404)
                .end();

                return;
            }
            
            MultiMap params = context.request().params();
              
            String nome = params.get("nome");
            String treinador = params.get("treinador");
            int modalidade = Integer.parseInt( params.get("modalidade") );
            
            boolean errors = false;
            List<String> display_errors = new ArrayList<>();
            
            if (nome.length() <= 3) {
                
                display_errors.add("\"nome\" : {\"mensagem\" : \"O nome da equipa deve conter no mínimo 4 carateres.\", \"valor\" : \"" + nome + "\"}");
                errors = true;
            }
            
            if (treinador.length() <= 3) {
            
                 display_errors.add("\"treinador\" : {\"mensagem\" : \"O nome do treinador deve conter no mínimo 4 carateres.\", \"valor\" : \"" + treinador + "\"}");
                 errors = true;
            }
            
            if (Modalidade.find(modalidade) == null) {
            
                display_errors.add("\"modalidade\": {\"mensagem\" : \"A modalidade selecionada não existe.\", \"valor\" : \"" + equipa.getModalidade().id() + "\" }");
                errors = true;
            }
            
            if (errors) {
                
                Map root = new HashMap();
                
                root.put("display_errors", "{ " + String.join(", ", display_errors) + " }");
                root.put("equipa", equipa.toString());
                root.put("modalidades", Modalidade.allJSON());
                
                Template t = config.getTemplate("equipas/edit.ftl.html");
                 
                Writer out = new OutputStreamWriter(System.out);
                StringWriter writer = new StringWriter();
                t.process(root, writer);

                context.response().putHeader("conten-type", "text/html");
                context.response().end(writer.toString());
                
            } else {
                
                equipa.setNome(nome);
                equipa.setTreinador(treinador);
                equipa.setModalidade(modalidade);
                equipa.save();
                
                context.response()
                  .setStatusCode(303)
                  .putHeader("location", "/equipas" + "?updated=" + equipa.getNome())
                  .end();
                
            }     

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    public static void create(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("EquipasController.create()");

            Template t = config.getTemplate("equipas/create.ftl.html");

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
    
    
    public static void store(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("EquipasController.store()");
            
            String nome = context.request().params().get("nome");
            String treinador = context.request().params().get("treinador");
            int modalidade = Integer.parseInt( context.request().params().get("modalidade") );
            
            boolean errors = false;
            List<String> display_errors = new ArrayList<>();
            
            if (nome.length() <= 3) {
                
                display_errors.add("{\"nome\" : {\"mensagem\" : \"O nome da equipa deve conter no mínimo 4 carateres.\", \"valor\" : \"" + nome + "\"} }");
            }
            
            if (treinador.length() <= 3) {
            
                 display_errors.add("{\"treinador\" : {\"mensagem\" : \"O nome do treinador deve conter no mínimo 4 carateres.\", \"valor\" : \"" + treinador + "\"} }");
            }
            
                
            if (errors) {
                
                Map root = new HashMap();
                
                root.put("display_errors", "[ " + String.join(", ", display_errors) + " ]");
                
                Template t = config.getTemplate("equipas/create.ftl.html");
                 
                Writer out = new OutputStreamWriter(System.out);
                StringWriter writer = new StringWriter();
                t.process(root, writer);

                context.response().putHeader("conten-type", "text/html");
                context.response().end(writer.toString());
                
            } else {
                
                Equipa equipa = new Equipa(nome, treinador, Modalidade.find( modalidade ));
                
                context.response()
                  .setStatusCode(303)
                  .putHeader("location", "/equipas" + "?created=" + equipa.getNome())
                  .end();
                
            }     

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    public static void show(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("EquipasController.show()");
            
            
            var equipa_id = context.request().getParam("equipa");

            int id = Integer.parseInt( equipa_id );

            Equipa equipa = Equipa.find(id);

            if (equipa == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("equipas/show.ftl.html");

            Map root = new HashMap();
            
            root.put("equipa", equipa.toString());
            root.put("jogadores", equipa.getJogadoresJSON());
            
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
            System.out.println("EquipasController.delete()");
            
            
            var equipa_id = context.request().getParam("equipa");

            int id = Integer.parseInt( equipa_id );

            Equipa equipa = Equipa.find(id);

            if (equipa == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("equipas/delete.ftl.html");

            Map root = new HashMap();
            
            root.put("equipa", equipa.toString());
            
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
            System.out.println("EquipasController.destroy()");
            
            
            var equipa_id = context.request().getParam("equipa");

            int id = Integer.parseInt( equipa_id );

             Equipa equipa = Equipa.find(id);

            if (equipa == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }
            
            if (equipa.getJogadores().size() > 0) {
                
                context.response()
                    .setStatusCode(303)
                    .putHeader("location", "/equipas" + "?error=A equipa " + equipa.getNome() + " ainda contem jogadores.")
                    .end();
                
            } else {
                
                equipa.delete();

                context.response()
                    .setStatusCode(303)
                    .putHeader("location", "/equipas" + "?deleted=" + equipa.getNome())
                    .end();
                
            }
                     



        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    
}
