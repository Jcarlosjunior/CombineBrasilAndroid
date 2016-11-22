package br.com.john.combinebrasil.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import br.com.john.combinebrasil.Classes.Athletes;
import br.com.john.combinebrasil.Classes.Results;
import br.com.john.combinebrasil.Classes.Tests;
import br.com.john.combinebrasil.Classes.User;

/**
 * Created by GTAC on 17/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    String DB_PATH = null;
    private static String DB_NAME = "Combine.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public DatabaseHelper(Context myContext) {
        super(myContext, DB_NAME, null, DATABASE_VERSION);
        this.myContext = myContext;
        this.DB_PATH = "/data/data/" + myContext.getPackageName() + "/" + "databases/";
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_NAME, null,
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
        myContext.deleteDatabase(DB_NAME);
    }

    /*****************************************INSERT IN DATABASE*****************************************************/

    public void addAthletes(ArrayList<Athletes> listAthletes) {
        long ret = 0;
        try{
            for (Athletes obj : listAthletes) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.ATHLETES_ID, obj.getId());
                values.put(Constants.ATHLETES_NAME, obj.getName());
                values.put(Constants.ATHLETES_BIRTHDAY, obj.getBirthday());
                values.put(Constants.ATHLETES_CPF, obj.getCPF());
                values.put(Constants.ATHLETES_HEIGHT, obj.getHeight());
                values.put(Constants.ATHLETES_WEIGHT, obj.getWeight());
                values.put(Constants.ATHLETES_CREATEDAT, obj.getCreatedAt());
                values.put(Constants.ATHLETES_UPDATEAT, obj.getUpdateAt());

                ret = db.insert(Constants.TABLE_ATHLETES, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addResults(ArrayList<Results> listResults) {
        long ret = 0;
        try{
            for (Results obj : listResults) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.RESULT_ID, obj.getId());
                values.put(Constants.RESULT_ID_PLAYER, obj.getIdPlayer());
                values.put(Constants.RESULT_ID_SELECTIVE, obj.getIdSelective());
                values.put(Constants.RESULT_ID_TEST, obj.getIdTest());
                values.put(Constants.RESULT_FIRST_VALUE, obj.getFirstValue());
                values.put(Constants.RESULT_SECOND_VALUE, obj.getSecondValue());

                ret = db.insert(Constants.TABLE_RESULTS, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTests(ArrayList<Tests> listTests) {
        long ret = 0;
        try{
            for (Tests obj : listTests) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.TEST_ID, obj.getId());
                values.put(Constants.TEST_NAME, obj.getName());
                values.put(Constants.TEST_DESCRIPTION, obj.getDescription());
                values.put(Constants.TEST_CODE, obj.getCode());
                values.put(Constants.TEST_TYPE, obj.getType());
                values.put(Constants.TEST_ID_SELECTIVE, obj.getIdSelective());
                values.put(Constants.TEST_ID_USER, obj.getIdUser());

                ret = db.insert(Constants.TABLE_TEST, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addUsers(ArrayList<User> listUser) {
        long ret = 0;
        try{
            for (User obj : listUser) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.USER_ID, obj.getId());
                values.put(Constants.USER_NAME, obj.getName());
                values.put(Constants.USER_USERNAME, obj.getUsername());
                values.put(Constants.USER_PASSWORD, obj.getPassword());
                values.put(Constants.USER_EMAIL, obj.getEmail());
                values.put(Constants.USER_TOKEN, obj.getToken());

                ret = db.insert(Constants.TABLE_USER, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void deleteCestas() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TESTS, null, null);
        db.close();
    }

    /****************************************SELECT DATABASE********************************************************/

    public ArrayList<Athletes> getAthletes() {
        this.openDataBase();
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Athletes> itens = new ArrayList<Athletes>();
        try {
            String selectQuery = "SELECT * FROM " + Constants.TABLE_ATHLETES;
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    Athletes obj = new Athletes(
                            c.getString(c.getColumnIndex(Constants.ATHLETES_ID)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_NAME)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_BIRTHDAY)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_CPF)),
                            c.getInt(c.getColumnIndex(Constants.ATHLETES_HEIGHT)),
                            c.getInt(c.getColumnIndex(Constants.ATHLETES_WEIGHT)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_CREATEDAT)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_UPDATEAT))
                    );
                    itens.add(obj);
                } while (c.moveToNext());

            } else {
                itens = null;
            }
            c.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return itens;
    }

    public ArrayList<Results> getResults() {
        this.openDataBase();
        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_RESULTS;
        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<Results> itens = new ArrayList<Results>();

        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                Results obj = new Results(
                        c.getString(c.getColumnIndex(Constants.RESULT_ID)),
                        c.getString(c.getColumnIndex(Constants.RESULT_ID_SELECTIVE)),
                        c.getString(c.getColumnIndex(Constants.RESULT_ID_TEST)),
                        c.getString(c.getColumnIndex(Constants.RESULT_ID_PLAYER)),
                        c.getString(c.getColumnIndex(Constants.RESULT_STATUS)),
                        c.getString(c.getColumnIndex(Constants.RESULT_FIRST_VALUE)),
                        c.getString(c.getColumnIndex(Constants.RESULT_SECOND_VALUE))
                );

                itens.add(obj);
            } while (c.moveToNext());

        } else {
            itens = null;
        }
        c.close();
        db.close();

        return itens;
    }

    public ArrayList<Tests> getTests() {
        this.openDataBase();

        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_TEST;
        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<Tests> itens = new ArrayList<Tests>();

        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                Tests obj = new Tests(
                        c.getString(c.getColumnIndex(Constants.TEST_ID)),
                        c.getString(c.getColumnIndex(Constants.TEST_NAME)),
                        c.getString(c.getColumnIndex(Constants.TEST_TYPE)),
                        c.getString(c.getColumnIndex(Constants.TEST_DESCRIPTION)),
                        c.getString(c.getColumnIndex(Constants.TEST_ID_USER)),
                        c.getString(c.getColumnIndex(Constants.TEST_ID_SELECTIVE)),
                        c.getString(c.getColumnIndex(Constants.TEST_CODE))
                );

                itens.add(obj);
            } while (c.moveToNext());

        } else {
            itens = null;
        }
        c.close();
        db.close();

        return itens;
    }

    public ArrayList<User> getUsres() {
        this.openDataBase();

        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_USER;
        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<User> itens = new ArrayList<User>();

        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                User obj = new User(
                        c.getString(c.getColumnIndex(Constants.USER_ID)),
                        c.getString(c.getColumnIndex(Constants.USER_NAME)),
                        c.getString(c.getColumnIndex(Constants.USER_USERNAME)),
                        c.getString(c.getColumnIndex(Constants.USER_PASSWORD)),
                        c.getString(c.getColumnIndex(Constants.USER_EMAIL)),
                        c.getString(c.getColumnIndex(Constants.USER_TOKEN))
                );

                itens.add(obj);
            } while (c.moveToNext());

        } else {
            itens = null;
        }
        c.close();
        db.close();

        return itens;
    }

    public ArrayList<Athletes> getPlayerById(String idPlayer){
        this.openDataBase();
        String selectQuery = "SELECT DISTINCT * FROM "+ Constants.TABLE_ATHLETES +
                " WHERE "+Constants.ATHLETES_ID +" ='"+idPlayer+"'";

        Cursor c = myDataBase.rawQuery(selectQuery, null);
        ArrayList<Athletes> playerses = new ArrayList<Athletes>();

        Athletes athlete = null;

        if (c.moveToFirst()) {
            athlete = new Athletes(
                    c.getString(c.getColumnIndex(Constants.ATHLETES_ID)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_NAME)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_BIRTHDAY)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_CPF)),
                    c.getInt(c.getColumnIndex(Constants.ATHLETES_HEIGHT)),
                    c.getInt(c.getColumnIndex(Constants.ATHLETES_WEIGHT)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_CREATEDAT)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_UPDATEAT))
            );
        } else {
            playerses = null;
        }
        c.close();
        this.close();
        return playerses;
    }

    public User checkExistsUser(String userName, String password) {
        String selectGetUser = "SELECT DISTINCT * FROM [" + Constants.TABLE_USER + "] WHERE Username = '" + userName +
                "'" + " AND password ='" + password + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectGetUser, null);
        User user = null;
        if(c.moveToFirst()){
            user = new User(
                    c.getString(c.getColumnIndex(Constants.USER_ID)),
                    c.getString(c.getColumnIndex(Constants.USER_NAME)),
                    c.getString(c.getColumnIndex(Constants.USER_USERNAME)),
                    c.getString(c.getColumnIndex(Constants.USER_PASSWORD)),
                    c.getString(c.getColumnIndex(Constants.USER_EMAIL)),
                    c.getString(c.getColumnIndex(Constants.USER_TOKEN))
            );
        }
        return user;
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