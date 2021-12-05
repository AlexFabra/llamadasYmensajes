package cat.dam.alex.llamadasYmensajes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private String tlf = "", sms = "";
    private static final String TLF = "", SMS = "";
    private TextView tv_tlf;
    private EditText et_sms;
    private final int SMS_DISPONIBLE = 111;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Desa l’estat actual del joc de l’usuari
        savedInstanceState.putString(TLF, tlf);
        savedInstanceState.putString(SMS,sms);
        // Sempre cridem a la superclasse perquè desi també la jerarquia de vistes actual
        super.onSaveInstanceState(savedInstanceState);
    }
    protected void onResume () {
        super.onResume();
        TextView tv_tlf = (TextView) findViewById(R.id.tv_tlf);
        EditText et_sms = (EditText) findViewById(R.id.et_sms);
        //pasamos los valores a String:
        String tlfS = tv_tlf.getText().toString();
        String smsS = et_sms.getText().toString();
        //lo guardamos en los views del layout:
        tv_tlf.setText(tlfS);
        et_sms.setText(smsS);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            tlf = savedInstanceState.getString(TLF);
            onResume();
            tv_tlf = (TextView) findViewById(R.id.tv_tlf);
            et_sms = (EditText) findViewById(R.id.et_sms);
            //para ir actualizando el sms en la variable mientras se escribe:
            et_sms.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                public void afterTextChanged(Editable s) { //despues de que cambie el contenido del EditText, guardamos su valor en la variable
                    sms=et_sms.getText().toString();
                }
            });

        } else {
            tv_tlf = (TextView) findViewById(R.id.tv_tlf);
            et_sms = (EditText) findViewById(R.id.et_sms);
            //para ir actualizando el sms en la variable mientras se escribe:
            et_sms.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                public void afterTextChanged(Editable s) { //despues de que cambie el contenido del EditText, guardamos su valor en la variable
                    sms=et_sms.getText().toString();
                }
            });
        }
    }

    public void numatelf(View v){
       int btn_id = v.getId();
        switch(btn_id){
            case R.id.btn_0:
                actualizaNum("0");
                break;
            case R.id.btn_1:
                actualizaNum("1");
                break;
            case R.id.btn_2:
                actualizaNum("2");
                break;
            case R.id.btn_3:
                actualizaNum("3");
                break;
            case R.id.btn_4:
                actualizaNum("4");
                break;
            case R.id.btn_5:
                actualizaNum("5");
                break;
            case R.id.btn_6:
                actualizaNum("6");
                break;
            case R.id.btn_7:
                actualizaNum("7");
                break;
            case R.id.btn_8:
                actualizaNum("8");
                break;
            case R.id.btn_9:
                actualizaNum("9");
                break;
        }
    }

    /** actualizaNum añade a tlf el String que se le pasa como parámetro
     * @param num String (se espera un número aunque se trate como String)
     */
    public void actualizaNum(String num){
        tlf+=num;
        tv_tlf.setText(tlf);
    }

    /** borrarNum borra el último numero del teléfono.
     * @param v View
     */
    public void borrarNum(View v){
        //pasamos el TextView a String
        String numOriginal = tv_tlf.getText().toString();
        //Si el String estuviera vacío, la operación de quitarle un caracter generaría un error
        //por lo que solo operaremos si no está vacío.
        if(!numOriginal.equals("")){
            //le quitamos el último caracter
            String numAcortado= numOriginal.substring(0, numOriginal.length() -1);
            tv_tlf.setText(numAcortado);
            tlf=numAcortado;
        }
    }

    /** llama comprueba que hay destinatario y si lo hay, ejecuta el proceso de llamada:
     * @param v
     */
    public void llama(View v){
        if(tlf.equals("")){
            Toast.makeText(getApplicationContext(),"Falta destinatario",Toast.LENGTH_SHORT).show();
        } else {
            Intent dial = new Intent();
            dial.setAction("android.intent.action.DIAL");
            dial.setData(Uri.parse("tel:"+tlf));
            startActivity(dial);
        }
    }

    /** enviaSMS comprueva que el mensaje y el telefono no estén vacíos,
     *  comprueva que se tenga permiso de enviar sms
     *  si no se tiene, pide permiso
     *  si se tiene, ejecuta el proceso de enviar el mensaje
     *  y vacía el el edit text del mensaje.
     */
    public void enviaSMS(View v){
        if(sms.equals("") || tlf.equals("")){
            Toast.makeText(getApplicationContext(),"Falta mensaje o destinatario",Toast.LENGTH_SHORT).show();
        }else{
            //si el permiso de envío de mensaje no está concedido, lo pediremos:
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_DISPONIBLE);
            }
            //si el permiso ya  está concedido, enviamos el mensaje con smsManager.sendTextMessage()
            else {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(tlf,null,sms,null,null);
                Toast.makeText(getApplicationContext(),"SMS enviado",Toast.LENGTH_SHORT).show();
                et_sms.setText("");
            }
        }
    }
}