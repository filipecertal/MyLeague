/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myleague.models;

import com.myleague.core.Database;

/**
 *
 * @author Filipe Certal <filipe.certal@gmail.com>
 */
public abstract class AbstractModel {
    
    protected static Database db = new Database();

    protected int id = -1;

    public int id() {

        return this.id;
    }

    public abstract boolean save();
    public abstract boolean delete();


    
}
