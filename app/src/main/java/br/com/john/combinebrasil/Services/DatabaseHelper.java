package br.com.john.combinebrasil.Services;

import android.app.Service;
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
import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.Results;
import br.com.john.combinebrasil.Classes.Selective;
import br.com.john.combinebrasil.Classes.SelectiveAthletes;
import br.com.john.combinebrasil.Classes.Team;
import br.com.john.combinebrasil.Classes.TeamUsers;
import br.com.john.combinebrasil.Classes.TestTypes;
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

    public void addPositions(ArrayList<Positions> positions) {
        long ret = 0;
        try{
            for (Positions obj : positions) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.POSITIONS_ID, obj.getID());
                values.put(Constants.POSITIONS_NAME, obj.getNAME());
                values.put(Constants.POSITIONS_DESCRIPTIONS, obj.getDESCRIPTION());

                ret = db.insert(Constants.TABLE_POSITIONS, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addSelectivesAthletes(ArrayList<SelectiveAthletes> selectiveAthletes) {
        long ret = 0;
        try{
            for (SelectiveAthletes obj : selectiveAthletes) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.SELECTIVEATHLETES_ID, obj.getId());
                values.put(Constants.SELECTIVEATHLETES_ATHLETE, obj.getAthlete());
                values.put(Constants.SELECTIVEATHLETES_SELECTIVE, obj.getSelective());
                values.put(Constants.SELECTIVEATHLETES_PRESENCE, Services.convertBoolInInt(obj.getPresence()));
                values.put(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER, obj.getInscriptionNumber());

                ret = db.insert(Constants.TABLE_SELECTIVEATHLETES, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addSelectives(ArrayList<Selective> selectives) {
        long ret = 0;
        try{
            for (Selective obj : selectives) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.SELECTIVES_ID, obj.getId());
                values.put(Constants.SELECTIVES_TITLE, obj.getTitle());
                values.put(Constants.SELECTIVES_TEAM, obj.getTeam());
                values.put(Constants.SELECTIVES_DATE, obj.getDate());

                ret = db.insert(Constants.TABLE_SELECTIVES, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTeamUsers(ArrayList<TeamUsers> teamUserses) {
        long ret = 0;
        try{
            for (TeamUsers obj : teamUserses) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.TEAMUSERS_ID, obj.getId());
                values.put(Constants.TEAMUSERS_USER, obj.getUser());
                values.put(Constants.TEAMUSERS_TEAM, obj.getTeam());

                ret = db.insert(Constants.TABLE_TEAMUSERS, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTeam(ArrayList<Team> teams) {
        long ret = 0;
        try{
            for (Team obj : teams) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.TEAM_ID, obj.getId());
                values.put(Constants.TEAM_NAME, obj.getName());
                values.put(Constants.TEAM_CITY, obj.getCity());
                values.put(Constants.TEAM_MODALITY, obj.getModality());

                ret = db.insert(Constants.TABLE_TEAM, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTestTypes(ArrayList<TestTypes> testTypes) {
        long ret = 0;
        try{
            for (TestTypes obj : testTypes) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.TESTTYPES_ID, obj.getId());
                values.put(Constants.TESTTYPES_NAME, obj.getName());
                values.put(Constants.TESTTYPES_ATTEMPTSLIMIT, obj.getAttemptsLimit());
                values.put(Constants.TESTTYPES_VISIBLETOREPORT, obj.getVisibleToReport());

                ret = db.insert(Constants.TABLE_TESTTYPES, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTests(ArrayList<Tests> tests) {
        long ret = 0;
        try{
            for (Tests obj : tests) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.TESTS_ID, obj.getId());
                values.put(Constants.TESTS_TYPE, obj.getType());
                values.put(Constants.TESTS_ATHLETE, obj.getAthlete());
                values.put(Constants.TESTS_VALUE, obj.getValue());
                values.put(Constants.TESTS_RATING, obj.getRating());

                ret = db.insert(Constants.TABLE_TESTS, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addUser(User user) {
        long ret = 0;
        try{
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Constants.USER_ID, user.getId());
                values.put(Constants.USER_NAME, user.getName());
                values.put(Constants.USER_EMAIL, user.getEmail());
                values.put(Constants.USER_ISADMIN, Services.convertBoolInInt(user.getIsAdmin()));
                values.put(Constants.USER_CANWRITE, Services.convertBoolInInt(user.getCanWrite()));
                values.put(Constants.USER_TOKEN, user.getToken());

                ret = db.insert(Constants.TABLE_USER, null, values);
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
                values.put(Constants.USER_EMAIL, obj.getEmail());
                values.put(Constants.USER_ISADMIN, Services.convertBoolInInt(obj.getIsAdmin()));
                values.put(Constants.USER_CANWRITE, Services.convertBoolInInt(obj.getCanWrite()));
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
                            c.getString(c.getColumnIndex(Constants.ATHLETES_UPDATEAT)),
                            null
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

    public ArrayList<Tests> getTests() {
        this.openDataBase();

        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_TESTS;
        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<Tests> itens = new ArrayList<Tests>();

        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                Tests obj = new Tests(
                        c.getString(c.getColumnIndex(Constants.TESTS_ID)),
                        c.getString(c.getColumnIndex(Constants.TESTS_TYPE)),
                        c.getString(c.getColumnIndex(Constants.TESTS_ATHLETE)),
                        c.getString(c.getColumnIndex(Constants.TESTS_VALUE)),
                        c.getString(c.getColumnIndex(Constants.TESTS_RATING))
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
                        c.getString(c.getColumnIndex(Constants.USER_EMAIL)),
                        Services.convertIntInBool(c.getInt(c.getColumnIndex(Constants.USER_ISADMIN))),
                        Services.convertIntInBool(c.getInt(c.getColumnIndex(Constants.USER_CANWRITE))),
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

    public Athletes getAthleteById(String idPlayer){
        this.openDataBase();
        String selectQuery = "SELECT DISTINCT * FROM "+ Constants.TABLE_ATHLETES +
                " WHERE "+Constants.ATHLETES_ID +" ='"+idPlayer+"'";

        Cursor c = myDataBase.rawQuery(selectQuery, null);

        Athletes athlete = new Athletes();

        if (c.moveToFirst()) {
            athlete = new Athletes(
                    c.getString(c.getColumnIndex(Constants.ATHLETES_ID)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_NAME)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_BIRTHDAY)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_CPF)),
                    c.getInt(c.getColumnIndex(Constants.ATHLETES_HEIGHT)),
                    c.getInt(c.getColumnIndex(Constants.ATHLETES_WEIGHT)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_CREATEDAT)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_UPDATEAT)),
                    null
            );
        } else {
            athlete = null;
        }
        c.close();
        this.close();
        return athlete;
    }

    public User checkExistsUser(String userName, String password) {
        String selectGetUser = "SELECT DISTINCT * FROM [" + Constants.TABLE_USER + "] WHERE Username = '" + userName +
                "'" + " AND password ='" + password + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectGetUser, null);
        User user = null;
        if(c.moveToFirst()){
            User obj = new User(
                    c.getString(c.getColumnIndex(Constants.USER_ID)),
                    c.getString(c.getColumnIndex(Constants.USER_NAME)),
                    c.getString(c.getColumnIndex(Constants.USER_EMAIL)),
                    Services.convertIntInBool(c.getInt(c.getColumnIndex(Constants.USER_ISADMIN))),
                    Services.convertIntInBool(c.getInt(c.getColumnIndex(Constants.USER_CANWRITE))),
                    c.getString(c.getColumnIndex(Constants.USER_TOKEN))
            );
        }
        return user;
    }

    /******************************** SEARCHS************************************************/

    public ArrayList<Athletes> searchAthletes(String search){
        this.openDataBase();
        String selectQuery =  "SELECT DISTINCT * FROM "+Constants.TABLE_ATHLETES+" WHERE " +
                "("+Constants.ATHLETES_NAME+" || \"\" || "+Constants.ATHLETES_CPF+") LIKE '%"+search+"%' GROUP BY "
                +Constants.ATHLETES_NAME+", "+Constants.ATHLETES_CPF+"";

        Cursor c = myDataBase.rawQuery(selectQuery, null);

        ArrayList<Athletes> athletes = new ArrayList<Athletes>();


        if(c.getCount()>0){
            c.moveToFirst();
            do{
                Athletes athlete = new Athletes(
                        c.getString(c.getColumnIndex(Constants.ATHLETES_ID)),
                        c.getString(c.getColumnIndex(Constants.ATHLETES_NAME)),
                        c.getString(c.getColumnIndex(Constants.ATHLETES_BIRTHDAY)),
                        c.getString(c.getColumnIndex(Constants.ATHLETES_CPF)),
                        c.getInt(c.getColumnIndex(Constants.ATHLETES_HEIGHT)),
                        c.getInt(c.getColumnIndex(Constants.ATHLETES_WEIGHT)),
                        c.getString(c.getColumnIndex(Constants.ATHLETES_CREATEDAT)),
                        c.getString(c.getColumnIndex(Constants.ATHLETES_UPDATEAT)),
                        null
                );
                athletes.add(athlete);
            }while(c.moveToNext());
        }


        return athletes;

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