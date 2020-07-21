/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague.models;

import com.myleague.core.Database;
import static com.myleague.models.AbstractModel.db;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Filipe Certal <filipe.certal@gmail.com>
 */
public class Competicao extends AbstractModel {

    public void removeVencedora() {
       
        this.vencedora = -1;
        
    }
    
    public enum Tipo {
        CAMPEONATO,
        TORNEIO,
        ELIMINATORIA
    }
    
    private String nome;
    private Tipo tipo;
    private String recinto;
    private int modalidade;
    private Date inicio;
    private int vencedora;
    
    public Competicao(String nome, Tipo tipo, String recinto, Modalidade modalidade, Date inicio) {
        
        String query = "INSERT INTO competicoes (nome, tipo, recinto, modalidade, inicio) VALUES (?, ?, ?, ?, ?)";
        
        db.connect();
        db.prepareStatement(query);
        db.setString(1, nome);
        db.setInt(2, tipo.ordinal());
        db.setString(3, recinto);
        db.setInt(4, modalidade.id());
        db.setDate(5, inicio);
        db.execute();
               
        this.id = db.getLastKey();
        this.nome = nome;
        this.tipo = tipo;
        this.recinto = recinto;
        this.modalidade = modalidade.id();
        this.inicio = inicio;
        this.vencedora = -1;
        
        db.close();
    }
    
    public Competicao(String nome, Tipo tipo, String recinto, Modalidade modalidade, Date inicio, int vencedora) {
        
        String query = "INSERT INTO competicoes (nome, tipo, recinto, modalidade, inicio, vencedora) VALUES (?, ?, ?, ?, ?, ?)";
            
        
        db.connect();
        db.prepareStatement(query);
        db.setString(1, nome);
        db.setInt(2, tipo.ordinal());
        db.setString(3, recinto);
        db.setInt(4, modalidade.id());
        db.setDate(5, inicio);
        db.setInt(6, vencedora);
        
        
        db.execute();
               
        this.id = db.getLastKey();
        this.nome = nome;
        this.tipo = tipo;
        this.recinto = recinto;
        this.modalidade = modalidade.id();
        this.inicio = inicio;
        this.vencedora = vencedora;
        
        db.close();
    }
    
    public Competicao(int id, String nome, int tipo, String recinto, int modalidade, Date inicio, int vencedora) {
        
        this.id = id;
        this.nome = nome;
        this.tipo = Tipo.values()[tipo];
        this.recinto = recinto;
        this.modalidade = modalidade;
        this.inicio = inicio;
        this.vencedora = vencedora;

    }
    
    public void setNome(String nome) {
        
        this.nome = nome;
    }
    
    public String getNome() {
        
        return this.nome;
    }
    
    public void setTipoCompeticao(int tipo) {
        
        this.tipo = Tipo.values()[tipo];
    }
    
    public void setTipoCompeticao(Tipo tipo) {
        
        this.tipo = tipo;
    }
    
    
    public Tipo getTipoCompeticao() {
                   
        return this.tipo;
    
    }
    
    public void setRecinto(String recinto) {
        
        this.recinto = recinto;
    }
    
    public String getRecinto() {
        
        return this.recinto;
    }
    
    public void setModalidade(int modalidade) {
        
        this.modalidade = modalidade;
    }
    
    public void setModalidade(Modalidade modalidade) {
        
        this.modalidade = modalidade.id();
    }
    
    public Modalidade getModalidade() {
        
        return Modalidade.find(this.modalidade);
    }

    public void setDataInicio(Date data) {
        
        this.inicio = data;
    }
    
    public Date getData() {
        
        return this.inicio;
    }
    
    public boolean terminado() {
        return this.vencedora != -1;
    }
    
    public Equipa getVencedora() {
        return Equipa.find(this.vencedora);
    }

    public void setVencedora(Equipa vencedora) {
        
        this.vencedora = vencedora.id();
    }
    
    public List<Equipa> getEquipas() {
        
        List<Equipa> lista = new ArrayList<>();
        
        db.connect();

        String query = "SELECT id, nome, treinador, modalidade, competicao FROM equipas WHERE competicao = ?";
        db.prepareStatement(query);
        db.setInt(1, this.id);
        db.execute();
        
        while (db.next()) {
            
            lista.add(
                new Equipa(
                    db.getInt("id"), 
                    db.getString("nome"),
                    db.getString("treinador"),
                    db.getInt("modalidade"),
                    db.getInt("competicao")
                )
            );
            
        }

        db.close();
        
        return lista;
    }
    
    public String getEquipasJSON() {
        
        List<Equipa> equipas = this.getEquipas();
        List<String> json = new ArrayList<String>();
        
        equipas.forEach(equipa -> {
            json.add(equipa.toString());
        });
        
        return "[ " + String.join(", ", json) + " ]";
        
    }
    
    @Override
    public boolean save() {
        
        String query;
        
        db.connect();
        
        if (this.vencedora == -1) {
            
            query = "UPDATE competicoes SET nome = ?, tipo = ?, recinto = ?, modalidade = ?, inicio = ?, vencedora = -1 WHERE id = ?";
            
        } else {
            
            query = "UPDATE competicoes SET nome = ?, tipo = ?, recinto = ?, modalidade = ?, inicio = ?, vencedora = ? WHERE id = ?";
        }
        
        db.prepareStatement(query);
        db.prepareStatement(query);
        db.setString(1, nome);
        db.setInt(2, tipo.ordinal());
        db.setString(3, recinto);
        db.setInt(4, modalidade);
        db.setDate(5, inicio);
        
        if (this.vencedora == -1) {
            
            db.setInt(6, this.id);
            
        } else {
            
            db.setInt(6, this.vencedora);
            db.setInt(7, this.id);
        }
        
        boolean result = db.execute();
        db.close();
        
        return result;
    }

