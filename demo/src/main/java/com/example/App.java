package com.example;

// import com.example.employee;
// import com.example.Database;

public class App {
    public static void main(String[] args) {

        employee e1 = new employee();
        // e1.setId(5);
        e1.setName("Abhishek -02");
        e1.setBranch("CSE");
        e1.setAge(21);

        Database db = new Database();
        // db.insertByPrepared(e1);
        db.showDB();
        // db.FetchByPrepared();
        // db.deleteByPrepared(9);
        // db.updateByPrepared("Shreya", 8);
        // db.FetchByPrepared();
        // db.updateEmpSalaryByCallable(101, 0.1);
        // db.resultSet();
        // db.getMetaData();
        // db.getDBMetaData();
        // db.insertPhoto();
        // db.retrievePhoto();
        // db.storeFile();
        // db.retrieveFile();
        // db.onConsoletxt();
        // db.batchByStatement();
        // db.batchByPrepareStmt();
        // db.usingRowset();
        // db.usingRowsetEventHandling();
    }
}
