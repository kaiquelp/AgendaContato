package br.senai.sp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.senai.sp.modelo.Contatos;

public class ContatoDAO extends SQLiteOpenHelper{

    public ContatoDAO(Context context) {
        super(context, "db_contato", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE tbl_contato (" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT NOT NULL, " +
                "telefone INT NOT NULL, " +
                "email TEXT NOT NULL," +
                "linkedin TEXT NOT NULL)";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

        public void salvar(Contatos contato){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues info = getContentValues(contato);

        db.insert("tbl_contato", null, info);

    }

    @NonNull
    private ContentValues getContentValues(Contatos contato){
        ContentValues info = new ContentValues();

        info.put("nome", contato.getNome());
        info.put("endereco", contato.getEndereco());
        info.put("telefone", contato.getTelefone());
        info.put("email", contato.getEmail());
        info.put("linkedin", contato.getLinkedin());
        return info;
    }

    public List<Contatos> getContatos(){

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM tbl_contato";

        Cursor c = db.rawQuery(sql, null);

        List<Contatos> contatos = new ArrayList<>();

        while(c.moveToNext()){
            Contatos contato = new Contatos();
            contato.setId(c.getInt(c.getColumnIndex("id")));
            contato.setNome(c.getString(c.getColumnIndex("nome")));
            contato.setEndereco(c.getString(c.getColumnIndex("endereco")));
            contato.setTelefone(c.getString(c.getColumnIndex("telefone")));
            contato.setEmail(c.getString(c.getColumnIndex("email")));
            contato.setLinkedin(c.getString(c.getColumnIndex("linkedin")));
            contatos.add(contato);
        }
        return contatos;
    }

    public void atualizar(Contatos contato){
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {String.valueOf(contato.getId())};

        ContentValues info = getContentValues(contato);

        db.update("tbl_contato", info, "id = ?", params);
    }

    public void excluir(Contatos contato){
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {String.valueOf(contato.getId())};
        db.delete("tbl_contato", "id = ?", params);
    }
}
