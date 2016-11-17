package br.com.john.combinebrasil.Services;

/**
 * Created by GTAC on 17/11/2016.
 */import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;

public class Mask implements TextWatcher{

    private static Mask instance;

    private String mMask;
    private EditText mEditText;

    private boolean isUpdating;
    private String old = "";
    public static  String errorMessage = "";

    public Mask(String mask, EditText editText) {
        mMask = mask;
        mEditText = editText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str = Services.unmask(s.toString());
        String mascara = "";

        if (isUpdating) {
            old = str;
            isUpdating = false;
            return;
        }

        if(str.length() > old.length())
            mascara = Services.mask(mMask,str);
        else
            mascara = s.toString();

        isUpdating = true;

        mEditText.setText(mascara);
        mEditText.setSelection(mascara.length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void afterTextChanged(Editable s) {

    }


    public static boolean isCPF(String CPF) {

        if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222") ||
                CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888") ||
                CPF.equals("99999999999") || (CPF.length() != 11))
            return(false);
        char dig10, dig11;
        int sm, i, r, num, peso;
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0; peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                sm += + (num * peso);
                peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else
                dig10 = (char)(r + 48);
            // converte no respectivo caractere numerico // Calculo do 2o. Digito Verificador
            sm = 0; peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else
                dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else
                return(false);
        } catch (InputMismatchException erro) {
            return(false);

        }

    }

    public static boolean isValidDate(String date) {
        Calendar cal = GregorianCalendar.getInstance();
        String[] newDate = date.split("/");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date testDate = null;
        try{
            testDate = sdf.parse(date);
        } catch (ParseException e){
            return false;
        }
        if (!sdf.format(testDate).equals(date)){
            return false;
        }
        if (Integer.parseInt(newDate[2]) < 1900 || Integer.parseInt(newDate[2]) > cal.get(Calendar.YEAR))
            return false;

        return true;

    } // end isValidDate

    public static boolean isValidTel(String tel){
        if (tel.length() >= 10)
            return true;
        else
            return false;
    }
}