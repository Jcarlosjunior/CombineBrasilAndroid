package br.com.john.combinebrasil.Services;

import android.os.Environment;

import java.io.File;

/**
 * Created by GTAC on 17/10/2016.
 */
public class Constants {
    public static final boolean debug = true;

    public static final int STATUS_NOT_INIT=0, STATUS_DONE=1, STATUS_PENDING=2;

    public static final String URL = "https://combine-api.herokuapp.com/";

    public static final String login = "api/login";

    public static final String API_ATHLETES = "api/athletes";
    public static final String API_POSITIONS = "api/positions";
    public static final String API_SELECTIVEATHLETES = "api/selectiveAthletes";
    public static final String API_TEAMS = "api/teams";
    public static final String API_TESTTYPES = "api/testTypes";

    public static final String API_SELECTIVES = "api/selectives";
    public static final String API_TEAMUSERS = "api/teamUsers";
    public static final String API_TESTS = "api/tests";
    public static final String API_USERS = "api/users";

      /*
    **************************CHAMADAS DO VOLLEY***********************************
    **/

    public static final String CALLED_LOGIN = "calledLogin";
    public static final String CALLED_GET_TESTS = "getTests";
    public static final String CALLED_GET_USER = "getUser";
    public static final String CALLED_GET_ATHLETES= "calledGetAthletes";
    public static final String CALLED_GET_POSITIONS= "getPositions";
    public static final String CALLED_GET_SELECTIVEATHLETES= "getSelectiveAthletes";
    public static final String CALLED_GET_SELECTIVE= "getSelective";
    public static final String CALLED_GET_TEAMUSERS= "getTeamUsers";
    public static final String CALLED_GET_TEAM= "getTeam";
    public static final String CALLED_GET_TESTTYPES= "getTestTypes";

    public static final String CALLED_POST_TESTS = "calledPostTests";

    public static final String CALLED_POST_ATHLETES = "calledPostAthletes";


    public static final String LOGIN_EMAIL = "email";
    public static final String LOGIN_ISADMIN = "isAdmin";
    public static final String LOGIN_CANWRITE = "canWrite";


    public static final String PATH_DEFAULT = new File(Environment.getExternalStorageDirectory(), File.separator+ "Android" +
            File.separator+"data"+File.separator+  AllActivities.mainActivity.getApplicationContext().getPackageName()).toString();

    public static final String PATH_DATABASE = PATH_DEFAULT+ File.separator+"database";
    public static final String NAME_DATABASE = "Combine.db";
    public static final String TESTS = "Testes";
    public static final String USER = "User";

    /****************************************NAMES ACTIVITYS**********************************/

    public static final String CRONOMETER_ACTIVITY = "CronometerActivity";
    public static final String RESULTS_ACTIVITY = "ResultsActivity";
    public static final String MAIN_ACTIVITY = "MainActivity";
    public static final String LOGIN_ACTIVITY = "LoginActivity";
    public static final String LOGIN_CREATEACCOUNTATHLETE = "CreateAccountAthlete";
    public static final String SYNC_ACTIVITY = "SyncActivity";

    //SHARED PREFERENCES
    public static final String TIMER_CHRONOMETER = "timer_chronometer",
            ID = "Id",
            LOGGED="Logged";

    /*
    ***************************DATABASES********************************************
    */

    /*****************************PLAYERS TABLE**********************************/
    public static final String TABLE_ATHLETES = "Athletes";
    public static final String ATHLETES_ID = "_id";
    public static final String ATHLETES_NAME = "name";
    public static final String ATHLETES_BIRTHDAY = "birthday";
    public static final String ATHLETES_EMAIL = "email";
    public static final String ATHLETES_CPF = "cpf";
    public static final String ATHLETES_PHONE = "phoneNumber";
    public static final String ATHLETES_ADDRESS = "address";
    public static final String ATHLETES_DESIRABLE_POSITION = "desirablePosition";
    public static final String ATHLETES_HEIGHT = "height";
    public static final String ATHLETES_WEIGHT = "weight";
    public static final String ATHLETES_CREATEDAT = "createdAt";
    public static final String ATHLETES_UPDATEAT = "updatedAt";
    public static final String ATHLETES_CODE = "code";
    public static final String ATHLETES_SYNC = "sync";
    public static final String ATHLETES_TERMSACCEPTED = "termsAccepted";

    /*******************************USER TABLE ***********************************/
    public static final String TABLE_USER = "User";
    public static final String USER_ID = "_id";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_ISADMIN = "isAdmin";
    public static final String USER_CANWRITE = "canWrite";
    public static final String USER_TOKEN = "token";

    /********************************POSITIONS TABLE ******************************/

    public static final String TABLE_POSITIONS = "Positions";
    public static final String POSITIONS_ID = "_id";
    public static final String POSITIONS_NAME = "name";
    public static final String POSITIONS_DESCRIPTIONS = "description";

