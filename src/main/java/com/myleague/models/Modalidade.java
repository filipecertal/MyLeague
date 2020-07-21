package com.myleague.models;

import com.myleague.core.Database;
import java.util.ArrayList;
import java.util.List;


public class Modalidade extends AbstractModel {
    
    // Atributos da modalidade:
    private String designacao;
    
    public Modalidade(String designacao) {
        
        String query = "INSERT INTO modalidades (designacao) VALUES (?)";
        
        db.connect();
        db.prepareStatement(query);
        db.setString(1, designacao);
        db.execute();
               
        this.id = db.getLastKey();
        this.designacao = designacao;
        
        db.close();
    }
    
    public Modalidade(int id, String designacao) {
        
        this.id = id;
        this.designacao = designacao;
    }
    
    public void setDesignacao(String designacao) {
        
        this.designacao = designacao;
    }
    
    public String getDesignacao() {
        
        return this.designacao;
    }
    
    public List<Equipa> getEquipas() {
        
        List<Equipa> lista = new ArrayList<>();
    
        db.connect();

        String query = "SELECT id, nome, treinador, modalidade, competicao FROM equipas WHERE modalidade = ?";
        db.prepareStatement(query);
        db.setInt(1, this.id);
        db.execute();
        
        Equipa equipa = null;

        
        while (db.next()) {
            
            equipa = new Equipa(
                    db.getInt("id"), 
                    db.getString("nome"),
                    db.getString("treinador"),
                    db.getInt("modalidade"),
                    db.getInt("competicao")
            );
            
            lista.add( equipa
               
            );
            
        }

        db.close();
        
        return lista; 
    }
    
    public String getEquipasJson() {
        
        List<String> lista = new ArrayList<>();
        
        this.getEquipas().forEach(equipa -> {
        
            lista.add(equipa.toString());
        });
        
        return "[ " + String.join(", ", lista) + " ]";
    }
    
    @Override
    public boolean save() {
        
        db.connect();
        
        String query = "UPDATE modalidades SET designacao = ? WHERE id = ?";
        db.prepareStatement(query);
        db.setString(1, this.designacao);
        db.setInt(2, this.id);
        boolean result = db.execute();
        
        db.close();
        
        return result;
    }

    @Override
    public boolean delete() {
        
        db.connect();
        
        String query = "DELETE FROM modalidades WHERE id = ?";
        db.prepareStatement(query);
        db.setInt(1, this.id);
        boolean result = db.execute();
        
        db.close();
        
        return result;
    }
    
    @Override
    public String toString() {
        
        return "{ \"id\" : " + this.id + ", \"designacao\" : \"" + this.designacao +"\"}";
    }

    
    public static void createTable() {
        
        Modalidade.db = new Database();

        db.connect();

        String query = "CREATE TABLE IF NOT EXISTS modalidades (" +
                       "    id INTEGER NOT NULL AUTO_INCREMENT," +
                       "    designacao NVARCHAR(255) NOT NULL," +
                       "    CONSTRAINT PK_Modalidades" +
                       "		PRIMARY KEY(id))";

        db.prepareStatement(query);
        db.execute();

        db.close();
    }
    
    public static void dropTable() {
       
        db.connect();

        String query = "DROP TABLE IF EXISTS modalidades";

        db.prepareStatement(query);
        db.execute();

        db.close();
    }
    
    public static Modalidade find(int id) {
               
        db.connect();

        String query = "SELECT id, designacao FROM modalidades WHERE id = ?";
        db.prepareStatement(query);
        db.setInt(1, id);
        db.execute();
        
        Modalidade modalidade =  null;
        
        if (db.next()) {
            
            modalidade = new Modalidade(
                db.getInt("id"), 
                db.getString("designacao"));
        }

        db.close();
        
        return modalidade;
    }
    
    public static List<Modalidade> all() {
        
        List<Modalidade> lista = new ArrayList<>();
        
        db.connect();

        String query = "SELECT id, designacao FROM modalidades";
        db.prepareStatement(query);
        db.execute();
         
        while(db.next()) {
            
            lista.add(
                    new Modalidade(
                            db.getInt("id"), 
                            db.getString("designacao"))
            );
        }
        
        db.close();
        
        return lista;
    }
    
    public static String allJSON() {
        
        List<Modalidade> modalidades = Modalidade.all();

        List<String> lista = new ArrayList<>();

        modalidades.forEach((modalidade) -> {
            lista.add(modalidade.toString());
          });

        return "[ " + String.join(", ", lista) + " ]";
    }
}
