/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague;

import com.myleague.models.Competicao;
import com.myleague.models.Competicao.Tipo;
import com.myleague.models.Equipa;
import com.myleague.models.Jogador;
import com.myleague.models.Modalidade;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Filipe Certal <filipe.certal@gmail.com>
 */
public class MyLeagueTests {
    
    public static void main(String argv[]) {
        
        System.out.println("Testing MyLeague Application...");
        
        // Apagar tabelas
        Jogador.dropTable();
        Equipa.dropTable();
        Competicao.dropTable();
        Modalidade.dropTable();

        // Criar tabelas
        Modalidade.createTable();
        Competicao.createTable();
        Equipa.createTable();
        Jogador.createTable();
 
        // Inserir as modalidades
        Modalidade futebol = new Modalidade("Futebol");
        Modalidade futsal = new Modalidade("Futsal");
        Modalidade andebol = new Modalidade("Andebol");
        Modalidade tenis = new Modalidade("Ténis");
        Modalidade basket = new Modalidade("Basket");
                
        // Inserir Equipas
        Equipa scp = new Equipa("Sporting CP", "Ruben Amorim", futebol);
        Equipa fcp = new Equipa("FC Porto", "Sérgio Conceição", futebol);
        Equipa slb = new Equipa("SL Benfica", "Bruno Lage", futebol);
        Equipa braga = new Equipa("Braga", "Custódio", futebol);
        Equipa ave = new Equipa("Rio Ave", "Carlos Carvalhal", futebol);
        Equipa vitoria = new Equipa("Guimarães", "Ivo Vieira", futebol);
        
        // Inserir jogadores
        Jogador acuna = new Jogador("Acuna", scp);
        Jogador battaglia = new Jogador("Battaglia", scp);
        Jogador coates = new Jogador("Coates", scp);
        
        Jogador danilo = new Jogador("Danilo", fcp);
        
        // Inserir tipos de competições

        
        // Mostrar dados
        //System.out.println("Modalidade inseridas: " + Modalidade.allJSON());
        
        //System.out.println("Jogadores do slb: " + scp.getJogadores());

        //System.out.println("Equipas por modalidade: ");
        

        /*List<String> lista = new ArrayList<>();

        Modalidade.all().forEach( modalidade -> {
            
            lista.add("{ \"modalidade\" : " + modalidade.toString() + ", \"equipas\" : " + modalidade.getEquipas() + " }");
           
        });
        
        String json = "[ " + String.join(",", lista) + "]";
        
        System.out.println(json);
        */
        //System.out.println("Tipos de Competições: " + TipoCompeticao.allJSON());
        
        
      /*  Competicao competicaoA = new Competicao("Competição A", Tipo.CAMPEONATO, "Casa", futebol, Date.valueOf("2020-6-1") );
        Competicao competicaoB = new Competicao("Competição B", Tipo.TORNEIO, "Estádio", futebol, Date.valueOf("2020-8-5") );


        competicaoA.setVencedora(fcp);
        competicaoA.save();
      
        System.out.println("Competição de teste: " + Competicao.find(1) );    */    
        
    }
    
}
