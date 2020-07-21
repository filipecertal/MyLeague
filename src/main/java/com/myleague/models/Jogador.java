/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague.models;

import com.myleague.core.Database;
import static com.myleague.models.AbstractModel.db;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Filipe Certal <filipe.certal@gmail.com>
 */
public class Jogador extends AbstractModel {
    
    private String nome;
    private int equipa;
    
    public Jogador(String nome, Equipa equipa) {
        
        String query = "INSERT INTO jogadores (nome, equipa) VALUES (?, ?)";
        
        db.connect();
        db.prepareStatement(query);
        db.setString(1, nome);
        db.setInt(2, equipa.id());
        db.execute();
               
        this.id = db.getLastKey();
        this.nome = nome;
        this.equipa = equipa.id();
        
        db.close(); 
    }
    
    public Jogador(int id, String nome, int equipa) {
        
        this.id = id;
        this.nome = nome;
        this.equipa = equipa;
    }
    
    public void setNome(String nome) {
        
        this.nome = nome;
    }
    
    public void setEquipa(int equipa) {
        
        this.equipa = equipa;
    }
    
    public void setEquipa(Equipa equipa) {
        
        this.equipa = equipa.id();
    }
    
    public String getNome() {
        
        return this.nome;
    }
    
    public Equipa getEquipa() {
        
        return Equipa.find(this.equipa);
    }

    @Override
    public boolean save() {
        
        db.connect();
        
        String query = "UPDATE jogadores SET nome = ?, equipa = ? WHERE id = ?";
        db.prepareStatement(query);
        db.setString(1, this.nome);
        db.setInt(2, this.equipa);
        db.setInt(3, this.id);
        boolean result = db.execute();
        
        db.close();
        
        return result;
    }

    @Override
    public boolean delete() {
                  
        db.connect();
        
        String query = "DELETE FROM jogadores WHERE id = ?";
        db.prepareStatement(query);
        db.setInt(1, this.id);
        boolean result = db.execute();
        
        db.close();
        
        return result;
    }
    
    @Override
    public String toString() {
        
        return "{ \"id\" : " + this.id + ", \"nome\" : \"" + this.nome + "\", \"equipa\" : { \"id\" : " + 
                
                this.getEquipa().id() + " , \"nome\" : \"" + this.getEquipa().getNome() + "\"  } }";
        
        
        
       
        
        
    }

    
    public static void createTable() {
        
        Equipa.db = new Database();

        db.connect();

        String query = "CREATE TABLE IF NOT EXISTS jogadores (" +
                       "    id INTEGER NOT NULL AUTO_INCREMENT," +
                       "    nome NVARCHAR(255) NOT NULL," +
                       "    equipa INTEGER NOT NULL," +
                       "    CONSTRAINT PK_Modalidades" +
                       "		PRIMARY KEY(id)," +
                       "    CONSTRAINT FK_Equipas_Jogadores" +
                       "        FOREIGN KEY (equipa)" +
                       "        REFERENCES equipas (id))";

        db.prepareStatement(query);
        db.execute();

        db.close();
    }
    
        public static void dropTable() {
        
        Equipa.db = new Database();

        db.connect();

        String query = "DROP TABLE IF EXISTS jogadores";

        db.prepareStatement(query);
        db.execute();

        db.close();
    }
        
    public static Jogador find(int id) {
               
        db.connect();

        String query = "SELECT id, nome, equipa FROM jogadores WHERE id = ?";
        db.prepareStatement(query);
        db.setInt(1, id);
        db.execute();
        
        Jogador jogador =  null;
        
        if (db.next()) {
            
            jogador = new Jogador(
                db.getInt("id"), 
                db.getString("nome"),
                db.getInt("equipa")
            );
        }

        db.close();
        
        return jogador;
    }
    
    public static List<Jogador> all() {
        
        List<Jogador> lista = new ArrayList<>();
        
        db.connect();

        String query = "SELECT id, nome, equipa FROM jogadores";
        db.prepareStatement(query);
        db.execute();
         
        while(db.next()) {
            
            lista.add(
                new Jogador(
                    db.getInt("id"), 
                    db.getString("nome"),
                    db.getInt("equipa")
                )
            );
        }
        
        db.close();
        
        return lista;
    }
    
    public static String allJSON() {
        
        List<Jogador> jogadores = Jogador.all();

        List<String> lista = new ArrayList<>();

        jogadores.forEach((equipa) -> {
            lista.add(equipa.toString());
          });

        return "[ " + String.join(", ", lista) + " ]";
    }
    
    
}
