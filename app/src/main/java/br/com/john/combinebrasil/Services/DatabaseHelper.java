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

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
        }

        if (checkDB != null)
            myDataBase = checkDB;

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
        this.openDataBase();
        try{
            for (Athletes obj : listAthletes) {
                ContentValues values = new ContentValues();

                values.put(Constants.ATHLETES_ID, obj.getId());
                values.put(Constants.ATHLETES_NAME, obj.getName());
                values.put(Constants.ATHLETES_BIRTHDAY, obj.getBirthday());
                values.put(Constants.ATHLETES_CPF, obj.getCPF());
                values.put(Constants.ATHLETES_ADDRESS, obj.getAddress());
                values.put(Constants.ATHLETES_DESIRABLE_POSITION, obj.getDesirablePosition());
                values.put(Constants.ATHLETES_HEIGHT, obj.getHeight());
                values.put(Constants.ATHLETES_WEIGHT, obj.getWeight());
                values.put(Constants.ATHLETES_CREATEDAT, obj.getCreatedAt());
                values.put(Constants.ATHLETES_UPDATEAT, obj.getUpdateAt());
                values.put(Constants.ATHLETES_CODE, obj.getCode());

                ret = myDataBase.insert(Constants.TABLE_ATHLETES, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public long addAthlete(Athletes athlete) {
        long ret = 0;
        this.openDataBase();
        try{
                ContentValues values = new ContentValues();

                values.put(Constants.ATHLETES_ID, athlete.getId());
                values.put(Constants.ATHLETES_NAME, athlete.getName());
                values.put(Constants.ATHLETES_BIRTHDAY, athlete.getBirthday());
                values.put(Constants.ATHLETES_CPF, athlete.getCPF());
                values.put(Constants.ATHLETES_ADDRESS, athlete.getAddress());
                values.put(Constants.ATHLETES_DESIRABLE_POSITION, athlete.getDesirablePosition());
                values.put(Constants.ATHLETES_HEIGHT, athlete.getHeight());
                values.put(Constants.ATHLETES_WEIGHT, athlete.getWeight());
                values.put(Constants.ATHLETES_CREATEDAT, athlete.getCreatedAt());
                values.put(Constants.ATHLETES_UPDATEAT, athlete.getUpdateAt());
                values.put(Constants.ATHLETES_CODE, athlete.getCode());

                return ret = myDataBase.insert(Constants.TABLE_ATHLETES, null, values);

        }catch (Exception e){
            Log.i("Error", e.getMessage());
            return 0;
        }
    }

    public void addPositions(ArrayList<Positions> positions) {
        long ret = 0;
        this.openDataBase();
        try{
            for (Positions obj : positions) {
                ContentValues values = new ContentValues();

                values.put(Constants.POSITIONS_ID, obj.getID());
                values.put(Constants.POSITIONS_NAME, obj.getNAME());
                values.put(Constants.POSITIONS_DESCRIPTIONS, obj.getDESCRIPTION());

                ret = myDataBase.insert(Constants.TABLE_POSITIONS, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addSelectivesAthletes(ArrayList<SelectiveAthletes> selectiveAthletes) {
        long ret = 0;
        this.openDataBase();
        try{
            for (SelectiveAthletes obj : selectiveAthletes) {
                ContentValues values = new ContentValues();

                values.put(Constants.SELECTIVEATHLETES_ID, obj.getId());
                values.put(Constants.SELECTIVEATHLETES_ATHLETE, obj.getAthlete());
                values.put(Constants.SELECTIVEATHLETES_SELECTIVE, obj.getSelective());
                values.put(Constants.SELECTIVEATHLETES_PRESENCE, Services.convertBoolInInt(obj.getPresence()));
                values.put(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER, obj.getInscriptionNumber());

                ret = myDataBase.insert(Constants.TABLE_SELECTIVEATHLETES, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addSelectiveAthlete(SelectiveAthletes selectiveAthlete) {
        long ret = 0;
        this.openDataBase();
        try{
                ContentValues values = new ContentValues();

                values.put(Constants.SELECTIVEATHLETES_ID, selectiveAthlete.getId());
                values.put(Constants.SELECTIVEATHLETES_ATHLETE, selectiveAthlete.getAthlete());
                values.put(Constants.SELECTIVEATHLETES_SELECTIVE, selectiveAthlete.getSelective());
                values.put(Constants.SELECTIVEATHLETES_PRESENCE, Services.convertBoolInInt(selectiveAthlete.getPresence()));
                values.put(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER, selectiveAthlete.getInscriptionNumber());

                ret = myDataBase.insert(Constants.TABLE_SELECTIVEATHLETES, null, values);
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addSelectives(ArrayList<Selective> selectives) {
        long ret = 0;
        this.openDataBase();
        try{
            for (Selective obj : selectives) {
                ContentValues values = new ContentValues();

                values.put(Constants.SELECTIVES_ID, obj.getId());
                values.put(Constants.SELECTIVES_TITLE, obj.getTitle());
                values.put(Constants.SELECTIVES_TEAM, obj.getTeam());
                values.put(Constants.SELECTIVES_DATE, obj.getDate());

                ret = myDataBase.insert(Constants.TABLE_SELECTIVES, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTeamUsers(ArrayList<TeamUsers> teamUserses) {
        long ret = 0;
        this.openDataBase();
        try{
            for (TeamUsers obj : teamUserses) {
                ContentValues values = new ContentValues();

                values.put(Constants.TEAMUSERS_ID, obj.getId());
                values.put(Constants.TEAMUSERS_USER, obj.getUser());
                values.put(Constants.TEAMUSERS_TEAM, obj.getTeam());

                ret = myDataBase.insert(Constants.TABLE_TEAMUSERS, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTeamUser(TeamUsers teamUserses) {
        long ret = 0;
        this.openDataBase();
        try{
            ContentValues values = new ContentValues();

            values.put(Constants.TEAMUSERS_ID, teamUserses.getId());
            values.put(Constants.TEAMUSERS_USER, teamUserses.getUser());
            values.put(Constants.TEAMUSERS_TEAM, teamUserses.getTeam());

            ret = myDataBase.insert(Constants.TABLE_TEAMUSERS, null, values);

        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTeam(ArrayList<Team> teams) {
        long ret = 0;
        this.openDataBase();
        try{
            for (Team obj : teams) {
                ContentValues values = new ContentValues();

                values.put(Constants.TEAM_ID, obj.getId());
                values.put(Constants.TEAM_NAME, obj.getName());
                values.put(Constants.TEAM_CITY, obj.getCity());
                values.put(Constants.TEAM_MODALITY, obj.getModality());

                ret = myDataBase.insert(Constants.TABLE_TEAM, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTestTypes(ArrayList<TestTypes> testTypes) {
        long ret = 0;
        this.openDataBase();
        try{
            for (TestTypes obj : testTypes) {
                ContentValues values = new ContentValues();

                values.put(Constants.TESTTYPES_ID, obj.getId());
                values.put(Constants.TESTTYPES_NAME, obj.getName());
                values.put(Constants.TESTTYPES_ATTEMPTSLIMIT, obj.getAttemptsLimit());
                values.put(Constants.TESTTYPES_VISIBLETOREPORT, Services.convertBoolInInt(obj.getVisibleToReport()));
                values.put(Constants.TESTTYPES_DESCRIPTION, obj.getDescription());
                values.put(Constants.TESTTYPES_VALUETYPES, obj.getValueType());

                ret = myDataBase.insert(Constants.TABLE_TESTTYPES, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTests(ArrayList<Tests> tests) {
        long ret = 0;
        this.openDataBase();
        try{
            for (Tests obj : tests) {
                ContentValues values = new ContentValues();

                values.put(Constants.TESTS_ID, obj.getId());
                values.put(Constants.TESTS_TYPE, obj.getType());
                values.put(Constants.TESTS_ATHLETE, obj.getAthlete());
                values.put(Constants.TESTS_FIRST_VALUE, obj.getFirstValue());
                values.put(Constants.TESTS_SECOND_VALUE, obj.getSecondValue());
                values.put(Constants.TESTS_RATING, obj.getRating());

                ret = myDataBase.insert(Constants.TABLE_TESTS, null, values);
            }
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addTest(Tests test) {
        long ret = 0;
        this.openDataBase();
        try{
                ContentValues values = new ContentValues();

                values.put(Constants.TESTS_ID, test.getId());
                values.put(Constants.TESTS_TYPE, test.getType());
                values.put(Constants.TESTS_ATHLETE, test.getAthlete());
                values.put(Constants.TESTS_FIRST_VALUE, test.getFirstValue());
                values.put(Constants.TESTS_SECOND_VALUE, test.getSecondValue());
                values.put(Constants.TESTS_RATING, test.getRating());

                ret = myDataBase.insert(Constants.TABLE_TESTS, null, values);

        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addUser(User user) {
        long ret = 0;
        this.openDataBase();
        try{
                ContentValues values = new ContentValues();

                values.put(Constants.USER_ID, user.getId());
                values.put(Constants.USER_NAME, user.getName());
                values.put(Constants.USER_EMAIL, user.getEmail());
                values.put(Constants.USER_ISADMIN, Services.convertBoolInInt(user.getIsAdmin()));
                values.put(Constants.USER_CANWRITE, Services.convertBoolInInt(user.getCanWrite()));
                values.put(Constants.USER_TOKEN, user.getToken());

                ret = myDataBase.insert(Constants.TABLE_USER, null, values);
        }catch (Exception e){
            Log.i("Error", e.getMessage());
        }
    }

    public void addUsers(ArrayList<User> listUser) {
        long ret = 0;
        this.openDataBase();
        try{
            for (User obj : listUser) {
                ContentValues values = new ContentValues();

                values.put(Constants.USER_ID, obj.getId());
                values.put(Constants.USER_NAME, obj.getName());
                values.put(Constants.USER_EMAIL, obj.getEmail());
                values.put(Constants.USER_ISADMIN, Services.convertBoolInInt(obj.getIsAdmin()));
                values.put(Constants.USER_CANWRITE, Services.convertBoolInInt(obj.getCanWrite()));
                values.put(Constants.USER_TOKEN, obj.getToken());

                ret = myDataBase.insert(Constants.TABLE_USER, null, values);
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
        ArrayList<Athletes> itens = new ArrayList<Athletes>();
        try {
            String selectQuery = "SELECT * FROM " + Constants.TABLE_ATHLETES;
            Cursor c = myDataBase.rawQuery(selectQuery, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    String code = c.getString(c.getColumnIndex(Constants.ATHLETES_CODE));
                    if(code == null)
                        code = "";
                    Athletes obj = new Athletes(
                            c.getString(c.getColumnIndex(Constants.ATHLETES_ID)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_NAME)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_BIRTHDAY)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_CPF)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_ADDRESS)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_DESIRABLE_POSITION)),
                            c.getDouble(c.getColumnIndex(Constants.ATHLETES_HEIGHT)),
                            c.getDouble(c.getColumnIndex(Constants.ATHLETES_WEIGHT)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_CREATEDAT)),
                            c.getString(c.getColumnIndex(Constants.ATHLETES_UPDATEAT)),
                            code
                    );
                    itens.add(obj);
                } while (c.moveToNext());

            } else {
                itens = null;
            }
            c.close();
            this.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return itens;
    }

    public ArrayList<SelectiveAthletes> getSelectivesAthletes() {
        this.openDataBase();
        ArrayList<SelectiveAthletes> itens = new ArrayList<SelectiveAthletes>();
        try {
            String selectQuery = "SELECT * FROM " + Constants.TABLE_SELECTIVEATHLETES;
            Cursor c = myDataBase.rawQuery(selectQuery, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    SelectiveAthletes obj = new SelectiveAthletes(
                            c.getString(c.getColumnIndex(Constants.SELECTIVEATHLETES_ID)),
                            c.getString(c.getColumnIndex(Constants.SELECTIVEATHLETES_ATHLETE)),
                            c.getString(c.getColumnIndex(Constants.SELECTIVEATHLETES_SELECTIVE)),
                            c.getString(c.getColumnIndex(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER)),
                            Services.convertIntInBool(c.getInt(c.getColumnIndex(Constants.SELECTIVEATHLETES_PRESENCE)))
                    );
                    itens.add(obj);
                } while (c.moveToNext());

            } else {
                itens = null;
            }
            c.close();
            this.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return itens;
    }

    public SelectiveAthletes getSelectiveAthletesFromAthlete(String athlete) {
        this.openDataBase();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_SELECTIVEATHLETES +
                " WHERE "+Constants.SELECTIVEATHLETES_ATHLETE+"='"+athlete+"'";
        Cursor c = myDataBase.rawQuery(selectQuery, null);
        SelectiveAthletes obj= new SelectiveAthletes();
        if (c.moveToNext()) {
            obj= new SelectiveAthletes(
                c.getString(c.getColumnIndex(Constants.SELECTIVEATHLETES_ID)),
                c.getString(c.getColumnIndex(Constants.SELECTIVEATHLETES_ATHLETE)),
                c.getString(c.getColumnIndex(Constants.SELECTIVEATHLETES_SELECTIVE)),
                c.getString(c.getColumnIndex(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER)),
                Services.convertIntInBool(c.getInt(c.getColumnIndex(Constants.SELECTIVEATHLETES_INSCRIPTIONNUMBER)))
                );

        } else {
            obj = null;
        }
        c.close();
        this.close();

        return obj;
    }

    public Selective getSelective() {
        this.openDataBase();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_SELECTIVES;

        Cursor c = myDataBase.rawQuery(selectQuery, null);
        Selective obj= new Selective();
        if (c.moveToNext()) {
            obj= new Selective(
                    c.getString(c.getColumnIndex(Constants.SELECTIVES_ID)),
                    c.getString(c.getColumnIndex(Constants.SELECTIVES_TITLE)),
                    c.getString(c.getColumnIndex(Constants.SELECTIVES_TEAM)),
                    c.getString(c.getColumnIndex(Constants.SELECTIVES_DATE))
            );

        } else {
            obj = null;
        }
        c.close();
        this.close();

        return obj;
    }

    public ArrayList<Positions> getPositions() {
        this.openDataBase();
        ArrayList<Positions> itens = new ArrayList<Positions>();
        try {
            String selectQuery = "SELECT * FROM " + Constants.TABLE_POSITIONS;
            Cursor c = myDataBase.rawQuery(selectQuery, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    Positions obj = new Positions(
                            c.getString(c.getColumnIndex(Constants.POSITIONS_ID)),
                            c.getString(c.getColumnIndex(Constants.POSITIONS_NAME)),
                            c.getString(c.getColumnIndex(Constants.POSITIONS_DESCRIPTIONS))
                    );
                    itens.add(obj);
                } while (c.moveToNext());

            } else {
                itens = null;
            }
            c.close();
            this.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return itens;
    }

    public Team getTeam() {
        this.openDataBase();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_TEAM;

        Cursor c = myDataBase.rawQuery(selectQuery, null);
        Team obj= new Team();
        if (c.getCount()>0) {
            obj= new Team(
                    c.getString(c.getColumnIndex(Constants.TEAM_ID)),
                    c.getString(c.getColumnIndex(Constants.TEAM_NAME)),
                    c.getString(c.getColumnIndex(Constants.TEAM_CITY)),
                    c.getString(c.getColumnIndex(Constants.TEAM_MODALITY))
            );

        } else {
            obj = null;
        }
        c.close();
        this.close();

        return obj;
    }

    public Selective getSelectiveFromTeam(String team) {
        this.openDataBase();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_SELECTIVES +" WHERE "
                +Constants.SELECTIVES_TEAM +"='"+team+"'" ;

        Cursor c = myDataBase.rawQuery(selectQuery, null);
        Selective obj= new Selective();
        if ( c.moveToFirst()) {
            obj= new Selective(
                    c.getString(c.getColumnIndex(Constants.SELECTIVES_ID)),
                    c.getString(c.getColumnIndex(Constants.SELECTIVES_TITLE)),
                    c.getString(c.getColumnIndex(Constants.SELECTIVES_TEAM)),
                    c.getString(c.getColumnIndex(Constants.SELECTIVES_DATE))
            );

        } else {
            obj = null;
        }
        c.close();
        this.close();

        return obj;
    }

    public ArrayList<Tests> getTests() {
        this.openDataBase();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_TESTS;
        Cursor c = myDataBase.rawQuery(selectQuery, null);

        ArrayList<Tests> itens = new ArrayList<Tests>();

        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                Tests obj = new Tests(
                        c.getString(c.getColumnIndex(Constants.TESTS_ID)),
                        c.getString(c.getColumnIndex(Constants.TESTS_TYPE)),
                        c.getString(c.getColumnIndex(Constants.TESTS_ATHLETE)),
                        c.getString(c.getColumnIndex(Constants.TESTS_FIRST_VALUE)),
                        c.getString(c.getColumnIndex(Constants.TESTS_SECOND_VALUE)),
                        c.getFloat(c.getColumnIndex(Constants.TESTS_RATING)),
                        c.getInt(c.getColumnIndex(Constants.TESTS_SYNC))
                );

                itens.add(obj);
            } while (c.moveToNext());

        } else {
            itens = null;
        }
        c.close();
        this.close();

        return itens;
    }

    public ArrayList<TestTypes> getTestsTypes() {
        this.openDataBase();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_TESTTYPES;
        Cursor c = myDataBase.rawQuery(selectQuery, null);

        ArrayList<TestTypes> itens = new ArrayList<TestTypes>();

        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                TestTypes obj = new TestTypes(
                        c.getString(c.getColumnIndex(Constants.TESTTYPES_ID)),
                        c.getString(c.getColumnIndex(Constants.TESTTYPES_NAME)),
                        c.getString(c.getColumnIndex(Constants.TESTTYPES_ATTEMPTSLIMIT)),
                        Services.convertIntInBool(c.getInt(c.getColumnIndex(Constants.TESTTYPES_VISIBLETOREPORT))),
                        c.getString(c.getColumnIndex(Constants.TESTTYPES_DESCRIPTION)),
                        c.getString(c.getColumnIndex(Constants.TESTTYPES_VALUETYPES))
                );

                itens.add(obj);
            } while (c.moveToNext());

        } else {
            itens = null;
        }
        c.close();
        this.close();

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
                    c.getString(c.getColumnIndex(Constants.ATHLETES_ADDRESS)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_DESIRABLE_POSITION)),
                    c.getDouble(c.getColumnIndex(Constants.ATHLETES_HEIGHT)),
                    c.getDouble(c.getColumnIndex(Constants.ATHLETES_WEIGHT)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_CREATEDAT)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_UPDATEAT)),
                    c.getString(c.getColumnIndex(Constants.ATHLETES_CODE))
            );
        } else {
            athlete = null;
        }
        c.close();
        this.close();
        return athlete;
    }

    public TestTypes getTestTypeFromId(String id){
        this.openDataBase();
        String selectQuery = "SELECT DISTINCT * FROM "+ Constants.TABLE_TESTTYPES +
                " WHERE "+Constants.TESTS_ID +" ='"+id+"'";

        Cursor c = myDataBase.rawQuery(selectQuery, null);

        TestTypes test;
        try {
            if (c.moveToFirst()) {
                test = new TestTypes(
                        c.getString(c.getColumnIndex(Constants.TESTTYPES_ID)),
                        c.getString(c.getColumnIndex(Constants.TESTTYPES_NAME)),
                        c.getString(c.getColumnIndex(Constants.TESTTYPES_ATTEMPTSLIMIT)),
                        Services.convertIntInBool(c.getInt(c.getColumnIndex(Constants.TESTTYPES_VISIBLETOREPORT))),
                        c.getString(c.getColumnIndex(Constants.TESTTYPES_DESCRIPTION)),
                        c.getString(c.getColumnIndex(Constants.TESTTYPES_VALUETYPES))
                );
            } else {
                test = null;
            }
        }catch (Exception e){
            test = null;
        }
        c.close();
        this.close();
        return test;
    }

    public Tests getTestFromAthleteAndType(String athlete, String type){
        this.openDataBase();
        String selectQuery = "SELECT DISTINCT * FROM "+ Constants.TABLE_TESTS +
                " WHERE "+Constants.TESTS_ATHLETE +" ='"+athlete+"' AND "+Constants.TESTS_TYPE+" ='"+type+"'";

        Cursor c = myDataBase.rawQuery(selectQuery, null);

        Tests test;
        try {
            if (c.moveToFirst()) {
                test = new Tests(
                        c.getString(c.getColumnIndex(Constants.TESTS_ID)),
                        c.getString(c.getColumnIndex(Constants.TESTS_TYPE)),
                        c.getString(c.getColumnIndex(Constants.TESTS_ATHLETE)),
                        c.getString(c.getColumnIndex(Constants.TESTS_FIRST_VALUE)),
                        c.getString(c.getColumnIndex(Constants.TESTS_SECOND_VALUE)),
                        c.getFloat(c.getColumnIndex(Constants.TESTS_RATING)),
                        c.getInt(c.getColumnIndex(Constants.TESTS_SYNC))
                );
            } else {
                test = null;
            }
        }catch (Exception e){
            test = null;
        }
        c.close();
        this.close();
        return test;
    }

    public Positions getPositiomById(String id){
        this.openDataBase();
        String selectQuery = "SELECT DISTINCT * FROM "+ Constants.TABLE_POSITIONS +
                " WHERE "+Constants.POSITIONS_ID +" ='"+id+"'";

        Cursor c = myDataBase.rawQuery(selectQuery, null);

        Positions position = new Positions();

        if (c.moveToFirst()) {
            position = new Positions(
                    c.getString(c.getColumnIndex(Constants.POSITIONS_ID)),
                    c.getString(c.getColumnIndex(Constants.POSITIONS_NAME)),
                    c.getString(c.getColumnIndex(Constants.POSITIONS_DESCRIPTIONS))
            );
        } else {
            position = null;
        }
        c.close();
        this.close();
        return position;
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
                        c.getString(c.getColumnIndex(Constants.ATHLETES_ADDRESS)),
                        c.getString(c.getColumnIndex(Constants.ATHLETES_DESIRABLE_POSITION)),
                        c.getDouble(c.getColumnIndex(Constants.ATHLETES_HEIGHT)),
                        c.getDouble(c.getColumnIndex(Constants.ATHLETES_WEIGHT)),
                        c.getString(c.getColumnIndex(Constants.ATHLETES_CREATEDAT)),
                        c.getString(c.getColumnIndex(Constants.ATHLETES_UPDATEAT)),
                        c.getString(c.getColumnIndex(Constants.ATHLETES_CODE))
                );
                athletes.add(athlete);
            }while(c.moveToNext());
        }
        return athletes;

    }
}