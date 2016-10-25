package br.com.john.combinebrasil.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by GTAC on 17/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static Context context;
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = Constants.PATH_DATABASE+ File.separator+Constants.NAME_DATABASE+".db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(getCreateClause(Constants.USER,
                Constants.ID,
                Constants.NAME,
                Constants.USERNAME,
                Constants.PASSWORD,
                Constants.EMAIL,
                Constants.TOKEN));

        db.execSQL(getCreateClause(Constants.TESTS,
                Constants.ID,
                Constants.NAME,
                Constants.TYPE,
                Constants.DESCRIPTION,
                Constants.USER));
    }

    private String getCreateClause(String TableName, String... fields) {
        String createClause = "CREATE TABLE " + TableName + " ( " + fields[0] + " TEXT PRIMARY KEY ";
        for (int i = 1; i < fields.length; i++) {
            createClause += ", " + fields[i] + " TEXT ";
        }
        createClause += " ) ";
        return createClause;
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_NAME, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null ? true : false;
    }

    public long delete(String table, String id){
        SQLiteDatabase db = getWritableDatabase();
        long ret = 0;
        ret = db.delete(table, "id = ?", new String[] { id });
        db.close();
        return  ret;
    }

    private static boolean ret;

    public boolean checkID(String table, String id){
        SQLiteDatabase db = getWritableDatabase();
        try {
            String selectQuery = "SELECT id FROM " + table + " WHERE id = '" + id + "'";
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                c.getString(c.getColumnIndex(Constants.ID));
                ret = true;
            } else {
                ret = false;
            }
            c.close();
            db.close();
        }
        catch(SQLiteException e){
            ret = false;
        }
        return ret == true ? true : false;
    }

    public void dropDatabase(){
        context.deleteDatabase(DATABASE_NAME);
    }

    /*****************************************TABLE ROUTES*****************************************************/

    /*public long addRoutes(ArrayList<Routes> listRoutes) {
        long ret = 0;
        for(Routes obj :listRoutes ){
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(Constants.ID, obj.getId());
            values.put(Constants.DATE, obj.getDate());
            values.put(Constants.PROCESS, obj.getProcess());
            values.put(Constants.SYSTEM, obj.getSystem());

            ret = db.insert(Constants.TABLES_ROUTES, null, values);

            db.close();
        }

        return ret;
    }*/

    public void deleteCestas() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TESTS, null, null);
        db.close();
    }

    /*public ArrayList<Routes> searchRoutes(String search){
        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT DISTINCT * FROM "+constants.TABLE_CESTAS+" WHERE " +
                "("+constants.NOME+" || \"\" || "+constants.VALOR+") LIKE '%"+search+"%' GROUP BY "
                +constants.NOME+", "+constants.VALOR+"";

        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<ItemListCestas> cestas = new ArrayList<ItemListCestas>();

        if (c.moveToFirst()) {
            do {
                ItemListCestas obj = new ItemListCestas();
                obj.setId(c.getString(c.getColumnIndex(constants.ID)));
                obj.setName(c.getString(c.getColumnIndex(constants.NOME)));
                obj.setValue(c.getDouble(c.getColumnIndex(constants.VALOR)));
                obj.setImage(c.getString(c.getColumnIndex(constants.IMAGEM)));
                cestas.add(obj);
            } while (c.moveToNext());

        } else {
            cestas = null;
        }
        c.close();
        db.close();

        return cestas;
    }*/

    /******************************************TABELA ATACADOS***********************************************************/

    /*public long addAtacado(ItemListAtacados obj) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(constants.ID, obj.getId());
        values.put(constants.NOME, obj.getName());
        values.put(constants.VALOR, obj.getPrice());
        values.put(constants.IMAGEM, obj.getImage());
        long ret = db.insert(constants.TABLE_ATACADOS, null, values);

        db.close();

        return ret;
    }

    public void deleteTodosAtacados() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(constants.TABLE_ATACADOS, null, null);
        db.close();
    }

    public ArrayList<ItemListAtacados> searchAtacados(String search){
        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT DISTINCT * FROM "+constants.TABLE_ATACADOS+" WHERE " +
                "("+constants.NOME+" || \"\" || "+constants.VALOR+") LIKE '%"+search+"%' GROUP BY "
                +constants.NOME+", "+constants.VALOR+"";

        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<ItemListAtacados> atacados = new ArrayList<ItemListAtacados>();

        if (c.moveToFirst()) {
            do {
                ItemListAtacados obj = new ItemListAtacados();
                obj.setId(c.getString(c.getColumnIndex(constants.ID)));
                obj.setName(c.getString(c.getColumnIndex(constants.NOME)));
                obj.setPrice(c.getDouble(c.getColumnIndex(constants.VALOR)));
                obj.setImage(c.getString(c.getColumnIndex(constants.IMAGEM)));
                atacados.add(obj);
            } while (c.moveToNext());

        } else {
            atacados = null;
        }
        c.close();
        db.close();

        return atacados;
    }*/

    /***********************************TABELA CARRINHO***************************************/

   /* public long addItemCarrinho(ItemListCarrinho obj) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(constants.ID, obj.getId());
        values.put(constants.NOME, obj.getNome());
        values.put(constants.VALOR, obj.getValor());
        values.put(constants.IMAGEM, obj.getImage());
        values.put(constants.PESO, obj.getPeso());
        values.put(constants.QUANTIDADE, obj.getQtd());
        values.put(constants.TOTAL, obj.getTotal());
        long ret = db.insert(constants.TABLE_CARRINHO, null, values);

        db.close();

        return ret;
    }

    public void deleteTodosItensCarrinho() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(constants.TABLE_CARRINHO, null, null);
        db.close();
    }

    public int contItensCarrinho(){
        int itens = 0;
        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT * FROM " + constants.TABLE_CARRINHO;
        Cursor c = db.rawQuery(selectQuery, null);


        if (c.moveToFirst()) {
            do {
                itens=itens+1;
            } while (c.moveToNext());

        } else {
            itens = 0;
        }
        c.close();
        db.close();

        return itens;
    }

    public ArrayList<ItemListCarrinho> getItensCarrinho() {

        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT * FROM " + constants.TABLE_CARRINHO;
        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<ItemListCarrinho> itens = new ArrayList<ItemListCarrinho>();

        if (c.moveToFirst()) {
            do {
                ItemListCarrinho obj = new ItemListCarrinho();
                obj.setId(c.getString(c.getColumnIndex(constants.ID)));
                obj.setNome(c.getString(c.getColumnIndex(constants.NOME)));
                obj.setValor(c.getDouble(c.getColumnIndex(constants.VALOR)));
                obj.setImage(c.getString(c.getColumnIndex(constants.IMAGEM)));
                obj.setPeso(c.getString(c.getColumnIndex(constants.PESO)));
                obj.setQtd(c.getInt(c.getColumnIndex(constants.QUANTIDADE)));
                obj.setTotal(c.getDouble(c.getColumnIndex(constants.TOTAL)));
                itens.add(obj);
            } while (c.moveToNext());

        } else {
            itens = null;
        }
        c.close();
        db.close();

        return itens;
    }

    public ItemListCarrinho getItemCarrinhoById(String id) {
        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT * FROM " + constants.TABLE_CARRINHO + " WHERE id = '" + id + "'";
        Cursor c = db.rawQuery(selectQuery, null);

        ItemListCarrinho obj = new ItemListCarrinho();
        if (c.moveToFirst()) {
            obj.setId(c.getString(c.getColumnIndex(constants.ID)));
            obj.setNome(c.getString(c.getColumnIndex(constants.NOME)));
            obj.setValor(c.getDouble(c.getColumnIndex(constants.VALOR)));
            obj.setImage(c.getString(c.getColumnIndex(constants.IMAGEM)));
            obj.setQtd(c.getInt(c.getColumnIndex(constants.QUANTIDADE)));
            obj.setTotal(c.getDouble(c.getColumnIndex(constants.TOTAL)));
            obj.setPeso(c.getString(c.getColumnIndex(constants.PESO)));
        } else {
            obj = null;
        }
        c.close();
        db.close();

        return obj;
    }

    public String getValorItemCarrinho(String id) {
        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT * FROM " + constants.TABLE_CARRINHO + " WHERE id = '" + id + "'";
        Cursor c = db.rawQuery(selectQuery, null);

        String valor;
        if (c.moveToFirst()) {
            valor = String.valueOf(c.getDouble(c.getColumnIndex(constants.VALOR)));
        } else {
            valor = null;
        }
        c.close();
        db.close();

        return valor;
    }

    public long updateItemCarrinho(ItemListCarrinho obj) {
        long ret = 0;

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(constants.ID, obj.getId());
        values.put(constants.NOME, obj.getNome());
        values.put(constants.VALOR, obj.getValor());
        values.put(constants.TOTAL, obj.getTotal());
        values.put(constants.PESO, obj.getPeso());
        values.put(constants.IMAGEM, obj.getImage());
        values.put(constants.QUANTIDADE, obj.getQtd());

        ret = db.update(constants.TABLE_CARRINHO, values, "id = ?", new String[] {
                String.valueOf(obj.getId())});

        db.close();

        return ret;
    }

    public long updatePrecoCarrinho(ItemListCarrinho obj) {
        long ret = 0;

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(constants.ID, obj.getId());
        values.put(constants.VALOR, obj.getValor());
        values.put(constants.TOTAL, obj.getTotal());

        ret = db.update(constants.TABLE_CARRINHO, values, "id = ?", new String[] {
                String.valueOf(obj.getId())});

        db.close();

        return ret;
    }

    public boolean verificaItemCarrinho(String id){
        SQLiteDatabase db = getWritableDatabase();
        String selectQueryProduto = "SELECT DISTINCT "+constants.ID+" FROM " + constants.TABLE_CARRINHO + " WHERE id = '"+id+"'";
        Log.i("select", selectQueryProduto);
        Cursor c = db.rawQuery(selectQueryProduto, null);
        if (!(c.moveToFirst())) {
            return false;
        }
        else
        {
            return true;
        }
    }*/

}