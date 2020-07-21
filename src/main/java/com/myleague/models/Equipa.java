/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague.models;

import com.myleague.core.Database;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Filipe Certal <filipe.certal@gmail.com>
 */
public class Equipa extends AbstractModel {
    
    private String nome;
    private String treinador;
    private int modalidade;
    private int competicao;
    
    public Equipa(String nome, String treinador, Modalidade modalidade) {
        
        String query = "INSERT INTO equipas (nome, treinador, modalidade) VALUES (?, ?, ?)";
        
        db.connect();
        db.prepareStatement(query);
        db.setString(1, nome);
        db.setString(2, treinador);
        db.setInt(3, modalidade.id());
        db.execute();
               
        this.id = db.getLastKey();
        this.nome = nome;
        this.treinador = treinador;
        this.modalidade = modalidade.id();
        this.competicao = -1;
        
        db.close(); 
    }
    
        
    public Equipa(String nome, String treinador, Modalidade modalidade, Competicao competicao) {
        
        String query = "INSERT INTO equipas (nome, treinador, modalidade, competicao) VALUES (?, ?, ?, ?)";
        
        db.connect();
        db.prepareStatement(query);
        db.setString(1, nome);
        db.setString(2, treinador);
        db.setInt(3, modalidade.id());
        db.setInt(4, competicao.id());
        db.execute();
               
        this.id = db.getLastKey();
        this.nome = nome;
        this.treinador = treinador;
        this.modalidade = modalidade.id();
        this.competicao = competicao.id();
        
        db.close(); 
    }
    
    public Equipa(int id, String nome, String treinador, int modalidade, int competicao) {
        
        this.id = id;
        this.nome = nome;
        this.treinador = treinador;
        this.modalidade = modalidade;
        this.competicao = competicao;
    }
    
    public void setNome(String nome) {
        
        this.nome = nome;
    }
    
    public void setTreinador(String treinador) {
        
        this.treinador = treinador;
    }
    
    public void setModalidade(int modalidade) {
        
        this.modalidade = modalidade;
    }
    
    public void setModalidade(Modalidade modalidade) {
        
        this.id = modalidade.id();
    }
    
    public String getNome() {
        
        return this.nome;
    }
    
    public String getTreinador() {
        
        return this.treinador;
    }
    
    public Modalidade getModalidade() {
        
        return Modalidade.find(this.modalidade);
    }
    
    public void setCompeticao(Competicao competicao) {
        
        this.competicao = competicao.id();
    }
    
    public void setCompeticao(int competicao) {
        
        this.competicao = competicao;
    }
    
    public Competicao getCompeticao() {
        
        return Competicao.find(this.competicao);
    }
    
    public List<Jogador> getJogadores() {
        
        List<Jogador> lista = new ArrayList<>();
        
        db.connect();

        String query = "SELECT id, nome, equipa FROM jogadores WHERE equipa = ?";
        db.prepareStatement(query);
        db.setInt(1, this.id);
        db.execute();
        
        Equipa equipa =  null;
        
        while (db.next()) {
            
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
    
    public String getJogadoresJSON() {
        
        List<Jogador> jogadores = this.getJogadores();
        List<String> json = new ArrayList<String>();
        
        jogadores.forEach(jogador -> {
            json.add(jogador.toString());
        });
        
        return "[ " + String.join(", ", json) + " ]";
        
    }

    @Override
    public boolean save() {
        
        db.connect();
        String query;
        
        if (this.competicao == -1) {
            
            query = "UPDATE equipas SET nome = ?, treinador = ?, modalidade = ?, competicao = null WHERE id = ?";
            
        } else {
            
            query = "UPDATE equipas SET nome = ?, treinador = ?, modalidade = ?, competicao = ? WHERE id = ?";
        }
        
        db.prepareStatement(query);
        db.setString(1, this.nome);
        db.setString(2, this.treinador);
        db.setInt(3, this.modalidade);
        
        
        if (this.competicao == -1) {
            db.setInt(4, this.id);
            
        } else {
            
            db.setInt(4, this.competicao);
            db.setInt(5, this.id);     
        }
        

        boolean result = db.execute();
        
        db.close();
        
        return result;
    }

    @Override
    public boolean delete() {
          
        db.connect();
        
        String query = "DELETE FROM equipas WHERE id = ?";
        db.prepareStatement(query);
        db.setInt(1, this.id);
        boolean result = db.execute();
        
        db.close();
        
        return result;
    }
    
    @Override
    public String toString() {

        
        return "{ \"id\" : " + this.id + 
            ", \"nome\" : \"" + this.nome +
            "\", \"treinador\" : \"" + this.treinador + 
            "\", \"modalidade\" : " + this.getModalidade() + 
            ", \"jogadores\" : " + this.getJogadores() +
            "}";
    }
    
    
    public static void createTable() {
        
        Equipa.db = new Database();

        db.connect();

        String query = "CREATE TABLE IF NOT EXISTS equipas (" +
                       "    id INTEGER NOT NULL AUTO_INCREMENT," +
                       "    nome NVARCHAR(255) NOT NULL," +
                       "    treinador NVARCHAR(255) NOT NULL," +
                       "    modalidade INTEGER NOT NULL," +
                       "    competicao INTEGER," +
                       "    CONSTRAINT PK_Modalidades" +
                       "		PRIMARY KEY(id)," +
                       "    CONSTRAINT FK_Equipas_Modalidades" +
                       "        FOREIGN KEY (modalidade)" +
                       "        REFERENCES modalidades (id)" +
                       "        ON DELETE CASCADE," +
                       "    CONSTRAINT FK_Equipas_Competicoes" +
                       "        FOREIGN KEY (competicao)" +
                       "        REFERENCES competicoes (id)" +
                       "        ON DELETE CASCADE)";

        db.prepareStatement(query);
        db.execute();

        db.close();
    }
    
    public static void dropTable() {
        
        Equipa.db = new Database();

        db.connect();

        String query = "DROP TABLE IF EXISTS equipas";

        db.prepareStatement(query);
        db.execute();

        db.close();
    }
    
    public static Equipa find(int id) {
               
        db.connect();

        String query = "SELECT id, nome, treinador, modalidade, competicao FROM equipas WHERE id = ?";
        db.prepareStatement(query);
        db.setInt(1, id);
        db.execute();
        
        Equipa equipa =  null;
        
        if (db.next()) {
            
            equipa = new Equipa(
                db.getInt("id"), 
                db.getString("nome"),
                db.getString("treinador"),
                db.getInt("modalidade"),
                db.getInt("competicao")
            );
        }

        db.close();
        
        return equipa;
    }
    
    public static List<Equipa> all() {
        
        List<Equipa> lista = new ArrayList<>();
        
        db.connect();

        String query = "SELECT id, nome, treinador, modalidade, competicao FROM equipas";
        db.prepareStatement(query);
        db.execute();
         
        while(db.next()) {
            
            int competicao = db.getInt("competicao");
            
            lista.add(
                new Equipa(
                    db.getInt("id"), 
                    db.getString("nome"),
                    db.getString("Treinador"),
                    db.getInt("modalidade"),
                    competicao == 0? -1 : competicao
                )
            );
        }
        
        db.close();
        
        return lista;
    }
    
    public static String allJSON() {
        
        List<Equipa> equipas = Equipa.all();

        List<String> lista = new ArrayList<>();

        equipas.forEach((equipa) -> {
            lista.add(equipa.toString());
          });

        return "[ " + String.join(", ", lista) + " ]";
    }

    public void removerCompeticao() {
        
        this.competicao = -1;
    }
    
    
    
    
    
}
