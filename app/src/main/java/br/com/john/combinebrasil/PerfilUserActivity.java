package br.com.john.combinebrasil;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import br.com.john.combinebrasil.Classes.User;
import br.com.john.combinebrasil.Services.DatabaseHelper;
import br.com.john.combinebrasil.Services.ImageConverter;
import br.com.john.combinebrasil.Services.ImageLoadedCallback;

public class PerfilUserActivity extends AppCompatActivity {
    Toolbar toolbar;
    boolean isMainUser = true;
    EditText editPswd, editNewPswd, editConfirmPswd;
    TextView textEmail, textName, textChangePswd;
    ImageView imageUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_user);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.linear_back_button);
        btnBack.setOnClickListener(clickedBack);
        LinearLayout btnMenu = (LinearLayout) findViewById(R.id.linear_menu_button);
        btnMenu.setVisibility(View.GONE);
        LinearLayout linearAddAccount = (LinearLayout) findViewById(R.id.linear_add_account);
        linearAddAccount.setVisibility(View.GONE);
        ImageView imgSearch = (ImageView) findViewById(R.id.imagePesquisarToolbar);
        imgSearch.setVisibility(View.GONE);
        TextView textTitle = (TextView) findViewById(R.id.text_title_toolbar);
        textTitle.setText(R.string.historic);

        textName = (TextView) findViewById(R.id.text_name_user);
        textEmail = (TextView) findViewById(R.id.text_email_user);
        textChangePswd = (TextView) findViewById(R.id.text_change_pwsd);
        imageUser = (ImageView) findViewById(R.id.image_user);
        editPswd = (EditText) findViewById(R.id.edit_pswd);
        editNewPswd = (EditText) findViewById(R.id.edit_new_pswd);
        editConfirmPswd = (EditText) findViewById(R.id.edit_confirm_pswd);

        textChangePswd.setOnClickListener(clickedTextChangePswd);

        showInfoUserPerfil();
    }

    private View.OnClickListener clickedTextChangePswd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showChangePswd();
        }
    };

    private void showChangePswd(){
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_change_pswd);
        constraintLayout.setVisibility(View.VISIBLE);
        constraintLayout = (ConstraintLayout)findViewById(R.id.constarint_user);
        constraintLayout.setVisibility(View.GONE);
        isMainUser = false;
    }

    private void showInfoUserPerfil(){
        DatabaseHelper db = new DatabaseHelper(this);
        User user = db.getUser();

        textEmail.setText(user.getEmail());
        textName.setText(user.getName());

        showImageTeam("https://scontent.fsjk2-1.fna.fbcdn.net/v/t1.0-9/17553779_1335695839811094_3378390160014516983_n.jpg?oh=81e4c17d65ebcab4b7e3142e662ddc9e&oe=59F8FAD6");
    }

    private void showImageTeam(String urlImage){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_image);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(urlImage)
                .into(imageUser,  new ImageLoadedCallback(progressBar) {
                    @Override
                    public void onSuccess() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                            roundedImage();
                        }
                    }
                    @Override
                    public void onError(){
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void roundedImage(){
        Bitmap bitmap = ((BitmapDrawable)imageUser.getDrawable()).getBitmap();
        bitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 200);
        imageUser.setImageBitmap(bitmap);
    }
    @Override
    public void onBackPressed() {
        finishAct();
    }


    private View.OnClickListener clickedBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAct();
        }
    };

    private void finishAct(){
        if(isMainUser)
            finish();
        else
            hideChangePswd();
    }
    private void hideChangePswd(){
        ConstraintLayout constraint = (ConstraintLayout) findViewById(R.id.constraint_change_pswd);
        constraint.setVisibility(View.GONE);
        constraint = (ConstraintLayout)findViewById(R.id.constarint_user);
        constraint.setVisibility(View.VISIBLE);
    }
}
