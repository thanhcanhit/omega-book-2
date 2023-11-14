/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Supplier_DAO;
import entity.Supplier;
import java.util.ArrayList;

/**
 *
 * @author KienTran
 */
public class SupplierManagement_BUS {
        private Supplier_DAO supplierDAO = new Supplier_DAO();
        
        public ArrayList<Supplier> getAll(){
            return supplierDAO.getAll();
        }
        public boolean update(String id, Supplier supplier){
            
            return supplierDAO.update(id, supplier);
        }
        public boolean create(String Name, String address) throws Exception{
            String id = supplierDAO.generateID();
            Supplier supplier = new Supplier(id, Name, address);
            return supplierDAO.create(supplier);
        }
        public ArrayList<Supplier> search(String id){
            ArrayList<Supplier> list = new ArrayList<>();
            for(Supplier supplier:supplierDAO.getAll()){
                if(supplier.getSupplierID().equals(id)){
                    list.add(supplier);
                }
            }
            return list;
        }
}
