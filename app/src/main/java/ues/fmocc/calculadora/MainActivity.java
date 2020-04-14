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
            result = str.substring(0, str.length() - 3);
            result += " " + newOpe + " ";
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

    public double solveString(final String str){
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
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
                        txtHistorico.setText(txtHistorico.getText().toString() + txtResultado.getText() + " " + operator + " ");
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
                        txtHistorico.setText(txtHistorico.getText().toString() + txtResultado.getText() + " " + operator + " ");
                    }

                }
                break;
            case R.id.btnMultiplicacion:
                operator = "*";
                if(lastBnt != R.id.btnResta){

                    if(isButtonOperator(currentBtn) && isButtonOperator(lastBnt)){
                        edited = editLast(txtHistorico.getText().toString(), operator);
                        txtHistorico.setText(edited);
                    }else if(validInput(txtResultado.getText().toString())){
                        txtHistorico.setText(txtHistorico.getText().toString() + txtResultado.getText() + " " + operator + " ");
                    }

                }
                break;
            case R.id.btnDivision:
                operator = "/";
                if(lastBnt != R.id.btnResta){

                    if(isButtonOperator(currentBtn) && isButtonOperator(lastBnt)){
                        edited = editLast(txtHistorico.getText().toString(), operator);
                        txtHistorico.setText(edited);
                    }else if(validInput(txtResultado.getText().toString())){
                        txtHistorico.setText(txtHistorico.getText().toString() + txtResultado.getText() + " " + operator + " ");
                    }

                }
                break;
            case R.id.btnIgual:
                operator = "=";
                edited = txtHistorico.getText().toString();

                //Esto hace que se agrege el ultimo valor del campo resultado si lo amerita
                if(isButtonNumber(lastBnt)){
                    edited += txtResultado.getText().toString();
                }

                edited = editLast(edited, "=");
                txtHistorico.setText(edited);
                edited = editLast(edited, " ");
                txtResultado.setText(solveString(edited)+"");
                break;

        }

        lastBnt = currentBtn;
    }
}
