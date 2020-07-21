package com.myleague;

import com.myleague.controllers.CompeticoesController;
import com.myleague.controllers.EquipasController;
import com.myleague.controllers.HomepageController;
import com.myleague.controllers.JogadoresController;
import com.myleague.controllers.ModalidadesController;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.io.File;
import java.io.IOException;


/**
 *
 * @author Filipe Certal <filipe.certal@gmail.com>
 */
public class Server {
    
    public static void main(String argv[]) throws IOException {
        
        Vertx vertx = Vertx.vertx();
        
        // Criar um servidor HTTP
        HttpServer server = vertx.createHttpServer();
        
        final Router router = Router.router(vertx);
        
        router.errorHandler(500, rc -> {
            System.err.println("Handling failure");
            Throwable failure = rc.failure();
            if (failure != null) {
                failure.printStackTrace();
            }
        });
        
        //https://freemarker.apache.org/docs/pgui_quickstart_createconfiguration.html
        Configuration config = new Configuration(Configuration.getVersion());
      
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        config.setLogTemplateExceptions(false);
        config.setWrapUncheckedExceptions(true);
       
        
        TemplateLoader templateLoader = new FileTemplateLoader(new File("src/main/resources/templates"));
        config.setTemplateLoader(templateLoader);
        
        // HOMEPAGE
        router.route(HttpMethod.GET, "/").handler(
                
                context -> { HomepageController.index(context, config); }
        );
            
        
        // MODALIDADES
        
        router.route("/modalidades*").handler(BodyHandler.create());
        
        router.route(HttpMethod.POST, "/modalidades/add").handler(
                
                context -> { ModalidadesController.store(context, config); }
        );
        
        router.route(HttpMethod.GET, "/modalidades/add").handler(
                
                context -> { ModalidadesController.create(context, config); }
        );
        
        router.route(HttpMethod.GET, "/modalidades/:modalidade").handler(
        
                context -> { ModalidadesController.show(context, config); }
        );
        
        router.route(HttpMethod.GET, "/modalidades/:modalidade/edit").handler(
        
                context -> { ModalidadesController.edit(context, config); }
        );
        
        router.route(HttpMethod.POST, "/modalidades/:modalidade/edit").handler(
        
                context -> { ModalidadesController.update(context, config); }
        );
        
        router.route(HttpMethod.GET, "/modalidades").handler(
                
                context -> { ModalidadesController.index(context, config); }
        );
        
        router.route(HttpMethod.GET, "/modalidades/:modalidade/delete").handler(
        
                context -> { ModalidadesController.delete(context, config); }
        );
        
        router.route(HttpMethod.POST, "/modalidades/:modalidade/delete").handler(
        
                context -> { ModalidadesController.destroy(context, config); }
        );
        
        
        // EQUIPAS
        
        router.route("/equipas*").handler(BodyHandler.create());
        
        router.route(HttpMethod.POST, "/equipas/add").handler(
                
                context -> { EquipasController.store(context, config); }
        );
        
        router.route(HttpMethod.GET, "/equipas/add").handler(
                
                context -> { EquipasController.create(context, config); }
        );
        
        router.route(HttpMethod.GET, "/equipas/:equipa").handler(
        
                context -> { EquipasController.show(context, config); }
        );
        
        router.route(HttpMethod.GET, "/equipas/:equipa/edit").handler(
        
                context -> { EquipasController.edit(context, config); }
        );
        
        router.route(HttpMethod.POST, "/equipas/:equipa/edit").handler(
        
                context -> { EquipasController.update(context, config); }
        );
        
        router.route(HttpMethod.GET, "/equipas").handler(
                
                context -> { EquipasController.index(context, config); }
        );
        
        router.route(HttpMethod.GET, "/equipas/:equipa/delete").handler(
        
                context -> { EquipasController.delete(context, config); }
        );
        
        router.route(HttpMethod.POST, "/equipas/:equipa/delete").handler(
        
                context -> { EquipasController.destroy(context, config); }
        );
        
        
        // JOGADORES
        
        router.route("/jogadores*").handler(BodyHandler.create());
        
        router.route(HttpMethod.POST, "/jogadores/add").handler(
                
                context -> { JogadoresController.store(context, config); }
        );
        
        router.route(HttpMethod.GET, "/jogadores/add").handler(
                
                context -> { JogadoresController.create(context, config); }
        );
        
        router.route(HttpMethod.GET, "/jogadores/:jogador").handler(
        
                context -> { JogadoresController.show(context, config); }
        );
        
        router.route(HttpMethod.GET, "/jogadores/:jogador/edit").handler(
        
                context -> { JogadoresController.edit(context, config); }
        );
        
        router.route(HttpMethod.POST, "/jogadores/:jogador/edit").handler(
        
                context -> { JogadoresController.update(context, config); }
        );
        
        router.route(HttpMethod.GET, "/jogadores").handler(
                
                context -> { JogadoresController.index(context, config); }
        );
        
        router.route(HttpMethod.GET, "/jogadores/:jogador/delete").handler(
        
                context -> { JogadoresController.delete(context, config); }
        );
        
        router.route(HttpMethod.POST, "/jogadores/:jogador/delete").handler(
        
                context -> { JogadoresController.destroy(context, config); }
        );
        
        
        // COMPETIÇÕES
        
        router.route("/competicoes*").handler(BodyHandler.create());
        
        router.route(HttpMethod.POST, "/competicoes/add").handler(
                
                context -> { CompeticoesController.store(context, config); }
        );
        
        router.route(HttpMethod.GET, "/competicoes/add").handler(
                
                context -> { CompeticoesController.create(context, config); }
        );
        
        router.route(HttpMethod.GET, "/competicoes/:competicao").handler(
        
                context -> { CompeticoesController.show(context, config); }
        );
        
        router.route(HttpMethod.GET, "/competicoes/:competicao/edit").handler(
        
                context -> { CompeticoesController.edit(context, config); }
        );
        
        router.route(HttpMethod.POST, "/competicoes/:competicao/edit").handler(
        
                context -> { CompeticoesController.update(context, config); }
        );
        
        router.route(HttpMethod.GET, "/competicoes").handler(
                
                context -> { CompeticoesController.index(context, config); }
        );
        
        router.route(HttpMethod.GET, "/competicoes/:competicao/delete").handler(
        
                context -> { CompeticoesController.delete(context, config); }
        );
        
        router.route(HttpMethod.POST, "/competicoes/:competicao/delete").handler(
        
                context -> { CompeticoesController.destroy(context, config); }
        );
        
                
        router.route(HttpMethod.GET, "/competicoes/:competicao/addEquipa").handler(
        
                context -> { CompeticoesController.addEquipa(context, config); }
        );
        
        router.route(HttpMethod.POST, "/competicoes/:competicao/addEquipa").handler(
        
                context -> { CompeticoesController.storeEquipa(context, config); }
        );
        
        
        router.route(HttpMethod.GET, "/competicoes/:competicao/deleteEquipa/:equipa").handler(
        
                context -> { CompeticoesController.deleteEquipa(context, config); }
        );
        
        
        router.route(HttpMethod.POST, "/competicoes/:competicao/deleteEquipa/:equipa").handler(
        
                context -> { CompeticoesController.destroyEquipa(context, config); }
        );
        
        router.route(HttpMethod.GET, "/competicoes/:competicao/vencedor/:equipa").handler(
        
                context -> { CompeticoesController.vencedor(context, config); }
        );
        
           
        // Acesso a recursos estáticos.
        router.route("/*").handler(StaticHandler.create("webroot"));
        
                
        server.requestHandler(router).listen(8080);
    }
}
