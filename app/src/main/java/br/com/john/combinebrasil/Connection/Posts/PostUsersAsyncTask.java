package br.com.john.combinebrasil.Connection.Posts;

import br.com.john.combinebrasil.RegisterActivity;

/**
 * Created by GTAC on 17/03/2017.
 */

public class PostUsersAsyncTask extends PostBase {

    @Override
    protected void onPostExecute(String status) {
        RegisterActivity.afterRegisterUser(activity, resp, result);
    }

}