    @Override
    public boolean delete() {
        
        db.connect();
        
        String query = "DELETE FROM competicoes WHERE id = ?";
        db.prepareStatement(query);
        db.setInt(1, this.id);
        boolean result = db.execute();
        
        db.close();
        
        return result;
    }
    
    @Override
    public String toString() {
        
        String t = this.tipo.name().substring(0,1).toUpperCase() + this.tipo.name().substring(1).toLowerCase();

        return "{ \"id\" : " + this.id + ", " +
             "\"nome\" : \"" + this.nome + "\", " +
             "\"tipo\" : \"" + t + "\", " +
             "\"recinto\" : \"" + this.recinto + "\", " +
             "\"modalidade\" : \"" + Modalidade.find(this.modalidade).getDesignacao()  + "\", " +
             "\"inicio\" : \"" + this.inicio.toString() + "\", " + 
             "\"vencedora\" : \"" + ((this.vencedora==-1) ? "--" : Equipa.find(this.vencedora).getNome()) + "\" }";

    } 

    
    public static void createTable() {
        
        Modalidade.db = new Database();

        db.connect();

        String query = "CREATE TABLE IF NOT EXISTS competicoes (" +
                       "    id INTEGER NOT NULL AUTO_INCREMENT," +
                       "    nome NVARCHAR(255) NOT NULL," +
                       "    tipo INT NOT NULL," +
                       "    recinto NVARCHAR(50) NOT NULL," +
                       "    modalidade INT NOT NULL," +
                       "    inicio DATETIME NOT NULL," +
                       "    vencedora INT," +
                       "    CONSTRAINT PK_Competicoes" +
                       "		PRIMARY KEY(id)," + 
                       "    CONSTRAINT FK_Competicoes_Modalidades" +
                       "        FOREIGN KEY (modalidade)" +
                       "        REFERENCES modalidades(id)" +
                       "            ON DELETE CASCADE)";

        db.prepareStatement(query);
        db.execute();

        db.close();
    }
    
    public static void dropTable() {
       
        db.connect();

        String query = "DROP TABLE IF EXISTS competicoes";

        db.prepareStatement(query);
        db.execute();

        db.close();
    }
    
    public static Competicao find(int id) {
               
        db.connect();

        String query = "SELECT id, nome, tipo, recinto, modalidade, inicio, vencedora FROM competicoes WHERE id = ?";
        db.prepareStatement(query);
        db.setInt(1, id);
        db.execute();
        
        Competicao competicao =  null;
        
        if (db.next()) {
            
            if (db.getInt("vencedora") == 0) {
                
                competicao = new Competicao(
                    db.getInt("id"), 
                    db.getString("nome"),
                    db.getInt("tipo"),
                    db.getString("recinto"),
                    db.getInt("modalidade"),
                    db.getDate("inicio"),
                    -1);
                
            } else {
                
                    competicao = new Competicao(
                    db.getInt("id"), 
                    db.getString("nome"),
                    db.getInt("tipo"),
                    db.getString("recinto"),
                    db.getInt("modalidade"),
                    db.getDate("inicio"),
                    db.getInt("vencedora"));
                
            }
            

        }

        db.close();
        
        return competicao;
    }
    
    public static List<Competicao> all() {
        
        List<Competicao> lista = new ArrayList<>();
        
        db.connect();

        String query = "SELECT id, nome, tipo, recinto, modalidade, inicio, vencedora FROM competicoes";
        db.prepareStatement(query);
        db.execute();
        
         
        while(db.next()) {
            

            if (db.getInt("vencedora") == 0) {
                
                lista.add(
                     new Competicao(
                        db.getInt("id"), 
                        db.getString("nome"),
                        db.getInt("tipo"),
                        db.getString("recinto"),
                        db.getInt("modalidade"),
                        db.getDate("inicio"),
                        -1)
                );  
                
            } else {
                lista.add(
                     new Competicao(
                        db.getInt("id"), 
                        db.getString("nome"),
                        db.getInt("tipo"),
                        db.getString("recinto"),
                        db.getInt("modalidade"),
                        db.getDate("inicio"),
                        db.getInt("vencedora"))
                );    
            }
        }
        
        db.close();
        
        return lista;
    }
    
    public static String allJSON() {
        
        List<Competicao> competicoes = Competicao.all();

        List<String> lista = new ArrayList<>();

        competicoes.forEach((competicao) -> {
            lista.add(competicao.toString());
          });

        return "[ " + String.join(", ", lista) + " ]";
    }
    
    public static String allADecorrerJSON() {
        
        List<Competicao> tipos = Competicao.all();

        List<String> lista = new ArrayList<>();

        tipos.forEach((competicao) -> {
            if (!competicao.terminado())
                lista.add(competicao.toString());
          });

        return "[ " + String.join(", ", lista) + " ]";
    }
    
    public static String allTerminadaJSON() {
        
        List<Competicao> tipos = Competicao.all();

        List<String> lista = new ArrayList<>();

        tipos.forEach((competicao) -> {
            if (competicao.terminado())
                lista.add(competicao.toString());
          });

        return "[ " + String.join(", ", lista) + " ]";
    }
    
}
