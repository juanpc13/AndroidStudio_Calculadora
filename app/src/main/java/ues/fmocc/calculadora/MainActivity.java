package ues.fmocc.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txtResultado;
    private TextView txtHistorico;

    //Datos para recordar seleccion
    private String operator;
    private int lastBnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtResultado = (TextView)findViewById(R.id.txtResultado);
        txtHistorico = (TextView)findViewById(R.id.txtHistorico);

        for (int i = 0; i <= 9; i++) {
            int id = getResources().getIdentifier("btn"+i, "id", getPackageName());
            ((Button) findViewById(id)).setOnClickListener(this);
        }
        ((Button) findViewById(R.id.btnDot)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnSuma)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnResta)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnMultiplicacion)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnDivision)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnClear)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnPower)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnCuadrado)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnRaiz)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnIgual)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnBorrar)).setOnClickListener(this);

        operator = "";
        lastBnt = 0;

    }

    private boolean letDecimal(String cadena){
        if(cadena.length() == 0){
            return false;
        }

        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) == '.'){
                return false;
            }
        }
        return true;
    }

    public String removeLastCharacter(String str) {
        String result = "";
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 1);
        }
        return result;
    }

    //Metodo valida que no exista un valor con punto decimal a medias (. NO)-(5. NO)-(5.5 SI)
    public boolean validInput(String cadena){
        if(cadena.length() == 0){
            return false;
        }

        if(cadena.indexOf('.') == cadena.length() - 1){
            return false;
        }
        return true;
    }

    //Edita el ultimo operador de la cadena de historico
    public String editLast(String str, String newOpe){
        if (str.isEmpty()){
            return "";
        }

        String result = str;
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 1);
            result += newOpe;
        }
        return result;
    }

    public boolean isButtonOperator(int lastButton){
        int operatations[] = {R.id.btnSuma, R.id.btnResta, R.id.btnMultiplicacion, R.id.btnDivision};
        for (int i = 0; i < operatations.length; i++) {
            if(operatations[i] == lastButton){
                return true;
            }
        }
        return false;
    }

    public boolean isButtonNumber(int idBtn){
        for (int i = 0; i <= 9; i++) {
            int id = getResources().getIdentifier("btn"+i, "id", getPackageName());
            if(id == idBtn){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int currentBtn  = v.getId();
        String edited = "";

        //Necesita refrescar el campo de resultado needsToClearInput
        if(isButtonOperator(lastBnt) && isButtonNumber(currentBtn)){
            txtResultado.setText("");
        }

        switch (currentBtn) {
            case R.id.btn0:
                txtResultado.setText(txtResultado.getText() + "0");
                break;
            case R.id.btn1:
                txtResultado.setText(txtResultado.getText() + "1");
                break;
            case R.id.btn2:
                txtResultado.setText(txtResultado.getText() + "2");
                break;
            case R.id.btn3:
                txtResultado.setText(txtResultado.getText() + "3");
                break;
            case R.id.btn4:
                txtResultado.setText(txtResultado.getText() + "4");
                break;
            case R.id.btn5:
                txtResultado.setText(txtResultado.getText() + "5");
                break;
            case R.id.btn6:
                txtResultado.setText(txtResultado.getText() + "6");
                break;
            case R.id.btn7:
                txtResultado.setText(txtResultado.getText() + "7");
                break;
            case R.id.btn8:
                txtResultado.setText(txtResultado.getText() + "8");
                break;
            case R.id.btn9:
                txtResultado.setText(txtResultado.getText() + "9");
                break;
            case R.id.btnDot:
                if(letDecimal(txtResultado.getText().toString())){
                    txtResultado.setText(txtResultado.getText() + ".");
                }
                break;
            case R.id.btnClear:
                txtResultado.setText("");
                txtHistorico.setText("");
                break;
            case R.id.btnBorrar:
                txtResultado.setText(removeLastCharacter(txtResultado.getText().toString()));
                break;
            case R.id.btnSuma:
                operator = "+";
                if(lastBnt != R.id.btnSuma){

                    if(isButtonOperator(currentBtn) && isButtonOperator(lastBnt)){
                        edited = editLast(txtHistorico.getText().toString(), operator);
                        txtHistorico.setText(edited);
                    }else if(validInput(txtResultado.getText().toString())){
                        //txtHistorico.setText(txtHistorico.getText().toString() + (txtHistorico.getText().length()==0?"":" + ") + txtResultado.getText());
                        txtHistorico.setText(txtHistorico.getText().toString() + txtResultado.getText() + "+");
                    }

                }
                break;
            case R.id.btnResta:
                operator = "-";
                if(lastBnt != R.id.btnResta){

                    if(isButtonOperator(currentBtn) && isButtonOperator(lastBnt)){
                        edited = editLast(txtHistorico.getText().toString(), operator);
                        txtHistorico.setText(edited);
                    }else if(validInput(txtResultado.getText().toString())){
                        txtHistorico.setText(txtHistorico.getText().toString() + txtResultado.getText() + "+");
                    }

                    edited = editLast(txtHistorico.getText().toString(), operator);
                    txtHistorico.setText(edited);

                }
                break;
        }

        lastBnt = currentBtn;
    }
}
