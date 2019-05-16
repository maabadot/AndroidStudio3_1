package com.example.practice3_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.practice3_1.DBHelper.KEY_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnRead, btnChange, btnClear;
    EditText etName, etSurname, etSecondName;
    int elCount = 0;
    DBHelper dbHelper;
    ArrayList names = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etSecondName = (EditText) findViewById(R.id.etSecondName);

        dbHelper = new DBHelper(this);

        String[] rndNames = new String[]{"Дмитрий", "Сергей", "Иван", "Артём", "Вячеслав", "Илья", "Даниил", "Григорий", "Евгений", "Никита", "Николай"};
        String[] rndSurnames = new String[]{"Пучков", "Большаков", "Васильев", "Андреев", "Петров", "Иванов", "Синицын", "Сергеев"};
        String[] rndSecondnames = new String[]{"Витальевич", "Иванович", "Ильич", "Сергеевич", "Альбертович", "Николаевич", "Евгеньевич"};

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        final String finalDateAndTime = dateText + ", " + timeText;

        database.delete(DBHelper.TABLE_STUDENTS, null, null);

        for (int i = 0; i < 5; i++)
        {
            int n1 = (int)Math.floor(Math.random() * rndNames.length);
            int n2 = (int)Math.floor(Math.random() * rndSurnames.length);
            int n3 = (int)Math.floor(Math.random() * rndSecondnames.length);

            String rndFIO = rndSurnames[n2] + " " + rndNames[n1] + " " + rndSecondnames[n3];
            names.add(rndFIO);

            contentValues.put(DBHelper.KEY_NAME, rndNames[n1]);
            contentValues.put(DBHelper.KEY_SURNAME, rndSurnames[n2]);
            contentValues.put(DBHelper.KEY_SECONDNAME, rndSecondnames[n3]);
            contentValues.put(DBHelper.KEY_TIME, finalDateAndTime);
            database.insert(DBHelper.TABLE_STUDENTS, null, contentValues);
        }
        elCount = 5;
    }

    @Override
    public void onClick(View v)
    {

        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String secondname = etSecondName.getText().toString();

        String FIO = surname + " " + name + " " + secondname;

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        final String finalDateAndTime = dateText + ", " + timeText;

        switch (v.getId())
        {
            case R.id.btnAdd:
                if (name.equals("") && surname.equals("") && secondname.equals(""))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы не заполнили поля.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (names.contains(FIO))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Такой человек уже есть в базе.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    contentValues.put(DBHelper.KEY_NAME, name);
                    contentValues.put(DBHelper.KEY_SURNAME, surname);
                    contentValues.put(DBHelper.KEY_SECONDNAME, secondname);
                    contentValues.put(DBHelper.KEY_TIME, finalDateAndTime);
                    names.add(FIO);
                    database.insert(DBHelper.TABLE_STUDENTS, null, contentValues);
                    elCount++;
                }
                break;

            case R.id.btnRead:
                Intent intent = new Intent(MainActivity.this, TableActivityV1.class);
                startActivity(intent);
                break;

            case R.id.btnChange:
                if (names.contains("Иванов Иван Иванович"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Такой человек уже есть в базе.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    contentValues.put(DBHelper.KEY_NAME, "Иван");
                    contentValues.put(DBHelper.KEY_SURNAME, "Иванов");
                    contentValues.put(DBHelper.KEY_SECONDNAME, "Иванович");
                    String where = KEY_ID + "=" + elCount;
                    names.set(elCount - 1, "Иванов Иван Иванович");
                    database.update(DBHelper.TABLE_STUDENTS, contentValues, where, null);
                }

                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_STUDENTS, null, null);
                names.clear();
                elCount = 0;
                break;
        }
        dbHelper.close();
    }
}
