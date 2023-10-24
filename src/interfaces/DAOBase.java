/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.ArrayList;

/**
 *
 * @author thanhcanhit
 * @param <Type> Loại entity mà lớp DAO tương tác trong database
 */
public interface DAOBase<Type> {

    public Type getOne(String id);

    public ArrayList<Type> getAll();

    public String generateID();

    public Boolean create(Type object);

    public Boolean update(String id, Type newObject);

    public Boolean delete(String id);
}
