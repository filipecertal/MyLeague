/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague.controllers;

import com.myleague.models.Competicao;
import com.myleague.models.Competicao.Tipo;
import com.myleague.models.Equipa;
import com.myleague.models.Modalidade;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Maria Carlão <maria.clcarlao@gmail.com>
 */
public class CompeticoesController {
    
      public static void index(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("CompeticoesController.index()");

            Template t = config.getTemplate("competicoes/index.ftl.html");

            Map root = new HashMap();
            
            root.put("competicoes", Competicao.allJSON());
            
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
            System.out.println("CompeticoesController.edit()");

            var competicao_id = context.request().getParam("competicao");

            int id = Integer.parseInt( competicao_id );

            Competicao competicao = Competicao.find(id);
         
            if (competicao == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("competicoes/edit.ftl.html");

            Map root = new HashMap();
            
            root.put("modalidades", Modalidade.allJSON());
            root.put("competicao", competicao);
            root.put("equipas", competicao.getEquipasJSON());
            
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
            System.out.println("CompeticoesController.update()");
            
            var competicao_id = context.request().getParam("competicao");

            int id = Integer.parseInt( competicao_id );

            Competicao competicao = Competicao.find(id);
            
            if (competicao == null) {

                context.response()
                .setStatusCode(404)
                .end();

                return;
            }
            
                       
            String nome = context.request().params().get("nome");
            Tipo tipo = Tipo.values()[ Integer.parseInt( context.request().params().get("tipo") ) ];
            String recinto = context.request().params().get("recinto");
            int modalidade = Integer.parseInt( context.request().params().get("modalidade") );
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date d = sdf.parse( context.request().params().get("inicio") );
            java.sql.Date inicio = new java.sql.Date(d.getTime());
            
            
            boolean errors = false;
            List<String> display_errors = new ArrayList<>();
            
            if (nome.length() <= 3) {
                
                display_errors.add("{\"nome\" : {\"mensagem\" : \"O nome da equipa deve conter no mínimo 4 carateres.\", \"valor\" : \"" + nome + "\"} }");
                errors = true;
            }
            
            if (recinto.length() <= 3) {
            
                 display_errors.add("{\"recinto\" : {\"mensagem\" : \"O nome do recindo deve conter no mínimo 4 carateres.\", \"valor\" : \"" + recinto + "\"} }");
                 errors = true;
            }
            
                
            if (errors) {
                
                Map root = new HashMap();
                
                root.put("display_errors", "[ " + String.join(", ", display_errors) + " ]");
                
                Template t = config.getTemplate("competicoes/create.ftl.html");
                 
                Writer out = new OutputStreamWriter(System.out);
                StringWriter writer = new StringWriter();
                t.process(root, writer);

                context.response().putHeader("conten-type", "text/html");
                context.response().end(writer.toString());
                
            } else {
                
               competicao.setNome(nome);
               competicao.setTipoCompeticao(tipo);
               competicao.setModalidade(modalidade);
               competicao.setDataInicio(inicio);
               competicao.save();
                
                
                context.response()
                  .setStatusCode(303)
                  .putHeader("location", "/competicoes" + "?updated=" + competicao.getNome())
                  .end();
                
            }     


        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    public static void create(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("CompeticoesController.create()");

            Template t = config.getTemplate("competicoes/create.ftl.html");

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
            System.out.println("CompeticoesController.store()");
            
            String nome = context.request().params().get("nome");
            Tipo tipo = Tipo.values()[ Integer.parseInt( context.request().params().get("tipo") ) ];
            String recinto = context.request().params().get("recinto");
            int modalidade = Integer.parseInt( context.request().params().get("modalidade") );
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date d = sdf.parse( context.request().params().get("inicio") );
            java.sql.Date inicio = new java.sql.Date(d.getTime());
            
            boolean errors = false;
            List<String> display_errors = new ArrayList<>();
            
            if (nome.length() <= 3) {
                
                display_errors.add("{\"nome\" : {\"mensagem\" : \"O nome da equipa deve conter no mínimo 4 carateres.\", \"valor\" : \"" + nome + "\"} }");
                errors = true;
            }
            
            if (recinto.length() <= 3) {
            
                 display_errors.add("{\"recinto\" : {\"mensagem\" : \"O nome do recindo deve conter no mínimo 4 carateres.\", \"valor\" : \"" + recinto + "\"} }");
                 errors = true;
            }
            
                
            if (errors) {
                
                Map root = new HashMap();
                
                root.put("display_errors", "[ " + String.join(", ", display_errors) + " ]");
                
                Template t = config.getTemplate("competicoes/create.ftl.html");
                 
                Writer out = new OutputStreamWriter(System.out);
                StringWriter writer = new StringWriter();
                t.process(root, writer);

                context.response().putHeader("conten-type", "text/html");
                context.response().end(writer.toString());
                
            } else {
                
                Competicao competicao = new Competicao(nome, tipo, recinto, Modalidade.find( modalidade ), inicio);
                
                context.response()
                  .setStatusCode(303)
                  .putHeader("location", "/competicoes" + "?created=" + competicao.getNome())
                  .end();
                
            }     

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    public static void show(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("CompeticoesController.show()");
            
            
            var competicao_id = context.request().getParam("competicao");

            int id = Integer.parseInt( competicao_id );

            Competicao competicao = Competicao.find(id);

            if (competicao == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("competicoes/show.ftl.html");

            Map root = new HashMap();
            
            root.put("competicao", competicao);
            root.put("equipas", competicao.getEquipasJSON());
            
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
            System.out.println("CompeticoesController.delete()");
            
            
            var competicao_id = context.request().getParam("competicao");

            int id = Integer.parseInt( competicao_id );

            Competicao competicao = Competicao.find(id);

            if (competicao == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("competicoes/delete.ftl.html");

            Map root = new HashMap();
            
            root.put("competicao", competicao.toString());
            
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
            System.out.println("CompeticoesController.destroy()");
            
            
            var competicao_id = context.request().getParam("competicao");

            int id = Integer.parseInt( competicao_id );

             Competicao competicao = Competicao.find(id);

            if (competicao == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

                
            competicao.delete();

            context.response()
                .setStatusCode(303)
                .putHeader("location", "/competicoes" + "?deleted=" + competicao.getNome())
                .end();



        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    public static void addEquipa(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("CompeticoesController.addEquipa()");
            
                 
            var competicao_id = context.request().getParam("competicao");

            int id = Integer.parseInt( competicao_id );

            Competicao competicao = Competicao.find(id);
          
            Template t = config.getTemplate("competicoes/addEquipa.ftl.html");

            Map root = new HashMap();
         
            root.put("competicao", competicao);
            root.put("equipas", competicao.getModalidade().getEquipasJson());
            
            Writer out = new OutputStreamWriter(System.out);
            StringWriter writer = new StringWriter();
            t.process(root, writer);


            context.response().putHeader("conten-type", "text/html");
            context.response().end(writer.toString());



        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
     public static void storeEquipa(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("CompeticoesController.storeEquipa()");
            
            var competicao_id = context.request().getParam("competicao");

            int id = Integer.parseInt( competicao_id );

            Competicao competicao = Competicao.find(id);
            
            int equipa_id = Integer.parseInt( context.request().params().get("equipa") );
            
            Equipa equipa = Equipa.find(equipa_id);
            
            
            
            if (competicao == null || equipa == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

           equipa.setCompeticao(competicao);
           equipa.save();
                
            context.response()
              .setStatusCode(303)
              .putHeader("location", "/competicoes/"+ competicao.id() + "?created=" + competicao.getNome())
              .end();
                
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
     
     
    public static void deleteEquipa(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("CompeticoesController.delete()");
            
            
            var competicao_id = context.request().getParam("competicao");
            Competicao competicao = Competicao.find(Integer.parseInt( competicao_id ));
                    
            var equipa_id = context.request().getParam("equipa");
            Equipa equipa = Equipa.find( Integer.parseInt( equipa_id ));

            if (competicao == null || equipa == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            Template t = config.getTemplate("competicoes/deleteEquipa.ftl.html");

            Map root = new HashMap();
            
            root.put("competicao", competicao.toString());
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
    
    public static void destroyEquipa(RoutingContext context, Configuration config) {
     
        try {
            System.out.println("CompeticoesController.destroyEquipa()");
            
            
            var competicao_id = context.request().getParam("competicao");
            Competicao competicao = Competicao.find(Integer.parseInt( competicao_id ));
                    
            var equipa_id = context.request().getParam("equipa");
            Equipa equipa = Equipa.find( Integer.parseInt( equipa_id ));


            if (competicao == null || equipa == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

                
            
            System.out.println(equipa.getCompeticao());
            equipa.removerCompeticao();
            System.out.println(equipa.getCompeticao());
            equipa.save();

            context.response()
                .setStatusCode(303)
                .putHeader("location", "/competicoes/" + competicao.id() + "?removed=" + competicao.getNome())
                .end();



        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void vencedor(RoutingContext context, Configuration config) {
      
        
        try {
            System.out.println("CompeticoesController.removeVencedora()");
            
            
            var competicao_id = context.request().getParam("competicao");
            Competicao competicao = Competicao.find(Integer.parseInt( competicao_id ));
                    
            var equipa_id = context.request().getParam("equipa");
            Equipa equipa = Equipa.find( Integer.parseInt( equipa_id ));


            if (competicao == null || equipa == null) {
                
                context.response()
                    .setStatusCode(404)
                    .end();
                
                return;
            }

            if (competicao.getVencedora() == null) {
                
                competicao.setVencedora(equipa);
                
                
            } else {
                
                competicao.removeVencedora();
                
                
            }
            
            competicao.save();
                
            


            context.response()
                .setStatusCode(303)
                .putHeader("location", "/competicoes/" + competicao.id() )
                .end();



        } catch (Exception e) {

            e.printStackTrace();
        }
        
    }
    
  
    
    
}