    /***********************************SELECTIVES ATHLETES TABLE ******************/
    public static final String TABLE_SELECTIVEATHLETES = "SelectiveAthletes";
    public static final String SELECTIVEATHLETES_ID = "_id";
    public static final String SELECTIVEATHLETES_ATHLETE = "athlete";
    public static final String SELECTIVEATHLETES_SELECTIVE = "selective";
    public static final String SELECTIVEATHLETES_PRESENCE = "presence";
    public static final String SELECTIVEATHLETES_INSCRIPTIONNUMBER = "inscriptionNumber";

    /***********************************SELECTIVES ATHLETES TABLE ******************/
    public static final String TABLE_SELECTIVES = "Selectives";
    public static final String SELECTIVES_ID = "_id";
    public static final String SELECTIVES_TITLE = "title";
    public static final String SELECTIVES_TEAM = "team";
    public static final String SELECTIVES_DATE = "date";
    public static final String SELECTIVES_CODESELECTIVE= "codeSelective";
    public static final String SELECTIVES_CANSYNC = "canSync";

    /****************************** TEAM TABLE***************************************/

    public static final String TABLE_TEAM= "team";
    public static final String TEAM_ID = "_id";
    public static final String TEAM_NAME = "name";
    public static final String TEAM_CITY = "city";
    public static final String TEAM_MODALITY = "modality";

    /****************************** TEAM USERS TABLE***************************************/

    public static final String TABLE_TEAMUSERS = "TeamUsers";
    public static final String TEAMUSERS_ID = "_id";
    public static final String TEAMUSERS_USER = "user";
    public static final String TEAMUSERS_TEAM = "team";

    /****************************** TEAM TYPES TABLE***************************************/

    public static final String TABLE_TESTTYPES = "TestTypes";
    public static final String TESTTYPES_ID = "_id";
    public static final String TESTTYPES_NAME = "name";
    public static final String TESTTYPES_ATTEMPTSLIMIT = "attemptsLimit";
    public static final String TESTTYPES_VISIBLETOREPORT = "visibleToReport";
    public static final String TESTTYPES_DESCRIPTION = "description";
    public static final String TESTTYPES_VALUETYPES= "valueType";

    /************************************TESTS*******************************************/
    public static final String TABLE_TESTS = "Tests";
    public static final String TESTS_ID = "_id";
    public static final String TESTS_TYPE = "type";
    public static final String TESTS_ATHLETE = "athlete";
    public static final String TESTS_SELECTIVE = "selective";
    public static final String TESTS_FIRST_VALUE = "firstValue";
    public static final String TESTS_SECOND_VALUE = "secondValue";
    public static final String TESTS_RATING = "rating";
    public static final String TESTS_WINGSPAN = "wingspan";
    public static final String TESTS_USER = "user";
    public static final String TESTS_SYNC = "sync";

    public static final String TERMS_TEXT = "<br>Declaro  para os devidos fins de direito  e ciência que:<br><br>" +
            "1. Participarei da seletiva e se caso selecionado, dos treinos e jogos de Futebol Americano e estou em pleno gozo de saúde e em condições de participar da seletiva, treinos e jogos, não apresentando qualquer tipo de impedimento ou restrição à prática e atividades físicas e esportivas.<br><br>" +
            "2.Assumo, todos os riscos e responsabilidade legal das minhas ações envolvidas na participação da seletiva, dos treinos e jogos. Isentando o time, comissão técnica e organizadora, colaboradores e patrocinadores DE TODA E QUALQUER RESPONSABILIDADE por quaisquer danos materiais, morais ou físicos, que porventura venha a sofrer ou provocar.<br><br>" +
            "3.Em caso de lesões do atleta, será acionado o serviço de atendimento médico da rede pública para atender o ocorrido. O atleta ou o seu (sua) acompanhante poderá decidir pelo atendimento, remoção ou transferência para hospitais da rede privada de saúde, eximindo de qualquer responsabilidade, o time, comissão técnica e organizadora, colaboradores e patrocinadores de ônus pelas despesas decorrentes deste atendimento. <br><br>" +
            "5. Concedo, a título universal e de forma irrevogável e irretratável, o direito de usar minha imagem, voz, sons a serem captados na seletiva, treinos e jogos, para usos informativos, promocionais ou publicitários , sem acarretar nenhum ônus ao time, comissão organizadora e aos parceiros. Renunciando o recebimento de qualquer renda que vier a ser auferida com tais direitos em qualquer tempo/data, sendo os direitos reservados ao time e comissão organizadora.<br><br>" +
            "6. Poderá o time solicitar ao atleta, exame médicos periódicos para os treinos e jogos, se caso não for realizado e apresentado, ficará então este exame substituído  por este TERMOS DE RESPONSABILIDADE, eximindo o time e assumindo o atleta suas consequências.<br><br>" +
            "7. Fica eleito o foro da cidade de São Jose dos Campos, preterido qualquer outro, por mais privilegiado que seja, para dirimir quaisquer questões oriundas deste Termo de Responsabilidade.<br><br>";

}
